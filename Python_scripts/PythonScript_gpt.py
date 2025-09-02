from openai import OpenAI
import xml.etree.ElementTree as ET
import os
import sys
import subprocess
import shutil
import time
import csv
import re
import random
import glob

client = OpenAI(
    api_key='<your_key>',
)

# Global variables initializing
token_usage = 0  # Token accumulator initialization
api_processing_time = 0  # Total time spent processing OpenAI API calls
# start_time = time.time()  # Script execution timer initialization

def identify_uncovered_files_from_jaCoCo_report(test_set_dir, jacoco_file_path):
    tree = ET.parse(jacoco_file_path)
    root = tree.getroot()

    uncovered_methods = []
    uncovered_files = set()  # Set to store uncovered file names

    # Mapping source files to class names
    class_map = {}
    for package in root.findall("package"):
        for class_elem in package.findall("class"):
            class_name = class_elem.get("name")
            source_file = class_elem.get("sourcefilename")
            class_map[source_file] = class_name

    # Extract uncovered methods and lines
    for package in root.findall("package"):
        for class_elem in package.findall("class"):
            class_name = class_elem.get("name")
            source_file_name = class_elem.get("sourcefilename")

            method_line_ranges = []
            for method in class_elem.findall("method"):
                method_name = method.get("name")
                start_line = int(method.get("line", -1))
                if start_line != -1:
                    method_line_ranges.append((method_name, start_line))

            method_line_ranges.sort(key=lambda x: x[1])

            method_lines_map = {method_name: [] for method_name, _ in method_line_ranges}

            for sourcefile in package.findall("sourcefile"):
                if sourcefile.get("name") == source_file_name:
                    for line in sourcefile.findall("line"):
                        mi = int(line.get("mi", 0))
                        mb = int(line.get("mb", 0))
                        if mi > 0 or mb > 0:
                            line_number = int(line.get("nr"))

                            for i in range(len(method_line_ranges)):
                                method_name, start_line = method_line_ranges[i]
                                next_start = method_line_ranges[i + 1][1] if i + 1 < len(method_line_ranges) else float('inf')

                                if start_line <= line_number < next_start:
                                    method_lines_map[method_name].append(str(line_number))
                                    break

            for method_name, lines in method_lines_map.items():
                if lines:
                    uncovered_methods.append((class_name, method_name, lines))
                    uncovered_files.add(source_file_name)  # Add uncovered file

    jacoco_file_name = os.path.basename(jacoco_file_path)

    # Save uncovered methods and lines
    uncovered_file_name = os.path.join(test_set_dir, f'{jacoco_file_name}.txt')
    with open(uncovered_file_name, 'w', encoding='utf-8') as file:
        file.write("-" * 90)
        for class_name, method_name, lines in uncovered_methods:
            file.write(f"\nClass name: {class_name}")
            file.write(f"\nMethod: {method_name}")
            file.write(f"\nLines: {', '.join(sorted(set(lines), key=int))}")
            file.write("\n" + "-" * 90)
    
    return uncovered_files, uncovered_file_name

def load_config(config_dir):
    config_path = os.path.join(config_dir, "config.txt")
    config = {}

    with open(config_path, "r", encoding="utf-8") as file:
        for line in file:
            line = line.strip()
            if "=" in line:
                key, value = line.split("=", 1)
                config[key.strip()] = value.strip()
            else:
                print(f"Warnging: not well formed line on config file: '{line}'")

    return config

def find_file_in_directory(filename, directory):
    for root, _, files in os.walk(directory):
        if filename in files:
            return os.path.join(root, filename)
    return None

def create_clean_directory(directory):
    if os.path.exists(directory):
        shutil.rmtree(directory)
    os.makedirs(directory)

def search_uncovered_informations(uncovered_info_file_path, class_name):
    results = []
    with open(uncovered_info_file_path, "r", encoding="utf-8") as file:
        content = file.read()

    sections = content.split("------------------------------------------------------------------------------------------")

    for section in sections:
        lines = section.strip().split("\n")
        for line in lines:
            if line.startswith("Class name:"):
                class_name_found = line.replace("Class name:", "").strip()
                if class_name_found.endswith(class_name):
                    results.append(section.strip())

    return "\n\n".join(results) if results else "No matches found."

# Generates tests from a prompt
def generate(user_prompt, system_prompt, model, temperature):
    
    global token_usage, api_processing_time

    print(temperature)
    print(model)

    start_time = time.time()  # Start time of API processing
    completion = client.chat.completions.create(
        model = model,
        messages = [
            {"role": "system", "content": system_prompt},
            {"role": "user", "content": user_prompt}
        ],
        temperature = temperature
    )
    end_time = time.time()  # End time of API processing
    
    # Update global tracking variables
    token_usage += completion.usage.total_tokens
    api_processing_time += (end_time - start_time)  # Accumulate only API processing time
    
    return completion.choices[0].message.content

# Saves the metrics (tokens and API processing time)
def save_metrics(test_set_dir, run_dir):
    
    print(token_usage)
    print(api_processing_time)
    
    with open(os.path.join(test_set_dir, f'{run_dir}_metrics.csv'), 'w', newline='') as csvfile:
        writer = csv.writer(csvfile)
        writer.writerow(['Total Tokens Used', 'Total API Processing Time (s)'])
        writer.writerow([token_usage, api_processing_time])

def clean_file(file_path):
    
    with open(file_path, 'r', encoding='utf-8') as file:
        content = file.read()
    
    new_content = content.replace("```java", "").replace("```", "")
    with open(file_path, 'w', encoding='utf-8') as file:
        file.write(new_content)

def clear_directory(directory_path):
    
    if os.path.exists(directory_path):
        shutil.rmtree(directory_path)
    os.makedirs(directory_path, exist_ok=True)

# Function to copies the files to the project folder and run with maven
def execute_maven_process(test_file_path, mvn_project_directory, test_project_directory, mode):
    
    clear_directory(test_project_directory)
    
    # Copy test file to test project directory
    shutil.copy(test_file_path, test_project_directory)
    
    # Change the context to the project directory
    os.chdir(mvn_project_directory)
    
    # Run maven according mode (compilation or execution test)
    if mode == "comp":
        result = subprocess.run(['mvn', 'clean', 'compile', 'test-compile'], capture_output=True, text=True)
    elif mode == "exec":
        result = subprocess.run(['mvn', 'clean', 'compile', 'test-compile', 'test'], capture_output=True, text=True)
    
    return result

def clean_ansi(line):
    
    ansi_sequences = [
        "\x1b[0m", "\x1b[1m", "\x1b[2m", "\x1b[3m", "\x1b[4m", "\x1b[5m", "\x1b[6m", "\x1b[7m", "\x1b[8m", "\x1b[9m",
        "\x1b[30m", "\x1b[31m", "\x1b[32m", "\x1b[33m", "\x1b[34m", "\x1b[35m", "\x1b[36m", "\x1b[37m",
        "\x1b[40m", "\x1b[41m", "\x1b[42m", "\x1b[43m", "\x1b[44m", "\x1b[45m", "\x1b[46m", "\x1b[47m",
        "\x1b[90m", "\x1b[91m", "\x1b[92m", "\x1b[93m", "\x1b[94m", "\x1b[95m", "\x1b[96m", "\x1b[97m",
        "\x1b[100m", "\x1b[101m", "\x1b[102m", "\x1b[103m", "\x1b[104m", "\x1b[105m", "\x1b[106m", "\x1b[107m",
        "\x1b[m", "\x1b(B", "\x1b)0", "\x1b]0;", "\x1b[K", "\x1b[J", "\x1b(G", "\x1b(E", "\x1b[?25h",
        "\x1b[1;31m", "\x1b[0;1m", "\x1b[0;1;31m" 
    ]

    for seq in ansi_sequences:
        line = line.replace(seq, "")

    line = "".join(char for char in line if ord(char) >= 32 or char in ("\t", " "))

    return line.strip()

# Save files with maven compilation results
def save_files_comp_maven_result(test_file_path, result_maven_process, iteraction_generate, iteraction_comp):
    
    test_file_dir = os.path.dirname(test_file_path)
    test_file_name = os.path.basename(test_file_path)

    output_file_path = os.path.join(test_file_dir, f'{test_file_name}._it_{iteraction_generate}_comp_{iteraction_comp}_mvn_output.txt')
    output_error_file_path = os.path.join(test_file_dir, f'{test_file_name}._it_{iteraction_generate}_comp_{iteraction_comp}_mvn_output_error.txt')

    with open(output_file_path, 'w', encoding='utf-8') as output_file:
        output_file.write(result_maven_process.stdout)
    
    with open(output_error_file_path, 'w', encoding='utf-8') as output_file:
        output_file.write(" ")

    copy = False
    with open(output_file_path, 'r') as f_in, open(output_error_file_path, 'w', encoding='utf-8') as f_out:
        for line in f_in:
            if "ERROR" in line:
                copy = True
            if "INFO" in line:
                copy = False
            if copy and "INFO" not in line:
                f_out.write(line)
        
    return output_error_file_path

def add_ignore_annotation(file_path, test_methods_to_ignore, junit_version):
    try:
        if not os.path.exists(file_path):
            print(f"Error: File '{file_path}' not found.")
            return

        with open(file_path, 'r', encoding='utf-8') as file:
            lines = file.readlines()

        updated_lines = []
        modifications_made = False

        # Determine the correct annotation and import statement based on JUnit version
        if junit_version == 4:
            ignore_annotation = "@Ignore"
            ignore_import = "import org.junit.Ignore;"
            test_annotation = "@Test"
        elif junit_version == 5:
            ignore_annotation = "@Disabled"
            ignore_import = "import org.junit.jupiter.api.Disabled;"
            test_annotation = "@Test"
        else:
            print(f"Error: Invalid JUnit version ({junit_version}). Use 4 or 5.")
            return

        # Check if the ignore annotation import is already present
        ignore_import_added = any(ignore_import in line for line in lines)

        for i in range(len(lines)):
            line = lines[i]
            stripped_line = line.strip()

            # Detect @Test annotation (both JUnit 4 and JUnit 5)
            if stripped_line.startswith(test_annotation):
                updated_lines.append(line)  # Keep @Test annotation

                # Check the next non-empty line for a method definition
                j = i + 1
                while j < len(lines) and lines[j].strip() == "":
                    j += 1  # Skip blank lines

                if j < len(lines) and re.match(r'public void (\w+)\(.*\)', lines[j].strip()):
                    method_match = re.match(r'public void (\w+)\(.*\)', lines[j].strip())
                    if method_match and method_match.group(1) in test_methods_to_ignore:
                        # Ensure the ignore annotation is not already present
                        if ignore_annotation not in lines[i + 1]:
                            updated_lines.append(f"    {ignore_annotation}\n")
                            modifications_made = True

            else:
                updated_lines.append(line)

        # Add the correct import statement if needed
        if modifications_made and not ignore_import_added:
            for i, line in enumerate(updated_lines):
                if line.strip().startswith("import"):
                    updated_lines.insert(i + 1, f"{ignore_import}\n")
                    break
            else:
                if updated_lines and updated_lines[0].strip().startswith("package"):
                    updated_lines.insert(1, f"\n{ignore_import}\n")
                else:
                    updated_lines.insert(0, f"{ignore_import}\n\n")

        with open(file_path, 'w', encoding='utf-8') as file:
            file.writelines(updated_lines)

        print(f"Updated: {file_path} (JUnit {junit_version})")

    except Exception as e:
        print(f"Error processing {file_path}: {e}")

# Function to saves files with maven execution results
def save_files_exec_maven_result(test_file_path, result_maven_process, iteraction_exec):
    
    failures = []
    results = {}
    qtd_failures_errors = -1

    test_file_dir = os.path.dirname(test_file_path)
    test_file_name = os.path.basename(test_file_path)
   
    output_file_path = os.path.join(test_file_dir, f'{test_file_name}._exec_{iteraction_exec}_mvn_output.txt')
    output_error_file_path = os.path.join(test_file_dir, f'{test_file_name}._exec_{iteraction_exec}_mvn_output_error.txt')

    with open(output_file_path, 'w', encoding='utf-8') as output_file:
        output_file.write(result_maven_process.stdout)
    
    with open(output_error_file_path, 'w', encoding='utf-8') as output_file:
        output_file.write("")
        
    # Read the file and remove ANSI characters
    with open(output_file_path, 'r', encoding='utf-8') as file:
        lines = [clean_ansi(line) for line in file.readlines()]

    # Capture the total number of failures and errors
    for i in range(len(lines) - 1):
        if "Tests run" in lines[i]:
            parts = lines[i].split(",")
            results = {}
            for part in parts[:-1]:
                key_value = part.split(":")
                if len(key_value) > 1:
                    key, value = key_value[0].strip(), key_value[1].strip()
                    if key == "Failures":
                        results["Failures"] = int(value)
                    elif key == "Errors":
                        results["Errors"] = int(value)

            print("-----------------------------------")
            print("Failures: ", results["Failures"])
            print("Errors: ", results["Errors"])
            qtd_failures_errors = results["Failures"] + results["Errors"]
            print("Total: ", qtd_failures_errors)
            print("-----------------------------------")
            break

    if qtd_failures_errors > 0:
        # Extract failures and errors
        for i in range(len(lines) - 1):
            line = lines[i].strip()
                    
            line_number = ""
            error_message = ""

            if "ERROR" in line:
                
                if "expected" in line:
                    match = re.search(r":(\d+)\s+(expected:.*)", line)
                    line_number = match.group(1) if match else "Unknown"
                    error_message = match.group(2) if match else line.strip()
                    
                    formatted_error = f"Line: {line_number}\n{error_message}\n" + "-" * 40
                    failures.append(formatted_error)
                    print(formatted_error)

                if "expectation failed" in line:
                    match = re.search(r":(\d+)\s+\d+\s+expectation failed\.", line, re.IGNORECASE)
                    line_number = match.group(1) if match else "Unknown"  

                    if i + 1 < len(lines):  # Avoids IndexError when accessing lines[i + 1]
                        if "status code" in lines[i + 1] or "application/json" in lines[i + 1]:
                            error_message = lines[i + 1].strip()
                        elif "JSON" in lines[i + 1] or "Response body doesn't match expectation" in lines[i + 1]:
                            next_lines = [lines[i + j].strip() for j in range(1, 4) if i + j < len(lines)]
                            error_message = "\n".join(next_lines)
                        else:
                            error_message = f"{line.strip()}\n{lines[i + 1].strip()}"
                    else:
                        error_message = line.strip()  # If there is no next line, save the current line.

                    formatted_error = f"Line: {line_number}\n{error_message}\n" + "-" * 40
                    failures.append(formatted_error)
                    print(formatted_error)
                
                if " » " in line and ":" in line:
                    match = re.search(r":(\d+)\s+»\s+(.*)", line)
                    line_number = match.group(1) if match else "Unknown"
                    error_message = match.group(2).strip() if match else line.strip()

                    formatted_error = f"Line: {line_number}\n{error_message}\n" + "-" * 40
                    failures.append(formatted_error)
                    print(formatted_error)
                
                if "->" in line and ":" in line:
                    match = re.search(r":(\d+)->(.*):(\d+)", line)
                    line_number = match.group(1) if match else "Unknown"
                    error_message = f"{match.group(2).strip()}:{match.group(3)}" if match else line.strip()

                    formatted_error = f"Line: {line_number}\n{error_message}\n" + "-" * 40
                    failures.append(formatted_error)
                    print(formatted_error)

        with open(output_error_file_path, 'w', encoding='utf-8') as file:
            for failure in failures:
                file.write(failure + "\n")

    return output_error_file_path, qtd_failures_errors

# main
if __name__ == '__main__':
    
    # Load config file from command line parameter
    config = load_config(sys.argv[1])

    try:
        rest_application_maven_dir = config["rest_application_maven_dir"]
        junit_version = int(config["junit_version"])
        rest_application_test_dir = config["rest_application_test_dir"]
        rest_application_code_dir = config["rest_application_code_dir"]
        swagger_file_path = config["swagger_file_path"]
        test_set_dir = config["test_set_dir"]
        model_dir=config["model_dir"]
        run_dir = config["run_dir"]
        jacoco_report_file_path = config["jacoco_report_file_path"]
        llm_model = config["llm_model"]
        user_prompt_generate_file_path = config["user_prompt_generate_file_path"]
        user_prompt_comp_file_path = config["user_prompt_comp_file_path"]
        user_prompt_exec_file_path = config["user_prompt_exec_file_path"]
        system_prompt_generate_file_path = config["system_prompt_generate_file_path"]
        system_prompt_comp_exec_file_path = config["system_prompt_comp_exec_file_path"]
        max_iteractions_generate_test_file = int(config["max_iteractions_generate_test_file"])
        max_iteractions_comp_test_file = int(config["max_iteractions_comp_test_file"])
        max_iteractions_exec_test_file = int(config["max_iteractions_exec_test_file"])
        ignore_files = config.get("ignore_files", "")
        generated_tests = config.get("generated_tests", "")
        temp_generate = float(config["temp_generate"])
        temp_comp = float(config["temp_comp"])
        temp_exec = float(config["temp_exec"])
    except KeyError as e:
        raise ValueError(f"Value not found in config: {e}")

    dir = "comp"
    comp_dir = os.path.join(test_set_dir, dir)
    create_clean_directory(comp_dir)
    dir = "exec"
    exec_dir = os.path.join(test_set_dir, dir)
    create_clean_directory(exec_dir)

    # Verify all files is found in application code dir
    uncovered_files, uncovered_info_file_path = identify_uncovered_files_from_jaCoCo_report(test_set_dir, jacoco_report_file_path)
    
    # Verify if files uncovered existis
    ignore_files_list = [f.strip() for f in ignore_files.split(",") if f.strip()]
    generated_tests_list = [f.strip() for f in generated_tests.split(",") if f.strip()]

    continue_execution = True
    for file in uncovered_files:
        application_file_path = find_file_in_directory(file, rest_application_code_dir)
        if application_file_path:
            print(f"\nFile {application_file_path} found")
        else:
            if file in ignore_files_list:
                print(f"{file} not found in application directory, but is in ignore list.")
            else:
                print(f"{file} not found in application directory.")
                continue_execution = False
    
    if continue_execution:
        
        print("\nExecution started")

        qtd_failures_errors = -1

        for file in uncovered_files:

            if file in generated_tests_list:
                print(f"{file} is in generated_tests list. Skipping test generation.")
                continue
            
            print(f"\nApplication code file name: {file}")
            application_file_path = find_file_in_directory(file, rest_application_code_dir)
            
            if not application_file_path:
                print(f"Error: {file} was not found. Skipping file.")
                continue
            
            file_dir = os.path.join(test_set_dir, file)
            create_clean_directory(file_dir)

            # Creates the class name from promtp version, model, run number and file name
            file_name = os.path.splitext(file)[0]
            class_name = model_dir + "_" + run_dir  + '_' + file_name + 'Test'

            number_result_maven = 1
            execute_test_file = True
            generate_iteractions = 1

            while number_result_maven == 1 and generate_iteractions <= max_iteractions_generate_test_file:

                # Creating the initial prompt and first test file version
                print("\n---------Compilation steps - Iteraction " + str(generate_iteractions) + " -----------")
                print("------------------1------------------")
                
                # Building initital user prompt
                user_prompt = ""
                with open(user_prompt_generate_file_path, 'r', encoding='utf-8') as f:
                    user_prompt += f.read()
                
                user_prompt = user_prompt.replace('<<class_name>>', class_name)

                text = search_uncovered_informations(uncovered_info_file_path, file_name)
                user_prompt = user_prompt.replace('<<uncovered_informations>>', text)
                
                with open(application_file_path, 'r', encoding='utf-8') as f:
                    text = f.read()
                user_prompt = user_prompt.replace('<<java_application_code>>', text)
                
                with open(swagger_file_path, 'r', encoding='utf-8') as f:
                    text = f.read()
                user_prompt = user_prompt.replace('<<swagger.json>>', text)

                prompt_file_path = os.path.join(file_dir, f'prompt_it_{generate_iteractions}_comp_1_{file_name}')
                with open(prompt_file_path, 'w', encoding='utf-8') as pf:
                    pf.write(user_prompt)

                # Load initital system prompt
                system_prompt = ""
                with open(system_prompt_generate_file_path, 'r', encoding='utf-8') as f:
                    system_prompt = f.read()

                temp_generate = round(random.uniform(0.7, 1.0), 1)

                # Generate first test file
                test_file_path = os.path.join(file_dir, f'{class_name}.java')
                with open(test_file_path, 'w', encoding='utf-8') as file_java:
                    file_java.write(generate(user_prompt, system_prompt, llm_model, temp_generate))

                clean_file(test_file_path)

                mode = "comp"
                complete_result_maven = execute_maven_process(test_file_path, rest_application_maven_dir, rest_application_test_dir, mode)
                output_error_file_path = save_files_comp_maven_result(test_file_path, complete_result_maven, generate_iteractions, 1)
                if complete_result_maven.returncode == 0:
                    shutil.copy(test_file_path, comp_dir)    
                number_result_maven = complete_result_maven.returncode
                print("number_result_maven: " + str(number_result_maven))

                # Start steps of correct compilation errors
                iteractions_comp = 2 # The first one always executes
                
                while number_result_maven == 1 and iteractions_comp <= max_iteractions_comp_test_file:

                    # Creating the comp prompt
                    print("\n---------Compilation steps - Iteraction " + str(generate_iteractions) + " -----------")
                    print("------------------" + str(iteractions_comp) + "------------------")

                    # Building comp user prompt
                    user_prompt = ""
                    with open(user_prompt_comp_file_path, 'r', encoding='utf-8') as f:
                        user_prompt += f.read()
                    
                    with open(output_error_file_path, 'r', encoding='utf-8') as f:
                        text = f.read()
                    user_prompt = user_prompt.replace('<<error_messages>>', text)
                                        
                    with open(test_file_path, 'r', encoding='utf-8') as f:
                        text = f.read()
                    user_prompt = user_prompt.replace('<<java_test_code>>', text)

                    prompt_file_path = os.path.join(file_dir, f'prompt_it_{generate_iteractions}_comp_{iteractions_comp}_{file_name}')
                    with open(prompt_file_path, 'w', encoding='utf-8') as pf:
                        pf.write(user_prompt)

                    # Load initital system prompt
                    system_prompt = ""
                    with open(system_prompt_comp_exec_file_path, 'r', encoding='utf-8') as f:
                        system_prompt = f.read()

                    temp_comp = round(random.uniform(0.3, 0.6), 1)

                    # Generate first test file
                    test_file_path = os.path.join(file_dir, f'{class_name}.java')
                    with open(test_file_path, 'w', encoding='utf-8') as file_java:
                        file_java.write(generate(user_prompt, system_prompt, llm_model, temp_comp))

                    clean_file(test_file_path)

                    mode = "comp"
                    complete_result_maven = execute_maven_process(test_file_path, rest_application_maven_dir, rest_application_test_dir, mode)
                    output_error_file_path = save_files_comp_maven_result(test_file_path, complete_result_maven, generate_iteractions, iteractions_comp)
                    if complete_result_maven.returncode == 0:
                        shutil.copy(test_file_path, comp_dir)
                    number_result_maven = complete_result_maven.returncode
                    print("number_result_maven: " + str(number_result_maven))

                    iteractions_comp += 1 # Increment for compilation while

                generate_iteractions += 1 # Increment for generate while
            
            # Stop execution before new for iteraction
            if number_result_maven == 1 and generate_iteractions == max_iteractions_generate_test_file + 1:
                print("\nExecution stoped: compiled file can not be generated")
                break
            else:
                
                # First execution
                print("\n---------Execution steps - Iteraction 1 -----------")
                iteractions_exec = 1
                min_failures_errors = -1

                mode = "exec"
                complete_result_maven = execute_maven_process(test_file_path, rest_application_maven_dir, rest_application_test_dir, mode)
                output_error_file_path, min_failures_errors = save_files_exec_maven_result(test_file_path, complete_result_maven, iteractions_exec)
                shutil.copy(test_file_path, exec_dir)
                number_result_maven = complete_result_maven.returncode
                print("number_result_maven: " + str(number_result_maven))

                # Start steps of correct execution errors
                iteractions_exec = 2 # The first one always executes

                while number_result_maven == 1 and iteractions_exec <= max_iteractions_exec_test_file:

                    print("\n---------Execution steps - Iteraction " + str(iteractions_exec) + " -----------")

                    # Building exec user prompt
                    user_prompt = ""
                    with open(user_prompt_exec_file_path, 'r', encoding='utf-8') as f:
                        user_prompt += f.read()
                    
                    with open(output_error_file_path, 'r', encoding='utf-8') as f:
                        text = f.read()
                    user_prompt = user_prompt.replace('<<error_messages>>', text)
                                        
                    with open(test_file_path, 'r', encoding='utf-8') as f:
                        text = f.read()
                    user_prompt = user_prompt.replace('<<java_test_code>>', text)

                    prompt_file_path = os.path.join(file_dir, f'prompt_exec_{iteractions_exec}_{file_name}')
                    with open(prompt_file_path, 'w', encoding='utf-8') as pf:
                        pf.write(user_prompt)

                    # Load system prompt
                    system_prompt = ""
                    with open(system_prompt_comp_exec_file_path, 'r', encoding='utf-8') as f:
                        system_prompt = f.read()

                    temp_exec = round(random.uniform(0.0, 0.2), 1)

                    # Generate first test file
                    test_file_path = os.path.join(file_dir, f'{class_name}.java')
                    with open(test_file_path, 'w', encoding='utf-8') as file_java:
                        file_java.write(generate(user_prompt, system_prompt, llm_model, temp_exec))

                    clean_file(test_file_path)

                    mode = "exec"
                    complete_result_maven = execute_maven_process(test_file_path, rest_application_maven_dir, rest_application_test_dir, mode)
                    output_error_file_path, qtd_failures_errors = save_files_exec_maven_result(test_file_path, complete_result_maven, iteractions_exec)
                    if qtd_failures_errors > -1 and qtd_failures_errors <= min_failures_errors:
                        shutil.copy(test_file_path, exec_dir)
                        min_failures_errors = qtd_failures_errors
                    number_result_maven = complete_result_maven.returncode
                    print("number_result_maven: " + str(number_result_maven))

                    iteractions_exec += 1 # Increment for execution while

                save_metrics(test_set_dir, run_dir)

            # Scritp to ignore/disable tests (failures and errors)
            # Discover tests
            if min_failures_errors > 0:
                print("Steps to ignore/disable tests")
                test_file_name = os.path.basename(test_file_path)
                exec_file_path = os.path.join(test_set_dir, "exec", test_file_name)
                mode = "exec"
                execute_maven_process(exec_file_path, rest_application_maven_dir, rest_application_test_dir, mode)
                reports_path = os.path.join(rest_application_maven_dir, "target", "surefire-reports")
                xml_files = glob.glob(os.path.join(reports_path, "*.xml"))
                print(xml_files)
                if not xml_files:
                    print(f"Error: none XML file found in '{reports_path}'.")
                else:
                    xml_file_path = xml_files[0]
                    tree = ET.parse(xml_file_path)
                    root = tree.getroot()
                    error_tests = [testcase.get("name") for testcase in root.findall("testcase") if testcase.find("error") is not None]
                    print("Error Test Cases:")
                    for test in error_tests:
                        print(test)
                    if error_tests:
                        add_ignore_annotation(exec_file_path, error_tests, junit_version)

    else:
        print("\nExecution stoped")
    
    save_metrics(test_set_dir, run_dir)
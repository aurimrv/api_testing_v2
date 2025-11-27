# Statistical Analysis of LLM Test Generation Effectiveness
# Comparing GPT-3.5 (T1), Claude (T2), and GPT-4o (T3)
# 
# Analysis includes:
# - Descriptive statistics and effectiveness calculation
# - Chi-square test for independence
# - Pairwise comparisons with Fisher's exact test
# - Effect size calculations (Cramer's V)
# - Visualization of results

# Read data
data <- read.csv("rq2.csv")

cat("=================================================================\n")
cat("   LLM TEST GENERATION EFFECTIVENESS ANALYSIS\n")
cat("   T1 = GPT-3.5 | T2 = Claude | T3 = GPT-4o\n")
cat("=================================================================\n\n")

# Display raw data
cat("RAW DATA:\n")
cat("---------\n")
print(data)
cat("\n")

# =====================================================================
# 1. CALCULATE EFFECTIVENESS FOR EACH MODEL AND PROJECT
# =====================================================================
cat("=================================================================\n")
cat("              EFFECTIVENESS CALCULATION\n")
cat("         Effectiveness = VALID / TOTAL * 100%\n")
cat("=================================================================\n\n")

# Calculate effectiveness for each model and project
data$T1_Effectiveness <- (data$T1_VALID / data$T1_TOTAL) * 100
data$T2_Effectiveness <- (data$T2_VALID / data$T2_TOTAL) * 100
data$T3_Effectiveness <- (data$T3_VALID / data$T3_TOTAL) * 100

effectiveness_table <- data.frame(
  Project = data$Project,
  T1_GPT35 = sprintf("%.2f%%", data$T1_Effectiveness),
  T2_Claude = sprintf("%.2f%%", data$T2_Effectiveness),
  T3_GPT4o = sprintf("%.2f%%", data$T3_Effectiveness)
)

print(effectiveness_table, row.names = FALSE)
cat("\n")

# =====================================================================
# 2. DESCRIPTIVE STATISTICS BY MODEL
# =====================================================================
cat("=================================================================\n")
cat("              DESCRIPTIVE STATISTICS BY MODEL\n")
cat("=================================================================\n\n")

# Aggregate totals by model
T1_totals <- c(sum(data$T1_TOTAL), sum(data$T1_FAILURES), sum(data$T1_SKIPPED), sum(data$T1_VALID))
T2_totals <- c(sum(data$T2_TOTAL), sum(data$T2_FAILURES), sum(data$T2_SKIPPED), sum(data$T2_VALID))
T3_totals <- c(sum(data$T3_TOTAL), sum(data$T3_FAILURES), sum(data$T3_SKIPPED), sum(data$T3_VALID))

summary_table <- data.frame(
  Model = c("T1 (GPT-3.5)", "T2 (Claude)", "T3 (GPT-4o)"),
  Total_Tests = c(T1_totals[1], T2_totals[1], T3_totals[1]),
  Failures = c(T1_totals[2], T2_totals[2], T3_totals[2]),
  Skipped = c(T1_totals[3], T2_totals[3], T3_totals[3]),
  Valid = c(T1_totals[4], T2_totals[4], T3_totals[4]),
  Effectiveness_Pct = c(
    (T1_totals[4] / T1_totals[1]) * 100,
    (T2_totals[4] / T2_totals[1]) * 100,
    (T3_totals[4] / T3_totals[1]) * 100
  )
)

print(summary_table, row.names = FALSE)
cat("\n")

# Calculate additional statistics
cat("EFFECTIVENESS STATISTICS:\n")
cat("-------------------------\n")
effectiveness_values <- c(data$T1_Effectiveness, data$T2_Effectiveness, data$T3_Effectiveness)
models <- factor(rep(c("T1_GPT35", "T2_Claude", "T3_GPT4o"), each = nrow(data)))

# Statistics by model
for (model_name in c("T1_GPT35", "T2_Claude", "T3_GPT4o")) {
  model_data <- effectiveness_values[models == model_name]
  cat(sprintf("\n%s:\n", model_name))
  cat(sprintf("  Mean:   %.2f%%\n", mean(model_data)))
  cat(sprintf("  Median: %.2f%%\n", median(model_data)))
  cat(sprintf("  SD:     %.2f\n", sd(model_data)))
  cat(sprintf("  Min:    %.2f%%\n", min(model_data)))
  cat(sprintf("  Max:    %.2f%%\n", max(model_data)))
}
cat("\n")

# =====================================================================
# 3. CHI-SQUARE TEST FOR INDEPENDENCE
# =====================================================================
cat("=================================================================\n")
cat("              CHI-SQUARE TEST FOR INDEPENDENCE\n")
cat("=================================================================\n\n")

cat("Research Question: Is there a significant association between\n")
cat("LLM model and test outcome (VALID vs. NOT VALID)?\n\n")

cat("H0 (Null Hypothesis): Test effectiveness is independent of LLM model\n")
cat("H1 (Alternative): Test effectiveness depends on the LLM model\n")
cat("Significance level: α = 0.05\n\n")

# Create contingency table for overall analysis
# Rows: Models, Columns: Valid vs Not Valid (Failures + Skipped)
contingency_table <- matrix(c(
  T1_totals[4], T1_totals[2] + T1_totals[3],  # T1: Valid, Not Valid
  T2_totals[4], T2_totals[2] + T2_totals[3],  # T2: Valid, Not Valid
  T3_totals[4], T3_totals[2] + T3_totals[3]   # T3: Valid, Not Valid
), nrow = 3, byrow = TRUE)

rownames(contingency_table) <- c("T1_GPT-3.5", "T2_Claude", "T3_GPT-4o")
colnames(contingency_table) <- c("VALID", "NOT_VALID")

cat("CONTINGENCY TABLE:\n")
print(contingency_table)
cat("\n")

# Perform chi-square test
chi_test <- chisq.test(contingency_table)

cat("CHI-SQUARE TEST RESULTS:\n")
cat(sprintf("  χ² statistic = %.4f\n", chi_test$statistic))
cat(sprintf("  Degrees of freedom = %d\n", chi_test$parameter))
cat(sprintf("  p-value = %.4f\n", chi_test$p.value))
cat("\n")

if (chi_test$p.value < 0.05) {
  cat("CONCLUSION: p < 0.05 → REJECT H0\n")
  cat("There IS a statistically significant association between LLM model\n")
  cat("and test effectiveness. Different models have different effectiveness rates.\n\n")
} else {
  cat("CONCLUSION: p ≥ 0.05 → FAIL TO REJECT H0\n")
  cat("There is NO statistically significant association between LLM model\n")
  cat("and test effectiveness at the 0.05 significance level.\n\n")
}

# Calculate Cramer's V (effect size)
n <- sum(contingency_table)
cramers_v <- sqrt(chi_test$statistic / (n * (min(dim(contingency_table)) - 1)))
cat(sprintf("Effect Size (Cramer's V) = %.4f\n", cramers_v))
cat("Interpretation: ")
if (cramers_v < 0.1) {
  cat("Negligible effect\n")
} else if (cramers_v < 0.3) {
  cat("Small effect\n")
} else if (cramers_v < 0.5) {
  cat("Medium effect\n")
} else {
  cat("Large effect\n")
}
cat("\n")

# =====================================================================
# 4. PAIRWISE COMPARISONS (FISHER'S EXACT TEST)
# =====================================================================
cat("=================================================================\n")
cat("         PAIRWISE COMPARISONS (FISHER'S EXACT TEST)\n")
cat("=================================================================\n\n")

cat("Fisher's Exact Test is used for pairwise comparisons (more accurate\n")
cat("for small sample sizes than chi-square).\n")
cat("Bonferroni correction applied: α = 0.05 / 3 = 0.0167\n\n")

pairwise_results <- data.frame(
  Comparison = character(),
  Model1_Eff = numeric(),
  Model2_Eff = numeric(),
  P_Value = numeric(),
  Significant = character(),
  stringsAsFactors = FALSE
)

# Define pairwise comparisons
comparisons <- list(
  c("T1_GPT-3.5", "T2_Claude", 1, 2),
  c("T1_GPT-3.5", "T3_GPT-4o", 1, 3),
  c("T2_Claude", "T3_GPT-4o", 2, 3)
)

for (comp in comparisons) {
  model1_name <- comp[1]
  model2_name <- comp[2]
  idx1 <- as.numeric(comp[3])
  idx2 <- as.numeric(comp[4])
  
  # Create 2x2 contingency table for this pair
  pair_table <- contingency_table[c(idx1, idx2), ]
  
  # Perform Fisher's exact test
  fisher_test <- fisher.test(pair_table)
  
  # Calculate effectiveness percentages
  eff1 <- (pair_table[1, 1] / sum(pair_table[1, ])) * 100
  eff2 <- (pair_table[2, 1] / sum(pair_table[2, ])) * 100
  
  # Determine significance (Bonferroni corrected)
  is_sig <- ifelse(fisher_test$p.value < 0.0167, 
                   "Yes* (p < 0.0167)", 
                   "No (p ≥ 0.0167)")
  
  pairwise_results <- rbind(pairwise_results, data.frame(
    Comparison = paste0(model1_name, " vs ", model2_name),
    Model1_Eff = round(eff1, 2),
    Model2_Eff = round(eff2, 2),
    P_Value = round(fisher_test$p.value, 4),
    Significant = is_sig,
    stringsAsFactors = FALSE
  ))
}

print(pairwise_results, row.names = FALSE)
cat("\n* Bonferroni-corrected significance level (α = 0.0167)\n\n")

# =====================================================================
# 5. PROPORTION TEST FOR EACH PAIRWISE COMPARISON
# =====================================================================
cat("=================================================================\n")
cat("           PROPORTION TEST (ALTERNATIVE APPROACH)\n")
cat("=================================================================\n\n")

cat("Two-proportion z-test for comparing effectiveness rates:\n\n")

prop_test_results <- data.frame(
  Comparison = character(),
  Z_Statistic = numeric(),
  P_Value = numeric(),
  CI_Lower = numeric(),
  CI_Upper = numeric(),
  Significant = character(),
  stringsAsFactors = FALSE
)

for (comp in comparisons) {
  model1_name <- comp[1]
  model2_name <- comp[2]
  idx1 <- as.numeric(comp[3])
  idx2 <- as.numeric(comp[4])
  
  # Get valid and total for each model
  valid <- contingency_table[c(idx1, idx2), 1]
  total <- rowSums(contingency_table[c(idx1, idx2), ])
  
  # Perform proportion test
  prop_test <- prop.test(valid, total)
  
  is_sig <- ifelse(prop_test$p.value < 0.05, 
                   "Yes (p < 0.05)", 
                   "No (p ≥ 0.05)")
  
  prop_test_results <- rbind(prop_test_results, data.frame(
    Comparison = paste0(model1_name, " vs ", model2_name),
    Z_Statistic = round(sqrt(prop_test$statistic), 4),
    P_Value = round(prop_test$p.value, 4),
    CI_Lower = round(prop_test$conf.int[1] * 100, 2),
    CI_Upper = round(prop_test$conf.int[2] * 100, 2),
    Significant = is_sig,
    stringsAsFactors = FALSE
  ))
}

print(prop_test_results, row.names = FALSE)
cat("\nCI = 95% Confidence Interval for difference in proportions\n\n")

# =====================================================================
# 6. DETAILED INTERPRETATION
# =====================================================================
cat("=================================================================\n")
cat("                  DETAILED INTERPRETATION\n")
cat("=================================================================\n\n")

cat("OVERALL EFFECTIVENESS RANKING:\n")
cat("------------------------------\n")
ranked <- summary_table[order(-summary_table$Effectiveness_Pct), ]
for (i in 1:nrow(ranked)) {
  cat(sprintf("%d. %s: %.2f%% (%d valid out of %d total)\n", 
              i, ranked$Model[i], ranked$Effectiveness_Pct[i], 
              ranked$Valid[i], ranked$Total_Tests[i]))
}
cat("\n")

cat("STATISTICAL FINDINGS:\n")
cat("--------------------\n")
if (chi_test$p.value < 0.05) {
  cat("✓ The chi-square test confirms significant differences in effectiveness\n")
  cat("  between the LLM models (p < 0.05).\n\n")
  
  cat("PAIRWISE ANALYSIS:\n")
  for (i in 1:nrow(pairwise_results)) {
    cat(sprintf("\n• %s:\n", pairwise_results$Comparison[i]))
    cat(sprintf("  Effectiveness: %.2f%% vs %.2f%%\n", 
                pairwise_results$Model1_Eff[i], 
                pairwise_results$Model2_Eff[i]))
    cat(sprintf("  p-value: %.4f → %s\n", 
                pairwise_results$P_Value[i],
                pairwise_results$Significant[i]))
    
    if (pairwise_results$P_Value[i] < 0.0167) {
      diff <- abs(pairwise_results$Model2_Eff[i] - pairwise_results$Model1_Eff[i])
      cat(sprintf("  Result: Statistically significant difference (Δ = %.2f%%)\n", diff))
    } else {
      cat("  Result: No significant difference after Bonferroni correction\n")
    }
  }
} else {
  cat("✗ The chi-square test did NOT find significant differences in effectiveness\n")
  cat("  between the LLM models (p ≥ 0.05).\n")
  cat("  All models perform similarly in terms of test validity.\n")
}

cat("\n\nPRACTICAL SIGNIFICANCE:\n")
cat("----------------------\n")
best_model <- ranked$Model[1]
best_eff <- ranked$Effectiveness_Pct[1]
worst_model <- ranked$Model[nrow(ranked)]
worst_eff <- ranked$Effectiveness_Pct[nrow(ranked)]
diff_percent <- best_eff - worst_eff

cat(sprintf("The best performing model (%s) achieved %.2f%% effectiveness,\n", 
            best_model, best_eff))
cat(sprintf("which is %.2f percentage points higher than the lowest performer\n", 
            diff_percent))
cat(sprintf("(%s at %.2f%%).\n\n", worst_model, worst_eff))

if (diff_percent > 10) {
  cat("This represents a SUBSTANTIAL practical difference (>10 percentage points).\n")
} else if (diff_percent > 5) {
  cat("This represents a MODERATE practical difference (5-10 percentage points).\n")
} else {
  cat("This represents a SMALL practical difference (<5 percentage points).\n")
}

cat("\n")
cat("=================================================================\n")
cat("                    ANALYSIS COMPLETE\n")
cat("=================================================================\n")

# =====================================================================
# 7. VISUALIZATIONS
# =====================================================================
cat("\n\nCreating visualizations...\n")

# Visualization 1: Bar plot of effectiveness by model
png("rq2_effectiveness_by_model.png", width = 800, height = 600, res = 100)
par(mar = c(5, 5, 4, 2))
barplot(summary_table$Effectiveness_Pct,
        names.arg = c("GPT-3.5", "Claude", "GPT-4o"),
        col = c("#E69F00", "#56B4E9", "#009E73"),
        main = "Test Generation Effectiveness by LLM Model",
        ylab = "Effectiveness (%)",
        xlab = "LLM Model",
        ylim = c(0, 100),
        las = 1,
        border = "black")
grid(nx = NA, ny = NULL)
# Add value labels on bars
text(x = seq(0.7, by = 1.2, length.out = 3), 
     y = summary_table$Effectiveness_Pct + 3, 
     labels = sprintf("%.1f%%", summary_table$Effectiveness_Pct),
     cex = 1.2, font = 2)
dev.off()

# Visualization 2: Stacked bar chart showing test outcomes
png("rq2_test_outcomes_stacked.png", width = 900, height = 600, res = 100)
par(mar = c(5, 5, 4, 8), xpd = TRUE)

outcome_matrix <- matrix(c(
  T1_totals[4], T2_totals[4], T3_totals[4],  # Valid
  T1_totals[2], T2_totals[2], T3_totals[2],  # Failures
  T1_totals[3], T2_totals[3], T3_totals[3]   # Skipped
), nrow = 3, byrow = TRUE)

# Convert to percentages
outcome_pct <- prop.table(outcome_matrix, 2) * 100

barplot(outcome_pct,
        names.arg = c("GPT-3.5", "Claude", "GPT-4o"),
        col = c("#2ECC40", "#FF4136", "#FFDC00"),
        main = "Test Outcome Distribution by LLM Model",
        ylab = "Percentage (%)",
        xlab = "LLM Model",
        ylim = c(0, 100),
        las = 1,
        border = "white",
        legend.text = c("Valid", "Failures", "Skipped"),
        args.legend = list(x = "topright", inset = c(-0.15, 0), bty = "n"))
grid(nx = NA, ny = NULL)
dev.off()

# Visualization 3: Effectiveness by project and model
png("rq2_effectiveness_by_project.png", width = 1000, height = 600, res = 100)
par(mar = c(6, 5, 4, 2))

effectiveness_matrix <- matrix(c(
  data$T1_Effectiveness,
  data$T2_Effectiveness,
  data$T3_Effectiveness
), nrow = 3, byrow = TRUE)

barplot(effectiveness_matrix,
        beside = TRUE,
        names.arg = data$Project,
        col = c("#E69F00", "#56B4E9", "#009E73"),
        main = "Test Generation Effectiveness by Project and Model",
        ylab = "Effectiveness (%)",
        xlab = "Project",
        ylim = c(0, 100),
        las = 2,
        border = "black",
        legend.text = c("GPT-3.5", "Claude", "GPT-4o"),
        args.legend = list(x = "topright", bty = "n"))
grid(nx = NA, ny = NULL)
dev.off()

# Visualization 4: Total tests generated by each model
png("rq2_total_tests_by_model.png", width = 800, height = 600, res = 100)
par(mar = c(5, 5, 4, 2))
barplot(summary_table$Total_Tests,
        names.arg = c("GPT-3.5", "Claude", "GPT-4o"),
        col = c("#E69F00", "#56B4E9", "#009E73"),
        main = "Total Test Cases Generated by LLM Model",
        ylab = "Number of Test Cases",
        xlab = "LLM Model",
        las = 1,
        border = "black")
grid(nx = NA, ny = NULL)
text(x = seq(0.7, by = 1.2, length.out = 3), 
     y = summary_table$Total_Tests + 15, 
     labels = summary_table$Total_Tests,
     cex = 1.2, font = 2)
dev.off()

cat("\nVisualizations saved to:\n")
cat("  - rq2_effectiveness_by_model.png\n")
cat("  - rq2_test_outcomes_stacked.png\n")
cat("  - rq2_effectiveness_by_project.png\n")
cat("  - rq2_total_tests_by_model.png\n")

cat("\n=================================================================\n")
cat("All analyses and visualizations completed successfully!\n")
cat("=================================================================\n")

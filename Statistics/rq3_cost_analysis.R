# Statistical Analysis of LLM Test Generation Cost
# Comparing GPT-3.5 (T1), Claude (T2), and GPT-4o (T3)
# 
# Research Question: Is there a cost difference for each LLM 
# in creating valid test cases?
#
# Cost Metrics Analyzed:
# 1. Total cost per test set (SET_COST)
# 2. Cost per test generated (PER_TEST)
# 3. Cost per VALID test (PER_VALID) - most important metric

# Read data
data <- read.csv("rq3.csv")

cat("=================================================================\n")
cat("     LLM TEST GENERATION COST ANALYSIS\n")
cat("     T1 = GPT-3.5 | T2 = Claude | T3 = GPT-4o\n")
cat("=================================================================\n\n")

cat("Research Question: Is there a cost difference between LLMs\n")
cat("for creating VALID test cases?\n\n")

# Display raw data
cat("RAW DATA:\n")
cat("---------\n")
print(data)
cat("\n")

# =====================================================================
# 1. DESCRIPTIVE STATISTICS
# =====================================================================
cat("=================================================================\n")
cat("                 DESCRIPTIVE STATISTICS\n")
cat("=================================================================\n\n")

# Calculate summary statistics for each metric and model
metrics <- list(
  list(name = "Total Set Cost ($)", 
       T1 = data$T1_SET_COST, T2 = data$T2_SET_COST, T3 = data$T3_SET_COST),
  list(name = "Cost per Test ($)", 
       T1 = data$T1_PER_TEST, T2 = data$T2_PER_TEST, T3 = data$T3_PER_TEST),
  list(name = "Cost per Valid Test ($)", 
       T1 = data$T1_PER_VALID, T2 = data$T2_PER_VALID, T3 = data$T3_PER_VALID)
)

for (metric in metrics) {
  cat(sprintf("\n%s:\n", metric$name))
  cat(strrep("-", nchar(metric$name) + 1), "\n", sep = "")
  
  summary_table <- data.frame(
    Model = c("T1 (GPT-3.5)", "T2 (Claude)", "T3 (GPT-4o)"),
    Mean = c(mean(metric$T1), mean(metric$T2), mean(metric$T3)),
    Median = c(median(metric$T1), median(metric$T2), median(metric$T3)),
    SD = c(sd(metric$T1), sd(metric$T2), sd(metric$T3)),
    Min = c(min(metric$T1), min(metric$T2), min(metric$T3)),
    Max = c(max(metric$T1), max(metric$T2), max(metric$T3))
  )
  
  print(summary_table, row.names = FALSE)
  cat("\n")
}

# =====================================================================
# 2. COST PER VALID TEST - PRIMARY ANALYSIS
# =====================================================================
cat("=================================================================\n")
cat("     PRIMARY ANALYSIS: COST PER VALID TEST\n")
cat("     (Most important metric - accounts for effectiveness)\n")
cat("=================================================================\n\n")

cat("RANKING BY MEAN COST PER VALID TEST:\n")
cat("------------------------------------\n")

valid_costs <- data.frame(
  Model = c("GPT-3.5", "Claude", "GPT-4o"),
  Mean_Cost = c(mean(data$T1_PER_VALID), 
                mean(data$T2_PER_VALID), 
                mean(data$T3_PER_VALID))
)

valid_costs <- valid_costs[order(valid_costs$Mean_Cost), ]

for (i in 1:nrow(valid_costs)) {
  cat(sprintf("%d. %s: $%.5f per valid test\n", 
              i, valid_costs$Model[i], valid_costs$Mean_Cost[i]))
}
cat("\n")

# Calculate cost differences
cheapest <- valid_costs$Mean_Cost[1]
most_expensive <- valid_costs$Mean_Cost[3]
cost_ratio <- most_expensive / cheapest

cat(sprintf("Cost Range: $%.5f to $%.5f\n", cheapest, most_expensive))
cat(sprintf("Most expensive is %.2fx more costly than cheapest\n\n", cost_ratio))

# =====================================================================
# 3. NORMALITY TESTING
# =====================================================================
cat("=================================================================\n")
cat("              NORMALITY TESTING (α = 0.05)\n")
cat("=================================================================\n\n")

cat("Shapiro-Wilk test for each cost metric:\n\n")

# Test normality for cost per valid test
normality_results <- data.frame(
  Model = character(),
  Metric = character(),
  W_Statistic = numeric(),
  P_Value = numeric(),
  Normal = character(),
  stringsAsFactors = FALSE
)

cost_metrics <- list(
  list(name = "Set Cost", T1 = data$T1_SET_COST, T2 = data$T2_SET_COST, T3 = data$T3_SET_COST),
  list(name = "Per Test", T1 = data$T1_PER_TEST, T2 = data$T2_PER_TEST, T3 = data$T3_PER_TEST),
  list(name = "Per Valid", T1 = data$T1_PER_VALID, T2 = data$T2_PER_VALID, T3 = data$T3_PER_VALID)
)

for (metric in cost_metrics) {
  for (model_name in c("GPT-3.5", "Claude", "GPT-4o")) {
    model_data <- switch(model_name,
                         "GPT-3.5" = metric$T1,
                         "Claude" = metric$T2,
                         "GPT-4o" = metric$T3)
    
    test <- shapiro.test(model_data)
    is_normal <- ifelse(test$p.value >= 0.05, "Yes", "No")
    
    normality_results <- rbind(normality_results, data.frame(
      Model = model_name,
      Metric = metric$name,
      W_Statistic = round(test$statistic, 4),
      P_Value = round(test$p.value, 4),
      Normal = is_normal,
      stringsAsFactors = FALSE
    ))
  }
}

print(normality_results, row.names = FALSE)

cat("\nINTERPRETATION:\n")
if (any(normality_results$P_Value < 0.05)) {
  cat("Some distributions are non-normal (p < 0.05).\n")
  cat("→ NON-PARAMETRIC tests (Friedman + Wilcoxon) are appropriate.\n\n")
} else {
  cat("All distributions appear normal (p ≥ 0.05).\n")
  cat("→ Could use parametric tests, but we'll use non-parametric for robustness.\n\n")
}

# =====================================================================
# 4. FRIEDMAN TEST (Overall Comparison)
# =====================================================================
cat("=================================================================\n")
cat("         FRIEDMAN TEST (Non-parametric Repeated Measures)\n")
cat("=================================================================\n\n")

cat("H0: No difference in costs between LLM models\n")
cat("H1: At least one model has different costs\n")
cat("Significance level: α = 0.05\n\n")

# Friedman test for each metric
friedman_results <- data.frame(
  Metric = character(),
  Chi_Square = numeric(),
  DF = numeric(),
  P_Value = numeric(),
  Significant = character(),
  stringsAsFactors = FALSE
)

for (metric in cost_metrics) {
  # Create matrix for Friedman test (rows = projects, columns = models)
  cost_matrix <- cbind(metric$T1, metric$T2, metric$T3)
  
  # Perform Friedman test
  friedman_test <- friedman.test(cost_matrix)
  
  is_sig <- ifelse(friedman_test$p.value < 0.05, 
                   "Yes (p < 0.05)", 
                   "No (p ≥ 0.05)")
  
  friedman_results <- rbind(friedman_results, data.frame(
    Metric = metric$name,
    Chi_Square = round(friedman_test$statistic, 4),
    DF = friedman_test$parameter,
    P_Value = round(friedman_test$p.value, 4),
    Significant = is_sig,
    stringsAsFactors = FALSE
  ))
}

print(friedman_results, row.names = FALSE)

cat("\nINTERPRETATION:\n")
for (i in 1:nrow(friedman_results)) {
  cat(sprintf("\n%s:\n", friedman_results$Metric[i]))
  if (friedman_results$P_Value[i] < 0.05) {
    cat(sprintf("  χ² = %.4f, p = %.4f → SIGNIFICANT\n", 
                friedman_results$Chi_Square[i], 
                friedman_results$P_Value[i]))
    cat("  → There ARE significant cost differences between models\n")
  } else {
    cat(sprintf("  χ² = %.4f, p = %.4f → NOT SIGNIFICANT\n", 
                friedman_results$Chi_Square[i], 
                friedman_results$P_Value[i]))
    cat("  → No significant cost differences detected\n")
  }
}
cat("\n")

# =====================================================================
# 5. PAIRWISE COMPARISONS (Wilcoxon Signed-Rank Tests)
# =====================================================================
cat("=================================================================\n")
cat("     PAIRWISE COMPARISONS (Wilcoxon Signed-Rank Tests)\n")
cat("=================================================================\n\n")

cat("Post-hoc pairwise comparisons for Cost per Valid Test\n")
cat("Bonferroni correction: α = 0.05 / 3 = 0.0167\n\n")

# Focus on cost per valid test (most important metric)
T1_valid <- data$T1_PER_VALID
T2_valid <- data$T2_PER_VALID
T3_valid <- data$T3_PER_VALID

comparisons <- list(
  list(name = "GPT-3.5 vs Claude", data1 = T1_valid, data2 = T2_valid),
  list(name = "GPT-3.5 vs GPT-4o", data1 = T1_valid, data2 = T3_valid),
  list(name = "Claude vs GPT-4o", data1 = T2_valid, data2 = T3_valid)
)

pairwise_results <- data.frame(
  Comparison = character(),
  Median_Diff = numeric(),
  V_Statistic = numeric(),
  P_Value = numeric(),
  Significant = character(),
  Winner = character(),
  stringsAsFactors = FALSE
)

for (comp in comparisons) {
  # Two-tailed test (no directional hypothesis for cost)
  test <- wilcox.test(comp$data1, comp$data2, 
                      paired = TRUE, 
                      exact = FALSE,
                      conf.int = TRUE)
  
  median_diff <- median(comp$data2 - comp$data1)
  is_sig <- ifelse(test$p.value < 0.0167, 
                   "Yes* (p < 0.0167)", 
                   "No (p ≥ 0.0167)")
  
  # Determine which is cheaper
  if (median_diff < 0) {
    winner <- strsplit(comp$name, " vs ")[[1]][2]  # Second model is cheaper
  } else if (median_diff > 0) {
    winner <- strsplit(comp$name, " vs ")[[1]][1]  # First model is cheaper
  } else {
    winner <- "Equal"
  }
  
  pairwise_results <- rbind(pairwise_results, data.frame(
    Comparison = comp$name,
    Median_Diff = round(median_diff, 5),
    V_Statistic = test$statistic,
    P_Value = round(test$p.value, 4),
    Significant = is_sig,
    Winner = winner,
    stringsAsFactors = FALSE
  ))
}

print(pairwise_results, row.names = FALSE)
cat("\n* Bonferroni-corrected significance level\n\n")

# =====================================================================
# 6. COST-EFFECTIVENESS ANALYSIS
# =====================================================================
cat("=================================================================\n")
cat("              COST-EFFECTIVENESS ANALYSIS\n")
cat("=================================================================\n\n")

cat("Combining cost data with effectiveness from RQ2:\n\n")

# Effectiveness from RQ2 analysis
effectiveness <- data.frame(
  Model = c("GPT-3.5", "Claude", "GPT-4o"),
  Effectiveness = c(78.11, 86.53, 77.78)
)

cost_effectiveness <- data.frame(
  Model = c("GPT-3.5", "Claude", "GPT-4o"),
  Cost_Per_Valid = c(mean(T1_valid), mean(T2_valid), mean(T3_valid)),
  Effectiveness_Pct = c(78.11, 86.53, 77.78)
)

# Calculate cost-effectiveness ratio (lower is better)
cost_effectiveness$CE_Ratio <- cost_effectiveness$Cost_Per_Valid / 
                                (cost_effectiveness$Effectiveness_Pct / 100)

# Calculate value score (higher is better)
cost_effectiveness$Value_Score <- (cost_effectiveness$Effectiveness_Pct / 100) / 
                                   cost_effectiveness$Cost_Per_Valid

# Rank by value score
cost_effectiveness <- cost_effectiveness[order(-cost_effectiveness$Value_Score), ]

cat("COST-EFFECTIVENESS RANKING:\n")
cat("---------------------------\n")
for (i in 1:nrow(cost_effectiveness)) {
  cat(sprintf("\n%d. %s:\n", i, cost_effectiveness$Model[i]))
  cat(sprintf("   Cost per valid test: $%.5f\n", 
              cost_effectiveness$Cost_Per_Valid[i]))
  cat(sprintf("   Effectiveness: %.2f%%\n", 
              cost_effectiveness$Effectiveness_Pct[i]))
  cat(sprintf("   Value Score: %.2f (effectiveness/cost)\n", 
              cost_effectiveness$Value_Score[i]))
}
cat("\n")

# =====================================================================
# 7. PROJECT-LEVEL ANALYSIS
# =====================================================================
cat("=================================================================\n")
cat("              PROJECT-LEVEL COST BREAKDOWN\n")
cat("=================================================================\n\n")

cat("Cost per Valid Test by Project:\n\n")

project_costs <- data.frame(
  Project = data$Project,
  GPT35 = data$T1_PER_VALID,
  Claude = data$T2_PER_VALID,
  GPT4o = data$T3_PER_VALID
)

# Add winner column
project_costs$Cheapest <- apply(project_costs[, c("GPT35", "Claude", "GPT4o")], 1, 
                                function(x) c("GPT-3.5", "Claude", "GPT-4o")[which.min(x)])

print(project_costs, row.names = FALSE)

cat("\n\nFREQUENCY OF BEING CHEAPEST:\n")
cat("----------------------------\n")
cheapest_counts <- table(project_costs$Cheapest)
for (model in names(cheapest_counts)) {
  cat(sprintf("%s: %d/%d projects (%.0f%%)\n", 
              model, cheapest_counts[model], nrow(data),
              (cheapest_counts[model] / nrow(data)) * 100))
}
cat("\n")

# =====================================================================
# 8. DETAILED INTERPRETATION
# =====================================================================
cat("=================================================================\n")
cat("                  DETAILED INTERPRETATION\n")
cat("=================================================================\n\n")

cat("PRIMARY QUESTION: Is there a cost difference between LLMs?\n")
cat("-----------------------------------------------------------\n\n")

# Check if Friedman test for Per Valid was significant
per_valid_friedman <- friedman_results[friedman_results$Metric == "Per Valid", ]

if (per_valid_friedman$P_Value < 0.05) {
  cat("✓ YES - Friedman test confirms significant cost differences\n")
  cat(sprintf("  (χ² = %.4f, p = %.4f)\n\n", 
              per_valid_friedman$Chi_Square, 
              per_valid_friedman$P_Value))
  
  cat("PAIRWISE FINDINGS:\n")
  for (i in 1:nrow(pairwise_results)) {
    cat(sprintf("\n• %s:\n", pairwise_results$Comparison[i]))
    cat(sprintf("  Median difference: $%.5f\n", pairwise_results$Median_Diff[i]))
    cat(sprintf("  p-value: %.4f → %s\n", 
                pairwise_results$P_Value[i], 
                pairwise_results$Significant[i]))
    cat(sprintf("  Cheaper model: %s\n", pairwise_results$Winner[i]))
  }
} else {
  cat("✗ NO - Friedman test did not find significant cost differences\n")
  cat(sprintf("  (χ² = %.4f, p = %.4f)\n\n", 
              per_valid_friedman$Chi_Square, 
              per_valid_friedman$P_Value))
}

cat("\n\nCOST ANALYSIS SUMMARY:\n")
cat("----------------------\n")

# Determine best value
best_value <- cost_effectiveness$Model[1]
best_cost <- cost_effectiveness$Cost_Per_Valid[1]
best_eff <- cost_effectiveness$Effectiveness_Pct[1]

worst_value <- cost_effectiveness$Model[nrow(cost_effectiveness)]
worst_cost <- cost_effectiveness$Cost_Per_Valid[nrow(cost_effectiveness)]
worst_eff <- cost_effectiveness$Effectiveness_Pct[nrow(cost_effectiveness)]

cat(sprintf("Best value: %s ($%.5f, %.2f%% effective)\n", 
            best_value, best_cost, best_eff))
cat(sprintf("Worst value: %s ($%.5f, %.2f%% effective)\n\n", 
            worst_value, worst_cost, worst_eff))

cat("KEY INSIGHTS:\n")
cat("1. Raw Cost Perspective:\n")
cheapest_model <- valid_costs$Model[1]
cat(sprintf("   → %s has the lowest mean cost per valid test ($%.5f)\n", 
            cheapest_model, valid_costs$Mean_Cost[1]))

cat("\n2. Effectiveness-Adjusted Perspective:\n")
cat(sprintf("   → %s offers the best value (highest effectiveness/cost ratio)\n", 
            best_value))

cat("\n3. Practical Consideration:\n")
if (best_value != cheapest_model) {
  cat(sprintf("   → %s is cheaper, but %s provides better value overall\n", 
              cheapest_model, best_value))
  cat("   → Choice depends on whether you optimize for absolute cost or value\n")
} else {
  cat(sprintf("   → %s is both cheapest AND best value!\n", best_value))
}

cat("\n")
cat("=================================================================\n")
cat("                    ANALYSIS COMPLETE\n")
cat("=================================================================\n")

# =====================================================================
# 9. VISUALIZATIONS
# =====================================================================
cat("\n\nCreating visualizations...\n")

# Visualization 1: Cost per valid test by model
png("rq3_cost_per_valid_by_model.png", width = 800, height = 600, res = 100)
par(mar = c(5, 6, 4, 2))

means <- c(mean(T1_valid), mean(T2_valid), mean(T3_valid))
barplot(means,
        names.arg = c("GPT-3.5", "Claude", "GPT-4o"),
        col = c("#E69F00", "#56B4E9", "#009E73"),
        main = "Mean Cost per Valid Test by LLM Model",
        ylab = "Cost per Valid Test ($)",
        xlab = "LLM Model",
        las = 1,
        border = "black")
grid(nx = NA, ny = NULL)
text(x = seq(0.7, by = 1.2, length.out = 3), 
     y = means + max(means) * 0.05, 
     labels = sprintf("$%.5f", means),
     cex = 1.2, font = 2)
dev.off()

# Visualization 2: Cost comparison by project
png("rq3_cost_by_project_comparison.png", width = 1000, height = 600, res = 100)
par(mar = c(7, 5, 4, 2))

cost_matrix <- matrix(c(
  data$T1_PER_VALID,
  data$T2_PER_VALID,
  data$T3_PER_VALID
), nrow = 3, byrow = TRUE)

barplot(cost_matrix,
        beside = TRUE,
        names.arg = data$Project,
        col = c("#E69F00", "#56B4E9", "#009E73"),
        main = "Cost per Valid Test by Project and Model",
        ylab = "Cost per Valid Test ($)",
        xlab = "",
        las = 2,
        border = "black",
        legend.text = c("GPT-3.5", "Claude", "GPT-4o"),
        args.legend = list(x = "topright", bty = "n"))
grid(nx = NA, ny = NULL)
dev.off()

# Visualization 3: Cost-effectiveness scatter plot
png("rq3_cost_effectiveness_scatter.png", width = 800, height = 600, res = 100)
par(mar = c(5, 5, 4, 2))

plot(cost_effectiveness$Cost_Per_Valid, 
     cost_effectiveness$Effectiveness_Pct,
     pch = 19,
     cex = 3,
     col = c("#E69F00", "#56B4E9", "#009E73")[match(cost_effectiveness$Model, 
                                                      c("GPT-3.5", "Claude", "GPT-4o"))],
     main = "Cost-Effectiveness Analysis",
     xlab = "Cost per Valid Test ($)",
     ylab = "Effectiveness (%)",
     xlim = c(0, max(cost_effectiveness$Cost_Per_Valid) * 1.1),
     ylim = c(75, 90),
     las = 1)
grid()

# Add labels
text(cost_effectiveness$Cost_Per_Valid, 
     cost_effectiveness$Effectiveness_Pct,
     labels = cost_effectiveness$Model,
     pos = 3,
     cex = 0.9,
     font = 2)

# Add reference lines
abline(v = mean(cost_effectiveness$Cost_Per_Valid), lty = 2, col = "gray50")
abline(h = mean(cost_effectiveness$Effectiveness_Pct), lty = 2, col = "gray50")

# Add quadrant labels
text(min(cost_effectiveness$Cost_Per_Valid) * 0.8, 89, 
     "Low Cost\nHigh Effectiveness\n(BEST)", cex = 0.8, col = "darkgreen", font = 2)
text(max(cost_effectiveness$Cost_Per_Valid) * 1.05, 76, 
     "High Cost\nLow Effectiveness\n(WORST)", cex = 0.8, col = "darkred", font = 2)

dev.off()

# Visualization 4: Boxplot comparison of all three metrics
png("rq3_cost_metrics_boxplots.png", width = 1200, height = 400, res = 100)
par(mfrow = c(1, 3), mar = c(5, 5, 4, 2))

# Set Cost
boxplot(data$T1_SET_COST, data$T2_SET_COST, data$T3_SET_COST,
        names = c("GPT-3.5", "Claude", "GPT-4o"),
        col = c("#E69F00", "#56B4E9", "#009E73"),
        main = "Total Set Cost",
        ylab = "Cost ($)",
        las = 1,
        border = "black")
grid(nx = NA, ny = NULL)

# Per Test
boxplot(data$T1_PER_TEST, data$T2_PER_TEST, data$T3_PER_TEST,
        names = c("GPT-3.5", "Claude", "GPT-4o"),
        col = c("#E69F00", "#56B4E9", "#009E73"),
        main = "Cost per Test",
        ylab = "Cost ($)",
        las = 1,
        border = "black")
grid(nx = NA, ny = NULL)

# Per Valid Test
boxplot(data$T1_PER_VALID, data$T2_PER_VALID, data$T3_PER_VALID,
        names = c("GPT-3.5", "Claude", "GPT-4o"),
        col = c("#E69F00", "#56B4E9", "#009E73"),
        main = "Cost per Valid Test",
        ylab = "Cost ($)",
        las = 1,
        border = "black")
grid(nx = NA, ny = NULL)

dev.off()

cat("\nVisualizations saved to:\n")
cat("  - rq3_cost_per_valid_by_model.png\n")
cat("  - rq3_cost_by_project_comparison.png\n")
cat("  - rq3_cost_effectiveness_scatter.png\n")
cat("  - rq3_cost_metrics_boxplots.png\n")

cat("\n=================================================================\n")
cat("All analyses and visualizations completed successfully!\n")
cat("=================================================================\n")
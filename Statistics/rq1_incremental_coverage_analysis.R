# Statistical Analysis of Incremental Test Coverage
# Comparing Coverage Across Test Sets: T0, T1, T2, T3
# 
# Data Structure: 
# - T1 is a SUPERSET of T0 (T1 includes all tests from T0 plus new ones)
# - T2 is a SUPERSET of T1 (T2 includes all tests from T1 plus new ones)
# - T3 is a SUPERSET of T2 (T3 includes all tests from T2 plus new ones)
#
# Statistical Approach:
# - Since test sets are incremental, use PAIRED tests (Wilcoxon signed-rank)
# - Test for DIRECTIONAL improvements (one-tailed tests where appropriate)
# - Analyze both instruction coverage and branch coverage

# Read data
instructions <- read.csv("rq1_instructions.csv")
branches <- read.csv("rq1_branches.csv")

cat("=================================================================\n")
cat("     INCREMENTAL TEST COVERAGE ANALYSIS\n")
cat("     Instruction Coverage & Branch Coverage\n")
cat("=================================================================\n\n")

cat("DATA STRUCTURE:\n")
cat("---------------\n")
cat("Test sets are INCREMENTAL (nested/cumulative):\n")
cat("  T0: Initial test set (baseline)\n")
cat("  T1: T0 + additional tests (superset of T0)\n")
cat("  T2: T1 + additional tests (superset of T1)\n")
cat("  T3: T2 + additional tests (superset of T2)\n\n")

cat("Research Question: Does coverage significantly improve as we\n")
cat("add more tests to the test suite?\n\n")

# Display raw data
cat("=================================================================\n")
cat("                    RAW DATA\n")
cat("=================================================================\n\n")

cat("INSTRUCTION COVERAGE (%):\n")
print(instructions)
cat("\n")

cat("BRANCH COVERAGE (%):\n")
print(branches)
cat("\n")

# =====================================================================
# FUNCTION: Perform comprehensive analysis for one coverage metric
# =====================================================================
analyze_coverage <- function(data, metric_name) {
  cat("=================================================================\n")
  cat(sprintf("               %s ANALYSIS\n", toupper(metric_name)))
  cat("=================================================================\n\n")
  
  # Extract data
  T0 <- data$T0
  T1 <- data$T1
  T2 <- data$T2
  T3 <- data$T3
  
  # ===================================================================
  # 1. DESCRIPTIVE STATISTICS
  # ===================================================================
  cat("-------------------------------------------------------------------\n")
  cat("1. DESCRIPTIVE STATISTICS\n")
  cat("-------------------------------------------------------------------\n\n")
  
  desc_stats <- data.frame(
    Test_Set = c("T0", "T1", "T2", "T3"),
    Mean = c(mean(T0), mean(T1), mean(T2), mean(T3)),
    Median = c(median(T0), median(T1), median(T2), median(T3)),
    SD = c(sd(T0), sd(T1), sd(T2), sd(T3)),
    Min = c(min(T0), min(T1), min(T2), min(T3)),
    Max = c(max(T0), max(T1), max(T2), max(T3)),
    Range = c(max(T0) - min(T0), max(T1) - min(T1), 
              max(T2) - min(T2), max(T3) - min(T3))
  )
  
  print(desc_stats, row.names = FALSE)
  cat("\n")
  
  # Calculate improvements
  cat("INCREMENTAL IMPROVEMENTS:\n")
  cat(sprintf("  T0 → T1: Mean Δ = %.2f%% (from %.2f%% to %.2f%%)\n", 
              mean(T1) - mean(T0), mean(T0), mean(T1)))
  cat(sprintf("  T1 → T2: Mean Δ = %.2f%% (from %.2f%% to %.2f%%)\n", 
              mean(T2) - mean(T1), mean(T1), mean(T2)))
  cat(sprintf("  T2 → T3: Mean Δ = %.2f%% (from %.2f%% to %.2f%%)\n", 
              mean(T3) - mean(T2), mean(T2), mean(T3)))
  cat(sprintf("  T0 → T3: Mean Δ = %.2f%% (from %.2f%% to %.2f%%) [TOTAL]\n\n", 
              mean(T3) - mean(T0), mean(T0), mean(T3)))
  
  # ===================================================================
  # 2. NORMALITY TESTING (Shapiro-Wilk)
  # ===================================================================
  cat("-------------------------------------------------------------------\n")
  cat("2. SHAPIRO-WILK NORMALITY TEST (α = 0.05)\n")
  cat("-------------------------------------------------------------------\n\n")
  
  cat("Testing normality of DIFFERENCES (appropriate for paired tests):\n\n")
  
  # Test differences for normality
  diff_T0_T1 <- T1 - T0
  diff_T1_T2 <- T2 - T1
  diff_T2_T3 <- T3 - T2
  diff_T0_T3 <- T3 - T0
  
  shapiro_results <- data.frame(
    Comparison = c("T1 - T0", "T2 - T1", "T3 - T2", "T3 - T0"),
    W_Statistic = numeric(4),
    P_Value = numeric(4),
    Normal = character(4),
    stringsAsFactors = FALSE
  )
  
  diffs <- list(diff_T0_T1, diff_T1_T2, diff_T2_T3, diff_T0_T3)
  
  for (i in 1:4) {
    test <- shapiro.test(diffs[[i]])
    shapiro_results$W_Statistic[i] <- round(test$statistic, 4)
    shapiro_results$P_Value[i] <- round(test$p.value, 4)
    shapiro_results$Normal[i] <- ifelse(test$p.value >= 0.05, 
                                        "Yes (p ≥ 0.05)", 
                                        "No (p < 0.05)")
  }
  
  print(shapiro_results, row.names = FALSE)
  
  cat("\nINTERPRETATION:\n")
  if (any(shapiro_results$P_Value < 0.05)) {
    cat("At least one set of differences is non-normal.\n")
    cat("→ NON-PARAMETRIC tests (Wilcoxon signed-rank) are appropriate.\n\n")
  } else {
    cat("All differences appear normally distributed.\n")
    cat("→ Could use parametric (paired t-test) or non-parametric tests.\n")
    cat("→ Using Wilcoxon signed-rank (more conservative, no assumptions).\n\n")
  }
  
  # ===================================================================
  # 3. WILCOXON SIGNED-RANK TEST (Paired, One-Tailed)
  # ===================================================================
  cat("-------------------------------------------------------------------\n")
  cat("3. WILCOXON SIGNED-RANK TEST (Paired, One-Tailed)\n")
  cat("-------------------------------------------------------------------\n\n")
  
  cat("Hypothesis for each comparison:\n")
  cat("  H0: No improvement in coverage (median difference ≤ 0)\n")
  cat("  H1: Coverage INCREASES (median difference > 0)\n")
  cat("  Alternative: 'greater' (one-tailed test)\n")
  cat("  Significance level: α = 0.05\n\n")
  
  comparisons <- list(
    list(name = "T0 vs T1", data1 = T0, data2 = T1),
    list(name = "T1 vs T2", data1 = T1, data2 = T2),
    list(name = "T2 vs T3", data1 = T2, data2 = T3),
    list(name = "T0 vs T3", data1 = T0, data2 = T3)
  )
  
  wilcoxon_results <- data.frame(
    Comparison = character(),
    Median_Diff = numeric(),
    V_Statistic = numeric(),
    P_Value = numeric(),
    Significant = character(),
    Direction = character(),
    stringsAsFactors = FALSE
  )
  
  for (comp in comparisons) {
    # One-tailed test (alternative = "greater")
    test <- wilcox.test(comp$data2, comp$data1, 
                        paired = TRUE, 
                        alternative = "greater",
                        exact = FALSE,
                        conf.int = FALSE)
    
    median_diff <- median(comp$data2 - comp$data1)
    is_sig <- ifelse(test$p.value < 0.05, 
                     "Yes (p < 0.05)", 
                     "No (p ≥ 0.05)")
    
    direction <- ifelse(median_diff > 0, "Increase ↑", 
                       ifelse(median_diff < 0, "Decrease ↓", "No change ="))
    
    wilcoxon_results <- rbind(wilcoxon_results, data.frame(
      Comparison = comp$name,
      Median_Diff = round(median_diff, 2),
      V_Statistic = test$statistic,
      P_Value = round(test$p.value, 4),
      Significant = is_sig,
      Direction = direction,
      stringsAsFactors = FALSE
    ))
  }
  
  print(wilcoxon_results, row.names = FALSE)
  cat("\n")
  
  # ===================================================================
  # 4. EFFECT SIZE (for significant results)
  # ===================================================================
  cat("-------------------------------------------------------------------\n")
  cat("4. EFFECT SIZE ANALYSIS\n")
  cat("-------------------------------------------------------------------\n\n")
  
  cat("Calculating effect size (r = Z / sqrt(N)) for significant comparisons:\n\n")
  
  n <- length(T0)
  
  for (i in 1:nrow(wilcoxon_results)) {
    comp_name <- wilcoxon_results$Comparison[i]
    v_stat <- wilcoxon_results$V_Statistic[i]
    p_val <- wilcoxon_results$P_Value[i]
    
    # Approximate Z from V statistic
    z_approx <- qnorm(1 - p_val)
    r <- abs(z_approx) / sqrt(n)
    
    effect_interp <- ifelse(r < 0.3, "Small", 
                           ifelse(r < 0.5, "Medium", "Large"))
    
    cat(sprintf("%s:\n", comp_name))
    cat(sprintf("  Effect size r ≈ %.3f (%s effect)\n", r, effect_interp))
    
    if (wilcoxon_results$Significant[i] == "Yes (p < 0.05)") {
      cat(sprintf("  → Statistically significant improvement (p = %.4f)\n", p_val))
    } else {
      cat(sprintf("  → Not statistically significant (p = %.4f)\n", p_val))
    }
    cat("\n")
  }
  
  # ===================================================================
  # 5. DETAILED INTERPRETATION
  # ===================================================================
  cat("-------------------------------------------------------------------\n")
  cat("5. DETAILED INTERPRETATION\n")
  cat("-------------------------------------------------------------------\n\n")
  
  cat(sprintf("OVERALL TREND: %s coverage ranges from %.2f%% (T0) to %.2f%% (T3)\n\n",
              metric_name, mean(T0), mean(T3)))
  
  sig_count <- sum(wilcoxon_results$P_Value < 0.05)
  
  if (sig_count == 0) {
    cat("RESULT: No statistically significant improvements detected.\n")
    cat("Despite incremental test additions, coverage improvements are not\n")
    cat("significant at the 0.05 level. This could indicate:\n")
    cat("  - The additional tests don't target new code areas\n")
    cat("  - Coverage was already high at baseline (ceiling effect)\n")
    cat("  - Sample size is too small to detect improvements\n\n")
  } else if (sig_count == 4) {
    cat("RESULT: ALL comparisons show statistically significant improvements!\n")
    cat("Each increment of tests (T0→T1, T1→T2, T2→T3, and overall T0→T3)\n")
    cat("leads to significant coverage gains. This demonstrates:\n")
    cat("  - Consistent and meaningful improvements at each stage\n")
    cat("  - Each new test batch contributes to coverage growth\n")
    cat("  - The incremental approach is effective\n\n")
  } else {
    cat(sprintf("RESULT: %d out of 4 comparisons show significant improvements.\n\n", sig_count))
    
    cat("Significant improvements in:\n")
    sig_comps <- wilcoxon_results$Comparison[wilcoxon_results$P_Value < 0.05]
    for (comp in sig_comps) {
      idx <- which(wilcoxon_results$Comparison == comp)
      cat(sprintf("  • %s: Δ = %.2f%% (p = %.4f)\n", 
                  comp, 
                  wilcoxon_results$Median_Diff[idx],
                  wilcoxon_results$P_Value[idx]))
    }
    cat("\n")
    
    if (length(sig_comps) < 4) {
      cat("No significant improvement in:\n")
      nonsig_comps <- wilcoxon_results$Comparison[wilcoxon_results$P_Value >= 0.05]
      for (comp in nonsig_comps) {
        idx <- which(wilcoxon_results$Comparison == comp)
        cat(sprintf("  • %s: Δ = %.2f%% (p = %.4f)\n", 
                    comp, 
                    wilcoxon_results$Median_Diff[idx],
                    wilcoxon_results$P_Value[idx]))
      }
      cat("\n")
    }
  }
  
  # Project-level analysis
  cat("PROJECT-LEVEL IMPROVEMENTS:\n")
  for (i in 1:nrow(data)) {
    cat(sprintf("\n%s:\n", data$Project[i]))
    cat(sprintf("  T0: %.2f%% → T1: %.2f%% → T2: %.2f%% → T3: %.2f%%\n", 
                T0[i], T1[i], T2[i], T3[i]))
    cat(sprintf("  Total improvement: %.2f percentage points\n", T3[i] - T0[i]))
    
    # Check for plateaus
    if (T2[i] == T3[i]) {
      cat("  ⚠ Plateau: No improvement from T2 to T3\n")
    }
    if (T1[i] == T0[i]) {
      cat("  ⚠ Plateau: No improvement from T0 to T1\n")
    }
  }
  
  cat("\n")
  return(wilcoxon_results)
}

# =====================================================================
# ANALYZE BOTH METRICS
# =====================================================================

cat("\n\n")
cat("#################################################################\n")
cat("#                                                               #\n")
cat("#                 INSTRUCTION COVERAGE ANALYSIS                 #\n")
cat("#                                                               #\n")
cat("#################################################################\n\n")

instr_results <- analyze_coverage(instructions, "Instruction Coverage")

cat("\n\n")
cat("#################################################################\n")
cat("#                                                               #\n")
cat("#                   BRANCH COVERAGE ANALYSIS                    #\n")
cat("#                                                               #\n")
cat("#################################################################\n\n")

branch_results <- analyze_coverage(branches, "Branch Coverage")

# =====================================================================
# COMPARATIVE SUMMARY
# =====================================================================
cat("\n\n")
cat("=================================================================\n")
cat("            COMPARATIVE SUMMARY & CONCLUSIONS\n")
cat("=================================================================\n\n")

cat("INSTRUCTION COVERAGE RESULTS:\n")
cat("-----------------------------\n")
print(instr_results[, c("Comparison", "Median_Diff", "P_Value", "Significant")], 
      row.names = FALSE)

cat("\n\nBRANCH COVERAGE RESULTS:\n")
cat("------------------------\n")
print(branch_results[, c("Comparison", "Median_Diff", "P_Value", "Significant")], 
      row.names = FALSE)

cat("\n\nKEY FINDINGS:\n")
cat("-------------\n")

# Count significant results
instr_sig <- sum(instr_results$P_Value < 0.05)
branch_sig <- sum(branch_results$P_Value < 0.05)

cat(sprintf("• Instruction Coverage: %d/4 comparisons significant\n", instr_sig))
cat(sprintf("• Branch Coverage: %d/4 comparisons significant\n\n", branch_sig))

if (instr_sig > branch_sig) {
  cat("→ Instruction coverage shows MORE significant improvements than branch coverage.\n")
} else if (branch_sig > instr_sig) {
  cat("→ Branch coverage shows MORE significant improvements than instruction coverage.\n")
} else {
  cat("→ Both metrics show SIMILAR patterns of improvement.\n")
}

cat("\n\nRECOMMENDATIONS:\n")
cat("----------------\n")

# Check T0->T3 comparison
instr_overall_sig <- instr_results$P_Value[instr_results$Comparison == "T0 vs T3"] < 0.05
branch_overall_sig <- branch_results$P_Value[branch_results$Comparison == "T0 vs T3"] < 0.05

if (instr_overall_sig && branch_overall_sig) {
  cat("✓ The incremental test generation approach is EFFECTIVE.\n")
  cat("  Both instruction and branch coverage show significant overall improvements.\n")
  cat("  Continue this incremental strategy for future test generation.\n")
} else if (!instr_overall_sig && !branch_overall_sig) {
  cat("⚠ The incremental approach shows LIMITED effectiveness.\n")
  cat("  Consider alternative strategies:\n")
  cat("    - Target specific uncovered areas\n")
  cat("    - Use coverage-guided test generation\n")
  cat("    - Analyze why additional tests don't improve coverage\n")
} else {
  cat("⚡ MIXED results - one metric improves significantly, the other doesn't.\n")
  cat("  Investigate why instruction and branch coverage diverge.\n")
}

cat("\n")
cat("=================================================================\n")
cat("                    ANALYSIS COMPLETE\n")
cat("=================================================================\n")

# =====================================================================
# VISUALIZATIONS
# =====================================================================
cat("\n\nCreating visualizations...\n")

# Visualization 1: Line plot for instruction coverage
png("rq1_instruction_coverage_trends.png", width = 1000, height = 600, res = 100)
par(mar = c(5, 5, 4, 8), xpd = TRUE)

plot(1:4, rep(0, 4), type="n", 
     xlim=c(1, 4), 
     ylim=c(0, 100),
     main = "Instruction Coverage Trends by Project",
     xlab = "Test Set",
     ylab = "Coverage (%)",
     xaxt = "n",
     las = 1)
axis(1, at = 1:4, labels = c("T0", "T1", "T2", "T3"))
grid()

colors <- c("#E41A1C", "#377EB8", "#4DAF4A", "#984EA3", "#FF7F00")
for (i in 1:nrow(instructions)) {
  values <- c(instructions$T0[i], instructions$T1[i], 
              instructions$T2[i], instructions$T3[i])
  lines(1:4, values, col=colors[i], lwd=2.5)
  points(1:4, values, col=colors[i], pch=19, cex=1.8)
}

legend("right", inset = c(-0.2, 0),
       legend = instructions$Project, 
       col = colors, 
       lwd = 2.5, 
       pch = 19, 
       cex = 0.9,
       bty = "n")
dev.off()

# Visualization 2: Line plot for branch coverage
png("rq1_branch_coverage_trends.png", width = 1000, height = 600, res = 100)
par(mar = c(5, 5, 4, 8), xpd = TRUE)

plot(1:4, rep(0, 4), type="n", 
     xlim=c(1, 4), 
     ylim=c(0, 100),
     main = "Branch Coverage Trends by Project",
     xlab = "Test Set",
     ylab = "Coverage (%)",
     xaxt = "n",
     las = 1)
axis(1, at = 1:4, labels = c("T0", "T1", "T2", "T3"))
grid()

for (i in 1:nrow(branches)) {
  values <- c(branches$T0[i], branches$T1[i], 
              branches$T2[i], branches$T3[i])
  lines(1:4, values, col=colors[i], lwd=2.5)
  points(1:4, values, col=colors[i], pch=19, cex=1.8)
}

legend("right", inset = c(-0.2, 0),
       legend = branches$Project, 
       col = colors, 
       lwd = 2.5, 
       pch = 19, 
       cex = 0.9,
       bty = "n")
dev.off()

# Visualization 3: Box plots comparing test sets
png("rq1_coverage_boxplots_comparison.png", width = 1200, height = 600, res = 100)
par(mfrow = c(1, 2), mar = c(5, 5, 4, 2))

# Instruction coverage boxplot
boxplot(instructions$T0, instructions$T1, instructions$T2, instructions$T3,
        names = c("T0", "T1", "T2", "T3"),
        main = "Instruction Coverage Distribution",
        ylab = "Coverage (%)",
        xlab = "Test Set",
        col = c("#E69F00", "#56B4E9", "#009E73", "#F0E442"),
        border = "black",
        ylim = c(0, 100),
        las = 1)
grid(nx = NA, ny = NULL)

# Branch coverage boxplot
boxplot(branches$T0, branches$T1, branches$T2, branches$T3,
        names = c("T0", "T1", "T2", "T3"),
        main = "Branch Coverage Distribution",
        ylab = "Coverage (%)",
        xlab = "Test Set",
        col = c("#E69F00", "#56B4E9", "#009E73", "#F0E442"),
        border = "black",
        ylim = c(0, 100),
        las = 1)
grid(nx = NA, ny = NULL)

dev.off()

# Visualization 4: Mean coverage progression
png("rq1_mean_coverage_progression.png", width = 900, height = 600, res = 100)
par(mar = c(5, 5, 4, 8), xpd = TRUE)

instr_means <- c(mean(instructions$T0), mean(instructions$T1), 
                 mean(instructions$T2), mean(instructions$T3))
branch_means <- c(mean(branches$T0), mean(branches$T1), 
                  mean(branches$T2), mean(branches$T3))

plot(1:4, instr_means, type="b", 
     col="#E41A1C", lwd=3, pch=19, cex=2,
     xlim=c(1, 4), ylim=c(min(c(instr_means, branch_means)) - 5, 100),
     main = "Mean Coverage Progression",
     xlab = "Test Set",
     ylab = "Mean Coverage (%)",
     xaxt = "n",
     las = 1)
lines(1:4, branch_means, type="b", col="#377EB8", lwd=3, pch=17, cex=2)
axis(1, at = 1:4, labels = c("T0", "T1", "T2", "T3"))
grid()

legend("right", inset = c(-0.25, 0),
       legend = c("Instruction Coverage", "Branch Coverage"),
       col = c("#E41A1C", "#377EB8"),
       lwd = 3,
       pch = c(19, 17),
       cex = 1,
       bty = "n")

# Add value labels
text(1:4, instr_means + 2, sprintf("%.1f%%", instr_means), col="#E41A1C", cex=0.9, font=2)
text(1:4, branch_means - 2, sprintf("%.1f%%", branch_means), col="#377EB8", cex=0.9, font=2)

dev.off()

cat("\nVisualizations saved to:\n")
cat("  - rq1_instruction_coverage_trends.png\n")
cat("  - rq1_branch_coverage_trends.png\n")
cat("  - rq1_coverage_boxplots_comparison.png\n")
cat("  - rq1_mean_coverage_progression.png\n")

cat("\n=================================================================\n")
cat("All analyses and visualizations completed successfully!\n")
cat("=================================================================\n")

/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.crftables.util.styling.rules;

import global.moja.crftables.daos.FormattingRule;
import global.moja.crftables.daos.ReportingVariableFormattingRule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
@Component
public class SummaryReportLandConvertedToLandFormattingRules {

    @Value("${summary.report.palette.index}")
    private Integer SUMMARY_REPORT_PALETTE_INDEX;

    @Value("${net.carbon.dioxide.emissions.removals.reporting.variable.id}")
    private Long NET_CARBON_DIOXIDE_EMISSIONS_REMOVALS_REPORTING_VARIABLE;

    @Value("${methane.reporting.variable.id}")
    private Long METHANE_REPORTING_VARIABLE;

    @Value("${nitrous.oxide.reporting.variable.id}")
    private Long NITROUS_OXIDE_REPORTING_VARIABLE;

    private FormattingRule titleFormattingRule;
    private List<ReportingVariableFormattingRule> reportingVariablesFormattingRules;

    @PostConstruct
    private void init() {

        titleFormattingRule =
                FormattingRule
                        .builder()
                        .formattingRulePaletteIndex(SUMMARY_REPORT_PALETTE_INDEX)
                        .formattingRuleColumn(0)
                        .formattingRuleRow(15)
                        .build();

        reportingVariablesFormattingRules =
                Arrays.asList(
                        ReportingVariableFormattingRule
                                .builder()
                                .reportingVariableId(NET_CARBON_DIOXIDE_EMISSIONS_REMOVALS_REPORTING_VARIABLE)
                                .reportingVariableTitleFormattingRule(
                                        FormattingRule
                                                .builder()
                                                .formattingRulePaletteIndex(SUMMARY_REPORT_PALETTE_INDEX)
                                                .formattingRuleColumn(1)
                                                .formattingRuleRow(13)
                                                .build())
                                .reportingVariableValueFormattingRule(
                                        FormattingRule
                                                .builder()
                                                .formattingRulePaletteIndex(SUMMARY_REPORT_PALETTE_INDEX)
                                                .formattingRuleColumn(2)
                                                .formattingRuleRow(13)
                                                .build())
                                .build(),

                        ReportingVariableFormattingRule
                                .builder()
                                .reportingVariableId(METHANE_REPORTING_VARIABLE)
                                .reportingVariableTitleFormattingRule(
                                        FormattingRule
                                                .builder()
                                                .formattingRulePaletteIndex(SUMMARY_REPORT_PALETTE_INDEX)
                                                .formattingRuleColumn(1)
                                                .formattingRuleRow(14)
                                                .build())
                                .reportingVariableValueFormattingRule(
                                        FormattingRule
                                                .builder()
                                                .formattingRulePaletteIndex(SUMMARY_REPORT_PALETTE_INDEX)
                                                .formattingRuleColumn(2)
                                                .formattingRuleRow(14)
                                                .build())
                                .build(),

                        ReportingVariableFormattingRule
                                .builder()
                                .reportingVariableId(NITROUS_OXIDE_REPORTING_VARIABLE)
                                .reportingVariableTitleFormattingRule(
                                        FormattingRule
                                                .builder()
                                                .formattingRulePaletteIndex(SUMMARY_REPORT_PALETTE_INDEX)
                                                .formattingRuleColumn(1)
                                                .formattingRuleRow(15)
                                                .build())
                                .reportingVariableValueFormattingRule(
                                        FormattingRule
                                                .builder()
                                                .formattingRulePaletteIndex(SUMMARY_REPORT_PALETTE_INDEX)
                                                .formattingRuleColumn(2)
                                                .formattingRuleRow(15)
                                                .build())
                                .build()
                );
    }


    public FormattingRule getSummaryReportTotalLandConvertedToLandTitleFormattingRule() {
        return titleFormattingRule;
    }

    public ReportingVariableFormattingRule getSummaryReportTotalLandConvertedToLandReportingVariableFormattingRule(
            Long reportingVariableId) {

        return reportingVariablesFormattingRules
                .stream()
                .filter(reportingVariableFormattingRule ->
                        reportingVariableFormattingRule.getReportingVariableId().equals(reportingVariableId))
                .findAny()
                .orElse(null);
    }
}

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
public class SectoralReportTotalLandFormattingRules {

    @Value("${sectoral.report.palette.index}")
    private Integer SECTORAL_REPORT_PALETTE_INDEX;

    @Value("${area.reporting.variable.id}")
    private Long AREA_REPORTING_VARIABLE;

    @Value("${net.carbon.stock.change.in.living.biomass.reporting.variable.id}")
    private Long NET_CARBON_STOCK_CHANGE_IN_LIVING_BIOMASS_REPORTING_VARIABLE;

    @Value("${net.carbon.stock.change.in.dead.organic.matter.reporting.variable.id}")
    private Long NET_CARBON_STOCK_CHANGE_IN_DEAD_ORGANIC_MATTER_REPORTING_VARIABLE;

    @Value("${net.carbon.stock.change.in.mineral.soils.reporting.variable.id}")
    private Long NET_CARBON_STOCK_CHANGE_IN_MINERAL_SOILS_REPORTING_VARIABLE;

    @Value("${net.carbon.stock.change.in.organic.soils.reporting.variable.id}")
    private Long NET_CARBON_STOCK_CHANGE_IN_ORGANIC_SOILS_REPORTING_VARIABLE;

    @Value("${net.carbon.dioxide.emissions.removals.reporting.variable.id}")
    private Long NET_CARBON_DIOXIDE_EMISSIONS_REMOVALS_REPORTING_VARIABLE;

    private FormattingRule categoryTitleFormattingRule;
    private FormattingRule subdivisionTitleFormattingRule;
    private List<ReportingVariableFormattingRule> reportingVariablesFormattingRules;

    @PostConstruct
    private void init() {

        categoryTitleFormattingRule =
                FormattingRule
                        .builder()
                        .formattingRulePaletteIndex(SECTORAL_REPORT_PALETTE_INDEX)
                        .formattingRuleColumn(0)
                        .formattingRuleRow(4)
                        .build();

        subdivisionTitleFormattingRule =
                FormattingRule
                        .builder()
                        .formattingRulePaletteIndex(SECTORAL_REPORT_PALETTE_INDEX)
                        .formattingRuleColumn(1)
                        .formattingRuleRow(4)
                        .build();

        reportingVariablesFormattingRules =
                Arrays.asList(
                        ReportingVariableFormattingRule
                                .builder()
                                .reportingVariableId(AREA_REPORTING_VARIABLE)
                                .reportingVariableTitleFormattingRule(
                                        FormattingRule
                                                .builder()
                                                .formattingRulePaletteIndex(SECTORAL_REPORT_PALETTE_INDEX)
                                                .formattingRuleColumn(2)
                                                .formattingRuleRow(4)
                                                .build())
                                .reportingVariableValueFormattingRule(
                                        FormattingRule
                                                .builder()
                                                .formattingRulePaletteIndex(SECTORAL_REPORT_PALETTE_INDEX)
                                                .formattingRuleColumn(3)
                                                .formattingRuleRow(4)
                                                .build())
                                .build(),

                        ReportingVariableFormattingRule
                                .builder()
                                .reportingVariableId(NET_CARBON_STOCK_CHANGE_IN_LIVING_BIOMASS_REPORTING_VARIABLE)
                                .reportingVariableTitleFormattingRule(
                                        FormattingRule
                                                .builder()
                                                .formattingRulePaletteIndex(SECTORAL_REPORT_PALETTE_INDEX)
                                                .formattingRuleColumn(2)
                                                .formattingRuleRow(5)
                                                .build())
                                .reportingVariableValueFormattingRule(
                                        FormattingRule
                                                .builder()
                                                .formattingRulePaletteIndex(SECTORAL_REPORT_PALETTE_INDEX)
                                                .formattingRuleColumn(3)
                                                .formattingRuleRow(5)
                                                .build())
                                .build(),

                        ReportingVariableFormattingRule
                                .builder()
                                .reportingVariableId(NET_CARBON_STOCK_CHANGE_IN_DEAD_ORGANIC_MATTER_REPORTING_VARIABLE)
                                .reportingVariableTitleFormattingRule(
                                        FormattingRule
                                                .builder()
                                                .formattingRulePaletteIndex(SECTORAL_REPORT_PALETTE_INDEX)
                                                .formattingRuleColumn(2)
                                                .formattingRuleRow(6)
                                                .build())
                                .reportingVariableValueFormattingRule(
                                        FormattingRule
                                                .builder()
                                                .formattingRulePaletteIndex(SECTORAL_REPORT_PALETTE_INDEX)
                                                .formattingRuleColumn(3)
                                                .formattingRuleRow(6)
                                                .build())
                                .build(),

                        ReportingVariableFormattingRule
                                .builder()
                                .reportingVariableId(NET_CARBON_STOCK_CHANGE_IN_MINERAL_SOILS_REPORTING_VARIABLE)
                                .reportingVariableTitleFormattingRule(
                                        FormattingRule
                                                .builder()
                                                .formattingRulePaletteIndex(SECTORAL_REPORT_PALETTE_INDEX)
                                                .formattingRuleColumn(2)
                                                .formattingRuleRow(7)
                                                .build())
                                .reportingVariableValueFormattingRule(
                                        FormattingRule
                                                .builder()
                                                .formattingRulePaletteIndex(SECTORAL_REPORT_PALETTE_INDEX)
                                                .formattingRuleColumn(3)
                                                .formattingRuleRow(7)
                                                .build())
                                .build(),

                        ReportingVariableFormattingRule
                                .builder()
                                .reportingVariableId(NET_CARBON_STOCK_CHANGE_IN_ORGANIC_SOILS_REPORTING_VARIABLE)
                                .reportingVariableTitleFormattingRule(
                                        FormattingRule
                                                .builder()
                                                .formattingRulePaletteIndex(SECTORAL_REPORT_PALETTE_INDEX)
                                                .formattingRuleColumn(2)
                                                .formattingRuleRow(8)
                                                .build())
                                .reportingVariableValueFormattingRule(
                                        FormattingRule
                                                .builder()
                                                .formattingRulePaletteIndex(SECTORAL_REPORT_PALETTE_INDEX)
                                                .formattingRuleColumn(3)
                                                .formattingRuleRow(8)
                                                .build())
                                .build(),

                        ReportingVariableFormattingRule
                                .builder()
                                .reportingVariableId(NET_CARBON_DIOXIDE_EMISSIONS_REMOVALS_REPORTING_VARIABLE)
                                .reportingVariableTitleFormattingRule(
                                        FormattingRule
                                                .builder()
                                                .formattingRulePaletteIndex(SECTORAL_REPORT_PALETTE_INDEX)
                                                .formattingRuleColumn(2)
                                                .formattingRuleRow(9)
                                                .build())
                                .reportingVariableValueFormattingRule(
                                        FormattingRule
                                                .builder()
                                                .formattingRulePaletteIndex(SECTORAL_REPORT_PALETTE_INDEX)
                                                .formattingRuleColumn(3)
                                                .formattingRuleRow(9)
                                                .build())
                                .build()
                );
    }


    public FormattingRule getSectoralReportTotalLandCategoryTitleFormattingRule() {
        return categoryTitleFormattingRule;
    }

    public FormattingRule getSectoralReportTotalLandSubdivisionTitleFormattingRule() {
        return subdivisionTitleFormattingRule;
    }

    public ReportingVariableFormattingRule getSectoralReportTotalLandReportingVariableFormattingRule(
            Long reportingVariableId) {

        return reportingVariablesFormattingRules
                .stream()
                .filter(reportingVariableFormattingRule ->
                        reportingVariableFormattingRule.getReportingVariableId().equals(reportingVariableId))
                .findAny()
                .orElse(null);
    }
}

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
@Component
public class SummaryReportFormattingRules {

    @Autowired
    private SummaryReportTotalLULUCFFormattingRules summaryReportTotalLULUCFFormattingRules;

    @Autowired
    private SummaryReportTotalLandFormattingRules summaryReportTotalLandFormattingRules;

    @Autowired
    private SummaryReportLandRemainingLandFormattingRules summaryReportLandRemainingLandFormattingRules;

    @Autowired
    private SummaryReportLandConvertedToLandFormattingRules summaryReportLandConvertedToLandFormattingRules;

    @Autowired
    private SummaryReportYearsFormattingRules summaryReportYearsFormattingRules;

    @Autowired
    private SummaryReportUnitsFormattingRules summaryReportUnitsFormattingRules;

    public FormattingRule getSummaryReportTotalLULUCFTitleFormattingRule() {
        return summaryReportTotalLULUCFFormattingRules
                .getSummaryReportTotalLULUCFTitleFormattingRule();
    }

    public ReportingVariableFormattingRule getSummaryReportTotalLULUCFReportingVariableFormattingRule(Long reportingVariableId) {
        return summaryReportTotalLULUCFFormattingRules
                .getSummaryReportTotalLULUCFReportingVariableFormattingRule(reportingVariableId);
    }

    public FormattingRule getSummaryReportTotalLandTitleFormattingRule() {
        return summaryReportTotalLandFormattingRules
                .getSummaryReportTotalLandTitleFormattingRule();
    }

    public ReportingVariableFormattingRule getSummaryReportTotalLandReportingVariableFormattingRule(Long reportingVariableId) {
        return summaryReportTotalLandFormattingRules
                .getSummaryReportTotalLandReportingVariableFormattingRule(reportingVariableId);
    }

    public FormattingRule getSummaryReportLandRemainingLandTitleFormattingRule() {
        return summaryReportLandRemainingLandFormattingRules
                .getSummaryReportLandRemainingLandTitleFormattingRule();
    }

    public ReportingVariableFormattingRule getSummaryReportLandRemainingLandReportingVariableFormattingRule(
            Long reportingVariableId) {
        return summaryReportLandRemainingLandFormattingRules
                .getSummaryReportLandRemainingLandReportingVariableFormattingRule(reportingVariableId);
    }

    public FormattingRule getSummaryReportTotalLandConvertedToLandTitleFormattingRule() {
        return summaryReportLandConvertedToLandFormattingRules
                .getSummaryReportTotalLandConvertedToLandTitleFormattingRule();
    }

    public ReportingVariableFormattingRule getSummaryReportTotalLandConvertedToLandReportingVariableFormattingRule(
            Long reportingVariableId) {

        return summaryReportLandConvertedToLandFormattingRules
                .getSummaryReportTotalLandConvertedToLandReportingVariableFormattingRule(reportingVariableId);
    }

    public FormattingRule getSummaryReportYearValueFormattingRule() {
        return summaryReportYearsFormattingRules
                .getSummaryReportYearValueFormattingRule();
    }

    public FormattingRule getSummaryReportUnitsTitleFormattingRule() {
        return summaryReportUnitsFormattingRules
                .getSummaryReportUnitsTitleFormattingRule();
    }
}

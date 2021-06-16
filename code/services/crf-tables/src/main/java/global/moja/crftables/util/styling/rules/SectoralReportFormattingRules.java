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
public class SectoralReportFormattingRules {

    @Autowired
    private SectoralReportTotalLandFormattingRules sectoralReportTotalLandFormattingRules;

    @Autowired
    private SectoralReportTotalLandRemainingLandFormattingRules sectoralReportTotalLandRemainingLandFormattingRules;

    @Autowired
    private SectoralReportTotalLandConvertedToLandFormattingRules sectoralReportTotalLandConvertedToLandFormattingRules;

    @Autowired
    private SectoralReportLandConvertedToLandFormattingRules sectoralReportLandConvertedToLandFormattingRules;

    @Autowired
    private SectoralReportYearsFormattingRules sectoralReportYearsFormattingRules;

    public FormattingRule getSectoralReportTotalLandCategoryTitleFormattingRule() {
        return sectoralReportTotalLandFormattingRules
                .getSectoralReportTotalLandCategoryTitleFormattingRule();
    }

    public FormattingRule getSectoralReportTotalLandSubdivisionTitleFormattingRule() {
        return sectoralReportTotalLandFormattingRules
                .getSectoralReportTotalLandSubdivisionTitleFormattingRule();
    }

    public ReportingVariableFormattingRule getSectoralReportTotalLandReportingVariableFormattingRule(
            Long reportingVariableId) {

        return sectoralReportTotalLandFormattingRules
                .getSectoralReportTotalLandReportingVariableFormattingRule(reportingVariableId);
    }

    public FormattingRule getSectoralReportLandRemainingLandCategoryTitleFormattingRule() {
        return sectoralReportTotalLandRemainingLandFormattingRules
                .getSectoralReportLandRemainingLandCategoryTitleFormattingRule();
    }

    public FormattingRule getSectoralReportLandRemainingLandSubdivisionTitleFormattingRule() {
        return sectoralReportTotalLandRemainingLandFormattingRules
                .getSectoralReportLandRemainingLandSubdivisionTitleFormattingRule();
    }

    public ReportingVariableFormattingRule getSectoralReportLandRemainingLandReportingVariableFormattingRule(
            Long reportingVariableId) {

        return sectoralReportTotalLandRemainingLandFormattingRules
                .getSectoralReportLandRemainingLandReportingVariableFormattingRule(reportingVariableId);
    }

    public FormattingRule getSectoralReportTotalLandConvertedToLandCategoryTitleFormattingRule() {
        return sectoralReportTotalLandConvertedToLandFormattingRules
                .getSectoralReportTotalLandConvertedToLandCategoryTitleFormattingRule();
    }

    public FormattingRule getSectoralReportTotalLandConvertedToLandSubdivisionTitleFormattingRule() {
        return sectoralReportTotalLandConvertedToLandFormattingRules
                .getSectoralReportTotalLandConvertedToLandSubdivisionTitleFormattingRule();
    }

    public ReportingVariableFormattingRule getSectoralReportTotalLandConvertedToLandReportingVariableFormattingRule(
            Long reportingVariableId) {

        return sectoralReportTotalLandConvertedToLandFormattingRules
                .getSectoralReportTotalLandConvertedToLandReportingVariableFormattingRule(reportingVariableId);
    }

    public FormattingRule getSectoralReportLandConvertedToLandCategoryTitleFormattingRule() {
        return sectoralReportLandConvertedToLandFormattingRules
                .getSectoralReportLandConvertedToLandCategoryTitleFormattingRule();
    }

    public FormattingRule getSectoralReportLandConvertedToLandSubdivisionTitleFormattingRule() {
        return sectoralReportLandConvertedToLandFormattingRules
                .getSectoralReportLandConvertedToLandSubdivisionTitleFormattingRule();
    }

    public ReportingVariableFormattingRule getSectoralReportLandConvertedToLandReportingVariableFormattingRule(
            Long reportingVariableId) {

        return sectoralReportLandConvertedToLandFormattingRules
                .getSectoralReportLandConvertedToLandReportingVariableFormattingRule(reportingVariableId);
    }

    public FormattingRule getSectoralReportYearValueFormattingRule() {
        return sectoralReportYearsFormattingRules
                .getSectoralReportYearValueFormattingRule();
    }
}

/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.crftables.util.styling.rules;

import global.moja.crftables.daos.FormattingRule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
@Component
public class SummaryReportUnitsFormattingRules {

    @Value("${summary.report.palette.index}")
    private Integer SUMMARY_REPORT_PALETTE_INDEX;

    private FormattingRule unitsTitleFormattingRule;

    @PostConstruct
    private void init() {

        unitsTitleFormattingRule =
                FormattingRule
                        .builder()
                        .formattingRulePaletteIndex(SUMMARY_REPORT_PALETTE_INDEX)
                        .formattingRuleColumn(2)
                        .formattingRuleRow(3)
                        .build();

    }


    public FormattingRule getSummaryReportUnitsTitleFormattingRule() {
        return unitsTitleFormattingRule;
    }

}

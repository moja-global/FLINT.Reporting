/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.crftables.util.styling;

import global.moja.crftables.daos.FormattingRule;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
@Component
@Slf4j
public class CellStyleUtil {

    public CellStyle getCellStyle(Workbook workbook, FormattingRule rule) throws IOException {

        log.debug("Rule palette index = {}", rule.getFormattingRulePaletteIndex());
        log.debug("Rule column index = {}", rule.getFormattingRuleColumn());
        log.debug("Rule row index = {}", rule.getFormattingRuleRow());

        return workbook.getSheetAt(rule.getFormattingRulePaletteIndex())
                .getRow(rule.getFormattingRuleRow())
                .getCell(rule.getFormattingRuleColumn())
                .getCellStyle();

    }

}

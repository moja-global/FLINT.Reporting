/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.crftables.services;

import global.moja.crftables.daos.Report;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
@Service
@Slf4j
public class ReportPreamblePagePreparationService {

    @Value("${preamble.template.index}")
    private Integer PREAMBLE_TEMPLATE_INDEX;

    public Mono<Report> enterPreambleData(Report report) {

        //Read in the preamble worksheet
        log.trace("Reading in the preamble worksheet");
        Sheet preamble = report.getWorkbook().getSheetAt(PREAMBLE_TEMPLATE_INDEX);

        //Set the report title
        log.trace("Setting the report title");
        Row row = preamble.getRow(1);
        Cell title = row.getCell(1);
        title.setCellValue("Draft 2006 GL Reporting Tables for " + report.getParty().getName());

        //Set the report source
        log.trace("Setting the report source");
        row = preamble.getRow(2);
        Cell source = row.getCell(1);
        source.setCellValue(report.getDatabase().getLabel());
        
        //Return the updated Report Details
        log.trace("Return the updated Report Details");
        return Mono.just(report);

    }

}

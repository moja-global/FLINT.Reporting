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
public class ReportCleanupService {

    @Value("${summary.report.template.index}")
    Integer SUMMARY_REPORT_TEMPLATE_INDEX;

    @Value("${summary.report.palette.index}")
    Integer SUMMARY_REPORT_PALETTE_INDEX;

    @Value("${sectoral.report.template.index}")
    Integer SECTORAL_REPORT_TEMPLATE_INDEX;

    @Value("${sectoral.report.palette.index}")
    Integer SECTORAL_REPORT_PALETTE_INDEX;

    public Mono<Report> cleanupReport(Report report) {

        log.trace("Entering cleanupReport()");

        for (String template : new String[]{
                report.getWorkbook().getSheetAt(SUMMARY_REPORT_TEMPLATE_INDEX).getSheetName(),
                report.getWorkbook().getSheetAt(SECTORAL_REPORT_TEMPLATE_INDEX).getSheetName(),
                report.getWorkbook().getSheetAt(SUMMARY_REPORT_PALETTE_INDEX).getSheetName(),
                report.getWorkbook().getSheetAt(SECTORAL_REPORT_PALETTE_INDEX).getSheetName()}) {

            for (int i = 0; i < report.getWorkbook().getNumberOfSheets(); i++) {
                Sheet tmpSheet = report.getWorkbook().getSheetAt(i);
                if (tmpSheet.getSheetName().equals(template)) {
                    report.getWorkbook().removeSheetAt(i);
                    break;
                }
            }
        }

        return Mono.just(report);

    }
}

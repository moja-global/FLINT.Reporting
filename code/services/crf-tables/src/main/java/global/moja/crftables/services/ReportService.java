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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
@Service
@Slf4j
public class ReportService {

    @Autowired
    ReportInitializationService reportInitializationService;

    @Autowired
    ReportPreamblePagePreparationService reportPreamblePagePreparationService;

    @Autowired
    ReportSummaryPagePreparationService reportSummaryPagePreparationService;

    @Autowired
    ReportSectoralPagesPreparationService reportSectoralPagesPreparationService;

    @Autowired
    ReportCleanupService reportCleanupService;

    @Autowired
    ReportSavingService reportSavingService;

    public Mono<Report> generateReport(
            Long partyId,
            Long databaseId,
            Integer minYear,
            Integer maxYear) {

        log.trace("Entering generateReport()");

        return reportInitializationService
                .initializeReport(partyId, databaseId, minYear, maxYear)
                .flatMap(report -> reportPreamblePagePreparationService.enterPreambleData(report))
                .flatMap(report -> reportSummaryPagePreparationService.prepareSummaryReport(report))
                .flatMap(report -> reportSectoralPagesPreparationService.prepareSectoralReports(report))
                .flatMap(report -> reportCleanupService.cleanupReport(report))
                .flatMap(report -> reportSavingService.saveReport(report));

    }
}

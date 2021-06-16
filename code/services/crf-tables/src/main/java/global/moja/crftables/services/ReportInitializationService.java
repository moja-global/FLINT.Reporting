/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.crftables.services;

import global.moja.crftables.daos.Report;
import global.moja.crftables.util.endpoints.EndpointsUtil;
import lombok.extern.slf4j.Slf4j;
import okio.Buffer;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.UUID;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
@Service
@Slf4j
public class ReportInitializationService {

    @Autowired
    EndpointsUtil endpointsUtil;

    private File templateWorkbook;

    @PostConstruct
    private void init() throws FileNotFoundException {
        templateWorkbook =
                ResourceUtils
                        .getFile("classpath:template.xlsx");
    }

    public Mono<Report> initializeReport(
            final Long partyId,
            final Long databaseId,
            final Integer minYear,
            final Integer maxYear) {

        return Mono
                .just(Report.builder().build())
                .flatMap(report ->
                        endpointsUtil
                                .retrieveParty(partyId)
                                .map(party -> {
                                    report.setParty(party);
                                    return report;
                                }))
                .flatMap(report ->
                        endpointsUtil
                                .retrieveDatabase(databaseId)
                                .map(database -> {
                                    report.setDatabase(database);
                                    return report;
                                }))
                .flatMap(report ->
                        endpointsUtil
                                .retrieveQuantityObservations(partyId, databaseId, minYear, maxYear)
                                .collectList()
                                .map(quantityObservations -> {
                                    report.setQuantityObservations(quantityObservations);
                                    return report;
                                }))
                .map(report -> {
                    report.setMinYear(minYear);
                    return report;
                })
                .map(report -> {
                    report.setMaxYear(maxYear);
                    return report;
                })
                .map(report -> {

                    // Clone and set the Report Workbook
                    File temp = new File(UUID.randomUUID() + ".xlsx");
                    try {

                        // Clone
                        try (FileOutputStream fos = new FileOutputStream(temp)) {

                            try (FileInputStream fis = new FileInputStream(templateWorkbook)) {
                                Buffer buffer = new Buffer();
                                buffer.readFrom(fis);
                                buffer.writeTo(fos);
                            }

                        }

                        // Set
                        report.setWorkbook(WorkbookFactory.create(temp));

                    } catch (IOException e) {

                        // Log and handle the error downstream
                        log.error("Report Template Cloning failed");
                        report.setWorkbook(null);
                    }

                    return report;
                });
    }


}

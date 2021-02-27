/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.reportingtable.repository.updation;

import global.moja.reportingtable.configurations.DatabaseConfig;
import global.moja.reportingtable.models.ReportingTable;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 1.0
 */
@Component
@Slf4j
public class UpdateReportingTablesQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Recursively Updates Reporting Tables records
     *
     * @param reportingTables an array of beans containing the Reporting Tables records details
     * @return the number of Reporting Tables records affected by each recursive query i.e updated
     */
    public Flux<Integer> updateReportingTables(ReportingTable[] reportingTables) {

        log.trace("Entering updateReportingTables()");

        String query = "UPDATE reporting_table SET reporting_framework_id = ?, number = ?, name = ?, description = ? WHERE id = ?";

        return
                Flux.from(
                        databaseConfig
                                .getDatabase()
                                .update(query)
                                .parameterListStream(getParametersListStream(reportingTables))
                                .counts());
    }

    private Flowable getParametersListStream(ReportingTable[] reportingTables) {

        List<List> temp = new ArrayList<>();

        for (ReportingTable reportingTable : reportingTables) {
            temp.add(Arrays.asList(
                    reportingTable.getReportingFrameworkId(),
                    reportingTable.getNumber(),
                    reportingTable.getName(),
                    reportingTable.getDescription(),
                    reportingTable.getId()
            ));
        }

        return Flowable.fromIterable(temp);
    }


}

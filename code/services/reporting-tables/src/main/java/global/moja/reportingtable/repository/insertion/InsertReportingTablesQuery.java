/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.reportingtable.repository.insertion;

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
public class InsertReportingTablesQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Inserts new Reporting Tables records into the database
     *
     * @param reportingTables an array of beans containing the Reporting Tables records details
     * @return the unique identifiers of the newly inserted Reporting Tables records
     */
    public Flux<Long> insertReportingTables(ReportingTable[] reportingTables) {

        log.trace("Entering insertReportingTables");

        String query = "INSERT INTO reporting_table(reporting_framework_id,number,name,description) VALUES(?,?,?,?)";

        return
                Flux.from(
                        databaseConfig
                                .getDatabase()
                                .update(query)
                                .parameterListStream(getParametersListStream(reportingTables))
                                .returnGeneratedKeys()
                                .getAs(Long.class));
    }

    private Flowable getParametersListStream(ReportingTable[] reportingTables) {

        List<List> temp = new ArrayList<>();

        for (ReportingTable reportingTable : reportingTables) {
            temp.add(Arrays.asList(
                    reportingTable.getReportingFrameworkId(),
                    reportingTable.getNumber(),
                    reportingTable.getName(),
                    reportingTable.getDescription()
            ));
        }

        return Flowable.fromIterable(temp);
    }

}

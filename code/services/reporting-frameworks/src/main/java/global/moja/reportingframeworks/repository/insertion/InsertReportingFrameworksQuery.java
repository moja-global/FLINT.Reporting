/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.reportingframeworks.repository.insertion;

import io.reactivex.Flowable;
import global.moja.reportingframeworks.configurations.DatabaseConfig;
import global.moja.reportingframeworks.models.ReportingFramework;
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
public class InsertReportingFrameworksQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Inserts new Reporting Frameworks records into the database
     *
     * @param reportingFrameworks an array of beans containing the Reporting Frameworks records details
     * @return the unique identifiers of the newly inserted Reporting Frameworks records
     */
    public Flux<Long> insertReportingFrameworks(ReportingFramework[] reportingFrameworks) {

        log.trace("Entering insertReportingFrameworks");

        String query = "INSERT INTO reporting_framework(name,description) VALUES(?,?)";

        return
                Flux.from(
                        databaseConfig
                                .getDatabase()
                                .update(query)
                                .parameterListStream(getParametersListStream(reportingFrameworks))
                                .returnGeneratedKeys()
                                .getAs(Long.class));
    }

    private Flowable getParametersListStream(ReportingFramework[] reportingFrameworks) {

        List<List> parameters = new ArrayList<>();

        for (ReportingFramework reportingFramework : reportingFrameworks) {
            parameters.add(Arrays.asList(
                    reportingFramework.getName(),
                    reportingFramework.getDescription()
            ));
        }

        return Flowable.fromIterable(parameters);
    }

}

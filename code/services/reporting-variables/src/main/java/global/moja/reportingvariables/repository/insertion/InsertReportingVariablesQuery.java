/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.reportingvariables.repository.insertion;

import io.reactivex.Flowable;
import global.moja.reportingvariables.configurations.DatabaseConfig;
import global.moja.reportingvariables.models.ReportingVariable;
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
public class InsertReportingVariablesQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Inserts new Reporting Variables records into the database
     *
     * @param reportingVariables an array of beans containing the Reporting Variables records details
     * @return the unique identifiers of the newly inserted Reporting Variables records
     */
    public Flux<Long> insertReportingVariables(ReportingVariable[] reportingVariables) {

        log.trace("Entering insertReportingVariables");

        String query = "INSERT INTO reporting_variable(reporting_framework_id,name,description) VALUES(?,?,?)";

        return
                Flux.from(
                        databaseConfig
                                .getDatabase()
                                .update(query)
                                .parameterListStream(getParametersListStream(reportingVariables))
                                .returnGeneratedKeys()
                                .getAs(Long.class));
    }

    private Flowable getParametersListStream(ReportingVariable[] reportingVariables) {

        List<List> parameters = new ArrayList<>();

        for (ReportingVariable reportingVariable : reportingVariables) {
            parameters.add(Arrays.asList(
                    reportingVariable.getReportingFrameworkId(),
                    reportingVariable.getName(),
                    reportingVariable.getDescription()
            ));
        }

        return Flowable.fromIterable(parameters);
    }

}

/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.landusesfluxtypestoreportingtables.repository.insertion;

import io.reactivex.Flowable;
import global.moja.landusesfluxtypestoreportingtables.configurations.DatabaseConfig;
import global.moja.landusesfluxtypestoreportingtables.models.LandUseFluxTypeToReportingTable;
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
public class InsertLandUsesFluxTypesToReportingTablesQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Inserts new Land Uses Flux Types To Reporting Tables records into the database
     *
     * @param landUsesFluxTypesToReportingTables an array of beans containing the Land Uses Flux Types To Reporting Tables records details
     * @return the unique identifiers of the newly inserted Land Uses Flux Types To Reporting Tables records
     */
    public Flux<Long> insertLandUsesFluxTypesToReportingTables(LandUseFluxTypeToReportingTable[] landUsesFluxTypesToReportingTables) {

        log.trace("Entering insertLandUsesFluxTypesToReportingTables");

        String query = "INSERT INTO land_use_flux_type_to_reporting_table(land_use_flux_type_id,emission_type_id,reporting_table_id) VALUES(?,?,?)";

        return
                Flux.from(
                        databaseConfig
                                .getDatabase()
                                .update(query)
                                .parameterListStream(getParametersListStream(landUsesFluxTypesToReportingTables))
                                .returnGeneratedKeys()
                                .getAs(Long.class));
    }

    private Flowable getParametersListStream(LandUseFluxTypeToReportingTable[] landUsesFluxTypesToReportingTables) {

        List<List> temp = new ArrayList<>();

        for (LandUseFluxTypeToReportingTable landUseFluxTypeToReportingTable : landUsesFluxTypesToReportingTables) {
            temp.add(Arrays.asList(
                    landUseFluxTypeToReportingTable.getLandUseFluxTypeId(),
                    landUseFluxTypeToReportingTable.getEmissionTypeId(),
                    landUseFluxTypeToReportingTable.getReportingTableId()
            ));
        }

        return Flowable.fromIterable(temp);
    }

}

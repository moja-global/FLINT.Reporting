/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.landusesfluxtypestoreportingtables.repository.updation;

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
public class UpdateLandUsesFluxTypesToReportingTablesQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Recursively Updates Land Uses Flux Types To Reporting Tables records
     *
     * @param landUsesFluxTypesToReportingTables an array of beans containing the Land Uses Flux Types To Reporting Tables records details
     * @return the number of Land Uses Flux Types To Reporting Tables records affected by each recursive query i.e updated
     */
    public Flux<Integer> updateLandUsesFluxTypesToReportingTables(LandUseFluxTypeToReportingTable[] landUsesFluxTypesToReportingTables) {

        log.trace("Entering updateLandUsesFluxTypesToReportingTables()");

        String query = "UPDATE land_use_flux_type_to_reporting_table SET land_use_flux_type_id = ?, emission_type_id  = ?, reporting_table_id = ? WHERE id = ?";

        return
                Flux.from(
                        databaseConfig
                                .getDatabase()
                                .update(query)
                                .parameterListStream(getParametersListStream(landUsesFluxTypesToReportingTables))
                                .counts());
    }

    private Flowable getParametersListStream(LandUseFluxTypeToReportingTable[] landUsesFluxTypesToReportingTables) {

        List<List> temp = new ArrayList<>();

        for (LandUseFluxTypeToReportingTable landUseFluxTypeToReportingTable : landUsesFluxTypesToReportingTables) {
            temp.add(Arrays.asList(
                    landUseFluxTypeToReportingTable.getLandUseFluxTypeId(),
                    landUseFluxTypeToReportingTable.getEmissionTypeId(),
                    landUseFluxTypeToReportingTable.getReportingTableId(),
                    landUseFluxTypeToReportingTable.getId()
            ));
        }

        return Flowable.fromIterable(temp);
    }


}

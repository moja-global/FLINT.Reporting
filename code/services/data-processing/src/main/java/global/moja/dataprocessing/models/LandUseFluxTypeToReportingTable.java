/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.dataprocessing.models;

import lombok.*;

/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Data
@Builder
public class LandUseFluxTypeToReportingTable implements Comparable<LandUseFluxTypeToReportingTable> {

    private Long id;
    private Long landUseFluxTypeId;
    private Long emissionTypeId;
    private Long reportingTableId;
    private Integer version;

    @Override
    public int compareTo(LandUseFluxTypeToReportingTable landUseFluxTypeToReportingTable) {

        if(this.id != null && landUseFluxTypeToReportingTable.getId() != null){
            return this.id.compareTo(landUseFluxTypeToReportingTable.getId());
        } else {
            return 0;
        }

    }

    @Override
    public String toString() {
        return
                String.format(
                        "Id: %d, " +
                        "Land Use Flux Type Id: %d, " +
                        "Emission Type Id: %d, " +
                        "Reporting Table Id: %d, " +
                        "Version: %d ",
                        id, landUseFluxTypeId, emissionTypeId, reportingTableId, version);
    }
}

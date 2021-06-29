/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.dataprocessor.daos;

import global.moja.dataprocessor.models.FluxReportingResult;
import global.moja.dataprocessor.models.LandUseCategory;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
@Jacksonized
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Data
@Builder
public class LocationLandUsesFluxReportingResultsHistory implements Comparable<LocationLandUsesFluxReportingResultsHistory> {

    private Long itemNumber;
    private Integer year;
    private LandUseCategory landUseCategory;
    private Boolean confirmed;
    @EqualsAndHashCode.Exclude
    private List<FluxReportingResult> fluxReportingResults;

    @Override
    public int compareTo(LocationLandUsesFluxReportingResultsHistory l) {

        if (this.itemNumber != null && l.getItemNumber() != null) {
            return this.itemNumber.compareTo(l.getItemNumber());
        } else if (this.year != null && l.getYear() != null) {
            return this.year.compareTo(l.getYear());
        } else if (this.landUseCategory != null && l.getLandUseCategory() != null) {
            return this.landUseCategory.compareTo(l.getLandUseCategory());
        } else if (this.confirmed != null && l.getConfirmed() != null) {
            return this.confirmed.compareTo(l.getConfirmed());
        } else {
            return 0;
        }

    }

    @Override
    public String toString() {
        return String.format("Timestep: %d, Year: %d, Land Use: %s, Confirmed: %s, Fluxes: %s",
                itemNumber, year, landUseCategory == null ? null : landUseCategory.toString(), confirmed, fluxReportingResults.toString());
    }


}

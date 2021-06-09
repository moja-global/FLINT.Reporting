/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.dataprocessing.daos;

import global.moja.dataprocessing.models.LandUseCategory;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.Objects;

/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */

@Jacksonized
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Data
@Builder
public class LocationLandUsesAllocatedFluxReportingResult implements Comparable<LocationLandUsesAllocatedFluxReportingResult> {

    private Long itemNumber;
    private Integer year;
    private LandUseCategory landUseCategory;
    private Boolean confirmed;
    private List<Allocation> allocations;

    @Override
    public int hashCode() {
        return Objects.hash(itemNumber, year, landUseCategory, confirmed);
    }

    @Override
    public int compareTo(LocationLandUsesAllocatedFluxReportingResult l) {

        if(this.itemNumber != null && l.getItemNumber() != null){
            return this.itemNumber.compareTo(l.getItemNumber());
        } else {
            return 0;
        }

    }

    @Override
    public String toString(){
        return String.format("Timestep: %d, Year: %d, Land Use: %s, Confirmed: %s, Fluxes: %s",
                itemNumber,
                year,
                landUseCategory == null ? null : landUseCategory.toString(),
                confirmed,
                allocations.toString());
    }

}

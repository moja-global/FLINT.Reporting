/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.dataprocessor.daos;

import global.moja.dataprocessor.models.VegetationType;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

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
public class LocationVegetationTypesHistory implements Comparable<LocationVegetationTypesHistory> {

    private Long itemNumber;
    private Integer year;
    private VegetationType vegetationType;

    @Override
    public int compareTo(LocationVegetationTypesHistory l) {

        if(this.itemNumber != null && l.getItemNumber() != null){
            return this.itemNumber.compareTo(l.getItemNumber());
        } else {
            return 0;
        }

    }

    @Override
    public String toString(){
        return String.format("Timestep: %d, Year: %d, Vegetation Type: %s",
                itemNumber, year, vegetationType == null ? null : vegetationType.toString());
    }

}

/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.businessintelligence.daos;

import global.moja.businessintelligence.models.VegetationType;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */

@Jacksonized
@Builder
@Setter
@Getter
@EqualsAndHashCode
public class VegetationTypesHistoricDetail implements Comparable<VegetationTypesHistoricDetail> {

    private Long itemNumber;
    private Integer year;
    private VegetationType vegetationType;

    @Override
    public int compareTo(VegetationTypesHistoricDetail coverType) {

        if(this.itemNumber != null && coverType.getItemNumber() != null){
            return this.itemNumber.compareTo(coverType.getItemNumber());
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

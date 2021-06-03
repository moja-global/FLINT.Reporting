/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.businessintelligence.daos;

import global.moja.businessintelligence.models.LandUseCategory;
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
public class LandUsesHistoricDetail implements Comparable<LandUsesHistoricDetail> {
    
    private Long itemNumber;
    private Integer year;
    private LandUseCategory landUseCategory;
    private Boolean confirmed;

    @Override
    public int compareTo(LandUsesHistoricDetail landUseCategory) {

        if(this.itemNumber != null && landUseCategory.getItemNumber() != null){
            return this.itemNumber.compareTo(landUseCategory.getItemNumber());
        } else {
            return 0;
        }

    }

    @Override
    public String toString(){
        return String.format("Timestep: %d, Year: %d, Land Use: %s, Confirmed: %s",
                itemNumber, year, landUseCategory == null ? null : landUseCategory.toString(), confirmed);
    }


}

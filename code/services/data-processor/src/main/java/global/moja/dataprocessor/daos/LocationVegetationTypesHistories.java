/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package global.moja.dataprocessor.daos;

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
public class LocationVegetationTypesHistories implements Comparable<LocationVegetationTypesHistories>{

    private Long locationId;
    private Long partyId;
    private Long tileId;
    private Long vegetationHistoryId;
    @EqualsAndHashCode.Exclude
    private Long unitCount;
    @EqualsAndHashCode.Exclude
    private Double unitAreaSum;
    @EqualsAndHashCode.Exclude
    private List<LocationVegetationTypesHistory> histories;

    @Override
    public int compareTo(LocationVegetationTypesHistories l) {

        if (!this.partyId.equals(l.getPartyId())) {
            return this.partyId.compareTo(l.getPartyId());
        } else if (!this.tileId.equals(l.getTileId())) {
            return this.tileId.compareTo(l.getTileId());
        } else if (!this.locationId.equals(l.getLocationId())) {
            return this.locationId.compareTo(l.getLocationId());
        } else if (!this.vegetationHistoryId.equals(l.getVegetationHistoryId())) {
            return this.vegetationHistoryId.compareTo(l.getVegetationHistoryId());
        } else {
            return 0;
        }

    }

    @Override
    public String toString() {
        return
                String.format(
                        "Id: %d, " +
                        "Party Id: %d, " +
                        "Tile Id: %d, " +
                        "Vegetation History Id: %d, " +
                        "Unit Count: %d, " +
                        "Unit Area Sum: %f, " +
                        "History: %s",
                        locationId, partyId, tileId, vegetationHistoryId, unitCount, unitAreaSum, histories.toString());
    }

}

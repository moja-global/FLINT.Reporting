/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package global.moja.dataprocessor.daos;

import global.moja.dataprocessor.models.Accountability;
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
public class LocationCoverTypesHistories implements Comparable<LocationCoverTypesHistories> {

    private Long locationId;
    private Long partyId;
    private Long tileId;
    private Long vegetationHistoryId;
    @EqualsAndHashCode.Exclude
    private Long unitCount;
    @EqualsAndHashCode.Exclude
    private Double unitAreaSum;
    @EqualsAndHashCode.Exclude
    private List<LocationCoverTypesHistory> histories;

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


    @Override
    public int compareTo(LocationCoverTypesHistories o) {

        if (!this.partyId.equals(o.getPartyId())) {
            return this.partyId.compareTo(o.getPartyId());
        } else if (!this.tileId.equals(o.getTileId())) {
            return this.tileId.compareTo(o.getTileId());
        } else if (!this.locationId.equals(o.getLocationId())) {
            return this.locationId.compareTo(o.getLocationId());
        } else if (!this.vegetationHistoryId.equals(o.getVegetationHistoryId())) {
            return this.vegetationHistoryId.compareTo(o.getVegetationHistoryId());
        } else {
            return 0;
        }

    }
}

/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.dataprocessing.daos;

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
public class LocationLandUsesAllocatedFluxReportingResults{

    private Long locationId;
    private Long partyId;
    private Long tileId;
    private Long vegetationHistoryId;
    private Long unitCount;
    private Double unitAreaSum;
    private List<LocationLandUsesAllocatedFluxReportingResult> allocations;

    @Override
    public int hashCode() {
        return Objects.hash(locationId, partyId, tileId, vegetationHistoryId, unitCount, unitAreaSum);
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
                                "Allocations: %s",
                        locationId, partyId, tileId, vegetationHistoryId, unitCount, unitAreaSum, allocations.toString());
    }

}

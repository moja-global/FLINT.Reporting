/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.dataprocessor.models;

import lombok.*;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Data
@Builder
public class FluxReportingResult implements Comparable<FluxReportingResult> {

    private Long id;
    private Long dateId;
    private Long locationId;
    private Long fluxTypeId;
    private Long sourcePoolId;
    private Long sinkPoolId;
    private Long itemCount;
    private Double flux;


    @Override
    public int compareTo(FluxReportingResult fluxReportingResult) {

        if (this.id.compareTo(fluxReportingResult.getId()) == 0) {
            if (this.dateId.compareTo(fluxReportingResult.getDateId()) == 0) {
                if (this.locationId.compareTo(fluxReportingResult.getLocationId()) == 0) {
                    if (this.fluxTypeId.compareTo(fluxReportingResult.getFluxTypeId()) == 0) {
                        if (this.sourcePoolId.compareTo(fluxReportingResult.getSourcePoolId()) == 0) {
                            if (this.sinkPoolId.compareTo(fluxReportingResult.getSinkPoolId()) == 0) {
                                return this.itemCount.compareTo(fluxReportingResult.getItemCount());
                            } else {
                                return this.sinkPoolId.compareTo(fluxReportingResult.getSinkPoolId());
                            }
                        } else {
                            return this.sourcePoolId.compareTo(fluxReportingResult.getSourcePoolId());
                        }
                    } else {
                        return this.fluxTypeId.compareTo(fluxReportingResult.getFluxTypeId());
                    }
                } else {
                    return this.locationId.compareTo(fluxReportingResult.getLocationId());
                }
            } else {
                return this.dateId.compareTo(fluxReportingResult.getDateId());
            }
        } else {
            return this.id.compareTo(fluxReportingResult.getId());
        }
    }


    @Override
    public String toString() {
        return
                String.format(
                        "Id: %d, " +
                        "Date Id: %d, " +
                        "Location Id: %d, " +
                        "Flux Type Id: %d, " +
                        "Source Pool Id: %d, " +
                        "Sink Pool Id: %d, " +
                        "Flux: %f, " +
                        "Item Count: %d ",
                        id, dateId, locationId, fluxTypeId, sourcePoolId, sinkPoolId, flux, itemCount);
    }
}

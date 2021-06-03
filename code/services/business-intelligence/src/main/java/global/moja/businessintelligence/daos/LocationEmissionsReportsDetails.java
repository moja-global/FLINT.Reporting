/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.businessintelligence.daos;

import global.moja.businessintelligence.models.Location;
import lombok.Builder;

import java.util.List;

/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
public class LocationEmissionsReports extends Location {

    private List<EmissionsReportsDetail> reports;

    @Builder
    public LocationEmissionsReports(
            Long id,
            Long partyId,
            Long tileId,
            Long vegetationHistoryId,
            Long unitCount,
            Double unitAreaSum,
            List<EmissionsReportsDetail> reports) {
        super(id, partyId, tileId, vegetationHistoryId, unitCount, unitAreaSum);
        this.reports = reports;
    }
}

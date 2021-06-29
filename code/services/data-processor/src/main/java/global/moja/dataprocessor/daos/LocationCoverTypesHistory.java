/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.dataprocessor.daos;

import global.moja.dataprocessor.models.CoverType;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

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
public class LocationCoverTypesHistory implements Comparable<LocationCoverTypesHistory> {

    private Long itemNumber;
    private Integer year;
    private CoverType coverType;

    @Override
    public int compareTo(LocationCoverTypesHistory l) {

        if (this.itemNumber != null && l.getItemNumber() != null) {
            return this.itemNumber.compareTo(l.getItemNumber());
        } else if (this.year != null && l.getYear() != null) {
            return this.year.compareTo(l.getYear());
        } else if (this.coverType != null && l.getCoverType() != null) {
            return this.coverType.compareTo(l.getCoverType());
        } else {
            return 0;
        }

    }

    @Override
    public String toString() {
        return String.format("Timestep: %d, Year: %d, Cover Type: %s",
                itemNumber, year, coverType == null ? null : coverType.toString());
    }
}

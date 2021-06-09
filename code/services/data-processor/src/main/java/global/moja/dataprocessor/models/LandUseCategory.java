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
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Data
@Builder
public class LandUseCategory implements Comparable<LandUseCategory> {

    private Long id;
    private Long reportingFrameworkId;
    private Long parentLandUseCategoryId;
    private Long coverTypeId;
    private String name;
    private Integer version;

    @Override
    public int compareTo(LandUseCategory landUseCategory) {

        if(this.id != null && landUseCategory.getId() != null){
            return this.id.compareTo(landUseCategory.getId());
        } else {
            return 0;
        }

    }

    @Override
    public String toString() {
        return
                String.format(
                        "Id: %d, " +
                        "Reporting Framework Id: %d, " +
                        "Parent Land Use Category Id: %d, " +
                        "Cover Type Id: %d, " +
                        "Name: %s, " +
                        "Version: %d",
                        id, reportingFrameworkId, parentLandUseCategoryId, coverTypeId, name, version);
    }
}

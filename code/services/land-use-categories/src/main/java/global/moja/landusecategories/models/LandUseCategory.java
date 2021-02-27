/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.landusecategories.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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
}

/*
 * Copyright (C) 2021 Second Mile
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ke.co.miles.businessintelligence.models;

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
public class BusinessIntelligence implements Comparable<BusinessIntelligence> {

    private Long id;
    private Long businessIntelligenceTypeId;
    private Long businessIntelligenceDataSourceId;
    private Long unitId;
    private String name;
    private String formulae;
    private Integer version;

    @Override
    public int compareTo(BusinessIntelligence businessIntelligence) {

        if(this.id != null && businessIntelligence.getId() != null){
            return this.id.compareTo(businessIntelligence.getId());
        } else {
            return 0;
        }

    }
}

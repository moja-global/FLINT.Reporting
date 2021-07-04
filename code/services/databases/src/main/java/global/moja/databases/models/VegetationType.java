/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.databases.models;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

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
public class VegetationType implements Comparable<VegetationType> {

    private Long id;
    private Long coverTypeId;
    private String name;
    private Boolean woodyType;
    private Boolean naturalSystem;

    @Override
    public int compareTo(VegetationType vegetationType) {

        if(this.id != null && vegetationType.getId() != null){
            return this.id.compareTo(vegetationType.getId());
        } else {
            return 0;
        }

    }
}

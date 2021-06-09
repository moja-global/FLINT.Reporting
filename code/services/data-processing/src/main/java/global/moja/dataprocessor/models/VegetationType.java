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

    @Override
    public String toString() {
        return
                String.format(
                        "Id: %d, " +
                        "Cover Type Id: %d, " +
                        "Name: %s, " +
                        "Woody Type: %s, " +
                        "Natural System: %s",
                        id, coverTypeId, name, woodyType, naturalSystem);
    }

}

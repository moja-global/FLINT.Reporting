/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.fluxtypes.models;

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
public class FluxType implements Comparable<FluxType> {

    private Long id;
    private String name;
    private String description;
    private Integer version;

    @Override
    public int compareTo(FluxType fluxType) {

        if(this.id != null && fluxType.getId() != null){
            return this.id.compareTo(fluxType.getId());
        } else {
            return 0;
        }

    }
}

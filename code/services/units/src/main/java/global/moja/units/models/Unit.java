/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.units.models;

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
public class Unit implements Comparable<Unit> {

    private Long id;
    private Long unitCategoryId;
    private String name;
    private String plural;
    private String symbol;
    private Double scaleFactor;
    private Integer version;

    @Override
    public int compareTo(Unit unit) {

        if(this.id != null && unit.getId() != null){
            if(id < unit.getId()){
                return -1;
            } else if(id > unit.getId()){
                return 1;
            } else {
                return 0;
            }
        } else {
            return 0;
        }

    }
}

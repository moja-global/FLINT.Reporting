/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.unfcccvariables.models;

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
public class UnfcccVariable implements Comparable<UnfcccVariable> {

    private Long id;
    private String name;
    private String measure;
    private String abbreviation;
    private Long unitId;
    private Integer version;

    @Override
    public int compareTo(UnfcccVariable unfcccVariable) {

        if(this.id != null && unfcccVariable.getId() != null){
            if(id < unfcccVariable.getId()){
                return -1;
            } else if(id > unfcccVariable.getId()){
                return 1;
            } else {
                return 0;
            }
        } else {
            return 0;
        }

    }
}

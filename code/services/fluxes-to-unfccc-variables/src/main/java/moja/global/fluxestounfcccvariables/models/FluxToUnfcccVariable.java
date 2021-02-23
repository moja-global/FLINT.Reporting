/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.fluxestounfcccvariables.models;

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
public class FluxToUnfcccVariable implements Comparable<FluxToUnfcccVariable> {

    private Long id;
    private Long startPoolId;
    private Long endPoolId;
    private Long unfcccVariableId;
    private String rule;
    private Integer version;

    @Override
    public int compareTo(FluxToUnfcccVariable fluxToUnfcccVariable) {

        if(this.id != null && fluxToUnfcccVariable.getId() != null){
            if(id < fluxToUnfcccVariable.getId()){
                return -1;
            } else if(id > fluxToUnfcccVariable.getId()){
                return 1;
            } else {
                return 0;
            }
        } else {
            return 0;
        }

    }
}

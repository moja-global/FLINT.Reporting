/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.fluxestoreportingvariables.models;

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
public class FluxToReportingVariable implements Comparable<FluxToReportingVariable> {

    private Long id;
    private Long startPoolId;
    private Long endPoolId;
    private Long reportingVariableId;
    private String rule;
    private Integer version;

    @Override
    public int compareTo(FluxToReportingVariable fluxToReportingVariable) {

        if(this.id != null && fluxToReportingVariable.getId() != null){
            if(id < fluxToReportingVariable.getId()){
                return -1;
            } else if(id > fluxToReportingVariable.getId()){
                return 1;
            } else {
                return 0;
            }
        } else {
            return 0;
        }

    }
}

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
public class ReportingVariableEmissionType implements Comparable<ReportingVariableEmissionType>{

    private Long reportingVariableId;
    private Long emissionTypeId;
    private Integer version;

    @Override
    public int compareTo(ReportingVariableEmissionType r) {

        if(this.reportingVariableId != null && r.getReportingVariableId() != null){
            return this.reportingVariableId.compareTo(r.getReportingVariableId());
        } else if(this.emissionTypeId != null && r.getEmissionTypeId()!= null){
            return this.emissionTypeId.compareTo(r.getEmissionTypeId());
        } else {
            return 0;
        }

    }


    @Override
    public String toString() {
        return
                String.format(
                        "Reporting Variable Id: %d, " +
                        "Emission Type Id: %d, " +
                        "Version: %d ",
                        reportingVariableId, emissionTypeId, version);
    }

}

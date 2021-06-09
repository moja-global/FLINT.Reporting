/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.dataprocessing.daos;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
@Jacksonized
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Data
@Builder
public class Allocation implements Comparable<Allocation>{

    private Long fluxReportingResultId;
    private Long reportingTableId;
    private Long reportingVariableId;

    @EqualsAndHashCode.Exclude
    private Double flux;

    @Override
    public int compareTo(Allocation l) {

        if (this.fluxReportingResultId.compareTo(l.getFluxReportingResultId()) == 0) {
            if (this.reportingTableId.compareTo(l.getReportingTableId()) == 0) {
                return this.reportingVariableId.compareTo(l.getReportingVariableId());
            } else {
                return this.reportingTableId.compareTo(l.getReportingTableId());
            }
        } else {
            return this.fluxReportingResultId.compareTo(l.getFluxReportingResultId());
        }

    }

    @Override
    public String toString() {
        return
                String.format(
                        "Flux Reporting Result Id: %d, " +
                        "Reporting Table Id: %d, " +
                        "Reporting Variable Id: %d, " +
                        "Flux: %f, ",
                        fluxReportingResultId,
                        reportingTableId,
                        reportingVariableId,
                        flux);
    }

}

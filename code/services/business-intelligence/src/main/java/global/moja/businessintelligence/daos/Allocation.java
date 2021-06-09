/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.businessintelligence.daos;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
@Jacksonized
@Builder
@Setter
@Getter
@EqualsAndHashCode
public class AllocatedFluxReportingResult {

    private Long fluxReportingResultId;
    private Long reportingTableId;
    private Long reportingVariableId;
    private Double flux;

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

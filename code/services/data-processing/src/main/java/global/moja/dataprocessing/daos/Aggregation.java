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

import java.math.BigDecimal;

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
public class Aggregation implements Comparable<Aggregation>{

    private Long reportingTableId;
    private Long landUseCategoryId;
    private Long reportingVariableId;
    private Integer year;

    @EqualsAndHashCode.Exclude
    private BigDecimal amount;

    @Override
    public int compareTo(Aggregation l) {

        if (this.reportingTableId.compareTo(l.getReportingTableId()) == 0) {
            if (this.landUseCategoryId.compareTo(l.getLandUseCategoryId()) == 0) {
                if (this.reportingVariableId.compareTo(l.getReportingVariableId()) == 0) {
                    return this.year.compareTo(l.getYear());
                } else {
                    return this.reportingVariableId.compareTo(l.getReportingVariableId());
                }
            } else {
                return this.landUseCategoryId.compareTo(l.getLandUseCategoryId());
            }
        } else {
            return this.reportingTableId.compareTo(l.getReportingTableId());
        }

    }

    @Override
    public String toString() {
        return
                String.format(
                        "Reporting Table Id: %d, " +
                        "Land Use Category Id: %d, " +
                        "Reporting Variable Id: %d, " +
                        "Year: %d, " +
                        "Amount: %f ",
                        reportingTableId,
                        landUseCategoryId,
                        reportingVariableId,
                        year,
                        amount);
    }

}

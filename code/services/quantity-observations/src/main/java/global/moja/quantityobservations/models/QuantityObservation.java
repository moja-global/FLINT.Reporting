/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.quantityobservations.models;

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
public class QuantityObservation implements Comparable<QuantityObservation> {

    private Long id;
    private Long observationTypeId;
    private Long taskId;
    private Long partyId;
    private Long databaseId;
    private Long landUseCategoryId;
    private Long reportingTableId;
    private Long reportingVariableId;
    private Integer year;
    private Double amount;
    private Long unitId;
    private Integer version;

    @Override
    public int compareTo(QuantityObservation quantityObservation) {

        if(this.id != null && quantityObservation.getId() != null){
            return this.id.compareTo(quantityObservation.getId());
        } else {
            return 0;
        }

    }
}

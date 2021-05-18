/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.businessintelligence.util.builders;

import global.moja.businessintelligence.daos.VegetationHistoryCoverType;

/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
public class VegetationHistoryCoverTypeBuilder{

    private Long id;
    private Long vegetationHistoryId;
    private Long coverTypeId;
    private String coverTypeDescription;
    private Long itemNumber;
    private Integer year;

    public VegetationHistoryCoverTypeBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public VegetationHistoryCoverTypeBuilder vegetationHistoryId(Long vegetationHistoryId) {
        this.vegetationHistoryId = vegetationHistoryId;
        return this;
    }

    public VegetationHistoryCoverTypeBuilder coverTypeId(Long coverTypeId) {
        this.coverTypeId = coverTypeId;
        return this;
    }

    public VegetationHistoryCoverTypeBuilder coverTypeDescription(String coverTypeDescription) {
        this.coverTypeDescription = coverTypeDescription;
        return this;
    }

    public VegetationHistoryCoverTypeBuilder itemNumber(Long itemNumber) {
        this.itemNumber = itemNumber;
        return this;
    }

    public VegetationHistoryCoverTypeBuilder year(Integer year) {
        this.year = year;
        return this;
    }

    public VegetationHistoryCoverType build() {
        return new VegetationHistoryCoverType(id, vegetationHistoryId, coverTypeId, coverTypeDescription, itemNumber, year);
    }
}

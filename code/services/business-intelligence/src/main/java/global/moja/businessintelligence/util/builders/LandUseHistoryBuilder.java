/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.businessintelligence.util.builders;


import global.moja.businessintelligence.daos.LandUseHistoricDetail;
import global.moja.businessintelligence.models.LandUseCategory;

/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
public class LandUseHistoryBuilder {
    
    private Long itemNumber;
    private Integer year;
    private LandUseCategory landUseCategory;
    private Boolean confirmed;

    public LandUseHistoryBuilder itemNumber(Long itemNumber) {
        this.itemNumber = itemNumber;
        return this;
    }

    public LandUseHistoryBuilder year(Integer year) {
        this.year = year;
        return this;
    }

    public LandUseHistoryBuilder landUseCategory(LandUseCategory landUseCategory) {
        this.landUseCategory = landUseCategory;
        return this;
    }

    public LandUseHistoryBuilder confirmed(Boolean confirmed) {
        this.confirmed = confirmed;
        return this;
    }

    public LandUseHistoricDetail build() {
        return new LandUseHistoricDetail(itemNumber, year, landUseCategory, confirmed);
    }
}

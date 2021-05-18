/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.businessintelligence.util.builders;

import global.moja.businessintelligence.daos.CoverTypeHistory;
import global.moja.businessintelligence.models.CoverType;

/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
public class CoverTypeHistoryBuilder {

    private Long itemNumber;
    private Integer year;
    private CoverType coverType;

    public CoverTypeHistoryBuilder itemNumber(Long itemNumber) {
        this.itemNumber = itemNumber;
        return this;
    }

    public CoverTypeHistoryBuilder year(Integer year) {
        this.year = year;
        return this;
    }

    public CoverTypeHistoryBuilder coverType(CoverType coverType) {
        this.coverType = coverType;
        return this;
    }

    public CoverTypeHistory build() {
        return new CoverTypeHistory(itemNumber, year, coverType);
    }
}

/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.vegetationhistoryvegetationtypes.util.builders;

import global.moja.vegetationhistoryvegetationtypes.models.VegetationHistoryVegetationType;


/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
public class VegetationHistoryVegetationTypeBuilder {

	private Long id;
	private Long vegetationHistoryId;
	private Long vegetationTypeId;
	private Long itemNumber;
	private Integer year;

	public VegetationHistoryVegetationTypeBuilder id(Long id) {
		this.id = id;
		return this;
	}

	public VegetationHistoryVegetationTypeBuilder vegetationHistoryId(Long vegetationHistoryId) {
		this.vegetationHistoryId = vegetationHistoryId;
		return this;
	}

	public VegetationHistoryVegetationTypeBuilder vegetationTypeId(Long vegetationTypeId) {
		this.vegetationTypeId = vegetationTypeId;
		return this;
	}

	public VegetationHistoryVegetationTypeBuilder itemNumber(Long itemNumber) {
		this.itemNumber = itemNumber;
		return this;
	}

	public VegetationHistoryVegetationTypeBuilder year(Integer year) {
		this.year = year;
		return this;
	}

	public VegetationHistoryVegetationType build(){
		return new VegetationHistoryVegetationType(id,vegetationHistoryId,vegetationTypeId,itemNumber,year);
	}
}

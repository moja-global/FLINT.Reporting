/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.conversionandremainingperiods.util.builders;

import global.moja.conversionandremainingperiods.models.ConversionAndRemainingPeriod;


/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
public class ConversionAndRemainingPeriodBuilder {

	private Long id;
	private Long previousLandCoverId;
	private Long currentLandCoverId;
	private Integer conversionPeriod;
	private Integer remainingPeriod;
	private Integer version;

	public ConversionAndRemainingPeriodBuilder id(Long id) {
		this.id = id;
		return this;
	}

	public ConversionAndRemainingPeriodBuilder previousLandCoverId(Long previousLandCoverId) {
		this.previousLandCoverId = previousLandCoverId;
		return this;
	}

	public ConversionAndRemainingPeriodBuilder currentLandCoverId(Long currentLandCoverId) {
		this.currentLandCoverId = currentLandCoverId;
		return this;
	}

	public ConversionAndRemainingPeriodBuilder conversionPeriod(Integer conversionPeriod) {
		this.conversionPeriod = conversionPeriod;
		return this;
	}

	public ConversionAndRemainingPeriodBuilder remainingPeriod(Integer remainingPeriod) {
		this.remainingPeriod = remainingPeriod;
		return this;
	}

	public ConversionAndRemainingPeriodBuilder version(Integer version) {
		this.version = version;
		return this;
	}

	public ConversionAndRemainingPeriod build(){
		return new ConversionAndRemainingPeriod(id,previousLandCoverId,currentLandCoverId,conversionPeriod,remainingPeriod,version);
	}
}

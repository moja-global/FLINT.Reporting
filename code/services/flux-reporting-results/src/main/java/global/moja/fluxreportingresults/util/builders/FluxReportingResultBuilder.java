/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.fluxreportingresults.util.builders;

import global.moja.fluxreportingresults.models.FluxReportingResult;


/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
public class FluxReportingResultBuilder {

	private Long id;
	private Long dateId;
	private Long locationId;
	private Long fluxTypeId;
	private Long sourcePoolId;
	private Long sinkPoolId;
	private Double flux;
	private Long itemCount;

	public FluxReportingResultBuilder id(Long id) {
		this.id = id;
		return this;
	}

	public FluxReportingResultBuilder dateId(Long dateId) {
		this.dateId = dateId;
		return this;
	}

	public FluxReportingResultBuilder locationId(Long locationId) {
		this.locationId = locationId;
		return this;
	}

	public FluxReportingResultBuilder fluxTypeId(Long fluxTypeId) {
		this.fluxTypeId = fluxTypeId;
		return this;
	}

	public FluxReportingResultBuilder sourcePoolId(Long sourcePoolId) {
		this.sourcePoolId = sourcePoolId;
		return this;
	}

	public FluxReportingResultBuilder sinkPoolId(Long sinkPoolId) {
		this.sinkPoolId = sinkPoolId;
		return this;
	}

	public FluxReportingResultBuilder flux(Double flux) {
		this.flux = flux;
		return this;
	}

	public FluxReportingResultBuilder itemCount(Long itemCount) {
		this.itemCount = itemCount;
		return this;
	}

	public FluxReportingResult build(){
		return new FluxReportingResult(id,dateId,locationId,fluxTypeId,sourcePoolId,sinkPoolId,flux,itemCount);
	}
}

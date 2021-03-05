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

	private Long dateId;
	private Long locationId;
	private Long fluxTypeId;
	private Long sourcePoolId;
	private Long sinkPoolId;

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

	public FluxReportingResult build(){
		return new FluxReportingResult(dateId,locationId,fluxTypeId,sourcePoolId,sinkPoolId);
	}
}

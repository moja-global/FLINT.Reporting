/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.landusesfluxtypestoreportingtables.util.builders;

import global.moja.landusesfluxtypestoreportingtables.models.LandUseFluxTypeToReportingTable;


/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
public class LandUseFluxTypeToReportingTableBuilder {

	private Long id;
	private Long landUseFluxTypeId;
	private Long emissionTypeId;
	private Long reportingTableId;
	private Integer version;

	public LandUseFluxTypeToReportingTableBuilder id(Long id) {
		this.id = id;
		return this;
	}

	public LandUseFluxTypeToReportingTableBuilder landUseFluxTypeId(Long landUseFluxTypeId) {
		this.landUseFluxTypeId = landUseFluxTypeId;
		return this;
	}

	public LandUseFluxTypeToReportingTableBuilder emissionTypeId(Long emissionTypeId) {
		this.emissionTypeId = emissionTypeId;
		return this;
	}

	public LandUseFluxTypeToReportingTableBuilder reportingTableId(Long reportingTableId) {
		this.reportingTableId = reportingTableId;
		return this;
	}

	public LandUseFluxTypeToReportingTableBuilder version(Integer version) {
		this.version = version;
		return this;
	}

	public LandUseFluxTypeToReportingTable build(){
		return new LandUseFluxTypeToReportingTable(id,landUseFluxTypeId,emissionTypeId,reportingTableId,version);
	}
}

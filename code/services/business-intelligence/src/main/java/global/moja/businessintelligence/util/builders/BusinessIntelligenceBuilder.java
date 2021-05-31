/*
 * Copyright (C) 2021 Second Mile
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.businessintelligence.util.builders;

import global.moja.businessintelligence.models.BusinessIntelligence;


/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
public class BusinessIntelligenceBuilder {

	private Long id;
	private Long businessIntelligenceTypeId;
	private Long businessIntelligenceDataSourceId;
	private Long unitId;
	private String name;
	private String formulae;
	private Integer version;

	public BusinessIntelligenceBuilder id(Long id) {
		this.id = id;
		return this;
	}

	public BusinessIntelligenceBuilder businessIntelligenceTypeId(Long businessIntelligenceTypeId) {
		this.businessIntelligenceTypeId = businessIntelligenceTypeId;
		return this;
	}

	public BusinessIntelligenceBuilder businessIntelligenceDataSourceId(Long businessIntelligenceDataSourceId) {
		this.businessIntelligenceDataSourceId = businessIntelligenceDataSourceId;
		return this;
	}

	public BusinessIntelligenceBuilder unitId(Long unitId) {
		this.unitId = unitId;
		return this;
	}

	public BusinessIntelligenceBuilder name(String name) {
		this.name = name;
		return this;
	}

	public BusinessIntelligenceBuilder formulae(String formulae) {
		this.formulae = formulae;
		return this;
	}

	public BusinessIntelligenceBuilder version(Integer version) {
		this.version = version;
		return this;
	}

	public BusinessIntelligence build(){
		return new BusinessIntelligence(id,businessIntelligenceTypeId,businessIntelligenceDataSourceId,unitId,name,formulae,version);
	}
}

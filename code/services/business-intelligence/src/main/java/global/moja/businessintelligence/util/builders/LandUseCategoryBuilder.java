/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.businessintelligence.util.builders;


import global.moja.businessintelligence.models.LandUseCategory;

/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
public class LandUseCategoryBuilder {

	private Long id;
	private Long reportingFrameworkId;
	private Long parentLandUseCategoryId;
	private Long coverTypeId;
	private String name;
	private Integer version;

	public LandUseCategoryBuilder id(Long id) {
		this.id = id;
		return this;
	}

	public LandUseCategoryBuilder reportingFrameworkId(Long reportingFrameworkId) {
		this.reportingFrameworkId = reportingFrameworkId;
		return this;
	}

	public LandUseCategoryBuilder parentLandUseCategoryId(Long parentLandUseCategoryId) {
		this.parentLandUseCategoryId = parentLandUseCategoryId;
		return this;
	}

	public LandUseCategoryBuilder coverTypeId(Long coverTypeId) {
		this.coverTypeId = coverTypeId;
		return this;
	}

	public LandUseCategoryBuilder name(String name) {
		this.name = name;
		return this;
	}

	public LandUseCategoryBuilder version(Integer version) {
		this.version = version;
		return this;
	}

	public LandUseCategory build(){
		return new LandUseCategory(id,reportingFrameworkId,parentLandUseCategoryId,coverTypeId,name,version);
	}
}

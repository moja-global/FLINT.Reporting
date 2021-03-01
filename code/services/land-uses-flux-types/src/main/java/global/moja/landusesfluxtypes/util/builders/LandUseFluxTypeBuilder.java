/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.landusesfluxtypes.util.builders;

import global.moja.landusesfluxtypes.models.LandUseFluxType;


/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
public class LandUseFluxTypeBuilder {

	private Long id;
	private Long landUseCategoryId;
	private Long fluxTypeId;
	private Integer version;

	public LandUseFluxTypeBuilder id(Long id) {
		this.id = id;
		return this;
	}

	public LandUseFluxTypeBuilder landUseCategoryId(Long landUseCategoryId) {
		this.landUseCategoryId = landUseCategoryId;
		return this;
	}

	public LandUseFluxTypeBuilder fluxTypeId(Long fluxTypeId) {
		this.fluxTypeId = fluxTypeId;
		return this;
	}

	public LandUseFluxTypeBuilder version(Integer version) {
		this.version = version;
		return this;
	}

	public LandUseFluxType build(){
		return new LandUseFluxType(id,landUseCategoryId,fluxTypeId,version);
	}
}

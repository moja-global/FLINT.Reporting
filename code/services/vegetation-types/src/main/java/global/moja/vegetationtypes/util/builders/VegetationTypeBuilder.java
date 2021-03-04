/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.vegetationtypes.util.builders;

import global.moja.vegetationtypes.models.VegetationType;


/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
public class VegetationTypeBuilder {

	private Long id;
	private Long coverTypeId;
	private String name;
	private Boolean woodyType;
	private Boolean naturalSystem;

	public VegetationTypeBuilder id(Long id) {
		this.id = id;
		return this;
	}

	public VegetationTypeBuilder coverTypeId(Long coverTypeId) {
		this.coverTypeId = coverTypeId;
		return this;
	}

	public VegetationTypeBuilder name(String name) {
		this.name = name;
		return this;
	}

	public VegetationTypeBuilder woodyType(Boolean woodyType) {
		this.woodyType = woodyType;
		return this;
	}

	public VegetationTypeBuilder naturalSystem(Boolean naturalSystem) {
		this.naturalSystem = naturalSystem;
		return this;
	}

	public VegetationType build(){
		return new VegetationType(id,coverTypeId,name,woodyType,naturalSystem);
	}
}

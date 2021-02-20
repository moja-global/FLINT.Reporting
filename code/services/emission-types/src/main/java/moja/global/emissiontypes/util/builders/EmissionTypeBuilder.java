/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.emissiontypes.util.builders;

import moja.global.emissiontypes.models.EmissionType;


/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
public class EmissionTypeBuilder {
	
	private Long id;
	private String name;
	private String abbreviation;
	private String description;
	private Integer version;

	public EmissionTypeBuilder id(Long id) {
		this.id = id;
		return this;
	}

	public EmissionTypeBuilder name(String name) {
		this.name = name;
		return this;
	}

	public EmissionTypeBuilder abbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
		return this;
	}

	public EmissionTypeBuilder description(String description) {
		this.description = description;
		return this;
	}

	public EmissionTypeBuilder version(Integer version) {
		this.version = version;
		return this;
	}
	
	
	public EmissionType build() {

		return new EmissionType(id, name, abbreviation, description, version);
	}

}

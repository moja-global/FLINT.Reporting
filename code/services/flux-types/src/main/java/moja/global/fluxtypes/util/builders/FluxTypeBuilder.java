/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.fluxtypes.util.builders;

import moja.global.fluxtypes.models.FluxType;


/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
public class FluxTypeBuilder {
	
	private Long id;
	private String name;
	private String description;
	private Integer version;

	public FluxTypeBuilder id(Long id) {
		this.id = id;
		return this;
	}

	public FluxTypeBuilder name(String name) {
		this.name = name;
		return this;
	}

	public FluxTypeBuilder description(String description) {
		this.description = description;
		return this;
	}

	public FluxTypeBuilder version(Integer version) {
		this.version = version;
		return this;
	}
	
	
	public FluxType build() {

		return new FluxType(id, name, description, version);
	}

}

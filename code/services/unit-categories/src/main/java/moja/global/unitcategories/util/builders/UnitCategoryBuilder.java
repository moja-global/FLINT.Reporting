/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.unitcategories.util.builders;

import moja.global.unitcategories.models.UnitCategory;


/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
public class UnitCategoryBuilder {
	
	private Long id;
	private String name;
	private Integer version;

	public UnitCategoryBuilder id(Long id) {
		this.id = id;
		return this;
	}

	public UnitCategoryBuilder name(String name) {
		this.name = name;
		return this;
	}

	public UnitCategoryBuilder version(Integer version) {
		this.version = version;
		return this;
	}
	
	
	public UnitCategory build() {

		return new UnitCategory(id, name, version);
	}

}

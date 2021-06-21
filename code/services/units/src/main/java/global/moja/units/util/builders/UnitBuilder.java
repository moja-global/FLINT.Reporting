/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.units.util.builders;

import global.moja.units.models.Unit;


/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
public class UnitBuilder {

	private Long id;
	private Long unitCategoryId;
	private String name;
	private String plural;
	private String symbol;
	private Double scaleFactor;
	private Integer version;

	public UnitBuilder id(Long id) {
		this.id = id;
		return this;
	}

	public UnitBuilder unitCategoryId(Long unitCategoryId) {
		this.unitCategoryId = unitCategoryId;
		return this;
	}

	public UnitBuilder name(String name) {
		this.name = name;
		return this;
	}

	public UnitBuilder plural(String plural) {
		this.plural = plural;
		return this;
	}

	public UnitBuilder symbol(String symbol) {
		this.symbol = symbol;
		return this;
	}

	public UnitBuilder scaleFactor(Double scaleFactor) {
		this.scaleFactor = scaleFactor;
		return this;
	}

	public UnitBuilder version(Integer version) {
		this.version = version;
		return this;
	}

	public Unit build() {
		return new Unit(id,unitCategoryId,name,plural,symbol,scaleFactor,version);
	}
}

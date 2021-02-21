/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.unfcccvariables.util.builders;

import moja.global.unfcccvariables.models.UnfcccVariable;


/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
public class UnfcccVariableBuilder {
	
	private Long id;
	private String name;
	private String measure;
	private String abbreviation;
	private Long unitId;
	private Integer version;

	public UnfcccVariableBuilder id(Long id) {
		this.id = id;
		return this;
	}

	public UnfcccVariableBuilder name(String name) {
		this.name = name;
		return this;
	}

	public UnfcccVariableBuilder measure(String measure) {
		this.measure = measure;
		return this;
	}

	public UnfcccVariableBuilder abbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
		return this;
	}

	public UnfcccVariableBuilder unitId(Long unitId) {
		this.unitId = unitId;
		return this;
	}

	public UnfcccVariableBuilder version(Integer version) {
		this.version = version;
		return this;
	}
	
	
	public UnfcccVariable build() {

		return new UnfcccVariable(id, name, measure, abbreviation, unitId, version);
	}

}

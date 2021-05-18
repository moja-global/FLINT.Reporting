/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.businessintelligence.util.builders;

import global.moja.covertypes.models.CoverType;


/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
public class CoverTypeBuilder {

	private Long id;
	private String code;
	private String description;
	private Integer version;

	public CoverTypeBuilder id(Long id) {
		this.id = id;
		return this;
	}

	public CoverTypeBuilder code(String code) {
		this.code = code;
		return this;
	}

	public CoverTypeBuilder description(String description) {
		this.description = description;
		return this;
	}

	public CoverTypeBuilder version(Integer version) {
		this.version = version;
		return this;
	}

	public CoverType build(){
		return new CoverType(id,code,description,version);
	}
}

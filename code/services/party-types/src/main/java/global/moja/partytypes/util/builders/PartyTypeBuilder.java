/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.partytypes.util.builders;

import global.moja.partytypes.models.PartyType;


/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
public class PartyTypeBuilder {

	private Long id;
	private Long parentPartyTypeId;
	private String name;
	private Integer version;

	public PartyTypeBuilder id(Long id) {
		this.id = id;
		return this;
	}

	public PartyTypeBuilder parentPartyTypeId(Long parentPartyTypeId) {
		this.parentPartyTypeId = parentPartyTypeId;
		return this;
	}



	public PartyTypeBuilder name(String name) {
		this.name = name;
		return this;
	}


	public PartyTypeBuilder version(Integer version) {
		this.version = version;
		return this;
	}

	public PartyType build(){
		return new PartyType(id,parentPartyTypeId,name,version);
	}


}

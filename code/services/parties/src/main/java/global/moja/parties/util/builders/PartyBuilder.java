/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.parties.util.builders;

import global.moja.parties.models.Party;


/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
public class PartyBuilder {

	private Long id;
	private Long partyTypeId;
	private String name;
	private Integer version;

	public PartyBuilder id(Long id) {
		this.id = id;
		return this;
	}

	public PartyBuilder partyTypeId(Long partyTypeId) {
		this.partyTypeId = partyTypeId;
		return this;
	}

	public PartyBuilder name(String name) {
		this.name = name;
		return this;
	}

	public PartyBuilder version(Integer version) {
		this.version = version;
		return this;
	}

	public Party build(){
		return new Party(id,partyTypeId,name,version);
	}
}

/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.accountabilitytypes.util.builders;

import global.moja.accountabilitytypes.models.AccountabilityType;


/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
public class AccountabilityTypeBuilder {

	private Long id;
	private String name;
	private Integer version;

	public AccountabilityTypeBuilder id(Long id) {
		this.id = id;
		return this;
	}

	public AccountabilityTypeBuilder name(String name) {
		this.name = name;
		return this;
	}


	public AccountabilityTypeBuilder version(Integer version) {
		this.version = version;
		return this;
	}

	public AccountabilityType build(){
		return new AccountabilityType(id,name,version);
	}
}

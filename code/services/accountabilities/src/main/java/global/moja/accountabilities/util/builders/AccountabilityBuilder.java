/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.accountabilities.util.builders;

import global.moja.accountabilities.models.Accountability;


/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
public class AccountabilityBuilder {

	private Long id;
	private Long accountabilityTypeId;
	private Long parentPartyId;
	private Long subsidiaryPartyId;
	private Integer version;

	public AccountabilityBuilder id(Long id) {
		this.id = id;
		return this;
	}

	public AccountabilityBuilder accountabilityTypeId(Long accountabilityTypeId) {
		this.accountabilityTypeId = accountabilityTypeId;
		return this;
	}

	public AccountabilityBuilder parentPartyId(Long parentPartyId) {
		this.parentPartyId = parentPartyId;
		return this;
	}

	public AccountabilityBuilder subsidiaryPartyId(Long subsidiaryPartyId) {
		this.subsidiaryPartyId = subsidiaryPartyId;
		return this;
	}


	public AccountabilityBuilder version(Integer version) {
		this.version = version;
		return this;
	}

	public Accountability build(){
		return new Accountability(id,accountabilityTypeId,parentPartyId,subsidiaryPartyId,version);
	}


}

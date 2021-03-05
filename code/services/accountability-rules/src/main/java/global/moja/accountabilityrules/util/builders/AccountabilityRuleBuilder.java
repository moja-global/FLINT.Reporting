/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.accountabilityrules.util.builders;

import global.moja.accountabilityrules.models.AccountabilityRule;


/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
public class AccountabilityRuleBuilder {

	private Long id;
	private Long accountabilityTypeId;
	private Long parentPartyTypeId;
	private Long subsidiaryPartyTypeId;
	private Integer version;

	public AccountabilityRuleBuilder id(Long id) {
		this.id = id;
		return this;
	}

	public AccountabilityRuleBuilder accountabilityTypeId(Long accountabilityTypeId) {
		this.accountabilityTypeId = accountabilityTypeId;
		return this;
	}

	public AccountabilityRuleBuilder parentPartyTypeId(Long parentPartyTypeId) {
		this.parentPartyTypeId = parentPartyTypeId;
		return this;
	}

	public AccountabilityRuleBuilder subsidiaryPartyTypeId(Long subsidiaryPartyTypeId) {
		this.subsidiaryPartyTypeId = subsidiaryPartyTypeId;
		return this;
	}



	public AccountabilityRuleBuilder version(Integer version) {
		this.version = version;
		return this;
	}

	public AccountabilityRule build(){
		return new AccountabilityRule(id,accountabilityTypeId,parentPartyTypeId,subsidiaryPartyTypeId,version);
	}



}

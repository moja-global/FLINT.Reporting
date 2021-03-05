/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.accountabilityrules.repository;

import global.moja.accountabilityrules.daos.QueryParameters;
import global.moja.accountabilityrules.models.AccountabilityRule;
import global.moja.accountabilityrules.repository.insertion.InsertAccountabilityRulesQuery;
import global.moja.accountabilityrules.repository.updation.UpdateAccountabilityRuleQuery;
import global.moja.accountabilityrules.repository.updation.UpdateAccountabilityRulesQuery;
import global.moja.accountabilityrules.repository.deletion.DeleteAccountabilityRuleQuery;
import global.moja.accountabilityrules.repository.deletion.DeleteAccountabilityRulesQuery;
import global.moja.accountabilityrules.repository.selection.SelectAccountabilityRulesQuery;
import global.moja.accountabilityrules.repository.insertion.InsertAccountabilityRuleQuery;
import global.moja.accountabilityrules.repository.selection.SelectAccountabilityRuleQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class AccountabilityRulesRepository {

	
	@Autowired
	InsertAccountabilityRuleQuery insertAccountabilityRuleQuery;
	
	@Autowired
    InsertAccountabilityRulesQuery insertAccountabilityRulesQuery;
	
	@Autowired
	SelectAccountabilityRuleQuery selectAccountabilityRuleQuery;
	
	@Autowired
	SelectAccountabilityRulesQuery selectAccountabilityRulesQuery;

	@Autowired
    UpdateAccountabilityRuleQuery updateAccountabilityRuleQuery;
	
	@Autowired
	UpdateAccountabilityRulesQuery updateAccountabilityRulesQuery;
	
	@Autowired
	DeleteAccountabilityRuleQuery deleteAccountabilityRuleQuery;
	
	@Autowired
    DeleteAccountabilityRulesQuery deleteAccountabilityRulesQuery;


	public Mono<Long> insertAccountabilityRule(AccountabilityRule accountabilityRule) {
		return insertAccountabilityRuleQuery.insertAccountabilityRule(accountabilityRule);
	}
	
	public Flux<Long> insertAccountabilityRules(AccountabilityRule[] accountabilityRules) {
		return insertAccountabilityRulesQuery.insertAccountabilityRules(accountabilityRules);
	}

	public Mono<AccountabilityRule> selectAccountabilityRule(Long id) {
		return selectAccountabilityRuleQuery.selectAccountabilityRule(id);
	}
	
	public Flux<AccountabilityRule> selectAccountabilityRules(QueryParameters parameters) {
		return selectAccountabilityRulesQuery.selectAccountabilityRules(parameters);
	}

	public Mono<Integer> updateAccountabilityRule(AccountabilityRule accountabilityRule) {
		return updateAccountabilityRuleQuery.updateAccountabilityRule(accountabilityRule);
	}
	
	public Flux<Integer> updateAccountabilityRules(AccountabilityRule[] accountabilityRules) {
		return updateAccountabilityRulesQuery.updateAccountabilityRules(accountabilityRules);
	}	
	
	public Mono<Integer> deleteAccountabilityRuleById(Long id) {
		return deleteAccountabilityRuleQuery.deleteAccountabilityRule(id);
	}
	
	public Mono<Integer> deleteAccountabilityRules(QueryParameters parameters) {
		return deleteAccountabilityRulesQuery.deleteAccountabilityRules(parameters);
	}


}

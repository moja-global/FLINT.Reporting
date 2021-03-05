/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.accountabilitytypes.repository;

import global.moja.accountabilitytypes.repository.deletion.DeleteAccountabilityTypesQuery;
import global.moja.accountabilitytypes.daos.QueryParameters;
import global.moja.accountabilitytypes.models.AccountabilityType;
import global.moja.accountabilitytypes.repository.updation.UpdateAccountabilityTypeQuery;
import global.moja.accountabilitytypes.repository.updation.UpdateAccountabilityTypesQuery;
import global.moja.accountabilitytypes.repository.deletion.DeleteAccountabilityTypeQuery;
import global.moja.accountabilitytypes.repository.selection.SelectAccountabilityTypesQuery;
import global.moja.accountabilitytypes.repository.insertion.InsertAccountabilityTypeQuery;
import global.moja.accountabilitytypes.repository.insertion.InsertAccountabilityTypesQuery;
import global.moja.accountabilitytypes.repository.selection.SelectAccountabilityTypeQuery;
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
public class AccountabilityTypesRepository {

	
	@Autowired
	InsertAccountabilityTypeQuery insertAccountabilityTypeQuery;
	
	@Autowired
	InsertAccountabilityTypesQuery insertAccountabilityTypesQuery;
	
	@Autowired
	SelectAccountabilityTypeQuery selectAccountabilityTypeQuery;
	
	@Autowired
	SelectAccountabilityTypesQuery selectAccountabilityTypesQuery;

	@Autowired
	UpdateAccountabilityTypeQuery updateAccountabilityTypeQuery;
	
	@Autowired
	UpdateAccountabilityTypesQuery updateAccountabilityTypesQuery;
	
	@Autowired
	DeleteAccountabilityTypeQuery deleteAccountabilityTypeQuery;
	
	@Autowired
    DeleteAccountabilityTypesQuery deleteAccountabilityTypesQuery;


	public Mono<Long> insertAccountabilityType(AccountabilityType accountabilityType) {
		return insertAccountabilityTypeQuery.insertAccountabilityType(accountabilityType);
	}
	
	public Flux<Long> insertAccountabilityTypes(AccountabilityType[] accountabilityTypes) {
		return insertAccountabilityTypesQuery.insertAccountabilityTypes(accountabilityTypes);
	}

	public Mono<AccountabilityType> selectAccountabilityType(Long id) {
		return selectAccountabilityTypeQuery.selectAccountabilityType(id);
	}
	
	public Flux<AccountabilityType> selectAccountabilityTypes(QueryParameters parameters) {
		return selectAccountabilityTypesQuery.selectAccountabilityTypes(parameters);
	}

	public Mono<Integer> updateAccountabilityType(AccountabilityType accountabilityType) {
		return updateAccountabilityTypeQuery.updateAccountabilityType(accountabilityType);
	}
	
	public Flux<Integer> updateAccountabilityTypes(AccountabilityType[] accountabilityTypes) {
		return updateAccountabilityTypesQuery.updateAccountabilityTypes(accountabilityTypes);
	}	
	
	public Mono<Integer> deleteAccountabilityTypeById(Long id) {
		return deleteAccountabilityTypeQuery.deleteAccountabilityType(id);
	}
	
	public Mono<Integer> deleteAccountabilityTypes(QueryParameters parameters) {
		return deleteAccountabilityTypesQuery.deleteAccountabilityTypes(parameters);
	}


}

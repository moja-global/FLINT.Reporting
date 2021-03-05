/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.accountabilities.repository;

import global.moja.accountabilities.models.Accountability;
import global.moja.accountabilities.repository.insertion.InsertAccountabilityQuery;
import global.moja.accountabilities.repository.selection.SelectAccountabilitiesQuery;
import global.moja.accountabilities.repository.updation.UpdateAccountabilityQuery;
import global.moja.accountabilities.daos.QueryParameters;
import global.moja.accountabilities.repository.updation.UpdateAccountabilitiesQuery;
import global.moja.accountabilities.repository.deletion.DeleteAccountabilityQuery;
import global.moja.accountabilities.repository.deletion.DeleteAccountabilitiesQuery;
import global.moja.accountabilities.repository.insertion.InsertAccountabilitiesQuery;
import global.moja.accountabilities.repository.selection.SelectAccountabilityQuery;
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
public class AccountabilitiesRepository {

	
	@Autowired
    InsertAccountabilityQuery insertAccountabilityQuery;
	
	@Autowired
	InsertAccountabilitiesQuery insertAccountabilitiesQuery;
	
	@Autowired
	SelectAccountabilityQuery selectAccountabilityQuery;
	
	@Autowired
    SelectAccountabilitiesQuery selectAccountabilitiesQuery;

	@Autowired
    UpdateAccountabilityQuery updateAccountabilityQuery;
	
	@Autowired
	UpdateAccountabilitiesQuery updateAccountabilitiesQuery;
	
	@Autowired
	DeleteAccountabilityQuery deleteAccountabilityQuery;
	
	@Autowired
    DeleteAccountabilitiesQuery deleteAccountabilitiesQuery;


	public Mono<Long> insertAccountability(Accountability accountability) {
		return insertAccountabilityQuery.insertAccountability(accountability);
	}
	
	public Flux<Long> insertAccountabilities(Accountability[] accountabilities) {
		return insertAccountabilitiesQuery.insertAccountabilities(accountabilities);
	}

	public Mono<Accountability> selectAccountability(Long id) {
		return selectAccountabilityQuery.selectAccountability(id);
	}
	
	public Flux<Accountability> selectAccountabilities(QueryParameters parameters) {
		return selectAccountabilitiesQuery.selectAccountabilities(parameters);
	}

	public Mono<Integer> updateAccountability(Accountability accountability) {
		return updateAccountabilityQuery.updateAccountability(accountability);
	}
	
	public Flux<Integer> updateAccountabilities(Accountability[] accountabilities) {
		return updateAccountabilitiesQuery.updateAccountabilities(accountabilities);
	}	
	
	public Mono<Integer> deleteAccountabilityById(Long id) {
		return deleteAccountabilityQuery.deleteAccountability(id);
	}
	
	public Mono<Integer> deleteAccountabilities(QueryParameters parameters) {
		return deleteAccountabilitiesQuery.deleteAccountabilities(parameters);
	}


}

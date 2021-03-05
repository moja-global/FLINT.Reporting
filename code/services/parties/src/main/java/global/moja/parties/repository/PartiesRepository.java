/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.parties.repository;

import global.moja.parties.models.Party;
import global.moja.parties.repository.deletion.DeletePartyQuery;
import global.moja.parties.repository.insertion.InsertPartiesQuery;
import global.moja.parties.repository.insertion.InsertPartyQuery;
import global.moja.parties.repository.selection.SelectPartiesQuery;
import global.moja.parties.repository.selection.SelectPartyQuery;
import global.moja.parties.repository.updation.UpdatePartyQuery;
import global.moja.parties.daos.QueryParameters;
import global.moja.parties.repository.updation.UpdatePartiesQuery;
import global.moja.parties.repository.deletion.DeletePartiesQuery;
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
public class PartiesRepository {

	
	@Autowired
    InsertPartyQuery insertPartyQuery;
	
	@Autowired
    InsertPartiesQuery insertPartiesQuery;
	
	@Autowired
    SelectPartyQuery selectPartyQuery;
	
	@Autowired
    SelectPartiesQuery selectPartiesQuery;

	@Autowired
    UpdatePartyQuery updatePartyQuery;
	
	@Autowired
	UpdatePartiesQuery updatePartiesQuery;
	
	@Autowired
    DeletePartyQuery deletePartyQuery;
	
	@Autowired
    DeletePartiesQuery deletePartiesQuery;


	public Mono<Long> insertParty(Party party) {
		return insertPartyQuery.insertParty(party);
	}
	
	public Flux<Long> insertParties(Party[] parties) {
		return insertPartiesQuery.insertParties(parties);
	}

	public Mono<Party> selectParty(Long id) {
		return selectPartyQuery.selectParty(id);
	}
	
	public Flux<Party> selectParties(QueryParameters parameters) {
		return selectPartiesQuery.selectParties(parameters);
	}

	public Mono<Integer> updateParty(Party party) {
		return updatePartyQuery.updateParty(party);
	}
	
	public Flux<Integer> updateParties(Party[] parties) {
		return updatePartiesQuery.updateParties(parties);
	}	
	
	public Mono<Integer> deletePartyById(Long id) {
		return deletePartyQuery.deleteParty(id);
	}
	
	public Mono<Integer> deleteParties(QueryParameters parameters) {
		return deletePartiesQuery.deleteParties(parameters);
	}


}

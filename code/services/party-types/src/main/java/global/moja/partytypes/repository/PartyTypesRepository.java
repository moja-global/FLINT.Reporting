/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.partytypes.repository;

import global.moja.partytypes.daos.QueryParameters;
import global.moja.partytypes.models.PartyType;
import global.moja.partytypes.repository.updation.UpdatePartyTypeQuery;
import global.moja.partytypes.repository.updation.UpdatePartyTypesQuery;
import global.moja.partytypes.repository.deletion.DeletePartyTypeQuery;
import global.moja.partytypes.repository.deletion.DeletePartyTypesQuery;
import global.moja.partytypes.repository.selection.SelectPartyTypesQuery;
import global.moja.partytypes.repository.insertion.InsertPartyTypeQuery;
import global.moja.partytypes.repository.insertion.InsertPartyTypesQuery;
import global.moja.partytypes.repository.selection.SelectPartyTypeQuery;
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
public class PartyTypesRepository {

	
	@Autowired
	InsertPartyTypeQuery insertPartyTypeQuery;
	
	@Autowired
	InsertPartyTypesQuery insertPartyTypesQuery;
	
	@Autowired
	SelectPartyTypeQuery selectPartyTypeQuery;
	
	@Autowired
	SelectPartyTypesQuery selectPartyTypesQuery;

	@Autowired
	UpdatePartyTypeQuery updatePartyTypeQuery;
	
	@Autowired
	UpdatePartyTypesQuery updatePartyTypesQuery;
	
	@Autowired
	DeletePartyTypeQuery deletePartyTypeQuery;
	
	@Autowired
    DeletePartyTypesQuery deletePartyTypesQuery;


	public Mono<Long> insertPartyType(PartyType partyType) {
		return insertPartyTypeQuery.insertPartyType(partyType);
	}
	
	public Flux<Long> insertPartyTypes(PartyType[] partyTypes) {
		return insertPartyTypesQuery.insertPartyTypes(partyTypes);
	}

	public Mono<PartyType> selectPartyType(Long id) {
		return selectPartyTypeQuery.selectPartyType(id);
	}
	
	public Flux<PartyType> selectPartyTypes(QueryParameters parameters) {
		return selectPartyTypesQuery.selectPartyTypes(parameters);
	}

	public Mono<Integer> updatePartyType(PartyType partyType) {
		return updatePartyTypeQuery.updatePartyType(partyType);
	}
	
	public Flux<Integer> updatePartyTypes(PartyType[] partyTypes) {
		return updatePartyTypesQuery.updatePartyTypes(partyTypes);
	}	
	
	public Mono<Integer> deletePartyTypeById(Long id) {
		return deletePartyTypeQuery.deletePartyType(id);
	}
	
	public Mono<Integer> deletePartyTypes(QueryParameters parameters) {
		return deletePartyTypesQuery.deletePartyTypes(parameters);
	}


}

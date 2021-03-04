/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.locations.repository;

import global.moja.locations.daos.QueryParameters;
import global.moja.locations.models.Location;
import global.moja.locations.repository.selection.SelectLocationQuery;
import global.moja.locations.repository.selection.SelectLocationsQuery;
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
public class LocationsRepository {

	@Autowired
	SelectLocationQuery selectLocationQuery;
	
	@Autowired
	SelectLocationsQuery selectLocationsQuery;


	public Mono<Location> selectLocation(Long databaseId, Long id) {
		return selectLocationQuery.selectLocation(databaseId,id);
	}
	
	public Flux<Location> selectLocations(Long databaseId, QueryParameters parameters) {
		return selectLocationsQuery.selectLocations(databaseId,parameters);
	}



}

/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.quantityobservations.repository;

import global.moja.quantityobservations.repository.updation.UpdateQuantityObservationsQuery;
import global.moja.quantityobservations.daos.QueryParameters;
import global.moja.quantityobservations.models.QuantityObservation;
import global.moja.quantityobservations.repository.updation.UpdateQuantityObservationQuery;
import global.moja.quantityobservations.repository.deletion.DeleteQuantityObservationQuery;
import global.moja.quantityobservations.repository.deletion.DeleteQuantityObservationsQuery;
import global.moja.quantityobservations.repository.selection.SelectQuantityObservationsQuery;
import global.moja.quantityobservations.repository.insertion.InsertQuantityObservationQuery;
import global.moja.quantityobservations.repository.insertion.InsertQuantityObservationsQuery;
import global.moja.quantityobservations.repository.selection.SelectQuantityObservationQuery;
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
public class QuantityObservationsRepository {

	
	@Autowired
	InsertQuantityObservationQuery insertQuantityObservationQuery;
	
	@Autowired
	InsertQuantityObservationsQuery insertQuantityObservationsQuery;
	
	@Autowired
	SelectQuantityObservationQuery selectQuantityObservationQuery;
	
	@Autowired
	SelectQuantityObservationsQuery selectQuantityObservationsQuery;

	@Autowired
	UpdateQuantityObservationQuery updateQuantityObservationQuery;
	
	@Autowired
    UpdateQuantityObservationsQuery updateQuantityObservationsQuery;
	
	@Autowired
	DeleteQuantityObservationQuery deleteQuantityObservationQuery;
	
	@Autowired
    DeleteQuantityObservationsQuery deleteQuantityObservationsQuery;


	public Mono<Long> insertQuantityObservation(QuantityObservation quantityObservation) {
		return insertQuantityObservationQuery.insertQuantityObservation(quantityObservation);
	}
	
	public Flux<Long> insertQuantityObservations(QuantityObservation[] quantityObservations) {
		return insertQuantityObservationsQuery.insertQuantityObservations(quantityObservations);
	}

	public Mono<QuantityObservation> selectQuantityObservation(Long id) {
		return selectQuantityObservationQuery.selectQuantityObservation(id);
	}
	
	public Flux<QuantityObservation> selectQuantityObservations(QueryParameters parameters) {
		return selectQuantityObservationsQuery.selectQuantityObservations(parameters);
	}

	public Mono<Integer> updateQuantityObservation(QuantityObservation quantityObservation) {
		return updateQuantityObservationQuery.updateQuantityObservation(quantityObservation);
	}
	
	public Flux<Integer> updateQuantityObservations(QuantityObservation[] quantityObservations) {
		return updateQuantityObservationsQuery.updateQuantityObservations(quantityObservations);
	}	
	
	public Mono<Integer> deleteQuantityObservationById(Long id) {
		return deleteQuantityObservationQuery.deleteQuantityObservation(id);
	}
	
	public Mono<Integer> deleteQuantityObservations(QueryParameters parameters) {
		return deleteQuantityObservationsQuery.deleteQuantityObservations(parameters);
	}


}

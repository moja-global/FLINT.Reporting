/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.accountabilitytypes.handlers;

import global.moja.accountabilitytypes.handlers.get.RetrieveAccountabilityTypeHandler;
import global.moja.accountabilitytypes.handlers.post.CreateAccountabilityTypesHandler;
import global.moja.accountabilitytypes.handlers.put.UpdateAccountabilityTypesHandler;
import global.moja.accountabilitytypes.handlers.delete.DeleteAccountabilityTypeHandler;
import global.moja.accountabilitytypes.handlers.delete.DeleteAccountabilityTypesHandler;
import global.moja.accountabilitytypes.handlers.get.RetrieveAccountabilityTypesHandler;
import global.moja.accountabilitytypes.handlers.put.UpdateAccountabilityTypeHandler;
import global.moja.accountabilitytypes.handlers.post.CreateAccountabilityTypeHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class AccountabilityTypesHandler {

	// POST HANDLERS
	@Autowired
	CreateAccountabilityTypeHandler createAccountabilityTypeHandler;

	@Autowired
    CreateAccountabilityTypesHandler createAccountabilityTypesHandler;

	
	// GET HANDLERS
	@Autowired
    RetrieveAccountabilityTypeHandler retrieveAccountabilityTypeByIdHandler;
	
	@Autowired
	RetrieveAccountabilityTypesHandler retrieveAccountabilityTypesHandler;

	
	// PUT HANDLERS
	@Autowired
	UpdateAccountabilityTypeHandler updateAccountabilityTypeHandler;
	
	@Autowired
    UpdateAccountabilityTypesHandler updateAccountabilityTypesHandler;

	
	// DELETE HANDLERS
	@Autowired
	DeleteAccountabilityTypeHandler deleteAccountabilityTypeByIdHandler;
	
	@Autowired
	DeleteAccountabilityTypesHandler deleteAccountabilityTypesHandler;

	
	// <editor-fold desc="POST">
	public Mono<ServerResponse> createAccountabilityType(ServerRequest request) {
		return this.createAccountabilityTypeHandler.createAccountabilityType(request);
	}
	
	public Mono<ServerResponse> createAccountabilityTypes(ServerRequest request) {
		return createAccountabilityTypesHandler.createAccountabilityTypes(request);
	}

	// </editor-fold>

	// <editor-fold desc="GET">

	public Mono<ServerResponse> retrieveAccountabilityType(ServerRequest request) {
		return this.retrieveAccountabilityTypeByIdHandler.retrieveAccountabilityType(request);
	}
	
	public Mono<ServerResponse> retrieveAccountabilityTypes(ServerRequest request) {
		return this.retrieveAccountabilityTypesHandler.retrieveAccountabilityTypes(request);
	}
	

	// </editor-fold>	

	// <editor-fold desc="PUT">
	public Mono<ServerResponse> updateAccountabilityType(ServerRequest request) {
		return this.updateAccountabilityTypeHandler.updateAccountabilityType(request);
	}
	
	public Mono<ServerResponse> updateAccountabilityTypes(ServerRequest request) {
		return this.updateAccountabilityTypesHandler.updateAccountabilityTypes(request);
	}	

	// </editor-fold>	
	
	// <editor-fold desc="DELETE">
	public Mono<ServerResponse> deleteAccountabilityType(ServerRequest request) {
		return this.deleteAccountabilityTypeByIdHandler.deleteAccountabilityType(request);
	}
	
	public Mono<ServerResponse> deleteAccountabilityTypes(ServerRequest request) {
		return this.deleteAccountabilityTypesHandler.deleteAccountabilityTypes(request);
	}	

	// </editor-fold>	

}

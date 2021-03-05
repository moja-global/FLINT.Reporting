/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.accountabilities.handlers;

import global.moja.accountabilities.handlers.delete.DeleteAccountabilitiesHandler;
import global.moja.accountabilities.handlers.delete.DeleteAccountabilityHandler;
import global.moja.accountabilities.handlers.get.RetrieveAccountabilitiesHandler;
import global.moja.accountabilities.handlers.get.RetrieveAccountabilityHandler;
import global.moja.accountabilities.handlers.post.CreateAccountabilitiesHandler;
import global.moja.accountabilities.handlers.post.CreateAccountabilityHandler;
import global.moja.accountabilities.handlers.put.UpdateAccountabilitiesHandler;
import global.moja.accountabilities.handlers.put.UpdateAccountabilityHandler;
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
public class AccountabilitiesHandler {

	// POST HANDLERS
	@Autowired
    CreateAccountabilityHandler createAccountabilityHandler;

	@Autowired
    CreateAccountabilitiesHandler createAccountabilitiesHandler;

	
	// GET HANDLERS
	@Autowired
    RetrieveAccountabilityHandler retrieveAccountabilityByIdHandler;
	
	@Autowired
    RetrieveAccountabilitiesHandler retrieveAccountabilitiesHandler;

	
	// PUT HANDLERS
	@Autowired
    UpdateAccountabilityHandler updateAccountabilityHandler;
	
	@Autowired
    UpdateAccountabilitiesHandler updateAccountabilitiesHandler;

	
	// DELETE HANDLERS
	@Autowired
    DeleteAccountabilityHandler deleteAccountabilityByIdHandler;
	
	@Autowired
    DeleteAccountabilitiesHandler deleteAccountabilitiesHandler;

	
	// <editor-fold desc="POST">
	public Mono<ServerResponse> createAccountability(ServerRequest request) {
		return this.createAccountabilityHandler.createAccountability(request);
	}
	
	public Mono<ServerResponse> createAccountabilities(ServerRequest request) {
		return createAccountabilitiesHandler.createAccountabilities(request);
	}

	// </editor-fold>

	// <editor-fold desc="GET">

	public Mono<ServerResponse> retrieveAccountability(ServerRequest request) {
		return this.retrieveAccountabilityByIdHandler.retrieveAccountability(request);
	}
	
	public Mono<ServerResponse> retrieveAccountabilities(ServerRequest request) {
		return this.retrieveAccountabilitiesHandler.retrieveAccountabilities(request);
	}
	

	// </editor-fold>	

	// <editor-fold desc="PUT">
	public Mono<ServerResponse> updateAccountability(ServerRequest request) {
		return this.updateAccountabilityHandler.updateAccountability(request);
	}
	
	public Mono<ServerResponse> updateAccountabilities(ServerRequest request) {
		return this.updateAccountabilitiesHandler.updateAccountabilities(request);
	}	

	// </editor-fold>	
	
	// <editor-fold desc="DELETE">
	public Mono<ServerResponse> deleteAccountability(ServerRequest request) {
		return this.deleteAccountabilityByIdHandler.deleteAccountability(request);
	}
	
	public Mono<ServerResponse> deleteAccountabilities(ServerRequest request) {
		return this.deleteAccountabilitiesHandler.deleteAccountabilities(request);
	}	

	// </editor-fold>	

}

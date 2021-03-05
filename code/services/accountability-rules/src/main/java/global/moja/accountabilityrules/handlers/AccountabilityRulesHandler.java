/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.accountabilityrules.handlers;

import global.moja.accountabilityrules.handlers.put.UpdateAccountabilityRuleHandler;
import global.moja.accountabilityrules.handlers.delete.DeleteAccountabilityRuleHandler;
import global.moja.accountabilityrules.handlers.delete.DeleteAccountabilityRulesHandler;
import global.moja.accountabilityrules.handlers.get.RetrieveAccountabilityRulesHandler;
import global.moja.accountabilityrules.handlers.put.UpdateAccountabilityRulesHandler;
import global.moja.accountabilityrules.handlers.post.CreateAccountabilityRulesHandler;
import global.moja.accountabilityrules.handlers.get.RetrieveAccountabilityRuleHandler;
import global.moja.accountabilityrules.handlers.post.CreateAccountabilityRuleHandler;
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
public class AccountabilityRulesHandler {

	// POST HANDLERS
	@Autowired
	CreateAccountabilityRuleHandler createAccountabilityRuleHandler;

	@Autowired
	CreateAccountabilityRulesHandler createAccountabilityRulesHandler;

	
	// GET HANDLERS
	@Autowired
	RetrieveAccountabilityRuleHandler retrieveAccountabilityRuleByIdHandler;
	
	@Autowired
	RetrieveAccountabilityRulesHandler retrieveAccountabilityRulesHandler;

	
	// PUT HANDLERS
	@Autowired
    UpdateAccountabilityRuleHandler updateAccountabilityRuleHandler;
	
	@Autowired
	UpdateAccountabilityRulesHandler updateAccountabilityRulesHandler;

	
	// DELETE HANDLERS
	@Autowired
	DeleteAccountabilityRuleHandler deleteAccountabilityRuleByIdHandler;
	
	@Autowired
	DeleteAccountabilityRulesHandler deleteAccountabilityRulesHandler;

	
	// <editor-fold desc="POST">
	public Mono<ServerResponse> createAccountabilityRule(ServerRequest request) {
		return this.createAccountabilityRuleHandler.createAccountabilityRule(request);
	}
	
	public Mono<ServerResponse> createAccountabilityRules(ServerRequest request) {
		return createAccountabilityRulesHandler.createAccountabilityRules(request);
	}

	// </editor-fold>

	// <editor-fold desc="GET">

	public Mono<ServerResponse> retrieveAccountabilityRule(ServerRequest request) {
		return this.retrieveAccountabilityRuleByIdHandler.retrieveAccountabilityRule(request);
	}
	
	public Mono<ServerResponse> retrieveAccountabilityRules(ServerRequest request) {
		return this.retrieveAccountabilityRulesHandler.retrieveAccountabilityRules(request);
	}
	

	// </editor-fold>	

	// <editor-fold desc="PUT">
	public Mono<ServerResponse> updateAccountabilityRule(ServerRequest request) {
		return this.updateAccountabilityRuleHandler.updateAccountabilityRule(request);
	}
	
	public Mono<ServerResponse> updateAccountabilityRules(ServerRequest request) {
		return this.updateAccountabilityRulesHandler.updateAccountabilityRules(request);
	}	

	// </editor-fold>	
	
	// <editor-fold desc="DELETE">
	public Mono<ServerResponse> deleteAccountabilityRule(ServerRequest request) {
		return this.deleteAccountabilityRuleByIdHandler.deleteAccountabilityRule(request);
	}
	
	public Mono<ServerResponse> deleteAccountabilityRules(ServerRequest request) {
		return this.deleteAccountabilityRulesHandler.deleteAccountabilityRules(request);
	}	

	// </editor-fold>	

}

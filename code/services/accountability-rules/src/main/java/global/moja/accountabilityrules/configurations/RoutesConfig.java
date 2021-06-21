/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.accountabilityrules.configurations;

import global.moja.accountabilityrules.handlers.AccountabilityRulesHandler;
import global.moja.accountabilityrules.models.AccountabilityRule;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.arrayschema.Builder.arraySchemaBuilder;
import static org.springdoc.core.fn.builders.content.Builder.contentBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;
import static org.springdoc.core.fn.builders.schema.Builder.schemaBuilder;
import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;

/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Configuration
public class RoutesConfig {


    @Bean
    RouterFunction<ServerResponse> routeRequests(AccountabilityRulesHandler handler) {

        return
                route()
                        .POST("/api/v1/accountability_rules",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::createAccountabilityRule,
                                ops -> ops
                                        .tag("Create")
                                        .operationId("createAccountabilityRule")
                                        .beanClass(AccountabilityRulesHandler.class)
                                        .beanMethod("createAccountabilityRule")
                                        .description("Inserts a single Accountability Rule Record into the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .schema(schemaBuilder()
                                                                        .implementation(AccountabilityRule.class))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("201").description("The Accountability Rule Record was successfully created")
                                                        .implementation(AccountabilityRule.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while creating the Accountability Rule Record")
                                                        .implementation(String.class)))
                        .build()

                .and(route()
                        .POST("/api/v1/accountability_rules/all",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::createAccountabilityRules,
                                ops -> ops
                                        .tag("Create")
                                        .operationId("createAccountabilityRules")
                                        .beanClass(AccountabilityRulesHandler.class)
                                        .beanMethod("createAccountabilityRules")
                                        .description("Inserts several Accountability Rules Records into the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .array(arraySchemaBuilder()
                                                                        .schema(schemaBuilder()
                                                                                .implementation(AccountabilityRule.class)))))

                                        .response(
                                                responseBuilder()
                                                        .responseCode("201").description("The Accountability Rules Records were successfully created")
                                                        .implementationArray(AccountabilityRule.class))

                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while creating the Accountability Rules Records")
                                                        .implementation(String.class)))
                        .build()

                .and(route()
                        .GET("/api/v1/accountability_rules/ids/{id}",
                                accept(APPLICATION_JSON),
                                handler::retrieveAccountabilityRule,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrieveAccountabilityRule")
                                        .beanClass(AccountabilityRulesHandler.class)
                                        .beanMethod("retrieveAccountabilityRule")
                                        .description("Retrieves a single Accountability Rule Record from the database given its unique identifier")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("id").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the Accountability Rule Record")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Accountability Rule Record was successfully retrieved")
                                                        .implementation(AccountabilityRule.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Accountability Rule Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .GET("/api/v1/accountability_rules/all",
                                accept(APPLICATION_JSON),
                                handler::retrieveAccountabilityRules,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrieveAccountabilityRules")
                                        .beanClass(AccountabilityRulesHandler.class)
                                        .beanMethod("retrieveAccountabilityRules")
                                        .description("Retrieves all or some of the Accountability Rules Records from the database depending on whether or not query parameters were included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("ids").in(ParameterIn.QUERY)
                                                        .description("The list of unique identifiers of the Accountability Rules Records to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("accountabilityTypeId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Accountability Type to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("parentPartyTypeId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Parent Party Type to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("subsidiaryPartyTypeId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Subsidiary Party Type to filter the returned values by")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Accountability Rules Records were successfully retrieved")
                                                        .implementationArray(AccountabilityRule.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Accountability Rules Records")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .PUT("/api/v1/accountability_rules",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::updateAccountabilityRule,
                                ops -> ops
                                        .tag("Update")
                                        .operationId("updateAccountabilityRule")
                                        .beanClass(AccountabilityRulesHandler.class)
                                        .beanMethod("updateAccountabilityRule")
                                        .description("Updates a single Accountability Rule Record in the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .schema(schemaBuilder()
                                                                        .implementation(AccountabilityRule.class))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Accountability Rule Record was successfully updated")
                                                        .implementation(AccountabilityRule.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while updating the Accountability Rule Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .PUT("/api/v1/accountability_rules/all",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::updateAccountabilityRules,
                                ops -> ops
                                        .tag("Update")
                                        .operationId("updateAccountabilityRule")
                                        .beanClass(AccountabilityRulesHandler.class)
                                        .beanMethod("updateAccountabilityRules")
                                        .description("Updates several Accountability Rules Records in the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .array(arraySchemaBuilder()
                                                                        .schema(schemaBuilder()
                                                                                .implementation(AccountabilityRule.class)))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Accountability Rules Records were successfully updated")
                                                        .implementation(AccountabilityRule.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while updating the Accountability Rules Records")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .DELETE("/api/v1/accountability_rules/ids/{id}",
                                accept(APPLICATION_JSON),
                                handler::deleteAccountabilityRule,
                                ops -> ops
                                        .tag("Delete")
                                        .operationId("deleteAccountabilityRule")
                                        .beanClass(AccountabilityRulesHandler.class)
                                        .beanMethod("deleteAccountabilityRule")
                                        .description("Deletes a single Accountability Rule Record from the database given its unique identifier")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("id").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the Accountability Rule Record")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Accountability Rule Record was successfully deleted")
                                                        .implementation(Integer.class)
                                                        .description("The number of Accountability Rules Records successfully deleted"))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while deleting the Accountability Rule Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .DELETE("/api/v1/accountability_rules/all",
                                accept(APPLICATION_JSON),
                                handler::deleteAccountabilityRules,
                                ops -> ops
                                        .tag("Delete")
                                        .operationId("deleteAccountabilityRules")
                                        .beanClass(AccountabilityRulesHandler.class)
                                        .beanMethod("deleteAccountabilityRules")
                                        .description("Deleted all or some of the Accountability Rules Records from the database depending on whether or not query parameters were included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("ids").in(ParameterIn.QUERY)
                                                        .description("The list of unique identifiers of the Accountability Rules Records to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("accountabilityTypeId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Accountability Type to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("parentPartyTypeId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Parent Party Type to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("subsidiaryPartyTypeId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Subsidiary Party Type Id to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Accountability Rules Records were successfully deleted")
                                                        .implementation(Integer.class)
                                                        .description("The number of Accountability Rules Records successfully deleted"))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while deleting the Accountability Rules Records")
                                                        .implementation(String.class)))
                        .build()));
    }

}

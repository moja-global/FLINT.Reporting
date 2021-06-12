/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.accountabilities.configurations;

import global.moja.accountabilities.models.Accountability;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import global.moja.accountabilities.handlers.AccountabilitiesHandler;
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
    RouterFunction<ServerResponse> routeRequests(AccountabilitiesHandler handler) {

        return
                route()
                        .POST("/api/v1/accountabilities",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::createAccountability,
                                ops -> ops
                                        .tag("Create")
                                        .operationId("createAccountability")
                                        .beanClass(AccountabilitiesHandler.class)
                                        .beanMethod("createAccountability")
                                        .description("Inserts a single Accountability Record into the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .schema(schemaBuilder()
                                                                        .implementation(Accountability.class))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("201").description("The Accountability Record was successfully created")
                                                        .implementation(Accountability.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while creating the Accountability Record")
                                                        .implementation(String.class)))
                        .build()

                .and(route()
                        .POST("/api/v1/accountabilities/all",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::createAccountabilities,
                                ops -> ops
                                        .tag("Create")
                                        .operationId("createAccountabilities")
                                        .beanClass(AccountabilitiesHandler.class)
                                        .beanMethod("createAccountabilities")
                                        .description("Inserts several Accountabilities Records into the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .array(arraySchemaBuilder()
                                                                        .schema(schemaBuilder()
                                                                                .implementation(Accountability.class)))))

                                        .response(
                                                responseBuilder()
                                                        .responseCode("201").description("The Accountabilities Records were successfully created")
                                                        .implementationArray(Accountability.class))

                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while creating the Accountabilities Records")
                                                        .implementation(String.class)))
                        .build()

                .and(route()
                        .GET("/api/v1/accountabilities/ids/{id}",
                                accept(APPLICATION_JSON),
                                handler::retrieveAccountability,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrieveAccountability")
                                        .beanClass(AccountabilitiesHandler.class)
                                        .beanMethod("retrieveAccountability")
                                        .description("Retrieves a single Accountability Record from the database given its unique identifier")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("id").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the Accountability Record")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Accountability Record was successfully retrieved")
                                                        .implementation(Accountability.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Accountability Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .GET("/api/v1/accountabilities/all",
                                accept(APPLICATION_JSON),
                                handler::retrieveAccountabilities,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrieveAccountabilities")
                                        .beanClass(AccountabilitiesHandler.class)
                                        .beanMethod("retrieveAccountabilities")
                                        .description("Retrieves all or some of the Accountabilities Records from the database depending on whether or not query parameters were included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("ids").in(ParameterIn.QUERY)
                                                        .description("The list of unique identifiers of the Accountabilities Records to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("accountabilityTypeId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Accountability type to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("parentPartyId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Parent Party Record to filter the returned values by. The API regards a parent id value of 0 as a null parent id value")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("subsidiaryPartyId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Subsidiary Party Record to filter the returned values by")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Accountabilities Records were successfully retrieved")
                                                        .implementationArray(Accountability.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Accountabilities Records")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .PUT("/api/v1/accountabilities",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::updateAccountability,
                                ops -> ops
                                        .tag("Update")
                                        .operationId("updateAccountability")
                                        .beanClass(AccountabilitiesHandler.class)
                                        .beanMethod("updateAccountability")
                                        .description("Updates a single Accountability Record in the database")
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Accountability Record was successfully updated")
                                                        .implementation(Accountability.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while updating the Accountability Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .PUT("/api/v1/accountabilities/all",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::updateAccountabilities,
                                ops -> ops
                                        .tag("Update")
                                        .operationId("updateAccountability")
                                        .beanClass(AccountabilitiesHandler.class)
                                        .beanMethod("updateAccountabilities")
                                        .description("Updates several Accountabilities Records in the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .array(arraySchemaBuilder()
                                                                        .schema(schemaBuilder()
                                                                                .implementation(Accountability.class)))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Accountabilities Records were successfully updated")
                                                        .implementation(Accountability.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while updating the Accountabilities Records")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .DELETE("/api/v1/accountabilities/ids/{id}",
                                accept(APPLICATION_JSON),
                                handler::deleteAccountability,
                                ops -> ops
                                        .tag("Delete")
                                        .operationId("deleteAccountability")
                                        .beanClass(AccountabilitiesHandler.class)
                                        .beanMethod("deleteAccountability")
                                        .description("Deletes a single Accountability Record from the database given its unique identifier")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("id").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the Accountability Record")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Accountability Record was successfully deleted")
                                                        .implementation(Integer.class)
                                                        .description("The number of Accountabilities Records successfully deleted"))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while deleting the Accountability Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .DELETE("/api/v1/accountabilities/all",
                                accept(APPLICATION_JSON),
                                handler::deleteAccountabilities,
                                ops -> ops
                                        .tag("Delete")
                                        .operationId("deleteAccountabilities")
                                        .beanClass(AccountabilitiesHandler.class)
                                        .beanMethod("deleteAccountabilities")
                                        .description("Deleted all or some of the Accountabilities Records from the database depending on whether or not query parameters were included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("ids").in(ParameterIn.QUERY)
                                                        .description("The list of unique identifiers of the Accountabilities Records to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("accountabilityTypeId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Accountability type to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("parentPartyId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Parent Party Record to filter the deleted values by. The API regards a parent id value of 0 as a null parent id value")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("subsidiaryPartyId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Subsidiary Party Record to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Accountabilities Records were successfully deleted")
                                                        .implementation(Integer.class)
                                                        .description("The number of Accountabilities Records successfully deleted"))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while deleting the Accountabilities Records")
                                                        .implementation(String.class)))
                        .build()));                        
    }

}

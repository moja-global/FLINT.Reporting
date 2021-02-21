/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.unfcccvariables.configurations;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import moja.global.unfcccvariables.handlers.UnfcccVariablesHandler;
import moja.global.unfcccvariables.models.UnfcccVariable;
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
    RouterFunction<ServerResponse> routeRequests(UnfcccVariablesHandler handler) {

        return
                route()
                        .POST("/api/v1/unfccc_variables",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::createUnfcccVariable,
                                ops -> ops
                                        .tag("Create")
                                        .operationId("createUnfcccVariable")
                                        .beanClass(UnfcccVariablesHandler.class)
                                        .beanMethod("createUnfcccVariable")
                                        .description("Inserts a single UNFCCC Variable Record into the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .schema(schemaBuilder()
                                                                        .implementation(UnfcccVariable.class))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("201").description("The UNFCCC Variable Record was successfully created")
                                                        .implementation(UnfcccVariable.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while creating the UNFCCC Variable Record")
                                                        .implementation(String.class)))
                        .build()

                .and(route()
                        .POST("/api/v1/unfccc_variables/all",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::createUnfcccVariables,
                                ops -> ops
                                        .tag("Create")
                                        .operationId("createUnfcccVariables")
                                        .beanClass(UnfcccVariablesHandler.class)
                                        .beanMethod("createUnfcccVariables")
                                        .description("Inserts several UNFCCC Variable Records into the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .array(arraySchemaBuilder()
                                                                        .schema(schemaBuilder()
                                                                                .implementation(UnfcccVariable.class)))))

                                        .response(
                                                responseBuilder()
                                                        .responseCode("201").description("The UNFCCC Variable Records were successfully created")
                                                        .implementationArray(UnfcccVariable.class))

                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while creating the UNFCCC Variable Records")
                                                        .implementation(String.class)))
                        .build()

                .and(route()
                        .GET("/api/v1/unfccc_variables/ids/{id}",
                                accept(APPLICATION_JSON),
                                handler::retrieveUnfcccVariable,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrieveUnfcccVariable")
                                        .beanClass(UnfcccVariablesHandler.class)
                                        .beanMethod("retrieveUnfcccVariable")
                                        .description("Retrieves a single UNFCCC Variable Record from the database given its unique identifier")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("id").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the UNFCCC Variable Record")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The UNFCCC Variable Record was successfully retrieved")
                                                        .implementation(UnfcccVariable.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the UNFCCC Variable Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .GET("/api/v1/unfccc_variables/all",
                                accept(APPLICATION_JSON),
                                handler::retrieveUnfcccVariables,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrieveUnfcccVariables")
                                        .beanClass(UnfcccVariablesHandler.class)
                                        .beanMethod("retrieveUnfcccVariables")
                                        .description("Retrieves all or some of the UNFCCC Variable Records from the database depending on whether or not the ids parameter was included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("ids").in(ParameterIn.QUERY)
                                                        .description("The unique identifiers of the UNFCCC Variable Records to restrict the retrieval to")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The UNFCCC Variable Records were successfully retrieved")
                                                        .implementationArray(UnfcccVariable.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the UNFCCC Variable Records")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .PUT("/api/v1/unfccc_variables",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::updateUnfcccVariable,
                                ops -> ops
                                        .tag("Update")
                                        .operationId("updateUnfcccVariable")
                                        .beanClass(UnfcccVariablesHandler.class)
                                        .beanMethod("updateUnfcccVariable")
                                        .description("Updates a single UNFCCC Variable Record in the database")
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The UNFCCC Variable Record was successfully updated")
                                                        .implementation(UnfcccVariable.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while updating the UNFCCC Variable Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .PUT("/api/v1/unfccc_variables/all",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::updateUnfcccVariables,
                                ops -> ops
                                        .tag("Update")
                                        .operationId("updateUnfcccVariable")
                                        .beanClass(UnfcccVariablesHandler.class)
                                        .beanMethod("updateUnfcccVariables")
                                        .description("Updates several UNFCCC Variable Records in the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .array(arraySchemaBuilder()
                                                                        .schema(schemaBuilder()
                                                                                .implementation(UnfcccVariable.class)))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The UNFCCC Variable Records were successfully updated")
                                                        .implementation(UnfcccVariable.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while updating the UNFCCC Variable Records")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .DELETE("/api/v1/unfccc_variables/ids/{id}",
                                accept(APPLICATION_JSON),
                                handler::deleteUnfcccVariable,
                                ops -> ops
                                        .tag("Delete")
                                        .operationId("deleteUnfcccVariable")
                                        .beanClass(UnfcccVariablesHandler.class)
                                        .beanMethod("deleteUnfcccVariable")
                                        .description("Deletes a single UNFCCC Variable Record from the database given its unique identifier")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("id").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the UNFCCC Variable Record")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The UNFCCC Variable Record was successfully deleted")
                                                        .implementation(Integer.class)
                                                        .description("The number of UNFCCC Variable Records successfully deleted"))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while deleting the UNFCCC Variable Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .DELETE("/api/v1/unfccc_variables/all",
                                accept(APPLICATION_JSON),
                                handler::deleteUnfcccVariables,
                                ops -> ops
                                        .tag("Delete")
                                        .operationId("deleteUnfcccVariables")
                                        .beanClass(UnfcccVariablesHandler.class)
                                        .beanMethod("deleteUnfcccVariables")
                                        .description("Deleted all or some of the UNFCCC Variable Records from the database depending on whether or not the ids parameter was included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("ids").in(ParameterIn.QUERY)
                                                        .description("The unique identifiers of the UNFCCC Variable Records to restrict the deletion to")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The UNFCCC Variable Records were successfully deleted")
                                                        .implementation(Integer.class)
                                                        .description("The number of UNFCCC Variable Records successfully deleted"))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while deleting the UNFCCC Variable Records")
                                                        .implementation(String.class)))
                        .build()));                        
    }

}

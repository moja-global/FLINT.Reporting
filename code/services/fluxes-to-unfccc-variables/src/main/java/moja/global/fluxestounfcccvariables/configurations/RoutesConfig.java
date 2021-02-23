/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.fluxestounfcccvariables.configurations;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import moja.global.fluxestounfcccvariables.handlers.FluxesToUnfcccVariablesHandler;
import moja.global.fluxestounfcccvariables.models.FluxToUnfcccVariable;
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
    RouterFunction<ServerResponse> routeRequests(FluxesToUnfcccVariablesHandler handler) {

        return
                route()
                        .POST("/api/v1/fluxes_to_unfccc_variables",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::createFluxToUnfcccVariable,
                                ops -> ops
                                        .tag("Create")
                                        .operationId("createFluxToUnfcccVariable")
                                        .beanClass(FluxesToUnfcccVariablesHandler.class)
                                        .beanMethod("createFluxToUnfcccVariable")
                                        .description("Inserts a single Flux To UNFCCC Variable Assignment Rule Record into the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .schema(schemaBuilder()
                                                                        .implementation(FluxToUnfcccVariable.class))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("201").description("The Flux To UNFCCC Variable Assignment Rule Record was successfully created")
                                                        .implementation(FluxToUnfcccVariable.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while creating the Flux To UNFCCC Variable Assignment Rule Record")
                                                        .implementation(String.class)))
                        .build()

                .and(route()
                        .POST("/api/v1/fluxes_to_unfccc_variables/all",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::createFluxesToUnfcccVariables,
                                ops -> ops
                                        .tag("Create")
                                        .operationId("createFluxesToUnfcccVariables")
                                        .beanClass(FluxesToUnfcccVariablesHandler.class)
                                        .beanMethod("createFluxesToUnfcccVariables")
                                        .description("Inserts several Fluxes To UNFCCC Variable Records into the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .array(arraySchemaBuilder()
                                                                        .schema(schemaBuilder()
                                                                                .implementation(FluxToUnfcccVariable.class)))))

                                        .response(
                                                responseBuilder()
                                                        .responseCode("201").description("The Fluxes To UNFCCC Variable Records were successfully created")
                                                        .implementationArray(FluxToUnfcccVariable.class))

                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while creating the Fluxes To UNFCCC Variable Records")
                                                        .implementation(String.class)))
                        .build()

                .and(route()
                        .GET("/api/v1/fluxes_to_unfccc_variables/ids/{id}",
                                accept(APPLICATION_JSON),
                                handler::retrieveFluxToUnfcccVariable,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrieveFluxToUnfcccVariable")
                                        .beanClass(FluxesToUnfcccVariablesHandler.class)
                                        .beanMethod("retrieveFluxToUnfcccVariable")
                                        .description("Retrieves a single Flux To UNFCCC Variable Assignment Rule Record from the database given its unique identifier")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("id").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the Flux To UNFCCC Variable Assignment Rule Record")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Flux To UNFCCC Variable Assignment Rule Record was successfully retrieved")
                                                        .implementation(FluxToUnfcccVariable.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Flux To UNFCCC Variable Assignment Rule Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .GET("/api/v1/fluxes_to_unfccc_variables/all",
                                accept(APPLICATION_JSON),
                                handler::retrieveFluxesToUnfcccVariables,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrieveFluxesToUnfcccVariables")
                                        .beanClass(FluxesToUnfcccVariablesHandler.class)
                                        .beanMethod("retrieveFluxesToUnfcccVariables")
                                        .description("Retrieves all or some of the Fluxes To UNFCCC Variable Records from the database depending on whether or not query parameters were included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("ids").in(ParameterIn.QUERY)
                                                        .description("The list of unique identifiers of the Fluxes To UNFCCC Variable Records to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("startPoolId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the start pool to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("endPoolId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the end pool to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("unfcccVariableId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the end pool to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("rule").in(ParameterIn.QUERY)
                                                        .description("The rule to filter the returned values by i.e \"add\", \"subtract\" or \"ignore\"")
                                                        .implementation(String.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Fluxes To UNFCCC Variable Records were successfully retrieved")
                                                        .implementationArray(FluxToUnfcccVariable.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Fluxes To UNFCCC Variable Records")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .PUT("/api/v1/fluxes_to_unfccc_variables",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::updateFluxToUnfcccVariable,
                                ops -> ops
                                        .tag("Update")
                                        .operationId("updateFluxToUnfcccVariable")
                                        .beanClass(FluxesToUnfcccVariablesHandler.class)
                                        .beanMethod("updateFluxToUnfcccVariable")
                                        .description("Updates a single Flux To UNFCCC Variable Assignment Rule Record in the database")
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Flux To UNFCCC Variable Assignment Rule Record was successfully updated")
                                                        .implementation(FluxToUnfcccVariable.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while updating the Flux To UNFCCC Variable Assignment Rule Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .PUT("/api/v1/fluxes_to_unfccc_variables/all",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::updateFluxesToUnfcccVariables,
                                ops -> ops
                                        .tag("Update")
                                        .operationId("updateFluxToUnfcccVariable")
                                        .beanClass(FluxesToUnfcccVariablesHandler.class)
                                        .beanMethod("updateFluxesToUnfcccVariables")
                                        .description("Updates several Fluxes To UNFCCC Variable Records in the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .array(arraySchemaBuilder()
                                                                        .schema(schemaBuilder()
                                                                                .implementation(FluxToUnfcccVariable.class)))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Fluxes To UNFCCC Variable Records were successfully updated")
                                                        .implementation(FluxToUnfcccVariable.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while updating the Fluxes To UNFCCC Variable Records")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .DELETE("/api/v1/fluxes_to_unfccc_variables/ids/{id}",
                                accept(APPLICATION_JSON),
                                handler::deleteFluxToUnfcccVariable,
                                ops -> ops
                                        .tag("Delete")
                                        .operationId("deleteFluxToUnfcccVariable")
                                        .beanClass(FluxesToUnfcccVariablesHandler.class)
                                        .beanMethod("deleteFluxToUnfcccVariable")
                                        .description("Deletes a single Flux To UNFCCC Variable Assignment Rule Record from the database given its unique identifier")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("id").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the Flux To UNFCCC Variable Assignment Rule Record")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Flux To UNFCCC Variable Assignment Rule Record was successfully deleted")
                                                        .implementation(Integer.class)
                                                        .description("The number of Fluxes To UNFCCC Variable Records successfully deleted"))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while deleting the Flux To UNFCCC Variable Assignment Rule Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .DELETE("/api/v1/fluxes_to_unfccc_variables/all",
                                accept(APPLICATION_JSON),
                                handler::deleteFluxesToUnfcccVariables,
                                ops -> ops
                                        .tag("Delete")
                                        .operationId("deleteFluxesToUnfcccVariables")
                                        .beanClass(FluxesToUnfcccVariablesHandler.class)
                                        .beanMethod("deleteFluxesToUnfcccVariables")
                                        .description("Deleted all or some of the Fluxes To UNFCCC Variable Records from the database depending on whether or not query parameters were included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("ids").in(ParameterIn.QUERY)
                                                        .description("The list of unique identifiers of the Fluxes To UNFCCC Variable Records to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("startPoolId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the start pool to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("endPoolId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the end pool to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("unfcccVariableId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the end pool to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("rule").in(ParameterIn.QUERY)
                                                        .description("The rule to filter the deleted values by i.e \"add\", \"subtract\" or \"ignore\"")
                                                        .implementation(String.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Fluxes To UNFCCC Variable Records were successfully deleted")
                                                        .implementation(Integer.class)
                                                        .description("The number of Fluxes To UNFCCC Variable Records successfully deleted"))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while deleting the Fluxes To UNFCCC Variable Records")
                                                        .implementation(String.class)))
                        .build()));                        
    }

}

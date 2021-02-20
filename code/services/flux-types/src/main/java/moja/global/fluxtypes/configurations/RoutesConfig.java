/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.fluxtypes.configurations;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import moja.global.fluxtypes.handlers.FluxTypesHandler;
import moja.global.fluxtypes.models.FluxType;
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
    RouterFunction<ServerResponse> routeRequests(FluxTypesHandler handler) {

        return
                route()
                        .POST("/api/v1/flux_types",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::createFluxType,
                                ops -> ops
                                        .tag("Create")
                                        .operationId("createFluxType")
                                        .beanClass(FluxTypesHandler.class)
                                        .beanMethod("createFluxType")
                                        .description("Inserts a single Flux Type Record into the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .schema(schemaBuilder()
                                                                        .implementation(FluxType.class))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("201").description("The Flux Type Record was successfully created")
                                                        .implementation(FluxType.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while creating the Flux Type Record")
                                                        .implementation(String.class)))
                        .build()

                .and(route()
                        .POST("/api/v1/flux_types/all",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::createFluxTypes,
                                ops -> ops
                                        .tag("Create")
                                        .operationId("createFluxTypes")
                                        .beanClass(FluxTypesHandler.class)
                                        .beanMethod("createFluxTypes")
                                        .description("Inserts several Flux Type Records into the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .array(arraySchemaBuilder()
                                                                        .schema(schemaBuilder()
                                                                                .implementation(FluxType.class)))))

                                        .response(
                                                responseBuilder()
                                                        .responseCode("201").description("The Flux Type Records were successfully created")
                                                        .implementationArray(FluxType.class))

                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while creating the Flux Type Records")
                                                        .implementation(String.class)))
                        .build()

                .and(route()
                        .GET("/api/v1/flux_types/ids/{id}",
                                accept(APPLICATION_JSON),
                                handler::retrieveFluxType,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrieveFluxType")
                                        .beanClass(FluxTypesHandler.class)
                                        .beanMethod("retrieveFluxType")
                                        .description("Retrieves a single Flux Type Record from the database given its unique identifier")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("id").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the Flux Type Record")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Flux Type Record was successfully retrieved")
                                                        .implementation(FluxType.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Flux Type Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .GET("/api/v1/flux_types/all",
                                accept(APPLICATION_JSON),
                                handler::retrieveFluxTypes,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrieveFluxTypes")
                                        .beanClass(FluxTypesHandler.class)
                                        .beanMethod("retrieveFluxTypes")
                                        .description("Retrieves all or some of the Flux Type Records from the database depending on whether or not the ids parameter was included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("ids").in(ParameterIn.QUERY)
                                                        .description("The unique identifiers of the Flux Type Records to restrict the retrieval to")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Flux Type Records were successfully retrieved")
                                                        .implementationArray(FluxType.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Flux Type Records")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .PUT("/api/v1/flux_types",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::updateFluxType,
                                ops -> ops
                                        .tag("Update")
                                        .operationId("updateFluxType")
                                        .beanClass(FluxTypesHandler.class)
                                        .beanMethod("updateFluxType")
                                        .description("Updates a single Flux Type Record in the database")
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Flux Type Record was successfully updated")
                                                        .implementation(FluxType.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while updating the Flux Type Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .PUT("/api/v1/flux_types/all",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::updateFluxTypes,
                                ops -> ops
                                        .tag("Update")
                                        .operationId("updateFluxType")
                                        .beanClass(FluxTypesHandler.class)
                                        .beanMethod("updateFluxTypes")
                                        .description("Updates several Flux Type Records in the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .array(arraySchemaBuilder()
                                                                        .schema(schemaBuilder()
                                                                                .implementation(FluxType.class)))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Flux Type Records were successfully updated")
                                                        .implementation(FluxType.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while updating the Flux Type Records")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .DELETE("/api/v1/flux_types/ids/{id}",
                                accept(APPLICATION_JSON),
                                handler::deleteFluxType,
                                ops -> ops
                                        .tag("Delete")
                                        .operationId("deleteFluxType")
                                        .beanClass(FluxTypesHandler.class)
                                        .beanMethod("deleteFluxType")
                                        .description("Deletes a single Flux Type Record from the database given its unique identifier")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("id").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the Flux Type Record")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Flux Type Record was successfully deleted")
                                                        .implementation(Integer.class)
                                                        .description("The number of Flux Type Records successfully deleted"))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while deleting the Flux Type Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .DELETE("/api/v1/flux_types/all",
                                accept(APPLICATION_JSON),
                                handler::deleteFluxTypes,
                                ops -> ops
                                        .tag("Delete")
                                        .operationId("deleteFluxTypes")
                                        .beanClass(FluxTypesHandler.class)
                                        .beanMethod("deleteFluxTypes")
                                        .description("Deleted all or some of the Flux Type Records from the database depending on whether or not the ids parameter was included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("ids").in(ParameterIn.QUERY)
                                                        .description("The unique identifiers of the Flux Type Records to restrict the deletion to")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Flux Type Records were successfully deleted")
                                                        .implementation(Integer.class)
                                                        .description("The number of Flux Type Records successfully deleted"))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while deleting the Flux Type Records")
                                                        .implementation(String.class)))
                        .build()));                        
    }

}

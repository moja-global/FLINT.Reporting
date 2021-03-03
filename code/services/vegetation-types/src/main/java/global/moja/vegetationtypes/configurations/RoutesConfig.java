/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.vegetationtypes.configurations;

import global.moja.vegetationtypes.handlers.VegetationTypesHandler;
import global.moja.vegetationtypes.models.VegetationType;
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
    RouterFunction<ServerResponse> routeRequests(VegetationTypesHandler handler) {

        return
                route()
                        .GET("/api/v1/vegetation_types/databases/{databaseId}/ids/{id}",
                                accept(APPLICATION_JSON),
                                handler::retrieveVegetationType,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrieveVegetationType")
                                        .beanClass(VegetationTypesHandler.class)
                                        .beanMethod("retrieveVegetationType")
                                        .description("Retrieves a single Vegetation Type Record from the database given its unique identifier")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("databaseId").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the database from which the record should be retrieved")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("id").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the Vegetation Type Record")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Vegetation Type Record was successfully retrieved")
                                                        .implementation(VegetationType.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Vegetation Type Record")
                                                        .implementation(String.class)))
                        .build()

                .and(route()
                        .GET("/api/v1/vegetation_types/databases/{databaseId}/all",
                                accept(APPLICATION_JSON),
                                handler::retrieveVegetationTypes,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrieveVegetationTypes")
                                        .beanClass(VegetationTypesHandler.class)
                                        .beanMethod("retrieveVegetationTypes")
                                        .description("Retrieves all or some of the Vegetation Types Records from the database depending on whether or not query parameters were included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("databaseId").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the database from which the records should be retrieved")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("ids").in(ParameterIn.QUERY)
                                                        .description("The list of unique identifiers of the Vegetation Types Records to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("coverTypeId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Cover Type to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("woodyType").in(ParameterIn.QUERY)
                                                        .description("The Woody Type Status (True or False) to filter the returned values by")
                                                        .implementation(Boolean.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("naturalSystem").in(ParameterIn.QUERY)
                                                        .description("The Natural System Status (True or False) to filter the returned values by")
                                                        .implementation(Boolean.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("name").in(ParameterIn.QUERY)
                                                        .description("The Vegetation Type name (or name fragment) to filter the returned values by ")
                                                        .implementation(String.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Vegetation Types Records were successfully retrieved")
                                                        .implementationArray(VegetationType.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Vegetation Types Records")
                                                        .implementation(String.class)))
                        .build());
    }

}

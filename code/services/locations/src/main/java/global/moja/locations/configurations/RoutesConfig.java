/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.locations.configurations;

import global.moja.locations.handlers.LocationsHandler;
import global.moja.locations.models.Location;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Configuration
public class RoutesConfig {


    @Bean
    RouterFunction<ServerResponse> routeRequests(LocationsHandler handler) {

        return
                route()
                        .GET("/api/v1/locations/databases/{databaseId}/ids/{id}",
                                accept(APPLICATION_JSON),
                                handler::retrieveLocation,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrieveLocation")
                                        .beanClass(LocationsHandler.class)
                                        .beanMethod("retrieveLocation")
                                        .description("Retrieves a single Location Record from the database given its unique identifier")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("id").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the Location Record")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Location Record was successfully retrieved")
                                                        .implementation(Location.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Location Record")
                                                        .implementation(String.class)))
                        .build()

                .and(route()
                        .GET("/api/v1/locations/databases/{databaseId}/all",
                                accept(APPLICATION_JSON),
                                handler::retrieveLocations,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrieveLocations")
                                        .beanClass(LocationsHandler.class)
                                        .beanMethod("retrieveLocations")
                                        .description("Retrieves all or some of the Locations Records from the database depending on whether or not query parameters were included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("ids").in(ParameterIn.QUERY)
                                                        .description("The list of unique identifiers of the Locations Records to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("partyId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Party to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("tileId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Tile to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("vegetationHistoryId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Vegetation History to filter the returned values by")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Locations Records were successfully retrieved")
                                                        .implementationArray(Location.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Locations Records")
                                                        .implementation(String.class)))
                        .build());
    }

}

/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.dates.configurations;

import global.moja.dates.handlers.DatesHandler;
import global.moja.dates.models.Date;
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
    RouterFunction<ServerResponse> routeRequests(DatesHandler handler) {

        return
                route()
                        .GET("/api/v1/dates/databases/{databaseId}/ids/{id}",
                                accept(APPLICATION_JSON),
                                handler::retrieveDate,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrieveDate")
                                        .beanClass(DatesHandler.class)
                                        .beanMethod("retrieveDate")
                                        .description("Retrieves a single Date Record from the database given its unique identifier")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("databaseId").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the database from which the record should be retrieved")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("id").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the Date Record")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Date Record was successfully retrieved")
                                                        .implementation(Date.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Date Record")
                                                        .implementation(String.class)))
                        .build()

                .and(route()
                        .GET("/api/v1/dates/databases/{databaseId}/all",
                                accept(APPLICATION_JSON),
                                handler::retrieveDates,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrieveDates")
                                        .beanClass(DatesHandler.class)
                                        .beanMethod("retrieveDates")
                                        .description("Retrieves all or some of the Dates Records from the database depending on whether or not query parameters were included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("databaseId").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the database from which the record should be retrieved")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("ids").in(ParameterIn.QUERY)
                                                        .description("The list of unique identifiers of the Dates Records to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("year").in(ParameterIn.QUERY)
                                                        .description("The Year to filter the returned values by")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Dates Records were successfully retrieved")
                                                        .implementationArray(Date.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Dates Records")
                                                        .implementation(String.class)))
                        .build());
    }

}

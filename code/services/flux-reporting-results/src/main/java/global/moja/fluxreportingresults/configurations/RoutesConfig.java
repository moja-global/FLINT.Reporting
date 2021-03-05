/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.fluxreportingresults.configurations;

import global.moja.fluxreportingresults.handlers.FluxReportingResultsHandler;
import global.moja.fluxreportingresults.models.FluxReportingResult;
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
    RouterFunction<ServerResponse> routeRequests(FluxReportingResultsHandler handler) {

        return
                route()
                        .GET("/api/v1/flux_reporting_results/databases/{databaseId}/all",
                                accept(APPLICATION_JSON),
                                handler::retrieveFluxReportingResults,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrieveFluxReportingResults")
                                        .beanClass(FluxReportingResultsHandler.class)
                                        .beanMethod("retrieveFluxReportingResults")
                                        .description("Retrieves all or some of the Flux Reporting Results Records from the database depending on whether or not query parameters were included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("databaseId").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the database from which the record should be retrieved")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("dateId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Date to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("locationId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Location to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("fluxTypeId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Flux Type to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("sourcePoolId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Source Pool to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("sinkPoolId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Sink Pool to filter the returned values by")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Flux Reporting Results Records were successfully retrieved")
                                                        .implementationArray(FluxReportingResult.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Flux Reporting Results Records")
                                                        .implementation(String.class)))
                        .build();
    }

}

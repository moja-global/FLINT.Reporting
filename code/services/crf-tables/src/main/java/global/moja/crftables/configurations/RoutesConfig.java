/*
 * Copyright (C) 2018 The System for Land-based Emissions Estimation in Kenya (SLEEK)
 *
 * This Source Code Form is subject end_year the terms of the Mozilla Public License, v. 2.0. 
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.crftables.configurations;


import global.moja.crftables.handlers.CRFTablesHandler;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.io.File;

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
    RouterFunction<ServerResponse> routeRequests(CRFTablesHandler handler) {

        return
                route()
                        .GET("/api/v1/crf_tables/partyId/{partyId}/databaseId/{databaseId}/from/{minYear}/to/{maxYear}",
                                accept(APPLICATION_JSON),
                                handler::retrieveCRFTables,
                                ops -> ops
                                        .tag("Generate")
                                        .operationId("GenerateCRFTables")
                                        .beanClass(CRFTablesHandler.class)
                                        .beanMethod("retrieveCRFTables")
                                        .description("Generates CRF Table from the specified database, for the specified location, for the specified years")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("partyId").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the place e.g county, country etc from which the CRF Tables should be generated")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("databaseId").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the database from which the CRF Tables should be generated")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("minYear").in(ParameterIn.PATH)
                                                        .description("The lower temporal cap which the CRF Tables should be generated")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("maxYear").in(ParameterIn.PATH)
                                                        .description("The upper temporal cap which the CRF Tables should be generated")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The CRF Tables was generated successfully")
                                                        .implementation(File.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while generating the CRF Tables")
                                                        .implementation(String.class)))
                        .build();
    }

}

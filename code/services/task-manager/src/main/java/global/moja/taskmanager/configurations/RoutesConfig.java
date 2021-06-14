/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.taskmanager.configurations;

import global.moja.taskmanager.handlers.TasksHandler;
import global.moja.taskmanager.handlers.post.PostDatabaseIntegrationTaskHandler;
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
    RouterFunction<ServerResponse> routeRequests(TasksHandler handler) {

        return
                route()
                        .POST("/api/v1/task_manager/databases/{databaseId}",
                                accept(APPLICATION_JSON),
                                handler::postDatabaseIntegrationTask,
                                ops -> ops
                                        .tag("Integrate")
                                        .operationId("integrateDatabase")
                                        .beanClass(PostDatabaseIntegrationTaskHandler.class)
                                        .beanMethod("postDatabaseIntegrationTask")
                                        .description("Submits a Database for integration")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("databaseId").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the database which should be integrated")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Database Integration Task was successfully submitted")
                                                        .implementation(String.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while submitting the database for integration")
                                                        .implementation(String.class)))
                        .build();
    }

}

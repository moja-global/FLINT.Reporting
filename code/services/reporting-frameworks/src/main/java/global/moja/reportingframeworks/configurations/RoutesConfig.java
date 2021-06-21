/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.reportingframeworks.configurations;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import global.moja.reportingframeworks.handlers.ReportingFrameworksHandler;
import global.moja.reportingframeworks.models.ReportingFramework;
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
    RouterFunction<ServerResponse> routeRequests(ReportingFrameworksHandler handler) {

        return
                route()
                        .POST("/api/v1/reporting_frameworks",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::createReportingFramework,
                                ops -> ops
                                        .tag("Create")
                                        .operationId("createReportingFramework")
                                        .beanClass(ReportingFrameworksHandler.class)
                                        .beanMethod("createReportingFramework")
                                        .description("Inserts a single Reporting Framework Record into the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .schema(schemaBuilder()
                                                                        .implementation(ReportingFramework.class))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("201").description("The Reporting Framework Record was successfully created")
                                                        .implementation(ReportingFramework.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while creating the Reporting Framework Record")
                                                        .implementation(String.class)))
                        .build()

                .and(route()
                        .POST("/api/v1/reporting_frameworks/all",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::createReportingFrameworks,
                                ops -> ops
                                        .tag("Create")
                                        .operationId("createReportingFrameworks")
                                        .beanClass(ReportingFrameworksHandler.class)
                                        .beanMethod("createReportingFrameworks")
                                        .description("Inserts several Reporting Frameworks Records into the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .array(arraySchemaBuilder()
                                                                        .schema(schemaBuilder()
                                                                                .implementation(ReportingFramework.class)))))

                                        .response(
                                                responseBuilder()
                                                        .responseCode("201").description("The Reporting Frameworks Records were successfully created")
                                                        .implementationArray(ReportingFramework.class))

                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while creating the Reporting Frameworks Records")
                                                        .implementation(String.class)))
                        .build()

                .and(route()
                        .GET("/api/v1/reporting_frameworks/ids/{id}",
                                accept(APPLICATION_JSON),
                                handler::retrieveReportingFramework,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrieveReportingFramework")
                                        .beanClass(ReportingFrameworksHandler.class)
                                        .beanMethod("retrieveReportingFramework")
                                        .description("Retrieves a single Reporting Framework Record from the database given its unique identifier")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("id").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the Reporting Framework Record")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Reporting Framework Record was successfully retrieved")
                                                        .implementation(ReportingFramework.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Reporting Framework Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .GET("/api/v1/reporting_frameworks/all",
                                accept(APPLICATION_JSON),
                                handler::retrieveReportingFrameworks,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrieveReportingFrameworks")
                                        .beanClass(ReportingFrameworksHandler.class)
                                        .beanMethod("retrieveReportingFrameworks")
                                        .description("Retrieves all or some of the Reporting Frameworks Records from the database depending on whether or not query parameters were included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("ids").in(ParameterIn.QUERY)
                                                        .description("The list of unique identifiers of the Reporting Frameworks Records to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("name").in(ParameterIn.QUERY)
                                                        .description("The Reporting Framework name (or name fragment) to filter the returned values by ")
                                                        .implementation(String.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Reporting Frameworks Records were successfully retrieved")
                                                        .implementationArray(ReportingFramework.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Reporting Frameworks Records")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .PUT("/api/v1/reporting_frameworks",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::updateReportingFramework,
                                ops -> ops
                                        .tag("Update")
                                        .operationId("updateReportingFramework")
                                        .beanClass(ReportingFrameworksHandler.class)
                                        .beanMethod("updateReportingFramework")
                                        .description("Updates a single Reporting Framework Record in the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .schema(schemaBuilder()
                                                                        .implementation(ReportingFramework.class))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Reporting Framework Record was successfully updated")
                                                        .implementation(ReportingFramework.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while updating the Reporting Framework Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .PUT("/api/v1/reporting_frameworks/all",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::updateReportingFrameworks,
                                ops -> ops
                                        .tag("Update")
                                        .operationId("updateReportingFramework")
                                        .beanClass(ReportingFrameworksHandler.class)
                                        .beanMethod("updateReportingFrameworks")
                                        .description("Updates several Reporting Frameworks Records in the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .array(arraySchemaBuilder()
                                                                        .schema(schemaBuilder()
                                                                                .implementation(ReportingFramework.class)))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Reporting Frameworks Records were successfully updated")
                                                        .implementation(ReportingFramework.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while updating the Reporting Frameworks Records")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .DELETE("/api/v1/reporting_frameworks/ids/{id}",
                                accept(APPLICATION_JSON),
                                handler::deleteReportingFramework,
                                ops -> ops
                                        .tag("Delete")
                                        .operationId("deleteReportingFramework")
                                        .beanClass(ReportingFrameworksHandler.class)
                                        .beanMethod("deleteReportingFramework")
                                        .description("Deletes a single Reporting Framework Record from the database given its unique identifier")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("id").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the Reporting Framework Record")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Reporting Framework Record was successfully deleted")
                                                        .implementation(Integer.class)
                                                        .description("The number of Reporting Frameworks Records successfully deleted"))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while deleting the Reporting Framework Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .DELETE("/api/v1/reporting_frameworks/all",
                                accept(APPLICATION_JSON),
                                handler::deleteReportingFrameworks,
                                ops -> ops
                                        .tag("Delete")
                                        .operationId("deleteReportingFrameworks")
                                        .beanClass(ReportingFrameworksHandler.class)
                                        .beanMethod("deleteReportingFrameworks")
                                        .description("Deleted all or some of the Reporting Frameworks Records from the database depending on whether or not query parameters were included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("ids").in(ParameterIn.QUERY)
                                                        .description("The list of unique identifiers of the Reporting Frameworks Records to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("name").in(ParameterIn.QUERY)
                                                        .description("The Reporting Framework name (or name fragment) filter the deleted values by ")
                                                        .implementation(String.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Reporting Frameworks Records were successfully deleted")
                                                        .implementation(Integer.class)
                                                        .description("The number of Reporting Frameworks Records successfully deleted"))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while deleting the Reporting Frameworks Records")
                                                        .implementation(String.class)))
                        .build()));                        
    }

}

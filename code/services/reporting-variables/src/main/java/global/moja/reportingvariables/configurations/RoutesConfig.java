/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.reportingvariables.configurations;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import global.moja.reportingvariables.handlers.ReportingVariablesHandler;
import global.moja.reportingvariables.models.ReportingVariable;
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
    RouterFunction<ServerResponse> routeRequests(ReportingVariablesHandler handler) {

        return
                route()
                        .POST("/api/v1/reporting_variables",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::createReportingVariable,
                                ops -> ops
                                        .tag("Create")
                                        .operationId("createReportingVariable")
                                        .beanClass(ReportingVariablesHandler.class)
                                        .beanMethod("createReportingVariable")
                                        .description("Inserts a single Reporting Variable Record into the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .schema(schemaBuilder()
                                                                        .implementation(ReportingVariable.class))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("201").description("The Reporting Variable Record was successfully created")
                                                        .implementation(ReportingVariable.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while creating the Reporting Variable Record")
                                                        .implementation(String.class)))
                        .build()

                .and(route()
                        .POST("/api/v1/reporting_variables/all",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::createReportingVariables,
                                ops -> ops
                                        .tag("Create")
                                        .operationId("createReportingVariables")
                                        .beanClass(ReportingVariablesHandler.class)
                                        .beanMethod("createReportingVariables")
                                        .description("Inserts several Reporting Variables Records into the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .array(arraySchemaBuilder()
                                                                        .schema(schemaBuilder()
                                                                                .implementation(ReportingVariable.class)))))

                                        .response(
                                                responseBuilder()
                                                        .responseCode("201").description("The Reporting Variables Records were successfully created")
                                                        .implementationArray(ReportingVariable.class))

                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while creating the Reporting Variables Records")
                                                        .implementation(String.class)))
                        .build()

                .and(route()
                        .GET("/api/v1/reporting_variables/ids/{id}",
                                accept(APPLICATION_JSON),
                                handler::retrieveReportingVariable,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrieveReportingVariable")
                                        .beanClass(ReportingVariablesHandler.class)
                                        .beanMethod("retrieveReportingVariable")
                                        .description("Retrieves a single Reporting Variable Record from the database given its unique identifier")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("id").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the Reporting Variable Record")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Reporting Variable Record was successfully retrieved")
                                                        .implementation(ReportingVariable.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Reporting Variable Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .GET("/api/v1/reporting_variables/all",
                                accept(APPLICATION_JSON),
                                handler::retrieveReportingVariables,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrieveReportingVariables")
                                        .beanClass(ReportingVariablesHandler.class)
                                        .beanMethod("retrieveReportingVariables")
                                        .description("Retrieves all or some of the Reporting Variables Records from the database depending on whether or not query parameters were included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("ids").in(ParameterIn.QUERY)
                                                        .description("The list of unique identifiers of the Reporting Variables Records to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("reportingFrameworkId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Reporting Framework to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("name").in(ParameterIn.QUERY)
                                                        .description("The Reporting Variable name (or name fragment) to filter the returned values by ")
                                                        .implementation(String.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Reporting Variables Records were successfully retrieved")
                                                        .implementationArray(ReportingVariable.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Reporting Variables Records")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .PUT("/api/v1/reporting_variables",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::updateReportingVariable,
                                ops -> ops
                                        .tag("Update")
                                        .operationId("updateReportingVariable")
                                        .beanClass(ReportingVariablesHandler.class)
                                        .beanMethod("updateReportingVariable")
                                        .description("Updates a single Reporting Variable Record in the database")
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Reporting Variable Record was successfully updated")
                                                        .implementation(ReportingVariable.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while updating the Reporting Variable Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .PUT("/api/v1/reporting_variables/all",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::updateReportingVariables,
                                ops -> ops
                                        .tag("Update")
                                        .operationId("updateReportingVariable")
                                        .beanClass(ReportingVariablesHandler.class)
                                        .beanMethod("updateReportingVariables")
                                        .description("Updates several Reporting Variables Records in the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .array(arraySchemaBuilder()
                                                                        .schema(schemaBuilder()
                                                                                .implementation(ReportingVariable.class)))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Reporting Variables Records were successfully updated")
                                                        .implementation(ReportingVariable.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while updating the Reporting Variables Records")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .DELETE("/api/v1/reporting_variables/ids/{id}",
                                accept(APPLICATION_JSON),
                                handler::deleteReportingVariable,
                                ops -> ops
                                        .tag("Delete")
                                        .operationId("deleteReportingVariable")
                                        .beanClass(ReportingVariablesHandler.class)
                                        .beanMethod("deleteReportingVariable")
                                        .description("Deletes a single Reporting Variable Record from the database given its unique identifier")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("id").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the Reporting Variable Record")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Reporting Variable Record was successfully deleted")
                                                        .implementation(Integer.class)
                                                        .description("The number of Reporting Variables Records successfully deleted"))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while deleting the Reporting Variable Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .DELETE("/api/v1/reporting_variables/all",
                                accept(APPLICATION_JSON),
                                handler::deleteReportingVariables,
                                ops -> ops
                                        .tag("Delete")
                                        .operationId("deleteReportingVariables")
                                        .beanClass(ReportingVariablesHandler.class)
                                        .beanMethod("deleteReportingVariables")
                                        .description("Deleted all or some of the Reporting Variables Records from the database depending on whether or not query parameters were included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("ids").in(ParameterIn.QUERY)
                                                        .description("The list of unique identifiers of the Reporting Variables Records to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("reportingFrameworkId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Reporting Framework to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("name").in(ParameterIn.QUERY)
                                                        .description("The Reporting Variable name (or name fragment) filter the deleted values by ")
                                                        .implementation(String.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Reporting Variables Records were successfully deleted")
                                                        .implementation(Integer.class)
                                                        .description("The number of Reporting Variables Records successfully deleted"))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while deleting the Reporting Variables Records")
                                                        .implementation(String.class)))
                        .build()));                        
    }

}

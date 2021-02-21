/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.reportingtables.configurations;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import moja.global.reportingtables.handlers.ReportingTablesHandler;
import moja.global.reportingtables.models.ReportingTable;
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
    RouterFunction<ServerResponse> routeRequests(ReportingTablesHandler handler) {

        return
                route()
                        .POST("/api/v1/reporting_tables",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::createReportingTable,
                                ops -> ops
                                        .tag("Create")
                                        .operationId("createReportingTable")
                                        .beanClass(ReportingTablesHandler.class)
                                        .beanMethod("createReportingTable")
                                        .description("Inserts a single Reporting Table Record into the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .schema(schemaBuilder()
                                                                        .implementation(ReportingTable.class))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("201").description("The Reporting Table Record was successfully created")
                                                        .implementation(ReportingTable.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while creating the Reporting Table Record")
                                                        .implementation(String.class)))
                        .build()

                .and(route()
                        .POST("/api/v1/reporting_tables/all",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::createReportingTables,
                                ops -> ops
                                        .tag("Create")
                                        .operationId("createReportingTables")
                                        .beanClass(ReportingTablesHandler.class)
                                        .beanMethod("createReportingTables")
                                        .description("Inserts several Reporting Table Records into the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .array(arraySchemaBuilder()
                                                                        .schema(schemaBuilder()
                                                                                .implementation(ReportingTable.class)))))

                                        .response(
                                                responseBuilder()
                                                        .responseCode("201").description("The Reporting Table Records were successfully created")
                                                        .implementationArray(ReportingTable.class))

                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while creating the Reporting Table Records")
                                                        .implementation(String.class)))
                        .build()

                .and(route()
                        .GET("/api/v1/reporting_tables/ids/{id}",
                                accept(APPLICATION_JSON),
                                handler::retrieveReportingTable,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrieveReportingTable")
                                        .beanClass(ReportingTablesHandler.class)
                                        .beanMethod("retrieveReportingTable")
                                        .description("Retrieves a single Reporting Table Record from the database given its unique identifier")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("id").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the Reporting Table Record")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Reporting Table Record was successfully retrieved")
                                                        .implementation(ReportingTable.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Reporting Table Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .GET("/api/v1/reporting_tables/all",
                                accept(APPLICATION_JSON),
                                handler::retrieveReportingTables,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrieveReportingTables")
                                        .beanClass(ReportingTablesHandler.class)
                                        .beanMethod("retrieveReportingTables")
                                        .description("Retrieves all or some of the Reporting Table Records from the database depending on whether or not the ids parameter was included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("ids").in(ParameterIn.QUERY)
                                                        .description("The unique identifiers of the Reporting Table Records to restrict the retrieval to")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Reporting Table Records were successfully retrieved")
                                                        .implementationArray(ReportingTable.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Reporting Table Records")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .PUT("/api/v1/reporting_tables",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::updateReportingTable,
                                ops -> ops
                                        .tag("Update")
                                        .operationId("updateReportingTable")
                                        .beanClass(ReportingTablesHandler.class)
                                        .beanMethod("updateReportingTable")
                                        .description("Updates a single Reporting Table Record in the database")
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Reporting Table Record was successfully updated")
                                                        .implementation(ReportingTable.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while updating the Reporting Table Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .PUT("/api/v1/reporting_tables/all",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::updateReportingTables,
                                ops -> ops
                                        .tag("Update")
                                        .operationId("updateReportingTable")
                                        .beanClass(ReportingTablesHandler.class)
                                        .beanMethod("updateReportingTables")
                                        .description("Updates several Reporting Table Records in the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .array(arraySchemaBuilder()
                                                                        .schema(schemaBuilder()
                                                                                .implementation(ReportingTable.class)))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Reporting Table Records were successfully updated")
                                                        .implementation(ReportingTable.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while updating the Reporting Table Records")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .DELETE("/api/v1/reporting_tables/ids/{id}",
                                accept(APPLICATION_JSON),
                                handler::deleteReportingTable,
                                ops -> ops
                                        .tag("Delete")
                                        .operationId("deleteReportingTable")
                                        .beanClass(ReportingTablesHandler.class)
                                        .beanMethod("deleteReportingTable")
                                        .description("Deletes a single Reporting Table Record from the database given its unique identifier")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("id").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the Reporting Table Record")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Reporting Table Record was successfully deleted")
                                                        .implementation(Integer.class)
                                                        .description("The number of Reporting Table Records successfully deleted"))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while deleting the Reporting Table Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .DELETE("/api/v1/reporting_tables/all",
                                accept(APPLICATION_JSON),
                                handler::deleteReportingTables,
                                ops -> ops
                                        .tag("Delete")
                                        .operationId("deleteReportingTables")
                                        .beanClass(ReportingTablesHandler.class)
                                        .beanMethod("deleteReportingTables")
                                        .description("Deleted all or some of the Reporting Table Records from the database depending on whether or not the ids parameter was included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("ids").in(ParameterIn.QUERY)
                                                        .description("The unique identifiers of the Reporting Table Records to restrict the deletion to")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Reporting Table Records were successfully deleted")
                                                        .implementation(Integer.class)
                                                        .description("The number of Reporting Table Records successfully deleted"))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while deleting the Reporting Table Records")
                                                        .implementation(String.class)))
                        .build()));                        
    }

}

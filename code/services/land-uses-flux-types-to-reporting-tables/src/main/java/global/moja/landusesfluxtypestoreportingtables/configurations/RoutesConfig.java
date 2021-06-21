/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.landusesfluxtypestoreportingtables.configurations;

import global.moja.landusesfluxtypestoreportingtables.models.LandUseFluxTypeToReportingTable;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import global.moja.landusesfluxtypestoreportingtables.handlers.LandUsesFluxTypesToReportingTablesHandler;
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
    RouterFunction<ServerResponse> routeRequests(LandUsesFluxTypesToReportingTablesHandler handler) {

        return
                route()
                        .POST("/api/v1/land_uses_flux_types_to_reporting_tables",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::createLandUseFluxTypeToReportingTable,
                                ops -> ops
                                        .tag("Create")
                                        .operationId("createLandUseFluxTypeToReportingTable")
                                        .beanClass(LandUsesFluxTypesToReportingTablesHandler.class)
                                        .beanMethod("createLandUseFluxTypeToReportingTable")
                                        .description("Inserts a single Land Use Flux Type To Reporting Table Record into the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .schema(schemaBuilder()
                                                                        .implementation(LandUseFluxTypeToReportingTable.class))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("201").description("The Land Use Flux Type To Reporting Table Record was successfully created")
                                                        .implementation(LandUseFluxTypeToReportingTable.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while creating the Land Use Flux Type To Reporting Table Record")
                                                        .implementation(String.class)))
                        .build()

                .and(route()
                        .POST("/api/v1/land_uses_flux_types_to_reporting_tables/all",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::createLandUsesFluxTypesToReportingTables,
                                ops -> ops
                                        .tag("Create")
                                        .operationId("createLandUsesFluxTypesToReportingTables")
                                        .beanClass(LandUsesFluxTypesToReportingTablesHandler.class)
                                        .beanMethod("createLandUsesFluxTypesToReportingTables")
                                        .description("Inserts several Land Uses Flux Types To Reporting Tables Records into the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .array(arraySchemaBuilder()
                                                                        .schema(schemaBuilder()
                                                                                .implementation(LandUseFluxTypeToReportingTable.class)))))

                                        .response(
                                                responseBuilder()
                                                        .responseCode("201").description("The Land Uses Flux Types To Reporting Tables Records were successfully created")
                                                        .implementationArray(LandUseFluxTypeToReportingTable.class))

                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while creating the Land Uses Flux Types To Reporting Tables Records")
                                                        .implementation(String.class)))
                        .build()

                .and(route()
                        .GET("/api/v1/land_uses_flux_types_to_reporting_tables/ids/{id}",
                                accept(APPLICATION_JSON),
                                handler::retrieveLandUseFluxTypeToReportingTable,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrieveLandUseFluxTypeToReportingTable")
                                        .beanClass(LandUsesFluxTypesToReportingTablesHandler.class)
                                        .beanMethod("retrieveLandUseFluxTypeToReportingTable")
                                        .description("Retrieves a single Land Use Flux Type To Reporting Table Record from the database given its unique identifier")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("id").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the Land Use Flux Type To Reporting Table Record")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Land Use Flux Type To Reporting Table Record was successfully retrieved")
                                                        .implementation(LandUseFluxTypeToReportingTable.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Land Use Flux Type To Reporting Table Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .GET("/api/v1/land_uses_flux_types_to_reporting_tables/all",
                                accept(APPLICATION_JSON),
                                handler::retrieveLandUsesFluxTypesToReportingTables,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrieveLandUsesFluxTypesToReportingTables")
                                        .beanClass(LandUsesFluxTypesToReportingTablesHandler.class)
                                        .beanMethod("retrieveLandUsesFluxTypesToReportingTables")
                                        .description("Retrieves all or some of the Land Uses Flux Types To Reporting Tables Records from the database depending on whether or not query parameters were included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("ids").in(ParameterIn.QUERY)
                                                        .description("The list of unique identifiers of the Land Uses Flux Types To Reporting Tables Records to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("landUseFluxTypeId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Land Use Flux Type To Reporting Table type to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("emissionTypeId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Land Use Flux Type To Reporting Table data source to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("reportingTableId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Land Use Flux Type To Reporting Table data source to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("name").in(ParameterIn.QUERY)
                                                        .description("The Land Use Flux Type To Reporting Table name (or name fragment) to filter the returned values by ")
                                                        .implementation(String.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Land Uses Flux Types To Reporting Tables Records were successfully retrieved")
                                                        .implementationArray(LandUseFluxTypeToReportingTable.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Land Uses Flux Types To Reporting Tables Records")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .PUT("/api/v1/land_uses_flux_types_to_reporting_tables",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::updateLandUseFluxTypeToReportingTable,
                                ops -> ops
                                        .tag("Update")
                                        .operationId("updateLandUseFluxTypeToReportingTable")
                                        .beanClass(LandUsesFluxTypesToReportingTablesHandler.class)
                                        .beanMethod("updateLandUseFluxTypeToReportingTable")
                                        .description("Updates a single Land Use Flux Type To Reporting Table Record in the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .schema(schemaBuilder()
                                                                        .implementation(LandUseFluxTypeToReportingTable.class))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Land Use Flux Type To Reporting Table Record was successfully updated")
                                                        .implementation(LandUseFluxTypeToReportingTable.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while updating the Land Use Flux Type To Reporting Table Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .PUT("/api/v1/land_uses_flux_types_to_reporting_tables/all",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::updateLandUsesFluxTypesToReportingTables,
                                ops -> ops
                                        .tag("Update")
                                        .operationId("updateLandUseFluxTypeToReportingTable")
                                        .beanClass(LandUsesFluxTypesToReportingTablesHandler.class)
                                        .beanMethod("updateLandUsesFluxTypesToReportingTables")
                                        .description("Updates several Land Uses Flux Types To Reporting Tables Records in the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .array(arraySchemaBuilder()
                                                                        .schema(schemaBuilder()
                                                                                .implementation(LandUseFluxTypeToReportingTable.class)))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Land Uses Flux Types To Reporting Tables Records were successfully updated")
                                                        .implementation(LandUseFluxTypeToReportingTable.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while updating the Land Uses Flux Types To Reporting Tables Records")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .DELETE("/api/v1/land_uses_flux_types_to_reporting_tables/ids/{id}",
                                accept(APPLICATION_JSON),
                                handler::deleteLandUseFluxTypeToReportingTable,
                                ops -> ops
                                        .tag("Delete")
                                        .operationId("deleteLandUseFluxTypeToReportingTable")
                                        .beanClass(LandUsesFluxTypesToReportingTablesHandler.class)
                                        .beanMethod("deleteLandUseFluxTypeToReportingTable")
                                        .description("Deletes a single Land Use Flux Type To Reporting Table Record from the database given its unique identifier")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("id").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the Land Use Flux Type To Reporting Table Record")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Land Use Flux Type To Reporting Table Record was successfully deleted")
                                                        .implementation(Integer.class)
                                                        .description("The number of Land Uses Flux Types To Reporting Tables Records successfully deleted"))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while deleting the Land Use Flux Type To Reporting Table Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .DELETE("/api/v1/land_uses_flux_types_to_reporting_tables/all",
                                accept(APPLICATION_JSON),
                                handler::deleteLandUsesFluxTypesToReportingTables,
                                ops -> ops
                                        .tag("Delete")
                                        .operationId("deleteLandUsesFluxTypesToReportingTables")
                                        .beanClass(LandUsesFluxTypesToReportingTablesHandler.class)
                                        .beanMethod("deleteLandUsesFluxTypesToReportingTables")
                                        .description("Deleted all or some of the Land Uses Flux Types To Reporting Tables Records from the database depending on whether or not query parameters were included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("ids").in(ParameterIn.QUERY)
                                                        .description("The list of unique identifiers of the Land Uses Flux Types To Reporting Tables Records to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("landUseFluxTypeId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Land Use Flux Type To Reporting Table type to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("emissionTypeId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Land Use Flux Type To Reporting Table data source to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("landUseFluxTypeToReportingTableId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Land Use Flux Type To Reporting Table data source to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("name").in(ParameterIn.QUERY)
                                                        .description("The Land Use Flux Type To Reporting Table name (or name fragment) filter the deleted values by ")
                                                        .implementation(String.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Land Uses Flux Types To Reporting Tables Records were successfully deleted")
                                                        .implementation(Integer.class)
                                                        .description("The number of Land Uses Flux Types To Reporting Tables Records successfully deleted"))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while deleting the Land Uses Flux Types To Reporting Tables Records")
                                                        .implementation(String.class)))
                        .build()));                        
    }

}

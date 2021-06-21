/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.fluxestoreportingvariables.configurations;

import global.moja.fluxestoreportingvariables.handlers.FluxesToReportingVariablesHandler;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import global.moja.fluxestoreportingvariables.models.FluxToReportingVariable;
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
    RouterFunction<ServerResponse> routeRequests(FluxesToReportingVariablesHandler handler) {

        return
                route()
                        .POST("/api/v1/fluxes_to_reporting_variables",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::createFluxToReportingVariable,
                                ops -> ops
                                        .tag("Create")
                                        .operationId("createFluxToReportingVariable")
                                        .beanClass(FluxesToReportingVariablesHandler.class)
                                        .beanMethod("createFluxToReportingVariable")
                                        .description("Inserts a single Flux To Reporting Variable Assignment Rule Record into the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .schema(schemaBuilder()
                                                                        .implementation(FluxToReportingVariable.class))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("201").description("The Flux To Reporting Variable Assignment Rule Record was successfully created")
                                                        .implementation(FluxToReportingVariable.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while creating the Flux To Reporting Variable Assignment Rule Record")
                                                        .implementation(String.class)))
                        .build()

                .and(route()
                        .POST("/api/v1/fluxes_to_reporting_variables/all",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::createFluxesToReportingVariables,
                                ops -> ops
                                        .tag("Create")
                                        .operationId("createFluxesToReportingVariables")
                                        .beanClass(FluxesToReportingVariablesHandler.class)
                                        .beanMethod("createFluxesToReportingVariables")
                                        .description("Inserts several Fluxes To Reporting Variable Records into the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .array(arraySchemaBuilder()
                                                                        .schema(schemaBuilder()
                                                                                .implementation(FluxToReportingVariable.class)))))

                                        .response(
                                                responseBuilder()
                                                        .responseCode("201").description("The Fluxes To Reporting Variable Records were successfully created")
                                                        .implementationArray(FluxToReportingVariable.class))

                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while creating the Fluxes To Reporting Variable Records")
                                                        .implementation(String.class)))
                        .build()

                .and(route()
                        .GET("/api/v1/fluxes_to_reporting_variables/ids/{id}",
                                accept(APPLICATION_JSON),
                                handler::retrieveFluxToReportingVariable,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrieveFluxToReportingVariable")
                                        .beanClass(FluxesToReportingVariablesHandler.class)
                                        .beanMethod("retrieveFluxToReportingVariable")
                                        .description("Retrieves a single Flux To Reporting Variable Assignment Rule Record from the database given its unique identifier")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("id").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the Flux To Reporting Variable Assignment Rule Record")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Flux To Reporting Variable Assignment Rule Record was successfully retrieved")
                                                        .implementation(FluxToReportingVariable.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Flux To Reporting Variable Assignment Rule Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .GET("/api/v1/fluxes_to_reporting_variables/all",
                                accept(APPLICATION_JSON),
                                handler::retrieveFluxesToReportingVariables,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrieveFluxesToReportingVariables")
                                        .beanClass(FluxesToReportingVariablesHandler.class)
                                        .beanMethod("retrieveFluxesToReportingVariables")
                                        .description("Retrieves all or some of the Fluxes To Reporting Variable Records from the database depending on whether or not query parameters were included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("ids").in(ParameterIn.QUERY)
                                                        .description("The list of unique identifiers of the Fluxes To Reporting Variable Records to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("startPoolId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the start pool to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("endPoolId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the end pool to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("reportingVariableId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the end pool to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("rule").in(ParameterIn.QUERY)
                                                        .description("The rule to filter the returned values by i.e \"add\", \"subtract\" or \"ignore\"")
                                                        .implementation(String.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Fluxes To Reporting Variable Records were successfully retrieved")
                                                        .implementationArray(FluxToReportingVariable.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Fluxes To Reporting Variable Records")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .PUT("/api/v1/fluxes_to_reporting_variables",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::updateFluxToReportingVariable,
                                ops -> ops
                                        .tag("Update")
                                        .operationId("updateFluxToReportingVariable")
                                        .beanClass(FluxesToReportingVariablesHandler.class)
                                        .beanMethod("updateFluxToReportingVariable")
                                        .description("Updates a single Flux To Reporting Variable Assignment Rule Record in the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .schema(schemaBuilder()
                                                                        .implementation(FluxToReportingVariable.class))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Flux To Reporting Variable Assignment Rule Record was successfully updated")
                                                        .implementation(FluxToReportingVariable.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while updating the Flux To Reporting Variable Assignment Rule Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .PUT("/api/v1/fluxes_to_reporting_variables/all",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::updateFluxesToReportingVariables,
                                ops -> ops
                                        .tag("Update")
                                        .operationId("updateFluxToReportingVariable")
                                        .beanClass(FluxesToReportingVariablesHandler.class)
                                        .beanMethod("updateFluxesToReportingVariables")
                                        .description("Updates several Fluxes To Reporting Variable Records in the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .array(arraySchemaBuilder()
                                                                        .schema(schemaBuilder()
                                                                                .implementation(FluxToReportingVariable.class)))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Fluxes To Reporting Variable Records were successfully updated")
                                                        .implementation(FluxToReportingVariable.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while updating the Fluxes To Reporting Variable Records")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .DELETE("/api/v1/fluxes_to_reporting_variables/ids/{id}",
                                accept(APPLICATION_JSON),
                                handler::deleteFluxToReportingVariable,
                                ops -> ops
                                        .tag("Delete")
                                        .operationId("deleteFluxToReportingVariable")
                                        .beanClass(FluxesToReportingVariablesHandler.class)
                                        .beanMethod("deleteFluxToReportingVariable")
                                        .description("Deletes a single Flux To Reporting Variable Assignment Rule Record from the database given its unique identifier")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("id").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the Flux To Reporting Variable Assignment Rule Record")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Flux To Reporting Variable Assignment Rule Record was successfully deleted")
                                                        .implementation(Integer.class)
                                                        .description("The number of Fluxes To Reporting Variable Records successfully deleted"))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while deleting the Flux To Reporting Variable Assignment Rule Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .DELETE("/api/v1/fluxes_to_reporting_variables/all",
                                accept(APPLICATION_JSON),
                                handler::deleteFluxesToReportingVariables,
                                ops -> ops
                                        .tag("Delete")
                                        .operationId("deleteFluxesToReportingVariables")
                                        .beanClass(FluxesToReportingVariablesHandler.class)
                                        .beanMethod("deleteFluxesToReportingVariables")
                                        .description("Deleted all or some of the Fluxes To Reporting Variable Records from the database depending on whether or not query parameters were included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("ids").in(ParameterIn.QUERY)
                                                        .description("The list of unique identifiers of the Fluxes To Reporting Variable Records to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("startPoolId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the start pool to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("endPoolId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the end pool to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("reportingVariableId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the end pool to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("rule").in(ParameterIn.QUERY)
                                                        .description("The rule to filter the deleted values by i.e \"add\", \"subtract\" or \"ignore\"")
                                                        .implementation(String.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Fluxes To Reporting Variable Records were successfully deleted")
                                                        .implementation(Integer.class)
                                                        .description("The number of Fluxes To Reporting Variable Records successfully deleted"))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while deleting the Fluxes To Reporting Variable Records")
                                                        .implementation(String.class)))
                        .build()));                        
    }

}

/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.conversionandremainingperiods.configurations;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import global.moja.conversionandremainingperiods.handlers.ConversionAndRemainingPeriodsHandler;
import global.moja.conversionandremainingperiods.models.ConversionAndRemainingPeriod;
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
    RouterFunction<ServerResponse> routeRequests(ConversionAndRemainingPeriodsHandler handler) {

        return
                route()
                        .POST("/api/v1/conversion_and_remaining_periods",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::createConversionAndRemainingPeriod,
                                ops -> ops
                                        .tag("Create")
                                        .operationId("createConversionAndRemainingPeriod")
                                        .beanClass(ConversionAndRemainingPeriodsHandler.class)
                                        .beanMethod("createConversionAndRemainingPeriod")
                                        .description("Inserts a single Conversion and Remaining Period Record into the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .schema(schemaBuilder()
                                                                        .implementation(ConversionAndRemainingPeriod.class))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("201").description("The Conversion and Remaining Period Record was successfully created")
                                                        .implementation(ConversionAndRemainingPeriod.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while creating the Conversion and Remaining Period Record")
                                                        .implementation(String.class)))
                        .build()

                .and(route()
                        .POST("/api/v1/conversion_and_remaining_periods/all",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::createConversionAndRemainingPeriods,
                                ops -> ops
                                        .tag("Create")
                                        .operationId("createConversionAndRemainingPeriods")
                                        .beanClass(ConversionAndRemainingPeriodsHandler.class)
                                        .beanMethod("createConversionAndRemainingPeriods")
                                        .description("Inserts several Conversion and Remaining Periods Records into the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .array(arraySchemaBuilder()
                                                                        .schema(schemaBuilder()
                                                                                .implementation(ConversionAndRemainingPeriod.class)))))

                                        .response(
                                                responseBuilder()
                                                        .responseCode("201").description("The Conversion and Remaining Periods Records were successfully created")
                                                        .implementationArray(ConversionAndRemainingPeriod.class))

                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while creating the Conversion and Remaining Periods Records")
                                                        .implementation(String.class)))
                        .build()

                .and(route()
                        .GET("/api/v1/conversion_and_remaining_periods/ids/{id}",
                                accept(APPLICATION_JSON),
                                handler::retrieveConversionAndRemainingPeriod,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrieveConversionAndRemainingPeriod")
                                        .beanClass(ConversionAndRemainingPeriodsHandler.class)
                                        .beanMethod("retrieveConversionAndRemainingPeriod")
                                        .description("Retrieves a single Conversion and Remaining Period Record from the database given its unique identifier")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("id").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the Conversion and Remaining Period Record")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Conversion and Remaining Period Record was successfully retrieved")
                                                        .implementation(ConversionAndRemainingPeriod.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Conversion and Remaining Period Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .GET("/api/v1/conversion_and_remaining_periods/all",
                                accept(APPLICATION_JSON),
                                handler::retrieveConversionAndRemainingPeriods,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrieveConversionAndRemainingPeriods")
                                        .beanClass(ConversionAndRemainingPeriodsHandler.class)
                                        .beanMethod("retrieveConversionAndRemainingPeriods")
                                        .description("Retrieves all or some of the Conversion and Remaining Periods Records from the database depending on whether or not query parameters were included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("ids").in(ParameterIn.QUERY)
                                                        .description("The list of unique identifiers of the Conversion and Remaining Periods Records to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("previousLandCoverId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the previous Land Cover to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("currentLandCoverId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the current Land Cover to filter the returned values by")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Conversion and Remaining Periods Records were successfully retrieved")
                                                        .implementationArray(ConversionAndRemainingPeriod.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Conversion and Remaining Periods Records")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .PUT("/api/v1/conversion_and_remaining_periods",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::updateConversionAndRemainingPeriod,
                                ops -> ops
                                        .tag("Update")
                                        .operationId("updateConversionAndRemainingPeriod")
                                        .beanClass(ConversionAndRemainingPeriodsHandler.class)
                                        .beanMethod("updateConversionAndRemainingPeriod")
                                        .description("Updates a single Conversion and Remaining Period Record in the database")
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Conversion and Remaining Period Record was successfully updated")
                                                        .implementation(ConversionAndRemainingPeriod.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while updating the Conversion and Remaining Period Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .PUT("/api/v1/conversion_and_remaining_periods/all",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::updateConversionAndRemainingPeriods,
                                ops -> ops
                                        .tag("Update")
                                        .operationId("updateConversionAndRemainingPeriod")
                                        .beanClass(ConversionAndRemainingPeriodsHandler.class)
                                        .beanMethod("updateConversionAndRemainingPeriods")
                                        .description("Updates several Conversion and Remaining Periods Records in the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .array(arraySchemaBuilder()
                                                                        .schema(schemaBuilder()
                                                                                .implementation(ConversionAndRemainingPeriod.class)))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Conversion and Remaining Periods Records were successfully updated")
                                                        .implementation(ConversionAndRemainingPeriod.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while updating the Conversion and Remaining Periods Records")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .DELETE("/api/v1/conversion_and_remaining_periods/ids/{id}",
                                accept(APPLICATION_JSON),
                                handler::deleteConversionAndRemainingPeriod,
                                ops -> ops
                                        .tag("Delete")
                                        .operationId("deleteConversionAndRemainingPeriod")
                                        .beanClass(ConversionAndRemainingPeriodsHandler.class)
                                        .beanMethod("deleteConversionAndRemainingPeriod")
                                        .description("Deletes a single Conversion and Remaining Period Record from the database given its unique identifier")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("id").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the Conversion and Remaining Period Record")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Conversion and Remaining Period Record was successfully deleted")
                                                        .implementation(Integer.class)
                                                        .description("The number of Conversion and Remaining Periods Records successfully deleted"))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while deleting the Conversion and Remaining Period Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .DELETE("/api/v1/conversion_and_remaining_periods/all",
                                accept(APPLICATION_JSON),
                                handler::deleteConversionAndRemainingPeriods,
                                ops -> ops
                                        .tag("Delete")
                                        .operationId("deleteConversionAndRemainingPeriods")
                                        .beanClass(ConversionAndRemainingPeriodsHandler.class)
                                        .beanMethod("deleteConversionAndRemainingPeriods")
                                        .description("Deleted all or some of the Conversion and Remaining Periods Records from the database depending on whether or not query parameters were included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("ids").in(ParameterIn.QUERY)
                                                        .description("The list of unique identifiers of the Conversion and Remaining Periods Records to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("previousLandCoverId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the previous Land Cover to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("currentLandCoverId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the current Land Cover to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Conversion and Remaining Periods Records were successfully deleted")
                                                        .implementation(Integer.class)
                                                        .description("The number of Conversion and Remaining Periods Records successfully deleted"))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while deleting the Conversion and Remaining Periods Records")
                                                        .implementation(String.class)))
                        .build()));                        
    }

}

/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.emissiontypes.configurations;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import global.moja.emissiontypes.handlers.EmissionTypesHandler;
import global.moja.emissiontypes.models.EmissionType;
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
    RouterFunction<ServerResponse> routeRequests(EmissionTypesHandler handler) {

        return
                route()
                        .POST("/api/v1/emission_types",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::createEmissionType,
                                ops -> ops
                                        .tag("Create")
                                        .operationId("createEmissionType")
                                        .beanClass(EmissionTypesHandler.class)
                                        .beanMethod("createEmissionType")
                                        .description("Inserts a single Emission Type Record into the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .schema(schemaBuilder()
                                                                        .implementation(EmissionType.class))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("201").description("The Emission Type Record was successfully created")
                                                        .implementation(EmissionType.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while creating the Emission Type Record")
                                                        .implementation(String.class)))
                        .build()

                .and(route()
                        .POST("/api/v1/emission_types/all",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::createEmissionTypes,
                                ops -> ops
                                        .tag("Create")
                                        .operationId("createEmissionTypes")
                                        .beanClass(EmissionTypesHandler.class)
                                        .beanMethod("createEmissionTypes")
                                        .description("Inserts several Emission Types Records into the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .array(arraySchemaBuilder()
                                                                        .schema(schemaBuilder()
                                                                                .implementation(EmissionType.class)))))

                                        .response(
                                                responseBuilder()
                                                        .responseCode("201").description("The Emission Types Records were successfully created")
                                                        .implementationArray(EmissionType.class))

                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while creating the Emission Types Records")
                                                        .implementation(String.class)))
                        .build()

                .and(route()
                        .GET("/api/v1/emission_types/ids/{id}",
                                accept(APPLICATION_JSON),
                                handler::retrieveEmissionType,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrieveEmissionType")
                                        .beanClass(EmissionTypesHandler.class)
                                        .beanMethod("retrieveEmissionType")
                                        .description("Retrieves a single Emission Type Record from the database given its unique identifier")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("id").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the Emission Type Record")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Emission Type Record was successfully retrieved")
                                                        .implementation(EmissionType.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Emission Type Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .GET("/api/v1/emission_types/all",
                                accept(APPLICATION_JSON),
                                handler::retrieveEmissionTypes,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrieveEmissionTypes")
                                        .beanClass(EmissionTypesHandler.class)
                                        .beanMethod("retrieveEmissionTypes")
                                        .description("Retrieves all or some of the Emission Types Records from the database depending on whether or not query parameters were included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("ids").in(ParameterIn.QUERY)
                                                        .description("The list of unique identifiers of the Emission Types Records to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("name").in(ParameterIn.QUERY)
                                                        .description("The Emission Type name (or name fragment) to filter the returned values by ")
                                                        .implementation(String.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("abbreviation").in(ParameterIn.QUERY)
                                                        .description("The Emission Type abbreviation (or abbreviation fragment) to filter the returned values by ")
                                                        .implementation(String.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Emission Types Records were successfully retrieved")
                                                        .implementationArray(EmissionType.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Emission Types Records")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .PUT("/api/v1/emission_types",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::updateEmissionType,
                                ops -> ops
                                        .tag("Update")
                                        .operationId("updateEmissionType")
                                        .beanClass(EmissionTypesHandler.class)
                                        .beanMethod("updateEmissionType")
                                        .description("Updates a single Emission Type Record in the database")
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Emission Type Record was successfully updated")
                                                        .implementation(EmissionType.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while updating the Emission Type Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .PUT("/api/v1/emission_types/all",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::updateEmissionTypes,
                                ops -> ops
                                        .tag("Update")
                                        .operationId("updateEmissionType")
                                        .beanClass(EmissionTypesHandler.class)
                                        .beanMethod("updateEmissionTypes")
                                        .description("Updates several Emission Types Records in the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .array(arraySchemaBuilder()
                                                                        .schema(schemaBuilder()
                                                                                .implementation(EmissionType.class)))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Emission Types Records were successfully updated")
                                                        .implementation(EmissionType.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while updating the Emission Types Records")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .DELETE("/api/v1/emission_types/ids/{id}",
                                accept(APPLICATION_JSON),
                                handler::deleteEmissionType,
                                ops -> ops
                                        .tag("Delete")
                                        .operationId("deleteEmissionType")
                                        .beanClass(EmissionTypesHandler.class)
                                        .beanMethod("deleteEmissionType")
                                        .description("Deletes a single Emission Type Record from the database given its unique identifier")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("id").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the Emission Type Record")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Emission Type Record was successfully deleted")
                                                        .implementation(Integer.class)
                                                        .description("The number of Emission Types Records successfully deleted"))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while deleting the Emission Type Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .DELETE("/api/v1/emission_types/all",
                                accept(APPLICATION_JSON),
                                handler::deleteEmissionTypes,
                                ops -> ops
                                        .tag("Delete")
                                        .operationId("deleteEmissionTypes")
                                        .beanClass(EmissionTypesHandler.class)
                                        .beanMethod("deleteEmissionTypes")
                                        .description("Deleted all or some of the Emission Types Records from the database depending on whether or not query parameters were included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("ids").in(ParameterIn.QUERY)
                                                        .description("The list of unique identifiers of the Emission Types Records to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("name").in(ParameterIn.QUERY)
                                                        .description("The Emission Type name (or name fragment) filter the deleted values by ")
                                                        .implementation(String.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("abbreviation").in(ParameterIn.QUERY)
                                                        .description("The Emission Type abbreviation (or abbreviation fragment) to filter the deleted values by ")
                                                        .implementation(String.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Emission Types Records were successfully deleted")
                                                        .implementation(Integer.class)
                                                        .description("The number of Emission Types Records successfully deleted"))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while deleting the Emission Types Records")
                                                        .implementation(String.class)))
                        .build()));                        
    }

}

/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.landusesfluxtypes.configurations;

import global.moja.landusesfluxtypes.handlers.LandUsesFluxTypesHandler;
import global.moja.landusesfluxtypes.models.LandUseFluxType;
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
    RouterFunction<ServerResponse> routeRequests(LandUsesFluxTypesHandler handler) {

        return
                route()
                        .POST("/api/v1/land_uses_flux_types",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::createLandUseFluxType,
                                ops -> ops
                                        .tag("Create")
                                        .operationId("createLandUseFluxType")
                                        .beanClass(LandUsesFluxTypesHandler.class)
                                        .beanMethod("createLandUseFluxType")
                                        .description("Inserts a single Land Use Flux Type Record into the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .schema(schemaBuilder()
                                                                        .implementation(LandUseFluxType.class))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("201").description("The Land Use Flux Type Record was successfully created")
                                                        .implementation(LandUseFluxType.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while creating the Land Use Flux Type Record")
                                                        .implementation(String.class)))
                        .build()

                .and(route()
                        .POST("/api/v1/land_uses_flux_types/all",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::createLandUsesFluxTypes,
                                ops -> ops
                                        .tag("Create")
                                        .operationId("createLandUsesFluxTypes")
                                        .beanClass(LandUsesFluxTypesHandler.class)
                                        .beanMethod("createLandUsesFluxTypes")
                                        .description("Inserts several Land Uses Flux Types Records into the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .array(arraySchemaBuilder()
                                                                        .schema(schemaBuilder()
                                                                                .implementation(LandUseFluxType.class)))))

                                        .response(
                                                responseBuilder()
                                                        .responseCode("201").description("The Land Uses Flux Types Records were successfully created")
                                                        .implementationArray(LandUseFluxType.class))

                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while creating the Land Uses Flux Types Records")
                                                        .implementation(String.class)))
                        .build()

                .and(route()
                        .GET("/api/v1/land_uses_flux_types/ids/{id}",
                                accept(APPLICATION_JSON),
                                handler::retrieveLandUseFluxType,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrieveLandUseFluxType")
                                        .beanClass(LandUsesFluxTypesHandler.class)
                                        .beanMethod("retrieveLandUseFluxType")
                                        .description("Retrieves a single Land Use Flux Type Record from the database given its unique identifier")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("id").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the Land Use Flux Type Record")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Land Use Flux Type Record was successfully retrieved")
                                                        .implementation(LandUseFluxType.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Land Use Flux Type Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .GET("/api/v1/land_uses_flux_types/all",
                                accept(APPLICATION_JSON),
                                handler::retrieveLandUsesFluxTypes,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrieveLandUsesFluxTypes")
                                        .beanClass(LandUsesFluxTypesHandler.class)
                                        .beanMethod("retrieveLandUsesFluxTypes")
                                        .description("Retrieves all or some of the Land Uses Flux Types Records from the database depending on whether or not query parameters were included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("ids").in(ParameterIn.QUERY)
                                                        .description("The list of unique identifiers of the Land Uses Flux Types Records to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("landUseCategoryId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Land Use Flux Type data source to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("fluxTypeId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Land Use Flux Type type to filter the returned values by")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Land Uses Flux Types Records were successfully retrieved")
                                                        .implementationArray(LandUseFluxType.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Land Uses Flux Types Records")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .PUT("/api/v1/land_uses_flux_types",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::updateLandUseFluxType,
                                ops -> ops
                                        .tag("Update")
                                        .operationId("updateLandUseFluxType")
                                        .beanClass(LandUsesFluxTypesHandler.class)
                                        .beanMethod("updateLandUseFluxType")
                                        .description("Updates a single Land Use Flux Type Record in the database")
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Land Use Flux Type Record was successfully updated")
                                                        .implementation(LandUseFluxType.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while updating the Land Use Flux Type Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .PUT("/api/v1/land_uses_flux_types/all",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::updateLandUsesFluxTypes,
                                ops -> ops
                                        .tag("Update")
                                        .operationId("updateLandUseFluxType")
                                        .beanClass(LandUsesFluxTypesHandler.class)
                                        .beanMethod("updateLandUsesFluxTypes")
                                        .description("Updates several Land Uses Flux Types Records in the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .array(arraySchemaBuilder()
                                                                        .schema(schemaBuilder()
                                                                                .implementation(LandUseFluxType.class)))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Land Uses Flux Types Records were successfully updated")
                                                        .implementation(LandUseFluxType.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while updating the Land Uses Flux Types Records")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .DELETE("/api/v1/land_uses_flux_types/ids/{id}",
                                accept(APPLICATION_JSON),
                                handler::deleteLandUseFluxType,
                                ops -> ops
                                        .tag("Delete")
                                        .operationId("deleteLandUseFluxType")
                                        .beanClass(LandUsesFluxTypesHandler.class)
                                        .beanMethod("deleteLandUseFluxType")
                                        .description("Deletes a single Land Use Flux Type Record from the database given its unique identifier")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("id").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the Land Use Flux Type Record")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Land Use Flux Type Record was successfully deleted")
                                                        .implementation(Integer.class)
                                                        .description("The number of Land Uses Flux Types Records successfully deleted"))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while deleting the Land Use Flux Type Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .DELETE("/api/v1/land_uses_flux_types/all",
                                accept(APPLICATION_JSON),
                                handler::deleteLandUsesFluxTypes,
                                ops -> ops
                                        .tag("Delete")
                                        .operationId("deleteLandUsesFluxTypes")
                                        .beanClass(LandUsesFluxTypesHandler.class)
                                        .beanMethod("deleteLandUsesFluxTypes")
                                        .description("Deleted all or some of the Land Uses Flux Types Records from the database depending on whether or not query parameters were included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("ids").in(ParameterIn.QUERY)
                                                        .description("The list of unique identifiers of the Land Uses Flux Types Records to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("landUseCategoryId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Land Use Flux Type data source to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("fluxTypeId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Land Use Flux Type type to filter the deleted values by")
                                                        .implementation(Long.class))

                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Land Uses Flux Types Records were successfully deleted")
                                                        .implementation(Integer.class)
                                                        .description("The number of Land Uses Flux Types Records successfully deleted"))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while deleting the Land Uses Flux Types Records")
                                                        .implementation(String.class)))
                        .build()));                        
    }

}

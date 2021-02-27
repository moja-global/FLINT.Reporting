/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.landusecategories.configurations;

import global.moja.landusecategories.models.LandUseCategory;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import global.moja.landusecategories.handlers.LandUseCategoriesHandler;
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
    RouterFunction<ServerResponse> routeRequests(LandUseCategoriesHandler handler) {

        return
                route()
                        .POST("/api/v1/land_use_categories",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::createLandUseCategory,
                                ops -> ops
                                        .tag("Create")
                                        .operationId("createLandUseCategory")
                                        .beanClass(LandUseCategoriesHandler.class)
                                        .beanMethod("createLandUseCategory")
                                        .description("Inserts a single Land Use Category Record into the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .schema(schemaBuilder()
                                                                        .implementation(LandUseCategory.class))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("201").description("The Land Use Category Record was successfully created")
                                                        .implementation(LandUseCategory.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while creating the Land Use Category Record")
                                                        .implementation(String.class)))
                        .build()

                .and(route()
                        .POST("/api/v1/land_use_categories/all",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::createLandUseCategories,
                                ops -> ops
                                        .tag("Create")
                                        .operationId("createLandUseCategories")
                                        .beanClass(LandUseCategoriesHandler.class)
                                        .beanMethod("createLandUseCategories")
                                        .description("Inserts several Land Use Categories Records into the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .array(arraySchemaBuilder()
                                                                        .schema(schemaBuilder()
                                                                                .implementation(LandUseCategory.class)))))

                                        .response(
                                                responseBuilder()
                                                        .responseCode("201").description("The Land Use Categories Records were successfully created")
                                                        .implementationArray(LandUseCategory.class))

                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while creating the Land Use Categories Records")
                                                        .implementation(String.class)))
                        .build()

                .and(route()
                        .GET("/api/v1/land_use_categories/ids/{id}",
                                accept(APPLICATION_JSON),
                                handler::retrieveLandUseCategory,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrieveLandUseCategory")
                                        .beanClass(LandUseCategoriesHandler.class)
                                        .beanMethod("retrieveLandUseCategory")
                                        .description("Retrieves a single Land Use Category Record from the database given its unique identifier")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("id").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the Land Use Category Record")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Land Use Category Record was successfully retrieved")
                                                        .implementation(LandUseCategory.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Land Use Category Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .GET("/api/v1/land_use_categories/all",
                                accept(APPLICATION_JSON),
                                handler::retrieveLandUseCategories,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrieveLandUseCategories")
                                        .beanClass(LandUseCategoriesHandler.class)
                                        .beanMethod("retrieveLandUseCategories")
                                        .description("Retrieves all or some of the Land Use Categories Records from the database depending on whether or not query parameters were included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("ids").in(ParameterIn.QUERY)
                                                        .description("The list of unique identifiers of the Land Use Categories Records to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("reportingFrameworkId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Reporting Framework to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("parentLandUseCategoryId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Parent Land Use Category to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("coverTypeId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Cover Type to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("name").in(ParameterIn.QUERY)
                                                        .description("The Land Use Category name (or name fragment) to filter the returned values by ")
                                                        .implementation(String.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Land Use Categories Records were successfully retrieved")
                                                        .implementationArray(LandUseCategory.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Land Use Categories Records")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .PUT("/api/v1/land_use_categories",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::updateLandUseCategory,
                                ops -> ops
                                        .tag("Update")
                                        .operationId("updateLandUseCategory")
                                        .beanClass(LandUseCategoriesHandler.class)
                                        .beanMethod("updateLandUseCategory")
                                        .description("Updates a single Land Use Category Record in the database")
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Land Use Category Record was successfully updated")
                                                        .implementation(LandUseCategory.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while updating the Land Use Category Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .PUT("/api/v1/land_use_categories/all",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::updateLandUseCategories,
                                ops -> ops
                                        .tag("Update")
                                        .operationId("updateLandUseCategory")
                                        .beanClass(LandUseCategoriesHandler.class)
                                        .beanMethod("updateLandUseCategories")
                                        .description("Updates several Land Use Categories Records in the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .array(arraySchemaBuilder()
                                                                        .schema(schemaBuilder()
                                                                                .implementation(LandUseCategory.class)))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Land Use Categories Records were successfully updated")
                                                        .implementation(LandUseCategory.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while updating the Land Use Categories Records")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .DELETE("/api/v1/land_use_categories/ids/{id}",
                                accept(APPLICATION_JSON),
                                handler::deleteLandUseCategory,
                                ops -> ops
                                        .tag("Delete")
                                        .operationId("deleteLandUseCategory")
                                        .beanClass(LandUseCategoriesHandler.class)
                                        .beanMethod("deleteLandUseCategory")
                                        .description("Deletes a single Land Use Category Record from the database given its unique identifier")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("id").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the Land Use Category Record")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Land Use Category Record was successfully deleted")
                                                        .implementation(Integer.class)
                                                        .description("The number of Land Use Categories Records successfully deleted"))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while deleting the Land Use Category Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .DELETE("/api/v1/land_use_categories/all",
                                accept(APPLICATION_JSON),
                                handler::deleteLandUseCategories,
                                ops -> ops
                                        .tag("Delete")
                                        .operationId("deleteLandUseCategories")
                                        .beanClass(LandUseCategoriesHandler.class)
                                        .beanMethod("deleteLandUseCategories")
                                        .description("Deleted all or some of the Land Use Categories Records from the database depending on whether or not query parameters were included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("ids").in(ParameterIn.QUERY)
                                                        .description("The list of unique identifiers of the Land Use Categories Records to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("reportingFrameworkId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Reporting Framework to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("parentLandUseCategoryId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Parent Land Use Category to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("coverTypeId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Cover Type to filter the deleted values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("name").in(ParameterIn.QUERY)
                                                        .description("The Land Use Category name (or name fragment) filter the deleted values by ")
                                                        .implementation(String.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Land Use Categories Records were successfully deleted")
                                                        .implementation(Integer.class)
                                                        .description("The number of Land Use Categories Records successfully deleted"))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while deleting the Land Use Categories Records")
                                                        .implementation(String.class)))
                        .build()));                        
    }

}

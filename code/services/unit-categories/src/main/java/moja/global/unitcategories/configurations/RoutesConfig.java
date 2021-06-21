/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.unitcategories.configurations;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import moja.global.unitcategories.handlers.UnitCategoriesHandler;
import moja.global.unitcategories.models.UnitCategory;
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
    RouterFunction<ServerResponse> routeRequests(UnitCategoriesHandler handler) {

        return
                route()
                        .POST("/api/v1/unit_categories",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::createUnitCategory,
                                ops -> ops
                                        .tag("Create")
                                        .operationId("createUnitCategory")
                                        .beanClass(UnitCategoriesHandler.class)
                                        .beanMethod("createUnitCategory")
                                        .description("Inserts a single Unit Category Record into the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .schema(schemaBuilder()
                                                                        .implementation(UnitCategory.class))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("201").description("The Unit Category Record was successfully created")
                                                        .implementation(UnitCategory.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while creating the Unit Category Record")
                                                        .implementation(String.class)))
                        .build()

                .and(route()
                        .POST("/api/v1/unit_categories/all",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::createUnitCategories,
                                ops -> ops
                                        .tag("Create")
                                        .operationId("createUnitCategories")
                                        .beanClass(UnitCategoriesHandler.class)
                                        .beanMethod("createUnitCategories")
                                        .description("Inserts several Unit Category Records into the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .array(arraySchemaBuilder()
                                                                        .schema(schemaBuilder()
                                                                                .implementation(UnitCategory.class)))))

                                        .response(
                                                responseBuilder()
                                                        .responseCode("201").description("The Unit Category Records were successfully created")
                                                        .implementationArray(UnitCategory.class))

                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while creating the Unit Category Records")
                                                        .implementation(String.class)))
                        .build()

                .and(route()
                        .GET("/api/v1/unit_categories/ids/{id}",
                                accept(APPLICATION_JSON),
                                handler::retrieveUnitCategory,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrieveUnitCategory")
                                        .beanClass(UnitCategoriesHandler.class)
                                        .beanMethod("retrieveUnitCategory")
                                        .description("Retrieves a single Unit Category Record from the database given its unique identifier")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("id").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the Unit Category Record")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Unit Category Record was successfully retrieved")
                                                        .implementation(UnitCategory.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Unit Category Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .GET("/api/v1/unit_categories/all",
                                accept(APPLICATION_JSON),
                                handler::retrieveUnitCategories,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrieveUnitCategories")
                                        .beanClass(UnitCategoriesHandler.class)
                                        .beanMethod("retrieveUnitCategories")
                                        .description("Retrieves all or some of the Unit Category Records from the database depending on whether or not the ids parameter was included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("ids").in(ParameterIn.QUERY)
                                                        .description("The unique identifiers of the Unit Category Records to restrict the retrieval to")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Unit Category Records were successfully retrieved")
                                                        .implementationArray(UnitCategory.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Unit Category Records")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .PUT("/api/v1/unit_categories",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::updateUnitCategory,
                                ops -> ops
                                        .tag("Update")
                                        .operationId("updateUnitCategory")
                                        .beanClass(UnitCategoriesHandler.class)
                                        .beanMethod("updateUnitCategory")
                                        .description("Updates a single Unit Category Record in the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .schema(schemaBuilder()
                                                                        .implementation(UnitCategory.class))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Unit Category Record was successfully updated")
                                                        .implementation(UnitCategory.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while updating the Unit Category Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .PUT("/api/v1/unit_categories/all",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::updateUnitCategories,
                                ops -> ops
                                        .tag("Update")
                                        .operationId("updateUnitCategory")
                                        .beanClass(UnitCategoriesHandler.class)
                                        .beanMethod("updateUnitCategories")
                                        .description("Updates several Unit Category Records in the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .array(arraySchemaBuilder()
                                                                        .schema(schemaBuilder()
                                                                                .implementation(UnitCategory.class)))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Unit Category Records were successfully updated")
                                                        .implementation(UnitCategory.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while updating the Unit Category Records")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .DELETE("/api/v1/unit_categories/ids/{id}",
                                accept(APPLICATION_JSON),
                                handler::deleteUnitCategory,
                                ops -> ops
                                        .tag("Delete")
                                        .operationId("deleteUnitCategory")
                                        .beanClass(UnitCategoriesHandler.class)
                                        .beanMethod("deleteUnitCategory")
                                        .description("Deletes a single Unit Category Record from the database given its unique identifier")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("id").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the Unit Category Record")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Unit Category Record was successfully deleted")
                                                        .implementation(Integer.class)
                                                        .description("The number of Unit Category Records successfully deleted"))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while deleting the Unit Category Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .DELETE("/api/v1/unit_categories/all",
                                accept(APPLICATION_JSON),
                                handler::deleteUnitCategories,
                                ops -> ops
                                        .tag("Delete")
                                        .operationId("deleteUnitCategories")
                                        .beanClass(UnitCategoriesHandler.class)
                                        .beanMethod("deleteUnitCategories")
                                        .description("Deleted all or some of the Unit Category Records from the database depending on whether or not the ids parameter was included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("ids").in(ParameterIn.QUERY)
                                                        .description("The unique identifiers of the Unit Category Records to restrict the deletion to")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Unit Category Records were successfully deleted")
                                                        .implementation(Integer.class)
                                                        .description("The number of Unit Category Records successfully deleted"))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while deleting the Unit Category Records")
                                                        .implementation(String.class)))
                        .build()));                        
    }

}

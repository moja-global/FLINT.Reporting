/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.tasks.configurations;

import global.moja.tasks.handlers.TasksHandler;
import global.moja.tasks.models.Task;
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
import static org.springframework.http.MediaType.APPLICATION_NDJSON;
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
    RouterFunction<ServerResponse> routeRequests(TasksHandler handler) {

        return
                route()
                        .POST("/api/v1/tasks",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::createTask,
                                ops -> ops
                                        .tag("Create")
                                        .operationId("createTask")
                                        .beanClass(TasksHandler.class)
                                        .beanMethod("createTask")
                                        .description("Inserts a single Task Record into the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .schema(schemaBuilder()
                                                                        .implementation(Task.class))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("201").description("The Task Record was successfully created")
                                                        .implementation(Task.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while creating the Task Record")
                                                        .implementation(String.class)))
                        .build()

                .and(route()
                        .POST("/api/v1/tasks/all",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::createTasks,
                                ops -> ops
                                        .tag("Create")
                                        .operationId("createTasks")
                                        .beanClass(TasksHandler.class)
                                        .beanMethod("createTasks")
                                        .description("Inserts several Tasks Records into the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .array(arraySchemaBuilder()
                                                                        .schema(schemaBuilder()
                                                                                .implementation(Task.class)))))

                                        .response(
                                                responseBuilder()
                                                        .responseCode("201").description("The Tasks Records were successfully created")
                                                        .implementationArray(Task.class))

                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while creating the Tasks Records")
                                                        .implementation(String.class)))
                        .build()

                .and(route()
                        .GET("/api/v1/tasks/ids/{id}",
                                accept(APPLICATION_JSON),
                                handler::retrieveTask,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrieveTask")
                                        .beanClass(TasksHandler.class)
                                        .beanMethod("retrieveTask")
                                        .description("Retrieves a single Task Record from the database given its unique identifier")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("id").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the Task Record")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Task Record was successfully retrieved")
                                                        .implementation(Task.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Task Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .GET("/api/v1/tasks/all",
                                accept(APPLICATION_JSON),
                                handler::retrieveTasks,
                                ops -> ops
                                        .tag("Retrieve")
                                        .operationId("retrieveTasks")
                                        .beanClass(TasksHandler.class)
                                        .beanMethod("retrieveTasks")
                                        .description("Retrieves all or some of the Tasks Records from the database depending on whether or not query parameters were included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("ids").in(ParameterIn.QUERY)
                                                        .description("The list of unique identifiers of the Tasks Records to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("taskTypeId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Task Type to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("taskStatusId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Task Status to filter the returned values by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("databaseId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Database to filter the returned values by")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Tasks Records were successfully retrieved")
                                                        .implementationArray(Task.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while retrieving the Tasks Records")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .PUT("/api/v1/tasks",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::updateTask,
                                ops -> ops
                                        .tag("Update")
                                        .operationId("updateTask")
                                        .beanClass(TasksHandler.class)
                                        .beanMethod("updateTask")
                                        .description("Updates a single Task Record in the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .schema(schemaBuilder()
                                                                        .implementation(Task.class))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Task Record was successfully updated")
                                                        .implementation(Task.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while updating the Task Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .PUT("/api/v1/tasks/all",
                                accept(APPLICATION_JSON).and(contentType(APPLICATION_JSON)),
                                handler::updateTasks,
                                ops -> ops
                                        .tag("Update")
                                        .operationId("updateTask")
                                        .beanClass(TasksHandler.class)
                                        .beanMethod("updateTasks")
                                        .description("Updates several Tasks Records in the database")
                                        .requestBody(
                                                requestBodyBuilder()
                                                        .content(contentBuilder()
                                                                .array(arraySchemaBuilder()
                                                                        .schema(schemaBuilder()
                                                                                .implementation(Task.class)))))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Tasks Records were successfully updated")
                                                        .implementation(Task.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while updating the Tasks Records")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .DELETE("/api/v1/tasks/ids/{id}",
                                accept(APPLICATION_JSON),
                                handler::deleteTask,
                                ops -> ops
                                        .tag("Delete")
                                        .operationId("deleteTask")
                                        .beanClass(TasksHandler.class)
                                        .beanMethod("deleteTask")
                                        .description("Deletes a single Task Record from the database given its unique identifier")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("id").in(ParameterIn.PATH)
                                                        .description("The unique identifier of the Task Record")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Task Record was successfully deleted")
                                                        .implementation(Integer.class)
                                                        .description("The number of Tasks Records successfully deleted"))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while deleting the Task Record")
                                                        .implementation(String.class)))
                        .build())

                .and(route()
                        .DELETE("/api/v1/tasks/all",
                                accept(APPLICATION_JSON),
                                handler::deleteTasks,
                                ops -> ops
                                        .tag("Delete")
                                        .operationId("deleteTasks")
                                        .beanClass(TasksHandler.class)
                                        .beanMethod("deleteTasks")
                                        .description("Deleted all or some of the Tasks Records from the database depending on whether or not query parameters were included in the query string")
                                        .parameter(
                                                parameterBuilder()
                                                        .name("ids").in(ParameterIn.QUERY)
                                                        .description("The list of unique identifiers of the Tasks Records to filter the records to be deleted by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("taskTypeId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Task Type to filter the records to be deleted by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("taskStatusId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Task Status to filter the records to be deleted by")
                                                        .implementation(Long.class))
                                        .parameter(
                                                parameterBuilder()
                                                        .name("databaseId").in(ParameterIn.QUERY)
                                                        .description("The unique identifier of the Database to filter the records to be deleted by")
                                                        .implementation(Long.class))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("200").description("The Tasks Records were successfully deleted")
                                                        .implementation(Integer.class)
                                                        .description("The number of Tasks Records successfully deleted"))
                                        .response(
                                                responseBuilder()
                                                        .responseCode("500").description("An unexpected condition was encountered while deleting the Tasks Records")
                                                        .implementation(String.class)))
                        .build())

                                .and(route()
                                        .GET("/api/v1/tasks/stream",
                                                accept(APPLICATION_NDJSON),
                                                handler::streamTasks,
                                                ops -> ops
                                                        .tag("Stream")
                                                        .operationId("streamTasks")
                                                        .beanClass(TasksHandler.class)
                                                        .beanMethod("streamTasks")
                                                        .description("Streams all, then, task updates only")
                                                        .response(
                                                                responseBuilder()
                                                                        .responseCode("200").description("The Tasks Records are being successfully streamed")
                                                                        .implementationArray(Task.class))
                                                        .response(
                                                                responseBuilder()
                                                                        .responseCode("500").description("An unexpected condition was encountered while streaming the Tasks Records")
                                                                        .implementation(String.class)))
                                        .build())
                );
    }

}

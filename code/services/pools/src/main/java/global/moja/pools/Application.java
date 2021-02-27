
/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.pools;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    @Bean
    public OpenAPI customOpenAPI(
            @Value("${application-description}") String appDescription,
            @Value("${application-version}") String appVersion) {

        return
                new OpenAPI()
                        .info(
                                new Info()
                                        .title("Moja Reporting Tool API")
                                        .version(appVersion)
                                        .description(appDescription)
                                        .license(
                                                new License()
                                                        .name("Mozilla Public License Version 2.0")
                                                        .url("http://mozilla.org/MPL/2.0/")));
    }
}

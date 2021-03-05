/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.dates.util.webclient.impl;



import global.moja.dates.configurations.HostsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
public class WebClientUtil {


    // See:
    // https://stackoverflow.com/questions/49095366/right-way-to-use-spring-webclient-in-multi-thread-environment
    private static WebClient databasesWebClient;

    @Autowired
    private HostsConfig hosts;


    public WebClient getDatabasesWebClient() {

        if (databasesWebClient == null) {
            databasesWebClient =
                    WebClient.builder().baseUrl("http://" + hosts.getDatabasesServiceHost()).build();
        }

        return databasesWebClient;
    }

}

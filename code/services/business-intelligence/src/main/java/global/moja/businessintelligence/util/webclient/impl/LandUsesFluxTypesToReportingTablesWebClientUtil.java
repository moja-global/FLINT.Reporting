/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.businessintelligence.util.webclient.impl;

import global.moja.businessintelligence.configurations.HostsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
@Component
public class LandUsesFluxTypesWebClientUtil {

    // See:
    // https://stackoverflow.com/questions/49095366/right-way-to-use-spring-webclient-in-multi-thread-environment
    private static WebClient landUsesFluxesWebClient;

    @Autowired
    private HostsConfig hosts;

    public WebClient getLandUsesFluxesWebClient() {

        if (landUsesFluxesWebClient == null) {
            landUsesFluxesWebClient =
                    WebClient
                            .builder()
                            .baseUrl("http://" + hosts.getLandUsesFluxTypesServiceHost())
                            .build();
        }

        return landUsesFluxesWebClient;
    }

}

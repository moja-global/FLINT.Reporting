/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.taskmanager.util.webclient.impl;

import global.moja.taskmanager.configurations.HostsConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
@Component
@Slf4j
public class AccountabilityRulesWebClientUtil {

    // See:
    // https://stackoverflow.com/questions/49095366/right-way-to-use-spring-webclient-in-multi-thread-environment
    private static WebClient accountabilityRulesWebClient;

    @Autowired
    HostsConfig hosts;

    public WebClient getAccountabilityRulesWebClient() {

        if (accountabilityRulesWebClient == null) {
            accountabilityRulesWebClient =
                    WebClient
                            .builder()
                            .baseUrl("http://" + hosts.getAccountabilityRulesServiceHost())
                            .build();
        }

        return accountabilityRulesWebClient;
    }

}

/*
 * Copyright (C) 2020 The Second Mile
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.dataprocessing.util.webclient.impl;

import global.moja.dataprocessing.configurations.HostsConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class QuantityObservationsWebClientUtil {

  // See:
  // https://stackoverflow.com/questions/49095366/right-way-to-use-spring-webclient-in-multi-thread-environment
  private static WebClient quantityObservationsWebClient;

  @Autowired
  HostsConfig hosts;

  public WebClient getQuantityObservationsWebClient() {

    if (quantityObservationsWebClient == null) {
      quantityObservationsWebClient =
              WebClient
                      .builder()
                      .baseUrl("http://" + hosts.getQuantityObservationsServiceHost())
                      .build();
    }

    return quantityObservationsWebClient;
  }

}

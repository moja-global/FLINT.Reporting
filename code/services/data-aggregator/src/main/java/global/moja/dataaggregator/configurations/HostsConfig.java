
/*
 * Copyright (C) 2020 The Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.dataaggregator.configurations;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Configuration
@PropertySource(value = {"classpath:hosts.properties"})
@Getter
public class HostsConfig {

    @Value("${accountabilities.service.host}/api/v1/accountabilities")
    private String accountabilitiesServiceHost;

    @Value("${quantity.observations.service.host}/api/v1/quantity_observations")
    private String quantityObservationsServiceHost;

}

/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package global.moja.dataprocessor.util.endpoints;

import global.moja.dataprocessor.models.LandUseCategory;
import global.moja.dataprocessor.util.webclient.impl.WebClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
@Component
@Slf4j
public class LandUseCategoriesEndpointsUtil {

    @Autowired
    WebClientUtil webClientUtil;

    public Flux<LandUseCategory> retrieveLandUseCategories(){
        return webClientUtil
                .getLandUseCategoriesWebClient()
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/all")
                                .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(LandUseCategory.class);
    }

}

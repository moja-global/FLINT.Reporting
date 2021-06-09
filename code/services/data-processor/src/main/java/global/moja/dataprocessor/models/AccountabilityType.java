/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.dataprocessor.models;

import lombok.*;

/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Data
@Builder
public class AccountabilityType implements Comparable<AccountabilityType> {

    private Long id;
    private String name;
    private Integer version;

    @Override
    public int compareTo(AccountabilityType accountabilityType) {

        if(this.id != null && accountabilityType.getId() != null){
            return this.id.compareTo(accountabilityType.getId());
        } else {
            return 0;
        }

    }
}

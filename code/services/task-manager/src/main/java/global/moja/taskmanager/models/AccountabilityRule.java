/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.taskmanager.models;

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
@EqualsAndHashCode
public class AccountabilityRule implements Comparable<AccountabilityRule> {

    private Long id;
    private Long accountabilityTypeId;
    private Long parentPartyTypeId;
    private Long subsidiaryPartyTypeId;
    private Integer version;

    @Override
    public int compareTo(AccountabilityRule o) {

        if(!this.accountabilityTypeId.equals(o.getAccountabilityTypeId())) {
            return this.accountabilityTypeId.compareTo(o.getAccountabilityTypeId());
        } else {
            if(this.parentPartyTypeId == null && o.getParentPartyTypeId() == null) {
                return this.id.compareTo(o.getId());
            } else if(this.parentPartyTypeId == null) {
                return 1;
            } else if(o.getParentPartyTypeId() == null) {
                return -1;
            } else {
                if(this.parentPartyTypeId.equals(o.getId())) {
                    return 1;
                } else if(this.id.equals(o.getParentPartyTypeId())) {
                    return -1;
                } else if(!this.parentPartyTypeId.equals(o.getParentPartyTypeId())) {
                    return this.parentPartyTypeId.compareTo(o.getParentPartyTypeId());
                } else {
                    return this.id.compareTo(o.getId());
                }
            }
        }
    }

    @Override
    public String toString() {
        return
                String.format(
                        "Id: %d, " +
                        "Accountability Type: %d, " +
                        "Parent Party Type: %d, " +
                        "Subsidiary Party Type: %d, " +
                        "Version: %d ",
                        id, accountabilityTypeId, parentPartyTypeId, subsidiaryPartyTypeId, version);
    }
}

/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.accountabilities.models;

import lombok.*;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Data
@Builder
public class Accountability implements Comparable<Accountability> {

    private Long id;
    private Long accountabilityTypeId;
    private Long accountabilityRuleId;
    private Long parentPartyId;
    private Long subsidiaryPartyId;
    private Integer version;

    @Override
    public int compareTo(Accountability o) {

        if (!this.accountabilityTypeId.equals(o.getAccountabilityTypeId())) {
            return this.accountabilityTypeId.compareTo(o.getAccountabilityTypeId());
        } else {
            if (!this.accountabilityRuleId.equals(o.getAccountabilityRuleId())) {
                return this.accountabilityRuleId.compareTo(o.getAccountabilityRuleId());
            } else {
                if (this.parentPartyId == null && o.getParentPartyId() == null) {
                    return this.id.compareTo(o.getId());
                } else if (this.parentPartyId == null) {
                    return 1;
                } else if (o.getParentPartyId() == null) {
                    return -1;
                } else {
                    if (this.parentPartyId.equals(o.getId())) {
                        return 1;
                    } else if (this.id.equals(o.getParentPartyId())) {
                        return -1;
                    } else if (!this.parentPartyId.equals(o.getParentPartyId())) {
                        return this.parentPartyId.compareTo(o.getParentPartyId());
                    } else if (!this.subsidiaryPartyId.equals(o.getSubsidiaryPartyId())) {
                        return this.subsidiaryPartyId.compareTo(o.getSubsidiaryPartyId());
                    } else {
                        return this.id.compareTo(o.getId());
                    }
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
                        "Accountability Rule: %d, " +
                        "Parent Party: %d, " +
                        "Subsidiary Party: %d, " +
                        "Version: %d ",
                        id, accountabilityTypeId, accountabilityRuleId, parentPartyId, subsidiaryPartyId, version);
    }
}

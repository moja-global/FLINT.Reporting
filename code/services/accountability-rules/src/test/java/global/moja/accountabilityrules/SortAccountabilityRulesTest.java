/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.accountabilityrules;

import global.moja.accountabilityrules.models.AccountabilityRule;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 1.0
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SortAccountabilityRulesTest {



    @Test
    public void Given_AccountabilityRules_When_Sort_Then_HierarchicallySortedAccountabilityRulesWillBeReturned() {

        AccountabilityRule rule1 =
                AccountabilityRule
                        .builder()
                        .id(1L)
                        .accountabilityTypeId(1L)
                        .parentPartyTypeId(0L)
                        .subsidiaryPartyTypeId(1L)
                        .build();

        AccountabilityRule rule2 =
                AccountabilityRule
                        .builder()
                        .id(1L)
                        .accountabilityTypeId(2L)
                        .parentPartyTypeId(0L)
                        .subsidiaryPartyTypeId(1L)
                        .build();

        AccountabilityRule rule3 =
                AccountabilityRule
                        .builder()
                        .id(2L)
                        .accountabilityTypeId(1L)
                        .parentPartyTypeId(1L)
                        .subsidiaryPartyTypeId(2L)
                        .build();

        AccountabilityRule rule4 =
                AccountabilityRule
                        .builder()
                        .id(3L)
                        .accountabilityTypeId(1L)
                        .parentPartyTypeId(2L)
                        .subsidiaryPartyTypeId(3L)
                        .build();

        AccountabilityRule rule5 =
                AccountabilityRule
                        .builder()
                        .id(4L)
                        .accountabilityTypeId(1L)
                        .parentPartyTypeId(3L)
                        .subsidiaryPartyTypeId(4L)
                        .build();


        List<AccountabilityRule> accountabilityRules = Arrays.asList(rule4, rule5,rule1,rule3,rule2);
        Collections.sort(accountabilityRules);

        List<AccountabilityRule> expected = Arrays.asList(rule1,rule3,rule4, rule5,rule2);

        Assertions.assertThat(accountabilityRules).isEqualTo(expected);
    }
}

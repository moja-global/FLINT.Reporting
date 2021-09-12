
import { AccountabilitiesRulesRecordsCreationModalComponent, AccountabilitiesRulesRecordsDeletionModalComponent } from "./modals";
import { AccountabilitiesRulesRecordsTabulationPageComponent } from "./accountabilities-rules-records-tabulation-page/accountabilities-rules-records-tabulation-page.component";
import { AccountabilitiesRulesRecordsHomePageComponent } from "./accountabilities-rules-records-home-page/accountabilities-rules-records-home-page.component";
import { AccountabilitiesRulesRecordsSelectionPageComponent } from "./accountabilities-rules-records-selection-page/accountabilities-rules-records-selection-page.component";

export const containers = [
    AccountabilitiesRulesRecordsCreationModalComponent,
    AccountabilitiesRulesRecordsDeletionModalComponent,
    AccountabilitiesRulesRecordsHomePageComponent,
    AccountabilitiesRulesRecordsSelectionPageComponent,
    AccountabilitiesRulesRecordsTabulationPageComponent
];

export * from "./accountabilities-rules-records-tabulation-page/accountabilities-rules-records-tabulation-page.component";
export * from "./accountabilities-rules-records-home-page/accountabilities-rules-records-home-page.component";
export * from "./accountabilities-rules-records-selection-page/accountabilities-rules-records-selection-page.component";
export * from "./modals";
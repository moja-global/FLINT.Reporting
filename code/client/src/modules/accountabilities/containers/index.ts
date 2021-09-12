import { AccountabilitiesRecordsTabulationPageComponent } from "./accountabilities-records-tabulation-page/accountabilities-records-tabulation-page.component";
import { AccountabilitiesRecordsCreationModalComponent, AccountabilitiesRecordsDeletionModalComponent } from "./modals";
import { AccountabilitiesRecordsSelectionPageComponent } from "./accountabilities-records-selection-page/accountabilities-records-selection-page.component";
import { AccountabilitiesRecordsHomePageComponent } from "./accountabilities-records-home-page/accountabilities-records-home-page.component";

export const containers = [
    AccountabilitiesRecordsCreationModalComponent,
    AccountabilitiesRecordsDeletionModalComponent,
    AccountabilitiesRecordsSelectionPageComponent,
    AccountabilitiesRecordsHomePageComponent,
    AccountabilitiesRecordsTabulationPageComponent];

export * from "./accountabilities-records-tabulation-page/accountabilities-records-tabulation-page.component";
export * from "./accountabilities-records-selection-page/accountabilities-records-selection-page.component";
export * from "./accountabilities-records-home-page/accountabilities-records-home-page.component";
export * from "./modals";


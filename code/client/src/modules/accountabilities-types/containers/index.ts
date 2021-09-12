import { AccountabilitiesTypesRecordsCreationModalComponent, AccountabilitiesTypesRecordsUpdationModalComponent, AccountabilitiesTypesRecordsDeletionModalComponent } from "./modals";
import { AccountabilitiesTypesRecordsSelectionPageComponent } from "./accountabilities-types-records-selection-page/accountabilities-types-records-selection-page.component";
import { AccountabilitiesTypesRecordsTabulationPageComponent } from "./accountabilities-types-records-tabulation-page/accountabilities-types-records-tabulation-page.component";
import { AccountabilitiesTypesRecordsHomePageComponent } from "./accountabilities-types-records-home-page/accountabilities-types-records-home-page.component";

export const containers = [
    AccountabilitiesTypesRecordsCreationModalComponent,
    AccountabilitiesTypesRecordsDeletionModalComponent,
    AccountabilitiesTypesRecordsSelectionPageComponent, 
    AccountabilitiesTypesRecordsHomePageComponent,
    AccountabilitiesTypesRecordsTabulationPageComponent,   
    AccountabilitiesTypesRecordsUpdationModalComponent];


export * from "./accountabilities-types-records-selection-page/accountabilities-types-records-selection-page.component";
export * from "./accountabilities-types-records-tabulation-page/accountabilities-types-records-tabulation-page.component";
export * from "./accountabilities-types-records-home-page/accountabilities-types-records-home-page.component";
export * from "./modals";


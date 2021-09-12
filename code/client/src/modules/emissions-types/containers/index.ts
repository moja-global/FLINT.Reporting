import { EmissionsTypesRecordsCreationModalComponent, EmissionsTypesRecordsUpdationModalComponent, EmissionsTypesRecordsDeletionModalComponent } from "./modals";
import { EmissionsTypesRecordsSelectionPageComponent } from "./emissions-types-records-selection-page/emissions-types-records-selection-page.component";
import { EmissionsTypesRecordsTabulationPageComponent } from "./emissions-types-records-tabulation-page/emissions-types-records-tabulation-page.component";
import { EmissionsTypesRecordsHomePageComponent } from "./emissions-types-records-home-page/emissions-types-records-home-page.component";

export const containers = [
    EmissionsTypesRecordsCreationModalComponent,
    EmissionsTypesRecordsDeletionModalComponent,
    EmissionsTypesRecordsSelectionPageComponent, 
    EmissionsTypesRecordsHomePageComponent,
    EmissionsTypesRecordsTabulationPageComponent,   
    EmissionsTypesRecordsUpdationModalComponent];


export * from "./emissions-types-records-selection-page/emissions-types-records-selection-page.component";
export * from "./emissions-types-records-tabulation-page/emissions-types-records-tabulation-page.component";
export * from "./emissions-types-records-home-page/emissions-types-records-home-page.component";
export * from "./modals";


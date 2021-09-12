import { PartiesTypesRecordsCreationModalComponent, PartiesTypesRecordsUpdationModalComponent, PartiesTypesRecordsDeletionModalComponent } from "./modals";
import { PartiesTypesRecordsSelectionPageComponent } from "./parties-types-records-selection-page/parties-types-records-selection-page.component";
import { PartiesTypesRecordsTabulationPageComponent } from "./parties-types-records-tabulation-page/parties-types-records-tabulation-page.component";
import { PartiesTypesRecordsHomePageComponent } from "./parties-types-records-home-page/parties-types-records-home-page.component";

export const containers = [
    PartiesTypesRecordsCreationModalComponent,
    PartiesTypesRecordsDeletionModalComponent,
    PartiesTypesRecordsSelectionPageComponent, 
    PartiesTypesRecordsHomePageComponent,
    PartiesTypesRecordsTabulationPageComponent,   
    PartiesTypesRecordsUpdationModalComponent];


export * from "./parties-types-records-selection-page/parties-types-records-selection-page.component";
export * from "./parties-types-records-tabulation-page/parties-types-records-tabulation-page.component";
export * from "./parties-types-records-home-page/parties-types-records-home-page.component";
export * from "./modals";


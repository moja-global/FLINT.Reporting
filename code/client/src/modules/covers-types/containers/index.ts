import { CoversTypesRecordsCreationModalComponent, CoversTypesRecordsUpdationModalComponent, CoversTypesRecordsDeletionModalComponent } from "./modals";
import { CoversTypesRecordsSelectionPageComponent } from "./covers-types-records-selection-page/covers-types-records-selection-page.component";
import { CoversTypesRecordsTabulationPageComponent } from "./covers-types-records-tabulation-page/covers-types-records-tabulation-page.component";
import { CoversTypesRecordsHomePageComponent } from "./covers-types-records-home-page/covers-types-records-home-page.component";

export const containers = [
    CoversTypesRecordsCreationModalComponent,
    CoversTypesRecordsDeletionModalComponent,
    CoversTypesRecordsSelectionPageComponent, 
    CoversTypesRecordsHomePageComponent,
    CoversTypesRecordsTabulationPageComponent,   
    CoversTypesRecordsUpdationModalComponent];


export * from "./covers-types-records-selection-page/covers-types-records-selection-page.component";
export * from "./covers-types-records-tabulation-page/covers-types-records-tabulation-page.component";
export * from "./covers-types-records-home-page/covers-types-records-home-page.component";
export * from "./modals";


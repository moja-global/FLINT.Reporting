
import { PartiesRecordsCreationModalComponent, PartiesRecordsUpdationModalComponent, PartiesRecordsDeletionModalComponent } from "./modals";
import { PartiesRecordsSelectionPageComponent } from "./parties-records-selection-page/parties-records-selection-page.component";
import { PartiesRecordsTabulationPageComponent } from "./parties-records-tabulation-page/parties-records-tabulation-page.component";
import { PartiesRecordsHomePageComponent } from "./parties-records-home-page/parties-records-home-page.component";

export const containers = [
    PartiesRecordsCreationModalComponent,
    PartiesRecordsDeletionModalComponent,
    PartiesRecordsSelectionPageComponent, 
    PartiesRecordsHomePageComponent,
    PartiesRecordsTabulationPageComponent,  
    PartiesRecordsUpdationModalComponent];

export * from "./parties-records-selection-page/parties-records-selection-page.component";
export * from "./parties-records-tabulation-page/parties-records-tabulation-page.component";
export * from "./parties-records-home-page/parties-records-home-page.component";
export * from "./modals";


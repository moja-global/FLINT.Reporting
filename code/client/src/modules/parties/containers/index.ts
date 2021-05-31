import { PartiesRecordsTabulationPageComponent } from "./parties-records-tabulation-page/parties-records-tabulation-page.component";
import { PartiesRecordsCreationModalComponent, PartiesRecordsUpdationModalComponent, PartiesRecordsDeletionModalComponent } from "./modals";

export const containers = [
    PartiesRecordsCreationModalComponent,
    PartiesRecordsDeletionModalComponent,
    PartiesRecordsTabulationPageComponent,    
    PartiesRecordsUpdationModalComponent];

export * from "./parties-records-tabulation-page/parties-records-tabulation-page.component";
export * from "./modals";


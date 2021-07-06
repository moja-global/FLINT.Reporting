import { DatabasesRecordsTabulationPageComponent } from "./databases-records-tabulation-page/databases-records-tabulation-page.component";
import { DatabasesRecordsCreationModalComponent, DatabasesRecordsUpdationModalComponent, DatabasesRecordsDeletionModalComponent } from "./modals";
import { DatabasesRecordsHomePageComponent } from "./databases-records-home-page/databases-records-home-page.component";

export const containers = [
    DatabasesRecordsCreationModalComponent,
    DatabasesRecordsDeletionModalComponent,
    DatabasesRecordsHomePageComponent, 
    DatabasesRecordsTabulationPageComponent,    
    DatabasesRecordsUpdationModalComponent];

export * from  "./databases-records-home-page/databases-records-home-page.component";
export * from "./databases-records-tabulation-page/databases-records-tabulation-page.component";
export * from "./modals";


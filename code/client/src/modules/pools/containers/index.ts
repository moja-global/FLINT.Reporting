import { PoolsRecordsCreationModalComponent, PoolsRecordsUpdationModalComponent, PoolsRecordsDeletionModalComponent } from "./modals";
import { PoolsRecordsSelectionPageComponent } from "./pools-records-selection-page/pools-records-selection-page.component";
import { PoolsRecordsTabulationPageComponent } from "./pools-records-tabulation-page/pools-records-tabulation-page.component";
import { PoolsRecordsHomePageComponent } from "./pools-records-home-page/pools-records-home-page.component";

export const containers = [
    PoolsRecordsCreationModalComponent,
    PoolsRecordsDeletionModalComponent,
    PoolsRecordsSelectionPageComponent, 
    PoolsRecordsHomePageComponent,
    PoolsRecordsTabulationPageComponent,   
    PoolsRecordsUpdationModalComponent];


export * from "./pools-records-selection-page/pools-records-selection-page.component";
export * from "./pools-records-tabulation-page/pools-records-tabulation-page.component";
export * from "./pools-records-home-page/pools-records-home-page.component";
export * from "./modals";


import { PoolsRecordsTabulationPageComponent } from "./pools-records-tabulation-page/pools-records-tabulation-page.component";
import { PoolsRecordsCreationModalComponent, PoolsRecordsUpdationModalComponent, PoolsRecordsDeletionModalComponent } from "./modals";

export const containers = [
    PoolsRecordsCreationModalComponent,
    PoolsRecordsDeletionModalComponent,
    PoolsRecordsTabulationPageComponent,    
    PoolsRecordsUpdationModalComponent];

export * from "./pools-records-tabulation-page/pools-records-tabulation-page.component";
export * from "./modals";


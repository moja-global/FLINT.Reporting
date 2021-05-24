import { UnitsRecordsTabulationPageComponent } from "./units-records-tabulation-page/units-records-tabulation-page.component";
import { UnitsRecordsCreationModalComponent, UnitsRecordsUpdationModalComponent, UnitsRecordsDeletionModalComponent } from "./modals";

export const containers = [
    UnitsRecordsCreationModalComponent,
    UnitsRecordsDeletionModalComponent,
    UnitsRecordsTabulationPageComponent,    
    UnitsRecordsUpdationModalComponent];

export * from "./units-records-tabulation-page/units-records-tabulation-page.component";
export * from "./modals";


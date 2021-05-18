import { CoverTypesRecordsTabulationPageComponent } from "./cover-types-records-tabulation-page/cover-types-records-tabulation-page.component";
import { CoverTypesRecordsCreationModalComponent, CoverTypesRecordsUpdationModalComponent, CoverTypesRecordsDeletionModalComponent } from "./modals";

export const containers = [
    CoverTypesRecordsCreationModalComponent,
    CoverTypesRecordsDeletionModalComponent,
    CoverTypesRecordsTabulationPageComponent,    
    CoverTypesRecordsUpdationModalComponent];

export * from "./cover-types-records-tabulation-page/cover-types-records-tabulation-page.component";
export * from "./modals";


import { EmissionTypesRecordsTabulationPageComponent } from "./emission-types-records-tabulation-page/emission-types-records-tabulation-page.component";
import { EmissionTypesRecordsCreationModalComponent, EmissionTypesRecordsUpdationModalComponent, EmissionTypesRecordsDeletionModalComponent } from "./modals";

export const containers = [
    EmissionTypesRecordsCreationModalComponent,
    EmissionTypesRecordsDeletionModalComponent,
    EmissionTypesRecordsTabulationPageComponent,    
    EmissionTypesRecordsUpdationModalComponent];

export * from "./emission-types-records-tabulation-page/emission-types-records-tabulation-page.component";
export * from "./modals";


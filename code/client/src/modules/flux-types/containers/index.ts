import { FluxTypesRecordsTabulationPageComponent } from "./flux-types-records-tabulation-page/flux-types-records-tabulation-page.component";
import { FluxTypesRecordsCreationModalComponent, FluxTypesRecordsUpdationModalComponent, FluxTypesRecordsDeletionModalComponent } from "./modals";

export const containers = [
    FluxTypesRecordsCreationModalComponent,
    FluxTypesRecordsDeletionModalComponent,
    FluxTypesRecordsTabulationPageComponent,    
    FluxTypesRecordsUpdationModalComponent];

export * from "./flux-types-records-tabulation-page/flux-types-records-tabulation-page.component";
export * from "./modals";


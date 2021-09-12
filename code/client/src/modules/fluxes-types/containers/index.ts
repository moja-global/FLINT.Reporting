import { FluxesTypesRecordsCreationModalComponent, FluxesTypesRecordsUpdationModalComponent, FluxesTypesRecordsDeletionModalComponent } from "./modals";
import { FluxesTypesRecordsSelectionPageComponent } from "./fluxes-types-records-selection-page/fluxes-types-records-selection-page.component";
import { FluxesTypesRecordsTabulationPageComponent } from "./fluxes-types-records-tabulation-page/fluxes-types-records-tabulation-page.component";
import { FluxesTypesRecordsHomePageComponent } from "./fluxes-types-records-home-page/fluxes-types-records-home-page.component";

export const containers = [
    FluxesTypesRecordsCreationModalComponent,
    FluxesTypesRecordsDeletionModalComponent,
    FluxesTypesRecordsSelectionPageComponent, 
    FluxesTypesRecordsHomePageComponent,
    FluxesTypesRecordsTabulationPageComponent,   
    FluxesTypesRecordsUpdationModalComponent];


export * from "./fluxes-types-records-selection-page/fluxes-types-records-selection-page.component";
export * from "./fluxes-types-records-tabulation-page/fluxes-types-records-tabulation-page.component";
export * from "./fluxes-types-records-home-page/fluxes-types-records-home-page.component";
export * from "./modals";


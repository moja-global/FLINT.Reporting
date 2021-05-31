import { PartyTypesRecordsTabulationPageComponent } from "./party-types-records-tabulation-page/party-types-records-tabulation-page.component";
import { PartyTypesRecordsCreationModalComponent, PartyTypesRecordsUpdationModalComponent, PartyTypesRecordsDeletionModalComponent } from "./modals";

export const containers = [
    PartyTypesRecordsCreationModalComponent,
    PartyTypesRecordsDeletionModalComponent,
    PartyTypesRecordsTabulationPageComponent,    
    PartyTypesRecordsUpdationModalComponent];

export * from "./party-types-records-tabulation-page/party-types-records-tabulation-page.component";
export * from "./modals";


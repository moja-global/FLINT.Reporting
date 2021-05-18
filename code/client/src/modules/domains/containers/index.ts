import { DomainsRecordsTabulationPageComponent } from "./domains-records-tabulation-page/domains-records-tabulation-page.component";
import { DomainsRecordsCreationModalComponent, DomainsRecordsDeletionModalComponent, DomainsRecordsUpdationModalComponent } from "./modals";

export const containers = [
    DomainsRecordsCreationModalComponent,
    DomainsRecordsDeletionModalComponent,
    DomainsRecordsTabulationPageComponent,    
    DomainsRecordsUpdationModalComponent];

export * from "./domains-records-tabulation-page/domains-records-tabulation-page.component";
export * from "./modals";


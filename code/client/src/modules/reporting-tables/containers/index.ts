import { ReportingTablesRecordsTabulationPageComponent } from "./reporting-tables-records-tabulation-page/reporting-tables-records-tabulation-page.component";
import { ReportingTablesRecordsCreationModalComponent, ReportingTablesRecordsUpdationModalComponent, ReportingTablesRecordsDeletionModalComponent } from "./modals";

export const containers = [
    ReportingTablesRecordsCreationModalComponent,
    ReportingTablesRecordsDeletionModalComponent,
    ReportingTablesRecordsTabulationPageComponent,    
    ReportingTablesRecordsUpdationModalComponent];

export * from "./reporting-tables-records-tabulation-page/reporting-tables-records-tabulation-page.component";
export * from "./modals";


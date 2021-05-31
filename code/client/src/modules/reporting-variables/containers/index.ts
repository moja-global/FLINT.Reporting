import { ReportingVariablesRecordsTabulationPageComponent } from "./reporting-variables-records-tabulation-page/reporting-variables-records-tabulation-page.component";
import { ReportingVariablesRecordsCreationModalComponent, ReportingVariablesRecordsUpdationModalComponent, ReportingVariablesRecordsDeletionModalComponent } from "./modals";

export const containers = [
    ReportingVariablesRecordsCreationModalComponent,
    ReportingVariablesRecordsDeletionModalComponent,
    ReportingVariablesRecordsTabulationPageComponent,    
    ReportingVariablesRecordsUpdationModalComponent];

export * from "./reporting-variables-records-tabulation-page/reporting-variables-records-tabulation-page.component";
export * from "./modals";


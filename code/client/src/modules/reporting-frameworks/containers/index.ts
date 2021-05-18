import { ReportingFrameworksRecordsTabulationPageComponent } from "./reporting-frameworks-records-tabulation-page/reporting-frameworks-records-tabulation-page.component";
import { ReportingFrameworksRecordsCreationModalComponent, ReportingFrameworksRecordsUpdationModalComponent, ReportingFrameworksRecordsDeletionModalComponent } from "./modals";

export const containers = [
    ReportingFrameworksRecordsCreationModalComponent,
    ReportingFrameworksRecordsDeletionModalComponent,
    ReportingFrameworksRecordsTabulationPageComponent,    
    ReportingFrameworksRecordsUpdationModalComponent];

export * from "./reporting-frameworks-records-tabulation-page/reporting-frameworks-records-tabulation-page.component";
export * from "./modals";


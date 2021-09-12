import { ReportingFrameworksRecordsCreationModalComponent, ReportingFrameworksRecordsUpdationModalComponent, ReportingFrameworksRecordsDeletionModalComponent } from "./modals";
import { ReportingFrameworksRecordsSelectionPageComponent } from "./reporting-frameworks-records-selection-page/reporting-frameworks-records-selection-page.component";
import { ReportingFrameworksRecordsTabulationPageComponent } from "./reporting-frameworks-records-tabulation-page/reporting-frameworks-records-tabulation-page.component";
import { ReportingFrameworksRecordsHomePageComponent } from "./reporting-frameworks-records-home-page/reporting-frameworks-records-home-page.component";

export const containers = [
    ReportingFrameworksRecordsCreationModalComponent,
    ReportingFrameworksRecordsDeletionModalComponent,
    ReportingFrameworksRecordsSelectionPageComponent, 
    ReportingFrameworksRecordsHomePageComponent,
    ReportingFrameworksRecordsTabulationPageComponent,   
    ReportingFrameworksRecordsUpdationModalComponent];


export * from "./reporting-frameworks-records-selection-page/reporting-frameworks-records-selection-page.component";
export * from "./reporting-frameworks-records-tabulation-page/reporting-frameworks-records-tabulation-page.component";
export * from "./reporting-frameworks-records-home-page/reporting-frameworks-records-home-page.component";
export * from "./modals";


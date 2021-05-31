import { LandUseCategoriesRecordsTabulationPageComponent } from "./land-use-categories-records-tabulation-page/land-use-categories-records-tabulation-page.component";
import { LandUseCategoriesRecordsCreationModalComponent, LandUseCategoriesRecordsUpdationModalComponent, LandUseCategoriesRecordsDeletionModalComponent } from "./modals";

export const containers = [
    LandUseCategoriesRecordsCreationModalComponent,
    LandUseCategoriesRecordsDeletionModalComponent,
    LandUseCategoriesRecordsTabulationPageComponent,    
    LandUseCategoriesRecordsUpdationModalComponent];

export * from "./land-use-categories-records-tabulation-page/land-use-categories-records-tabulation-page.component";
export * from "./modals";


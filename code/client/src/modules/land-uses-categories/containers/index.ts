
import { LandUsesCategoriesRecordsCreationModalComponent, LandUsesCategoriesRecordsUpdationModalComponent, LandUsesCategoriesRecordsDeletionModalComponent } from "./modals";
import { LandUsesCategoriesRecordsSelectionPageComponent } from "./land-uses-categories-records-selection-page/land-uses-categories-records-selection-page.component";
import { LandUsesCategoriesRecordsTabulationPageComponent } from "./land-uses-categories-records-tabulation-page/land-uses-categories-records-tabulation-page.component";
import { LandUsesCategoriesRecordsHomePageComponent } from "./land-uses-categories-records-home-page/land-uses-categories-records-home-page.component";

export const containers = [
    LandUsesCategoriesRecordsCreationModalComponent,
    LandUsesCategoriesRecordsDeletionModalComponent,
    LandUsesCategoriesRecordsSelectionPageComponent, 
    LandUsesCategoriesRecordsHomePageComponent,
    LandUsesCategoriesRecordsTabulationPageComponent,  
    LandUsesCategoriesRecordsUpdationModalComponent];

export * from "./land-uses-categories-records-selection-page/land-uses-categories-records-selection-page.component";
export * from "./land-uses-categories-records-tabulation-page/land-uses-categories-records-tabulation-page.component";
export * from "./land-uses-categories-records-home-page/land-uses-categories-records-home-page.component";
export * from "./modals";


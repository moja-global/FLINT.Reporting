import { UnitCategoriesRecordsTabulationPageComponent } from "./unit-categories-records-tabulation-page/unit-categories-records-tabulation-page.component";
import { UnitCategoriesRecordsCreationModalComponent, UnitCategoriesRecordsUpdationModalComponent, UnitCategoriesRecordsDeletionModalComponent } from "./modals";
import { UnitCategoriesRecordsHomePageComponent } from "./unit-categories-records-home-page/unit-categories-records-home-page.component";

export const containers = [
    UnitCategoriesRecordsCreationModalComponent,
    UnitCategoriesRecordsDeletionModalComponent,
    UnitCategoriesRecordsHomePageComponent, 
    UnitCategoriesRecordsTabulationPageComponent,    
    UnitCategoriesRecordsUpdationModalComponent];

export * from  "./unit-categories-records-home-page/unit-categories-records-home-page.component";
export * from "./unit-categories-records-tabulation-page/unit-categories-records-tabulation-page.component";
export * from "./modals";


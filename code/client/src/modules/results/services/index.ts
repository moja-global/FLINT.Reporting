import { ActivitiesDataService } from "./activities-data.service";
import { DomainsDataService } from "./domains-data.service";
import { PhenomenonTypesDataService } from "./phenomenon-types--data.service";
import { QuantityObservationsDataService } from "./quantity-observations-data.service";
import { UnitsDataService } from "./units-data.service";

export const services = [
    ActivitiesDataService,
    DomainsDataService,
    PhenomenonTypesDataService,
    QuantityObservationsDataService,
    UnitsDataService
];

export * from "./activities-data.service";
export * from "./domains-data.service";
export * from "./phenomenon-types--data.service";
export * from "./quantity-observations-data.service";
export * from "./units-data.service"; 



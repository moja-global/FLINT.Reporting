import { CompositionsDataService } from "./compositions-data.service";
import { PhenomenonTypesDataService } from "./phenomenon-types--data.service";
import { ProgressionsDataService } from "./progression-data.service";
import { QuantityObservationsDataService } from "./quantity-observations-data.service";
import { IssuesDataService } from "./issues-data.service";
import { UnitsDataService } from "./units-data.service";


export const services = [
    CompositionsDataService,
    PhenomenonTypesDataService,
    ProgressionsDataService,
    QuantityObservationsDataService,
    IssuesDataService,
    UnitsDataService
];

export * from "./compositions-data.service";
export * from "./phenomenon-types--data.service";
export * from "./progression-data.service";
export * from "./quantity-observations-data.service";
export * from "./issues-data.service";
export * from "./units-data.service";



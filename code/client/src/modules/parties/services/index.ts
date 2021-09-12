

import { ParentPartiesRecordsTabulationService } from './parent-parties-records-tabulation.service';
import { PartiesDataService } from './parties-data.service';
import { PartiesRecordsTabulationService } from './parties-records-tabulation.service';
import { SubsidiaryPartiesRecordsTabulationService } from './subsidiary-parties-records-tabulation.service';

export const services = [PartiesDataService, PartiesRecordsTabulationService, ParentPartiesRecordsTabulationService, SubsidiaryPartiesRecordsTabulationService];


export * from './parties-data.service';
export * from './parties-records-tabulation.service';
export * from './parent-parties-records-tabulation.service';
export * from './subsidiary-parties-records-tabulation.service';

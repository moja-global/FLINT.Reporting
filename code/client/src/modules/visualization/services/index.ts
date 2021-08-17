import { IndicatorsDataService } from './indicators-data.service';
import { OrganizationsDataService } from './organizations-data.service';
import { QuantityObservationsDataService } from './quantity-observations-data.service';
import { UnitsDataService } from './units-data.service';
import { VisualizationAxesDataService } from './visualization-axes-data.service';
import { VisualizationVariablesDataService } from './visualization-variables-data.service';
import { VisualizationsDataService } from './visualizations-data.service';

export const services = [
    IndicatorsDataService,
    OrganizationsDataService,
    QuantityObservationsDataService,
    UnitsDataService,
    VisualizationAxesDataService,
    VisualizationVariablesDataService,    
    VisualizationsDataService,
];

export * from './indicators-data.service';
export * from './organizations-data.service';
export * from './quantity-observations-data.service';
export * from './units-data.service';
export * from './visualization-axes-data.service';
export * from './visualization-variables-data.service';
export * from './visualizations-data.service';
import { ChartComponent } from './chart/chart.component';
import { CrfTableComponent } from './crf-table/crf-table.component';
import { DashboardCardsComponent } from './dashboard-cards/dashboard-cards.component';
import { DashboardChartsComponent } from './dashboard-charts/dashboard-charts.component';
import { DashboardTablesComponent } from './dashboard-tables/dashboard-tables.component';
import { TotalGHGEmissionsByGasesChartComponent } from './total-ghg-emissions-by-gases-chart/total-ghg-emissions-by-gases-chart.component';
import { TotalGHGEmissionsByPoolsChartComponent } from './total-ghg-emissions-by-pools-chart/total-ghg-emissions-by-pools-chart.component';
import { TotalGHGEmissionsChartComponent } from './total-ghg-emissions-chart/total-ghg-emissions-chart.component';

export const components = [
    ChartComponent,
    CrfTableComponent,
    DashboardCardsComponent,
    DashboardChartsComponent,
    DashboardTablesComponent,
    TotalGHGEmissionsChartComponent,
    TotalGHGEmissionsByPoolsChartComponent,
    TotalGHGEmissionsByGasesChartComponent
];


export * from './chart/chart.component';
export * from './dashboard-cards/dashboard-cards.component';
export * from './dashboard-charts/dashboard-charts.component';
export * from './dashboard-tables/dashboard-tables.component';
export * from './crf-table/crf-table.component';
export * from './total-ghg-emissions-by-pools-chart/total-ghg-emissions-by-pools-chart.component';
export * from './total-ghg-emissions-chart/total-ghg-emissions-chart.component';

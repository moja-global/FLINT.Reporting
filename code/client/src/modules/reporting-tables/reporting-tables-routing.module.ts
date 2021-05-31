/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

/* Module */
import { ReportingTablesModule } from './reporting-tables.module';

/* Containers */
import * as reportingTablesContainers from './containers';

/* Guards */
import * as reportingTablesGuards from './guards';
import { SBRouteData } from '@modules/navigation/models';

/* Routes */
export const ROUTES: Routes = [
    {
        path: ':reportingFrameworkId',
        canActivate: [],
        component: reportingTablesContainers.ReportingTablesRecordsTabulationPageComponent,
        data: {
            title: 'Reporting Tables',
            breadcrumbs: [
                {
                    text: 'Reporting Framework',
                    link: '/reporting_frameworks',
                    active: true
                }
            ],
        } as SBRouteData,
    },
];



@NgModule({
    imports: [ReportingTablesModule, RouterModule.forChild(ROUTES)],
    exports: [RouterModule],
})
export class ReportingTablesRoutingModule {}

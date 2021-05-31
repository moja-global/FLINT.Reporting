/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

/* Module */
import { ReportingVariablesModule } from './reporting-variables.module';

/* Containers */
import * as reportingVariablesContainers from './containers';

/* Guards */
import * as reportingVariablesGuards from './guards';
import { SBRouteData } from '@modules/navigation/models';

/* Routes */
export const ROUTES: Routes = [
    {
        path: ':reportingFrameworkId',
        canActivate: [],
        component: reportingVariablesContainers.ReportingVariablesRecordsTabulationPageComponent,
        data: {
            title: 'Reporting Variables',
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
    imports: [ReportingVariablesModule, RouterModule.forChild(ROUTES)],
    exports: [RouterModule],
})
export class ReportingVariablesRoutingModule {}

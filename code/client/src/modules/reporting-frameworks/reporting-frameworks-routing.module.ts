/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

/* Module */
import { ReportingFrameworksModule } from './reporting-frameworks.module';

/* Containers */
import * as reportingFrameworksContainers from './containers';

/* Guards */
import * as reportingFrameworksGuards from './guards';
import { SBRouteData } from '@modules/navigation/models';

/* Routes */
export const ROUTES: Routes = [ 
    {
        path: '',
        canActivate: [],
        component: reportingFrameworksContainers.ReportingFrameworksRecordsTabulationPageComponent,
        data: {
            title: 'ReportingFrameworks',
            breadcrumbs: [],
        } as SBRouteData,
    },
    {
        path: '**',
        pathMatch: 'full',
        loadChildren: () =>
            import('modules/error/error-routing.module').then(m => m.ErrorRoutingModule),
    }           
];



@NgModule({
    imports: [ReportingFrameworksModule, RouterModule.forChild(ROUTES)],
    exports: [RouterModule],
})
export class ReportingFrameworksRoutingModule {}

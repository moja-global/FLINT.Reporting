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
            title: 'Reporting Frameworks',
            breadcrumbs: [
                {
                    text: 'Configurations',
                    link: '',
                    active: false
                }, 
                {
                    text: 'Reporting Frameworks',
                    active: false
                }
            ],
        } as SBRouteData,
    },
    {
        path: 'home',
        canActivate: [],
        component: reportingFrameworksContainers.ReportingFrameworksRecordsHomePageComponent,
        data: {
            title: 'Reporting Framework Home',
            breadcrumbs: [
                {
                    text: 'Configurations',
                    link: '',
                    active: false
                },                
                {
                    text: 'Reporting Frameworks',
                    link: '/reporting_frameworks',
                    active: true
                },
                {
                    text: 'Home',
                    link: '/reporting_frameworks/home',
                    active: false
                }             
            ],
        } as SBRouteData,
    }        
];

@NgModule({
    imports: [ReportingFrameworksModule, RouterModule.forChild(ROUTES)],
    exports: [RouterModule],
})
export class ReportingFrameworksRoutingModule {}

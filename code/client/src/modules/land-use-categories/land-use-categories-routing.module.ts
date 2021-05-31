/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

/* Module */
import { LandUseCategoriesModule } from './land-use-categories.module';

/* Containers */
import * as landUseCategoriesContainers from './containers';

/* Guards */
import * as landUseCategoriesGuards from './guards';
import { SBRouteData } from '@modules/navigation/models';

/* Routes */
export const ROUTES: Routes = [
    {
        path: ':reportingFrameworkId',
        canActivate: [],
        component: landUseCategoriesContainers.LandUseCategoriesRecordsTabulationPageComponent,
        data: {
            title: 'Land Use Categories',
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
    imports: [LandUseCategoriesModule, RouterModule.forChild(ROUTES)],
    exports: [RouterModule],
})
export class LandUseCategoriesRoutingModule {}

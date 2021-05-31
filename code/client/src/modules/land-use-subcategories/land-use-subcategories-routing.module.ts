/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

/* Module */
import { LandUseSubcategoriesModule } from './land-use-subcategories.module';

/* Containers */
import * as landUseSubcategoriesContainers from './containers';

/* Guards */
import * as landUseSubcategoriesGuards from './guards';
import { SBRouteData } from '@modules/navigation/models';

/* Routes */
export const ROUTES: Routes = [
    {
        path: ':reportingFrameworkId',
        canActivate: [],
        component: landUseSubcategoriesContainers.LandUseSubcategoriesRecordsTabulationPageComponent,
        data: {
            title: 'Land Use Subcategories',
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
    imports: [LandUseSubcategoriesModule, RouterModule.forChild(ROUTES)],
    exports: [RouterModule],
})
export class LandUseSubcategoriesRoutingModule {}

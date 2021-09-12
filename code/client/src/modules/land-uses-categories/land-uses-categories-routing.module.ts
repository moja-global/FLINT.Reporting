/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

/* Modules */
import { LandUsesCategoriesModule } from './land-uses-categories.module';


/* Containers */
import * as landUsesCategoriesContainers from './containers';

/* Guards */
import { SBRouteData } from '@modules/navigation/models';



/* Routes */
export const ROUTES: Routes = [     
    {
        path: ':reportingFrameworkId',
        canActivate: [],
        component: landUsesCategoriesContainers.LandUsesCategoriesRecordsTabulationPageComponent,
        data: {
            title: 'Land Uses Categories',
            breadcrumbs: [],
        } as SBRouteData,
    },         
];



@NgModule({
    imports: [LandUsesCategoriesModule, RouterModule.forChild(ROUTES)],
    exports: [RouterModule],
})
export class LandUsesCategoriesRoutingModule {}

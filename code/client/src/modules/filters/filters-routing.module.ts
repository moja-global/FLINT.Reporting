/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

/* Module */
import { FiltersModule } from './filters.module';

/* Containers */
import * as filtersContainers from './containers';

/* Guards */
import * as filtersGuards from './guards';
import { SBRouteData } from '@modules/navigation/models';

/* Routes */
export const ROUTES: Routes = [ 
    {
        path: ':filterRuleId',
        canActivate: [],
        component: filtersContainers.FiltersRecordsTabulationPageComponent,
        data: {
            title: 'Filters',
            breadcrumbs: [
                {
                    text: 'Filters',
                    link: '/filters',
                    active: false
                }
            ],
        } as SBRouteData,
    }   
];

@NgModule({
    imports: [FiltersModule, RouterModule.forChild(ROUTES)],
    exports: [RouterModule],
})
export class FiltersRoutingModule {}

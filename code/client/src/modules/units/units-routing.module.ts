/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

/* Module */
import { UnitsModule } from './units.module';

/* Containers */
import * as unitsContainers from './containers';

/* Guards */
import * as unitsGuards from './guards';
import { SBRouteData } from '@modules/navigation/models';

/* Routes */
export const ROUTES: Routes = [
    {
        path: '',
        canActivate: [],
        component: unitsContainers.UnitsRecordsTabulationPageComponent,
        data: {
            title: 'Units',
            breadcrumbs: [
                {
                    text: 'Unit Categories',
                    link: '/unit_categories',
                    active: true
                }
            ],
        } as SBRouteData,
    },
];



@NgModule({
    imports: [UnitsModule, RouterModule.forChild(ROUTES)],
    exports: [RouterModule],
})
export class UnitsRoutingModule {}

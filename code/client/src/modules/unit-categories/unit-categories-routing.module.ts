/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

/* Module */
import { UnitCategoriesModule } from './unit-categories.module';

/* Containers */
import * as unitCategoriesContainers from './containers';

/* Guards */
import * as unitCategoriesGuards from './guards';
import { SBRouteData } from '@modules/navigation/models';

/* Routes */
export const ROUTES: Routes = [
    {
        path: '',
        canActivate: [],
        component: unitCategoriesContainers.UnitCategoriesRecordsTabulationPageComponent,
        data: {
            title: 'Unit Categories',
            breadcrumbs: [
                {
                    text: 'Configurations',
                    active: false
                },
                {
                    text: 'Unit Categories',
                    active: false
                }
            ],
        } as SBRouteData,
    },
];

@NgModule({
    imports: [UnitCategoriesModule, RouterModule.forChild(ROUTES)],
    exports: [RouterModule],
})
export class UnitCategoriesRoutingModule {}

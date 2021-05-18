/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

/* Module */
import { PoolsModule } from './pools.module';

/* Containers */
import * as poolsContainers from './containers';

/* Guards */
import * as poolsGuards from './guards';
import { SBRouteData } from '@modules/navigation/models';

/* Routes */
export const ROUTES: Routes = [
    {
        path: '',
        canActivate: [],
        component: poolsContainers.PoolsRecordsTabulationPageComponent,
        data: {
            title: 'Pools',
            breadcrumbs: [
                {
                    text: 'Configurations',
                    active: false
                },
                {
                    text: 'Pools',
                    active: false
                }
            ],
        } as SBRouteData,
    },
];

@NgModule({
    imports: [PoolsModule, RouterModule.forChild(ROUTES)],
    exports: [RouterModule],
})
export class PoolsRoutingModule {}

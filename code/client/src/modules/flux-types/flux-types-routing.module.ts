/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

/* Module */
import { FluxTypesModule } from './flux-types.module';

/* Containers */
import * as fluxTypesContainers from './containers';

/* Guards */
import * as fluxTypesGuards from './guards';
import { SBRouteData } from '@modules/navigation/models';

/* Routes */
export const ROUTES: Routes = [
    {
        path: '',
        canActivate: [],
        component: fluxTypesContainers.FluxTypesRecordsTabulationPageComponent,
        data: {
            title: 'Flux Types',
            breadcrumbs: [
                {
                    text: 'Configurations',
                    active: false
                },
                {
                    text: 'Flux Types',
                    active: false
                }
            ]
        } as SBRouteData,
    },
];

@NgModule({
    imports: [FluxTypesModule, RouterModule.forChild(ROUTES)],
    exports: [RouterModule],
})
export class FluxTypesRoutingModule {}

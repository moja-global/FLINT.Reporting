/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

/* Module */
import { FluxesTypesModule } from './fluxes-types.module';

/* Containers */
import * as fluxesTypesContainers from './containers';

/* Guards */
import * as fluxesTypesGuards from './guards';
import { SBRouteData } from '@modules/navigation/models';

/* Routes */
export const ROUTES: Routes = [ 
    {
        path: '',
        canActivate: [],
        component: fluxesTypesContainers.FluxesTypesRecordsTabulationPageComponent,
        data: {
            title: 'FluxesTypes',
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
    imports: [FluxesTypesModule, RouterModule.forChild(ROUTES)],
    exports: [RouterModule],
})
export class FluxesTypesRoutingModule {}

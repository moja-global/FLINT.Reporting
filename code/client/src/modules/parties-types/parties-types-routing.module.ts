/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

/* Module */
import { PartiesTypesModule } from './parties-types.module';

/* Containers */
import * as partiesTypesContainers from './containers';

/* Guards */
import * as partiesTypesGuards from './guards';
import { SBRouteData } from '@modules/navigation/models';

/* Routes */
export const ROUTES: Routes = [ 
    {
        path: '',
        canActivate: [],
        component: partiesTypesContainers.PartiesTypesRecordsTabulationPageComponent,
        data: {
            title: 'PartiesTypes',
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
    imports: [PartiesTypesModule, RouterModule.forChild(ROUTES)],
    exports: [RouterModule],
})
export class PartiesTypesRoutingModule {}

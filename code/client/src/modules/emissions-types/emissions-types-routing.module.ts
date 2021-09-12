/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

/* Module */
import { EmissionsTypesModule } from './emissions-types.module';

/* Containers */
import * as emissionsTypesContainers from './containers';

/* Guards */
import * as emissionsTypesGuards from './guards';
import { SBRouteData } from '@modules/navigation/models';

/* Routes */
export const ROUTES: Routes = [ 
    {
        path: '',
        canActivate: [],
        component: emissionsTypesContainers.EmissionsTypesRecordsTabulationPageComponent,
        data: {
            title: 'EmissionsTypes',
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
    imports: [EmissionsTypesModule, RouterModule.forChild(ROUTES)],
    exports: [RouterModule],
})
export class EmissionsTypesRoutingModule {}

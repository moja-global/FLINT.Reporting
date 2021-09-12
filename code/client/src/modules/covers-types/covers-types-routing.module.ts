/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

/* Module */
import { CoversTypesModule } from './covers-types.module';

/* Containers */
import * as coversTypesContainers from './containers';

/* Guards */
import * as coversTypesGuards from './guards';
import { SBRouteData } from '@modules/navigation/models';

/* Routes */
export const ROUTES: Routes = [ 
    {
        path: '',
        canActivate: [],
        component: coversTypesContainers.CoversTypesRecordsTabulationPageComponent,
        data: {
            title: 'CoversTypes',
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
    imports: [CoversTypesModule, RouterModule.forChild(ROUTES)],
    exports: [RouterModule],
})
export class CoversTypesRoutingModule {}

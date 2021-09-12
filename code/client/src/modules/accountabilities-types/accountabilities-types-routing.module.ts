/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

/* Module */
import { AccountabilitiesTypesModule } from './accountabilities-types.module';

/* Containers */
import * as accountabilitiesTypesContainers from './containers';

/* Guards */
import * as accountabilitiesTypesGuards from './guards';
import { SBRouteData } from '@modules/navigation/models';

/* Routes */
export const ROUTES: Routes = [ 
    {
        path: '',
        canActivate: [],
        component: accountabilitiesTypesContainers.AccountabilitiesTypesRecordsTabulationPageComponent,
        data: {
            title: 'AccountabilitiesTypes',
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
    imports: [AccountabilitiesTypesModule, RouterModule.forChild(ROUTES)],
    exports: [RouterModule],
})
export class AccountabilitiesTypesRoutingModule {}

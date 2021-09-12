/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

/* Module */
import { PartiesModule } from './parties.module';


/* Containers */
import * as partiesContainers from './containers';

/* Guards */
import { SBRouteData } from '@modules/navigation/models';


/* Routes */
export const ROUTES: Routes = [     
    {
        path: ':partyTypeId',
        canActivate: [],
        component: partiesContainers.PartiesRecordsTabulationPageComponent,
        data: {
            title: 'Parties',
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
    imports: [PartiesModule, RouterModule.forChild(ROUTES)],
    exports: [RouterModule],
})
export class PartiesRoutingModule {}

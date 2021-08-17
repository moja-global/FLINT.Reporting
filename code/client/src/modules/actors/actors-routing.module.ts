/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

/* Module */
import { ActorsModule } from './actors.module';

/* Containers */
import * as actorsContainers from './containers';

/* Guards */
import * as actorsGuards from './guards';
import { SBRouteData } from '@modules/navigation/models';

/* Routes */
export const ROUTES: Routes = [ 
    {
        path: '',
        canActivate: [],
        component: actorsContainers.ActorsDashboardComponent,
        data: {
            title: 'Actor',
            breadcrumbs: []
        } as SBRouteData,
    }   
];

@NgModule({
    imports: [ActorsModule, RouterModule.forChild(ROUTES)],
    exports: [RouterModule],
})
export class ActorsRoutingModule {}

/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

/* Module */
import { SituationsModule } from './situations.module';

/* Containers */
import * as situationsContainers from './containers';

/* Guards */
import * as situationsGuards from './guards';
import { SBRouteData } from '@modules/navigation/models';

/* Routes */
export const ROUTES: Routes = [ 
    {
        path: '',
        canActivate: [],
        component: situationsContainers.SituationsDashboardComponent,
        data: {
            title: 'Situation',
            breadcrumbs: []
        } as SBRouteData,
    }   
];

@NgModule({
    imports: [SituationsModule, RouterModule.forChild(ROUTES)],
    exports: [RouterModule],
})
export class SituationsRoutingModule {}

/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

/* Module */
import { ResultsModule } from './results.module';

/* Containers */
import * as resultsContainers from './containers';

/* Guards */
import * as resultsGuards from './guards';
import { SBRouteData } from '@modules/navigation/models';

/* Routes */
export const ROUTES: Routes = [ 
    {
        path: '',
        canActivate: [],
        component: resultsContainers.ResultsDashboardComponent,
        data: {
            title: 'Result',
            breadcrumbs: []
        } as SBRouteData,
    }   
];

@NgModule({
    imports: [ResultsModule, RouterModule.forChild(ROUTES)],
    exports: [RouterModule],
})
export class ResultsRoutingModule {}

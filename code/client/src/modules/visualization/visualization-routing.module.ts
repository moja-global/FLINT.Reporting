/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

/* Module */
import { VisualizationModule } from './visualization.module';

/* Containers */
import * as visualizationContainers from './containers';

/* Guards */
import * as visualizationGuards from './guards';

/* Routes */
export const ROUTES: Routes = [
    {
        path: '',
        canActivate: [],
        component: visualizationContainers.VersionComponent,
    },
];

@NgModule({
    imports: [VisualizationModule, RouterModule.forChild(ROUTES)],
    exports: [RouterModule],
})
export class VisualizationRoutingModule {}

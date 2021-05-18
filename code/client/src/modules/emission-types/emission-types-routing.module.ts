/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

/* Module */
import { EmissionTypesModule } from './emission-types.module';

/* Containers */
import * as emissionTypesContainers from './containers';

/* Guards */
import * as emissionTypesGuards from './guards';
import { SBRouteData } from '@modules/navigation/models';

/* Routes */
export const ROUTES: Routes = [
    {
        path: '',
        canActivate: [],
        component: emissionTypesContainers.EmissionTypesRecordsTabulationPageComponent,
        data: {
            title: 'Emission Types',
            breadcrumbs: [
                {
                    text: 'Configurations',
                    active: false
                },
                {
                    text: 'Emission Types',
                    active: false
                }
            ],
        } as SBRouteData,
    },
];

@NgModule({
    imports: [EmissionTypesModule, RouterModule.forChild(ROUTES)],
    exports: [RouterModule],
})
export class EmissionTypesRoutingModule {}

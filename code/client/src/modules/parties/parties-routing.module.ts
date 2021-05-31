/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

/* Module */
import { PartiesModule } from './parties.module';

/* Containers */
import * as partiesContainers from './containers';

/* Guards */
import * as partiesGuards from './guards';
import { SBRouteData } from '@modules/navigation/models';

/* Routes */
export const ROUTES: Routes = [
    {
        path: ':partyTypeId',
        canActivate: [],
        component: partiesContainers.PartiesRecordsTabulationPageComponent,
        data: {
            title: 'Parties',
            breadcrumbs: [
                {
                    text: 'Configurations',
                    active: false
                },
                {
                    text: 'Parties',
                    active: false
                }
            ],
        } as SBRouteData,
    }  
];

@NgModule({
    imports: [PartiesModule, RouterModule.forChild(ROUTES)],
    exports: [RouterModule],
})
export class PartiesRoutingModule {}

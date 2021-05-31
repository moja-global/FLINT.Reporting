/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

/* Module */
import { PartyTypesModule } from './party-types.module';

/* Containers */
import * as partyTypesContainers from './containers';

/* Guards */
import * as partyTypesGuards from './guards';
import { SBRouteData } from '@modules/navigation/models';

/* Routes */
export const ROUTES: Routes = [
    {
        path: ':parentPartyTypeId',
        canActivate: [],
        component: partyTypesContainers.PartyTypesRecordsTabulationPageComponent,
        data: {
            title: 'Party Types',
            breadcrumbs: [
                {
                    text: 'Configurations',
                    active: false
                },
                {
                    text: 'Party Types',
                    active: false
                }
            ],
        } as SBRouteData,
    }  
];

@NgModule({
    imports: [PartyTypesModule, RouterModule.forChild(ROUTES)],
    exports: [RouterModule],
})
export class PartyTypesRoutingModule {}

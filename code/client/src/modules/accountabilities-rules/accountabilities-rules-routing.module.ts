/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

/* Module */
import { AccountabilitiesRulesModule } from './accountabilities-rules.module';


/* Containers */
import * as accountabilitiesRulesContainers from './containers';

/* Guards */
import { SBRouteData } from '@modules/navigation/models';


/* Routes */
export const ROUTES: Routes = [     
    {
        path: ':accountabilityTypeId',
        canActivate: [],
        component: accountabilitiesRulesContainers.AccountabilitiesRulesRecordsTabulationPageComponent,
        data: {
            title: 'Accountability Types',
            breadcrumbs: [],
        } as SBRouteData,
    },         
];



@NgModule({
    imports: [AccountabilitiesRulesModule, RouterModule.forChild(ROUTES)],
    exports: [RouterModule],
})
export class AccountabilitiesRulesRoutingModule {}

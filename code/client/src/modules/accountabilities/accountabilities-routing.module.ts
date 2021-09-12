/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

/* Module */
import { AccountabilitiesModule } from './accountabilities.module';

/* Containers */
import * as accountabilitiesContainers from './containers';

/* Guards */
import * as accountabilitiesGuards from './guards';
import { SBRouteData } from '@modules/navigation/models';

/* Routes */
export const ROUTES: Routes = [ 
    {
        path: ':accountabilityRuleId',
        canActivate: [],
        component: accountabilitiesContainers.AccountabilitiesRecordsTabulationPageComponent,
        data: {
            title: 'Accountabilities',
            breadcrumbs: [
                {
                    text: 'Accountabilities',
                    link: '/accountabilities',
                    active: false
                }
            ],
        } as SBRouteData,
    }   
];

@NgModule({
    imports: [AccountabilitiesModule, RouterModule.forChild(ROUTES)],
    exports: [RouterModule],
})
export class AccountabilitiesRoutingModule {}

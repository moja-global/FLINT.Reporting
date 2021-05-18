/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

/* Module */
import { DomainsModule } from './domains.module';

/* Containers */
import * as domainsContainers from './containers';

/* Guards */
import * as domainsGuards from './guards';
import { SBRouteData } from '@modules/navigation/models';

/* Routes */
export const ROUTES: Routes = [
    {
        path: '',
        canActivate: [],
        component: domainsContainers.DomainsRecordsTabulationPageComponent,
        data: {
            title: 'Domains',
            breadcrumbs: [
                {
                    text: 'Configurations',
                    active: false
                },
                {
                    text: 'Domains',
                    active: false
                }
            ],
        } as SBRouteData,
    },
];

@NgModule({
    imports: [DomainsModule, RouterModule.forChild(ROUTES)],
    exports: [RouterModule],
})
export class DomainsRoutingModule {}

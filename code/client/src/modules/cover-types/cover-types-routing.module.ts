/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

/* Module */
import { CoverTypesModule } from './cover-types.module';

/* Containers */
import * as coverTypesContainers from './containers';

/* Guards */
import * as coverTypesGuards from './guards';
import { SBRouteData } from '@modules/navigation/models';

/* Routes */
export const ROUTES: Routes = [
    {
        path: '',
        canActivate: [],
        component: coverTypesContainers.CoverTypesRecordsTabulationPageComponent,
        data: {
            title: 'Cover Types',
            breadcrumbs: [
                {
                    text: 'Configurations',
                    active: false
                },
                {
                    text: 'Cover Types',
                    active: false
                }
            ],
        } as SBRouteData,
    },
];

@NgModule({
    imports: [CoverTypesModule, RouterModule.forChild(ROUTES)],
    exports: [RouterModule],
})
export class CoverTypesRoutingModule {}

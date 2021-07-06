/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

/* Module */
import { QuantityObservationsModule } from './quantity-observations.module';

/* Containers */
import * as quantityObservationsContainers from './containers';

/* Guards */
import * as quantityObservationsGuards from './guards';
import { SBRouteData } from '@modules/navigation/models';

/* Routes */
export const ROUTES: Routes = [
    {
        path: '',
        canActivate: [],
        component: quantityObservationsContainers.QuantityObservationsRecordsTabulationPageComponent,
        data: {
            title: 'Quantity Observations',
            breadcrumbs: [
                {
                    text: 'Configurations',
                    active: false
                },
                {
                    text: 'Quantity Observations',
                    active: false
                }
            ],
        } as SBRouteData,
    },
];

@NgModule({
    imports: [QuantityObservationsModule, RouterModule.forChild(ROUTES)],
    exports: [RouterModule],
})
export class QuantityObservationsRoutingModule {}

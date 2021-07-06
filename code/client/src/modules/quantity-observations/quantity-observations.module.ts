/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { CommonModule, DecimalPipe } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

/* Modules */
import { AppCommonModule } from '@common/app-common.module';
import { NavigationModule } from '@modules/navigation/navigation.module';

/* Components */
import * as quantityObservationsComponents from './components';


/* Containers */
import * as quantityObservationsContainers from './containers';

/* Guards */
import * as quantityObservationsGuards from './guards';

/* Services */
import * as quantityObservationsServices from './services';

@NgModule({
    imports: [
        CommonModule,
        RouterModule,
        ReactiveFormsModule,
        FormsModule,
        AppCommonModule,
        NavigationModule
    ],
    providers: [
        DecimalPipe,
        ...quantityObservationsServices.services,
        ...quantityObservationsGuards.guards
    ],
    declarations: [
        ...quantityObservationsContainers.containers,
        ...quantityObservationsComponents.components
    ],
    exports: [...quantityObservationsContainers.containers, ...quantityObservationsComponents.components]
})
export class QuantityObservationsModule {}

/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { CommonModule, DecimalPipe } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

/* Modules */
import { AppCommonModule } from '@common/app-common.module';
import { NavigationModule } from '@modules/navigation/navigation.module';

/* Components */
import * as fluxesTypesComponents from './components';


/* Containers */
import * as fluxesTypesContainers from './containers';
import * as fluxesTypesModalContainers from './containers/modals';

/* Guards */
import * as fluxesTypesGuards from './guards';

/* Services */
import * as fluxesTypesServices from './services';


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
        ...fluxesTypesServices.services,
        ...fluxesTypesGuards.guards
    ],
    declarations: [
        ...fluxesTypesContainers.containers,
        ...fluxesTypesComponents.components
    ],
    exports: [...fluxesTypesContainers.containers, ...fluxesTypesComponents.components],
    entryComponents: [...fluxesTypesModalContainers.containers]
})
export class FluxesTypesModule {}

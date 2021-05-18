/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { CommonModule, DecimalPipe } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

/* Modules */
import { AppCommonModule } from '@common/app-common.module';
import { NavigationModule } from '@modules/navigation/navigation.module';

/* Components */
import * as fluxTypesComponents from './components';


/* Containers */
import * as fluxTypesContainers from './containers';
import * as fluxTypesModalContainers from './containers/modals';

/* Guards */
import * as fluxTypesGuards from './guards';

/* Services */
import * as fluxTypesServices from './services';

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
        ...fluxTypesServices.services,
        ...fluxTypesGuards.guards
    ],
    declarations: [
        ...fluxTypesContainers.containers,
        ...fluxTypesComponents.components
    ],
    exports: [...fluxTypesContainers.containers, ...fluxTypesComponents.components],
    entryComponents: [...fluxTypesModalContainers.containers]
})
export class FluxTypesModule {}

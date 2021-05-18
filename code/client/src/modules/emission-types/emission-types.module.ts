/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { CommonModule, DecimalPipe } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

/* Modules */
import { AppCommonModule } from '@common/app-common.module';
import { NavigationModule } from '@modules/navigation/navigation.module';

/* Components */
import * as emissionTypesComponents from './components';


/* Containers */
import * as emissionTypesContainers from './containers';
import * as emissionTypesModalContainers from './containers/modals';

/* Guards */
import * as emissionTypesGuards from './guards';

/* Services */
import * as emissionTypesServices from './services';

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
        ...emissionTypesServices.services,
        ...emissionTypesGuards.guards
    ],
    declarations: [
        ...emissionTypesContainers.containers,
        ...emissionTypesComponents.components
    ],
    exports: [...emissionTypesContainers.containers, ...emissionTypesComponents.components],
    entryComponents: [...emissionTypesModalContainers.containers]
})
export class EmissionTypesModule {}

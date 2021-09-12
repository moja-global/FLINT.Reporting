/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { CommonModule, DecimalPipe } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

/* Modules */
import { AppCommonModule } from '@common/app-common.module';
import { NavigationModule } from '@modules/navigation/navigation.module';

/* Components */
import * as accountabilitiesTypesComponents from './components';


/* Containers */
import * as accountabilitiesTypesContainers from './containers';
import * as accountabilitiesTypesModalContainers from './containers/modals';

/* Guards */
import * as accountabilitiesTypesGuards from './guards';

/* Services */
import * as accountabilitiesTypesServices from './services';


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
        ...accountabilitiesTypesServices.services,
        ...accountabilitiesTypesGuards.guards
    ],
    declarations: [
        ...accountabilitiesTypesContainers.containers,
        ...accountabilitiesTypesComponents.components
    ],
    exports: [...accountabilitiesTypesContainers.containers, ...accountabilitiesTypesComponents.components],
    entryComponents: [...accountabilitiesTypesModalContainers.containers]
})
export class AccountabilitiesTypesModule {}

/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { CommonModule, DecimalPipe } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

/* Modules */
import { AppCommonModule } from '@common/app-common.module';
import { NavigationModule } from '@modules/navigation/navigation.module';

/* Components */
import * as partiesTypesComponents from './components';


/* Containers */
import * as partiesTypesContainers from './containers';
import * as partiesTypesModalContainers from './containers/modals';

/* Guards */
import * as partiesTypesGuards from './guards';

/* Services */
import * as partiesTypesServices from './services';


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
        ...partiesTypesServices.services,
        ...partiesTypesGuards.guards
    ],
    declarations: [
        ...partiesTypesContainers.containers,
        ...partiesTypesComponents.components
    ],
    exports: [...partiesTypesContainers.containers, ...partiesTypesComponents.components],
    entryComponents: [...partiesTypesModalContainers.containers]
})
export class PartiesTypesModule {}

/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { CommonModule, DecimalPipe } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

/* Modules */
import { AppCommonModule } from '@common/app-common.module';
import { NavigationModule } from '@modules/navigation/navigation.module';
import { UnitsModule } from '@modules/units/units.module';

/* Components */
import * as partyTypesComponents from './components';


/* Containers */
import * as partyTypesContainers from './containers';
import * as partyTypesModalContainers from './containers/modals';

/* Guards */
import * as partyTypesGuards from './guards';

/* Services */
import * as partyTypesServices from './services';

@NgModule({
    imports: [
        CommonModule,
        RouterModule,
        ReactiveFormsModule,
        FormsModule,
        AppCommonModule,
        NavigationModule,
        UnitsModule
    ],
    providers: [
        DecimalPipe,
        ...partyTypesServices.services,
        ...partyTypesGuards.guards
    ],
    declarations: [
        ...partyTypesContainers.containers,
        ...partyTypesComponents.components
    ],
    exports: [...partyTypesContainers.containers, ...partyTypesComponents.components],
    entryComponents: [...partyTypesModalContainers.containers]
})
export class PartyTypesModule {}

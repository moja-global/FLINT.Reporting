/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { CommonModule, DecimalPipe } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

/* Modules */
import { AppCommonModule } from '@common/app-common.module';
import { NavigationModule } from '@modules/navigation/navigation.module';
import { PartiesTypesModule } from '@modules/parties-types/parties-types.module';

/* Components */
import * as accountabilitiesRulesComponents from './components';


/* Containers */
import * as accountabilitiesRulesContainers from './containers';
import * as accountabilitiesRulesModalContainers from './containers/modals';

/* Guards */
import * as accountabilitiesRulesGuards from './guards';

/* Services */
import * as accountabilitiesRulesServices from './services';




@NgModule({
    imports: [
        CommonModule,
        RouterModule,
        ReactiveFormsModule,
        FormsModule,
        AppCommonModule,
        NavigationModule,
        PartiesTypesModule
    ],
    providers: [
        DecimalPipe,
        ...accountabilitiesRulesServices.services,
        ...accountabilitiesRulesGuards.guards
    ],
    declarations: [
        ...accountabilitiesRulesContainers.containers,
        ...accountabilitiesRulesComponents.components
    ],
    exports: [...accountabilitiesRulesContainers.containers, ...accountabilitiesRulesComponents.components],
    entryComponents: [...accountabilitiesRulesModalContainers.containers]
})
export class AccountabilitiesRulesModule {}

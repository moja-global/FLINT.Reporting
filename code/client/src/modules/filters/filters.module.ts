/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { CommonModule, DecimalPipe } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

/* Modules */
import { AppCommonModule } from '@common/app-common.module';
import { NavigationModule } from '@modules/navigation/navigation.module';
import { PartiesTypesModule } from '@modules/parties-types/parties-types.module';
import { PartiesModule } from '@modules/parties/parties.module';
import { AccountabilitiesTypesModule } from '@modules/accountabilities-types/accountabilities-types.module';
import { AccountabilitiesRulesModule } from '@modules/accountabilities-rules/accountabilities-rules.module';
import { AccountabilitiesModule } from '@modules/accountabilities/accountabilities.module';


/* Components */
import * as accountabilitiesComponents from './components';


/* Containers */
import * as accountabilitiesContainers from './containers';
import * as accountabilitiesModalContainers from './containers/modals';

/* Guards */
import * as accountabilitiesGuards from './guards';

/* Services */
import * as accountabilitiesServices from './services';


@NgModule({
    imports: [
        CommonModule,
        RouterModule,
        ReactiveFormsModule,
        FormsModule,
        AppCommonModule,
        NavigationModule,
        PartiesTypesModule,
        PartiesModule,
        AccountabilitiesTypesModule,
        AccountabilitiesRulesModule,
        AccountabilitiesModule

    ],
    providers: [
        DecimalPipe,
        ...accountabilitiesServices.services,
        ...accountabilitiesGuards.guards
    ],
    declarations: [
        ...accountabilitiesContainers.containers,
        ...accountabilitiesComponents.components
    ],
    exports: [...accountabilitiesContainers.containers, ...accountabilitiesComponents.components],
    entryComponents: [...accountabilitiesModalContainers.containers]
})
export class FiltersModule {}

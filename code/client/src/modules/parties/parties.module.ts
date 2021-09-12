/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { CommonModule, DecimalPipe } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

/* Modules */
import { AppCommonModule } from '@common/app-common.module';
import { NavigationModule } from '@modules/navigation/navigation.module';

/* Components */
import * as partiesComponents from './components';


/* Containers */
import * as partiesContainers from './containers';
import * as partiesModalContainers from './containers/modals';

/* Guards */
import * as partiesGuards from './guards';

/* Services */
import * as partiesServices from './services';


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
        ...partiesServices.services,
        ...partiesGuards.guards
    ],
    declarations: [
        ...partiesContainers.containers,
        ...partiesComponents.components
    ],
    exports: [...partiesContainers.containers, ...partiesComponents.components],
    entryComponents: [...partiesModalContainers.containers]
})
export class PartiesModule {}

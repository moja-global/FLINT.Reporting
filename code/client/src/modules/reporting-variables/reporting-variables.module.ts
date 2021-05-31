/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { CommonModule, DecimalPipe } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

/* Modules */
import { AppCommonModule } from '@common/app-common.module';
import { NavigationModule } from '@modules/navigation/navigation.module';

/* Components */
import * as reportingVariablesComponents from './components';


/* Containers */
import * as reportingVariablesContainers from './containers';
import * as reportingVariablesModalContainers from './containers/modals';

/* Guards */
import * as reportingVariablesGuards from './guards';

/* Services */
import * as reportingVariablesServices from './services';

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
        ...reportingVariablesServices.services,
        ...reportingVariablesGuards.guards
    ],
    declarations: [
        ...reportingVariablesContainers.containers,
        ...reportingVariablesComponents.components
    ],
    exports: [...reportingVariablesContainers.containers, ...reportingVariablesComponents.components],
    entryComponents: [...reportingVariablesModalContainers.containers]
})
export class ReportingVariablesModule {}

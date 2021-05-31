/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { CommonModule, DecimalPipe } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

/* Modules */
import { AppCommonModule } from '@common/app-common.module';
import { NavigationModule } from '@modules/navigation/navigation.module';

/* Components */
import * as reportingTablesComponents from './components';


/* Containers */
import * as reportingTablesContainers from './containers';
import * as reportingTablesModalContainers from './containers/modals';

/* Guards */
import * as reportingTablesGuards from './guards';

/* Services */
import * as reportingTablesServices from './services';

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
        ...reportingTablesServices.services,
        ...reportingTablesGuards.guards
    ],
    declarations: [
        ...reportingTablesContainers.containers,
        ...reportingTablesComponents.components
    ],
    exports: [...reportingTablesContainers.containers, ...reportingTablesComponents.components],
    entryComponents: [...reportingTablesModalContainers.containers]
})
export class ReportingTablesModule {}

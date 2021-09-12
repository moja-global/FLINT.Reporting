/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { CommonModule, DecimalPipe } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

/* Modules */
import { AppCommonModule } from '@common/app-common.module';
import { NavigationModule } from '@modules/navigation/navigation.module';
import { ReportingTablesModule } from '@modules/reporting-tables/reporting-tables.module';
import { ReportingVariablesModule } from '@modules/reporting-variables/reporting-variables.module';
import { LandUseCategoriesModule } from '@modules/land-use-categories/land-use-categories.module';


/* Components */
import * as reportingFrameworksComponents from './components';


/* Containers */
import * as reportingFrameworksContainers from './containers';
import * as reportingFrameworksModalContainers from './containers/modals';

/* Guards */
import * as reportingFrameworksGuards from './guards';

/* Services */
import * as reportingFrameworksServices from './services';



@NgModule({
    imports: [
        CommonModule,
        RouterModule,
        ReactiveFormsModule,
        FormsModule,
        AppCommonModule,
        NavigationModule,
        ReportingTablesModule,
        ReportingVariablesModule,
        LandUseCategoriesModule
    ],
    providers: [
        DecimalPipe,
        ...reportingFrameworksServices.services,
        ...reportingFrameworksGuards.guards
    ],
    declarations: [
        ...reportingFrameworksContainers.containers,
        ...reportingFrameworksComponents.components
    ],
    exports: [...reportingFrameworksContainers.containers, ...reportingFrameworksComponents.components],
    entryComponents: [...reportingFrameworksModalContainers.containers]
})
export class ReportingFrameworksModule {}

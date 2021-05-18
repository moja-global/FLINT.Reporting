/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { CommonModule, DecimalPipe } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

/* Modules */
import { AppCommonModule } from '@common/app-common.module';
import { NavigationModule } from '@modules/navigation/navigation.module';

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
        NavigationModule
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

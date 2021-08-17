/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { CommonModule, DecimalPipe } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

/* Modules */
import { AppCommonModule } from '@common/app-common.module';
import { NavigationModule } from '@modules/navigation/navigation.module';
import { DashboardModule } from '@modules/dashboard/dashboard.module';
import { CountUpModule } from 'ngx-countup';
import { AngularResizedEventModule } from 'angular-resize-event';
import { VisualizationModule } from '@modules/visualization/visualization.module';


/* Components */
import * as resultsComponents from './components';


/* Containers */
import * as resultsContainers from './containers';
import * as resultsModalContainers from './containers/modals';

/* Guards */
import * as resultsGuards from './guards';

/* Services */
import * as resultsServices from './services';






@NgModule({
    imports: [
        CommonModule,
        RouterModule,
        ReactiveFormsModule,
        FormsModule,
        AppCommonModule,
        NavigationModule,
        DashboardModule,
        AngularResizedEventModule,
        CountUpModule,
        VisualizationModule
    ],
    providers: [
        DecimalPipe,
        ...resultsServices.services,
        ...resultsGuards.guards
    ],
    declarations: [
        ...resultsContainers.containers,
        ...resultsComponents.components
    ],
    exports: [...resultsContainers.containers, ...resultsComponents.components],
    entryComponents: [...resultsModalContainers.containers]
})
export class ResultsModule {}

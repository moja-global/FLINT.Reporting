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
import * as situationsComponents from './components';


/* Containers */
import * as situationsContainers from './containers';
import * as situationsModalContainers from './containers/modals';

/* Guards */
import * as situationsGuards from './guards';

/* Services */
import * as situationsServices from './services';



@NgModule({
    imports: [
        CommonModule,
        RouterModule,
        ReactiveFormsModule,
        FormsModule,
        AppCommonModule,
        NavigationModule,
        DashboardModule,
        CountUpModule,
        AngularResizedEventModule,
        VisualizationModule
    ],
    providers: [
        DecimalPipe,
        ...situationsServices.services,
        ...situationsGuards.guards
    ],
    declarations: [
        ...situationsContainers.containers,
        ...situationsComponents.components
    ],
    exports: [...situationsContainers.containers, ...situationsComponents.components],
    entryComponents: [...situationsModalContainers.containers]
})
export class SituationsModule {}

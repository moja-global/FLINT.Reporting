/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

/* Modules */
import { AppCommonModule } from '@common/app-common.module';
import { NavigationModule } from '@modules/navigation/navigation.module';
import { CountUpModule } from 'ngx-countup';
import { AngularResizedEventModule } from 'angular-resize-event';

/* Components */
import * as visualizationComponents from './components';

/* Containers */
import * as visualizationContainers from './containers';

/* Guards */
import * as visualizationGuards from './guards';

/* Services */
import * as visualizationServices from './services';

@NgModule({
    imports: [
        CommonModule,
        RouterModule,
        ReactiveFormsModule,
        FormsModule,
        AppCommonModule,
        NavigationModule,
        CountUpModule,
        AngularResizedEventModule
    ],
    providers: [...visualizationServices.services, ...visualizationGuards.guards],
    declarations: [...visualizationContainers.containers, ...visualizationComponents.components],
    exports: [...visualizationContainers.containers, ...visualizationComponents.components],
})
export class VisualizationModule {}

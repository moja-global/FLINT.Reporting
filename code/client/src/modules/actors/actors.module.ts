/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { CommonModule, DecimalPipe } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

/* Modules */
import { AppCommonModule } from '@common/app-common.module';
import { NavigationModule } from '@modules/navigation/navigation.module';
import { DashboardModule } from '@modules/dashboard/dashboard.module';
import { VisualizationModule } from '@modules/visualization/visualization.module';


/* Components */
import * as actorsComponents from './components';


/* Containers */
import * as actorsContainers from './containers';
import * as actorsModalContainers from './containers/modals';

/* Guards */
import * as actorsGuards from './guards';

/* Services */
import * as actorsServices from './services';






@NgModule({
    imports: [
        CommonModule,
        RouterModule,
        ReactiveFormsModule,
        FormsModule,
        AppCommonModule,
        NavigationModule,
        DashboardModule,
        VisualizationModule
    ],
    providers: [
        DecimalPipe,
        ...actorsServices.services,
        ...actorsGuards.guards
    ],
    declarations: [
        ...actorsContainers.containers,
        ...actorsComponents.components
    ],
    exports: [...actorsContainers.containers, ...actorsComponents.components],
    entryComponents: [...actorsModalContainers.containers]
})
export class ActorsModule {}

/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { CommonModule, DecimalPipe } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

/* Modules */
import { AppCommonModule } from '@common/app-common.module';
import { NavigationModule } from '@modules/navigation/navigation.module';
import { ReportingFrameworksModule } from '@modules/reporting-frameworks/reporting-frameworks.module';

/* Components */
import * as landUsesCategoriesComponents from './components';


/* Containers */
import * as landUsesCategoriesContainers from './containers';
import * as landUsesCategoriesModalContainers from './containers/modals';

/* Guards */
import * as landUsesCategoriesGuards from './guards';

/* Services */
import * as landUsesCategoriesServices from './services';




@NgModule({
    imports: [
        CommonModule,
        RouterModule,
        ReactiveFormsModule,
        FormsModule,
        AppCommonModule,
        NavigationModule,
        ReportingFrameworksModule
    ],
    providers: [
        DecimalPipe,
        ...landUsesCategoriesServices.services,
        ...landUsesCategoriesGuards.guards
    ],
    declarations: [
        ...landUsesCategoriesContainers.containers,
        ...landUsesCategoriesComponents.components
    ],
    exports: [...landUsesCategoriesContainers.containers, ...landUsesCategoriesComponents.components],
    entryComponents: [...landUsesCategoriesModalContainers.containers]
})
export class LandUsesCategoriesModule {}

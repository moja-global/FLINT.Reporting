/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { CommonModule, DecimalPipe } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

/* Modules */
import { AppCommonModule } from '@common/app-common.module';
import { NavigationModule } from '@modules/navigation/navigation.module';

/* Components */
import * as landUseCategoriesComponents from './components';


/* Containers */
import * as landUseCategoriesContainers from './containers';
import * as landUseCategoriesModalContainers from './containers/modals';

/* Guards */
import * as landUseCategoriesGuards from './guards';

/* Services */
import * as landUseCategoriesServices from './services';

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
        ...landUseCategoriesServices.services,
        ...landUseCategoriesGuards.guards
    ],
    declarations: [
        ...landUseCategoriesContainers.containers,
        ...landUseCategoriesComponents.components
    ],
    exports: [...landUseCategoriesContainers.containers, ...landUseCategoriesComponents.components],
    entryComponents: [...landUseCategoriesModalContainers.containers]
})
export class LandUseCategoriesModule {}

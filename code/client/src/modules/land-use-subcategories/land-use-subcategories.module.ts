/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { CommonModule, DecimalPipe } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

/* Modules */
import { AppCommonModule } from '@common/app-common.module';
import { NavigationModule } from '@modules/navigation/navigation.module';

/* Components */
import * as landUseSubcategoriesComponents from './components';


/* Containers */
import * as landUseSubcategoriesContainers from './containers';
import * as landUseSubcategoriesModalContainers from './containers/modals';

/* Guards */
import * as landUseSubcategoriesGuards from './guards';

/* Services */
import * as landUseSubcategoriesServices from './services';

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
        ...landUseSubcategoriesServices.services,
        ...landUseSubcategoriesGuards.guards
    ],
    declarations: [
        ...landUseSubcategoriesContainers.containers,
        ...landUseSubcategoriesComponents.components
    ],
    exports: [...landUseSubcategoriesContainers.containers, ...landUseSubcategoriesComponents.components],
    entryComponents: [...landUseSubcategoriesModalContainers.containers]
})
export class LandUseSubcategoriesModule {}

/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { CommonModule, DecimalPipe } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

/* Modules */
import { AppCommonModule } from '@common/app-common.module';
import { NavigationModule } from '@modules/navigation/navigation.module';
import { UnitsModule } from '@modules/units/units.module';

/* Components */
import * as unitCategoriesComponents from './components';


/* Containers */
import * as unitCategoriesContainers from './containers';
import * as unitCategoriesModalContainers from './containers/modals';

/* Guards */
import * as unitCategoriesGuards from './guards';

/* Services */
import * as unitCategoriesServices from './services';

@NgModule({
    imports: [
        CommonModule,
        RouterModule,
        ReactiveFormsModule,
        FormsModule,
        AppCommonModule,
        NavigationModule,
        UnitsModule
    ],
    providers: [
        DecimalPipe,
        ...unitCategoriesServices.services,
        ...unitCategoriesGuards.guards
    ],
    declarations: [
        ...unitCategoriesContainers.containers,
        ...unitCategoriesComponents.components
    ],
    exports: [...unitCategoriesContainers.containers, ...unitCategoriesComponents.components],
    entryComponents: [...unitCategoriesModalContainers.containers]
})
export class UnitCategoriesModule {}

/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { CommonModule, DecimalPipe } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

/* Modules */
import { AppCommonModule } from '@common/app-common.module';
import { NavigationModule } from '@modules/navigation/navigation.module';

/* Components */
import * as coversTypesComponents from './components';


/* Containers */
import * as coversTypesContainers from './containers';
import * as coversTypesModalContainers from './containers/modals';

/* Guards */
import * as coversTypesGuards from './guards';

/* Services */
import * as coversTypesServices from './services';


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
        ...coversTypesServices.services,
        ...coversTypesGuards.guards
    ],
    declarations: [
        ...coversTypesContainers.containers,
        ...coversTypesComponents.components
    ],
    exports: [...coversTypesContainers.containers, ...coversTypesComponents.components],
    entryComponents: [...coversTypesModalContainers.containers]
})
export class CoversTypesModule {}

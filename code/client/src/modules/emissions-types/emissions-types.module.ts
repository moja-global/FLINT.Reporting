/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { CommonModule, DecimalPipe } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

/* Modules */
import { AppCommonModule } from '@common/app-common.module';
import { NavigationModule } from '@modules/navigation/navigation.module';

/* Components */
import * as emissionsTypesComponents from './components';


/* Containers */
import * as emissionsTypesContainers from './containers';
import * as emissionsTypesModalContainers from './containers/modals';

/* Guards */
import * as emissionsTypesGuards from './guards';

/* Services */
import * as emissionsTypesServices from './services';


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
        ...emissionsTypesServices.services,
        ...emissionsTypesGuards.guards
    ],
    declarations: [
        ...emissionsTypesContainers.containers,
        ...emissionsTypesComponents.components
    ],
    exports: [...emissionsTypesContainers.containers, ...emissionsTypesComponents.components],
    entryComponents: [...emissionsTypesModalContainers.containers]
})
export class EmissionsTypesModule {}

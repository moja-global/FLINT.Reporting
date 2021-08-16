/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { CommonModule, DecimalPipe } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

/* Modules */
import { AppCommonModule } from '@common/app-common.module';
import { NavigationModule } from '@modules/navigation/navigation.module';

/* Components */
import * as coverTypesComponents from './components';


/* Containers */
import * as coverTypesContainers from './containers';

/* Guards */
import * as coverTypesGuards from './guards';

/* Services */
import * as coverTypesServices from './services';

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
        ...coverTypesServices.services,
        ...coverTypesGuards.guards
    ],
    declarations: [
        ...coverTypesContainers.containers,
        ...coverTypesComponents.components
    ],
    exports: [...coverTypesContainers.containers, ...coverTypesComponents.components]
})
export class CoverTypesModule {}

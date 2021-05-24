/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { CommonModule, DecimalPipe } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

/* Modules */
import { AppCommonModule } from '@common/app-common.module';
import { NavigationModule } from '@modules/navigation/navigation.module';

/* Components */
import * as unitsComponents from './components';


/* Containers */
import * as unitsContainers from './containers';
import * as unitsModalContainers from './containers/modals';

/* Guards */
import * as unitsGuards from './guards';

/* Services */
import * as unitsServices from './services';

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
        ...unitsServices.services,
        ...unitsGuards.guards
    ],
    declarations: [
        ...unitsContainers.containers,
        ...unitsComponents.components
    ],
    exports: [...unitsContainers.containers, ...unitsComponents.components],
    entryComponents: [...unitsModalContainers.containers]
})
export class UnitsModule {}

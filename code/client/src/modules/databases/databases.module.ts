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
import * as databasesComponents from './components';


/* Containers */
import * as databasesContainers from './containers';
import * as databasesModalContainers from './containers/modals';

/* Guards */
import * as databasesGuards from './guards';

/* Services */
import * as databasesServices from './services';

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
        ...databasesServices.services,
        ...databasesGuards.guards
    ],
    declarations: [
        ...databasesContainers.containers,
        ...databasesComponents.components
    ],
    exports: [...databasesContainers.containers, ...databasesComponents.components],
    entryComponents: [...databasesModalContainers.containers]
})
export class DatabasesModule {}

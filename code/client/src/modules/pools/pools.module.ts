/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { CommonModule, DecimalPipe } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

/* Modules */
import { AppCommonModule } from '@common/app-common.module';
import { NavigationModule } from '@modules/navigation/navigation.module';

/* Components */
import * as poolsComponents from './components';


/* Containers */
import * as poolsContainers from './containers';
import * as poolsModalContainers from './containers/modals';

/* Guards */
import * as poolsGuards from './guards';

/* Services */
import * as poolsServices from './services';

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
        ...poolsServices.services,
        ...poolsGuards.guards
    ],
    declarations: [
        ...poolsContainers.containers,
        ...poolsComponents.components
    ],
    exports: [...poolsContainers.containers, ...poolsComponents.components],
    entryComponents: [...poolsModalContainers.containers]
})
export class PoolsModule {}

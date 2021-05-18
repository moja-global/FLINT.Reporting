/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

/* Modules */
import { AppCommonModule } from '@common/app-common.module';
import { NavigationModule } from '@modules/navigation/navigation.module';

/* Components */
import * as homeComponents from './components';

/* Containers */
import * as homeContainers from './containers';

/* Guards */
import * as homeGuards from './guards';

/* Services */
import * as homeServices from './services';

@NgModule({
    imports: [
        CommonModule,
        RouterModule,
        ReactiveFormsModule,
        FormsModule,
        AppCommonModule,
        NavigationModule,
    ],
    providers: [...homeServices.services, ...homeGuards.guards],
    declarations: [...homeContainers.containers, ...homeComponents.components],
    exports: [...homeContainers.containers, ...homeComponents.components],
})
export class HomeModule {}

/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { CommonModule, DecimalPipe } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

/* Modules */
import { AppCommonModule } from '@common/app-common.module';
import { NavigationModule } from '@modules/navigation/navigation.module';

/* Components */
import * as domainsComponents from './components';


/* Containers */
import * as domainsContainers from './containers';
import * as domainsModalContainers from './containers/modals';

/* Guards */
import * as domainsGuards from './guards';

/* Services */
import * as domainsServices from './services';

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
        ...domainsServices.services,
        ...domainsGuards.guards
    ],
    declarations: [
        ...domainsContainers.containers,
        ...domainsComponents.components
    ],
    exports: [...domainsContainers.containers, ...domainsComponents.components],
    entryComponents: [...domainsModalContainers.containers]
})
export class DomainsModule {}

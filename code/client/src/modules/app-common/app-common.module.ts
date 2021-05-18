/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormsModule } from '@angular/forms'

/* Third Party */
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { IconsModule } from '@modules/icons/icons.module';

const thirdParty = [IconsModule, NgbModule];

/* Components */
import * as appCommonComponents from './components';

/* Containers */
import * as appCommonContainers from './containers';

/* Directives */
import * as appCommonDirectives from '../app-common/directives';

/* Guards */
import * as appCommonGuards from './guards';

/* Services */
import * as appCommonServices from './services';
import * as authServices from '@modules/auth/services';

@NgModule({
    imports: [CommonModule, RouterModule, ReactiveFormsModule, FormsModule, ...thirdParty],
    providers: [
        ...appCommonDirectives.directives, 
        ...appCommonGuards.guards,
        ...appCommonServices.services, 
        ...authServices.services],
    declarations: [
        ...appCommonComponents.components, 
        ...appCommonContainers.containers,  
        ...appCommonDirectives.directives
    ],
    exports: [
        ...appCommonComponents.components, 
        ...appCommonContainers.containers,  
        ...appCommonDirectives.directives, 
        ...thirdParty],
})
export class AppCommonModule {}

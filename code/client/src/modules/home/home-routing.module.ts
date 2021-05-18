/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { SBRouteData } from '@modules/navigation/models';

/* Module */
import { HomeModule } from './home.module';

/* Containers */
import * as homeContainers from './containers';

/* Guards */
import * as homeGuards from './guards';

/* Routes */
export const ROUTES: Routes = [
    {
        path: '',
        canActivate: [],
        component: homeContainers.HomeComponent,
        data: {
            title: 'Home',
        } as SBRouteData,
    }
];

@NgModule({
    imports: [HomeModule, RouterModule.forChild(ROUTES)],
    exports: [RouterModule],
})
export class HomeRoutingModule {}

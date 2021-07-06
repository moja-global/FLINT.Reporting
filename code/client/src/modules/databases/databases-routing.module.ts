/* tslint:disable: ordered-imports*/
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

/* Module */
import { DatabasesModule } from './databases.module';

/* Containers */
import * as databasesContainers from './containers';

/* Guards */
import * as databasesGuards from './guards';
import { SBRouteData } from '@modules/navigation/models';

/* Routes */
export const ROUTES: Routes = [
    {
        path: '',
        canActivate: [],
        component: databasesContainers.DatabasesRecordsTabulationPageComponent,
        data: {
            title: 'Databases',
            breadcrumbs: [
                {
                    text: 'Configurations',
                    active: false
                },
                {
                    text: 'Databases',
                    active: false
                }
            ],
        } as SBRouteData,
    },
    {
        path: ':databaseId',
        canActivate: [],
        component: databasesContainers.DatabasesRecordsHomePageComponent,
        data: {
            title: 'Database Details',
            breadcrumbs: [
                {
                    text: 'Databases',
                    link: '/databases',
                    active: false
                }
            ],
        } as SBRouteData,
    }    
];

@NgModule({
    imports: [DatabasesModule, RouterModule.forChild(ROUTES)],
    exports: [RouterModule],
})
export class DatabasesRoutingModule {}

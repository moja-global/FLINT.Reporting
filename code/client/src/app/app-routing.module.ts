import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
    {
        path: '',
        pathMatch: 'full',
        redirectTo: '/dashboard',
    }, 
    {
        path: 'auth',
        loadChildren: () =>
            import('modules/auth/auth-routing.module').then(m => m.AuthRoutingModule),
    },    
    {
        path: 'charts',
        loadChildren: () =>
            import('modules/charts/charts-routing.module').then(m => m.ChartsRoutingModule),
    }, 
    {
        path: 'dashboard',
        loadChildren: () =>
            import('modules/dashboard/dashboard-routing.module').then(
                m => m.DashboardRoutingModule
            ),
    },
    {
        path: 'cover_types',
        loadChildren: () =>
            import('modules/cover-types/cover-types-routing.module').then(m => m.CoverTypesRoutingModule)
    }, 
    {
        path: 'emission_types',
        loadChildren: () =>
            import('modules/emission-types/emission-types-routing.module').then(m => m.EmissionTypesRoutingModule)
    },  
    {
        path: 'error',
        loadChildren: () =>
            import('modules/error/error-routing.module').then(m => m.ErrorRoutingModule),
    },    
    {
        path: 'flux_types',
        loadChildren: () =>
            import('modules/flux-types/flux-types-routing.module').then(m => m.FluxTypesRoutingModule)
    },            
    {
        path: 'home',
        loadChildren: () =>
            import('modules/home/home-routing.module').then(
                m => m.HomeRoutingModule
            ),
    },
    {
        path: 'pools',
        loadChildren: () =>
            import('modules/pools/pools-routing.module').then(m => m.PoolsRoutingModule)
    },    
    {
        path: 'reporting_frameworks',
        loadChildren: () =>
            import('modules/reporting-frameworks/reporting-frameworks-routing.module').then(m => m.ReportingFrameworksRoutingModule)
    },    
    {
        path: 'unit_categories',
        loadChildren: () =>
            import('modules/unit-categories/unit-categories-routing.module').then(m => m.UnitCategoriesRoutingModule)
    },
    {
        path: 'units',
        loadChildren: () =>
            import('modules/units/units-routing.module').then(m => m.UnitsRoutingModule)
    },         
    {
        path: 'version',
        loadChildren: () =>
            import('modules/utility/utility-routing.module').then(m => m.UtilityRoutingModule),
    },
    {
        path: '**',
        pathMatch: 'full',
        loadChildren: () =>
            import('modules/error/error-routing.module').then(m => m.ErrorRoutingModule),
    },
];


@NgModule({
    imports: [RouterModule.forRoot(routes, { relativeLinkResolution: 'legacy', scrollPositionRestoration: 'top', useHash: true })],
    exports: [RouterModule],
})
export class AppRoutingModule {}

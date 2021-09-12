import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [  
    {
        path: '',
        pathMatch: 'full',
        redirectTo: '/dashboard',
    },
    {
        path: 'accountabilities_types',
        loadChildren: () =>
            import('modules/accountabilities-types/accountabilities-types-routing.module').then(
                m => m.AccountabilitiesTypesRoutingModule
            ),
    },  
    {
        path: 'accountabilities_rules',
        loadChildren: () =>
            import('modules/accountabilities-rules/accountabilities-rules-routing.module').then(
                m => m.AccountabilitiesRulesRoutingModule
            ),
    }, 
    {
        path: 'accountabilities',
        loadChildren: () =>
            import('modules/accountabilities/accountabilities-routing.module').then(
                m => m.AccountabilitiesRoutingModule
            ),
    },     
    {
        path: 'auth',
        loadChildren: () =>
            import('modules/auth/auth-routing.module').then(m => m.AuthRoutingModule),
    }, 
    {
        path: 'cover_types',
        loadChildren: () =>
            import('modules/covers-types/covers-types-routing.module').then(m => m.CoversTypesRoutingModule)
    },     
    {
        path: 'databases',
        loadChildren: () =>
            import('modules/databases/databases-routing.module').then(
                m => m.DatabasesRoutingModule
            ),
    },    
    {
        path: 'dashboard',
        loadChildren: () =>
            import('modules/dashboard/dashboard-routing.module').then(
                m => m.DashboardRoutingModule
            ),
    },
    {
        path: 'emission_types',
        loadChildren: () =>
            import('modules/emissions-types/emissions-types-routing.module').then(m => m.EmissionsTypesRoutingModule)
    },  
    {
        path: 'error',
        loadChildren: () =>
            import('modules/error/error-routing.module').then(m => m.ErrorRoutingModule),
    },    
    {
        path: 'flux_types',
        loadChildren: () =>
            import('modules/fluxes-types/fluxes-types-routing.module').then(m => m.FluxesTypesRoutingModule)
    },            
    {
        path: 'home',
        loadChildren: () =>
            import('modules/home/home-routing.module').then(
                m => m.HomeRoutingModule
            ),
    },
    {
        path: 'land_uses_categories',
        loadChildren: () =>
            import('modules/land-uses-categories/land-uses-categories-routing.module').then(m => m.LandUsesCategoriesRoutingModule)
    },     
    {
        path: 'parties_types',
        loadChildren: () =>
            import('modules/parties-types/parties-types-routing.module').then(
                m => m.PartiesTypesRoutingModule
            ),
    },
    {
        path: 'parties',
        loadChildren: () =>
            import('modules/parties/parties-routing.module').then(
                m => m.PartiesRoutingModule
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
        path: 'reporting_tables',
        loadChildren: () =>
            import('modules/reporting-tables/reporting-tables-routing.module').then(m => m.ReportingTablesRoutingModule)
    },  
    {
        path: 'reporting_variables',
        loadChildren: () =>
            import('modules/reporting-variables/reporting-variables-routing.module').then(m => m.ReportingVariablesRoutingModule)
    }, 
    {
        path: 'results',
        loadChildren: () =>
            import('modules/results/results-routing.module').then(
                m => m.ResultsRoutingModule
            ),
    },     
    {
        path: 'situation',
        loadChildren: () =>
            import('modules/situations/situations-routing.module').then(
                m => m.SituationsRoutingModule
            ),
    },          
    {
        path: 'unit_categories',
        loadChildren: () =>
            import('modules/unit-categories/unit-categories-routing.module').then(m => m.UnitCategoriesRoutingModule)
    },
    {
        path: 'units/:unitCategoryId',
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

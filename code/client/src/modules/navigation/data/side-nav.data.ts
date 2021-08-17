import { SideNavItems, SideNavSection } from '@modules/navigation/models';

export const sideNavSections: SideNavSection[] = [
    {
        text: 'DASHBOARD',
        items: ['core'],
    },
    {
        text: 'MANAGEMENT',
        items: ['databases'],
    },     
    {
        text: 'CONFIGURATIONS',
        items: ['coverTypes', 'emissionTypes', 'fluxTypes', 'pools', 'reportingFrameworks','units'],
    }     
];

export const sideNavItems: SideNavItems = {
    core: {
        icon: 'tachometer-alt',
        text: 'Dashboard',
        link: '/dashboard',
    },
    coverTypes: {
        icon: 'seedling',
        text: 'Cover Types',
        link: '/cover_types',
    },
    databases: {
        icon: 'database',
        text: 'Databases',
        link: '/databases',
    },    
    emissionTypes: {
        icon: 'fire',
        text: 'Emission Types',
        link: '/emission_types',
    }, 
    fluxTypes: {
        icon: 'exchange-alt',
        text: 'Flux Types',
        link: '/flux_types',
    },    
    pools: {
        icon: 'water',
        text: 'Pools',
        link: '/pools',
    },         
    reportingFrameworks: {
        icon: 'file-excel',
        text: 'Reporting Frameworks',
        link: '/reporting_frameworks',
    },
    units: {
        icon: 'balance-scale',
        text: 'Units',
        link: '/unit_categories'
    }           
};

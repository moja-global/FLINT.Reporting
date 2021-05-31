import { SideNavItems, SideNavSection } from '@modules/navigation/models';

export const sideNavSections: SideNavSection[] = [
    {
        text: 'DASHBOARD',
        items: ['core'],
    },
    {
        text: 'CONFIGURATIONS',
        items: ['coverTypes', 'emissionTypes', 'fluxTypes', 'pools', 'reportingFrameworks','units'],
    },    
];

export const sideNavItems: SideNavItems = {
    core: {
        icon: 'tachometer-alt',
        text: 'Core',
        link: '/dashboard',
    },
    coverTypes: {
        icon: 'cog',
        text: 'Cover Types',
        link: '/cover_types',
    },
    emissionTypes: {
        icon: 'cog',
        text: 'Emission Types',
        link: '/emission_types',
    }, 
    fluxTypes: {
        icon: 'cog',
        text: 'Flux Types',
        link: '/flux_types',
    },    
    pools: {
        icon: 'cog',
        text: 'Pools',
        link: '/pools',
    },         
    reportingFrameworks: {
        icon: 'cog',
        text: 'Reporting Frameworks',
        link: '/reporting_frameworks',
    },
    units: {
        icon: 'cog',
        text: 'Units',
        link: '/unit_categories'
    }           
};

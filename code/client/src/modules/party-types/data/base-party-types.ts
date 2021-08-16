import { PartyType } from '../models';

export const BasePartyTypes: PartyType[] = [
    {
        id: 4,
        parentAdministrativeLevelId: null,
        name: 'Administrative Unit',
        plural: 'Administrative Units',
        version: 1, 
        pos: 1
    },
    {
        id: 10,
        parentAdministrativeLevelId: null,
        name: 'Organization',
        plural: 'Organizations',
        version: 1, 
        pos: 2
    },
    {
        id: 1,
        parentAdministrativeLevelId: null,
        name: 'Person',
        plural: 'People',
        version: 1, 
        pos: 3
    },
    {
        id: 18,
        parentAdministrativeLevelId: null,
        name: 'Beneficiary',
        plural: 'Beneficiaries',
        version: 1, 
        pos: 4
    },
    
];

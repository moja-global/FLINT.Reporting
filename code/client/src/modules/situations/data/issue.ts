import { Issue } from '../models';

export const ISSUES: Issue[] = [
    {
        id: 1,
        primaryPhenomenonTypeId: 1,
        secondaryPhenomenonTypeId: 2,
        colorCode: 'default'
    },
    {
        id: 2,
        primaryPhenomenonTypeId: 3,
        secondaryPhenomenonTypeId: -1,
        colorCode: 'default'
    },
    {
        id: 3,
        primaryPhenomenonTypeId: 4,
        secondaryPhenomenonTypeId: 5,
        colorCode: 'blue'
    },
    {
        id: 4,
        primaryPhenomenonTypeId: 6,
        secondaryPhenomenonTypeId: 7,
        colorCode: 'green'
    },
    {
        id: 5,
        primaryPhenomenonTypeId: 8,
        secondaryPhenomenonTypeId: 9,
        colorCode: 'red'
    }
];

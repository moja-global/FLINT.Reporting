import { TestBed } from '@angular/core/testing';
import { EmissionsTypesRecordsGuard } from './emissions-types-records.guard';

const LOG_PREFIX: string = "[Emissions Types Records Guards]";

describe('Emissions Types Records Guards', () => {

    let emissionsTypesRecordsGuard: EmissionsTypesRecordsGuard;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [],
            providers: [EmissionsTypesRecordsGuard],
        });
        emissionsTypesRecordsGuard = TestBed.inject(EmissionsTypesRecordsGuard);
    });

    describe('canActivate', () => {
        it('should return an Observable<boolean>', () => {
            emissionsTypesRecordsGuard.canActivate().subscribe(response => {
                expect(response).toEqual(true);
            });
        });
    });
});

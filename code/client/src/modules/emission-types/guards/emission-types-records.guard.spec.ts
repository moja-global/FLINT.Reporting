import { TestBed } from '@angular/core/testing';
import { EmissionTypesRecordsGuard } from './emission-types-records.guard';

const LOG_PREFIX: string = "[Emission Types Records Guards]";

describe('Emission Types Records Guards', () => {

    let emissionTypesRecordsGuard: EmissionTypesRecordsGuard;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [],
            providers: [EmissionTypesRecordsGuard],
        });
        emissionTypesRecordsGuard = TestBed.inject(EmissionTypesRecordsGuard);
    });

    describe('canActivate', () => {
        it('should return an Observable<boolean>', () => {
            emissionTypesRecordsGuard.canActivate().subscribe(response => {
                expect(response).toEqual(true);
            });
        });
    });
});

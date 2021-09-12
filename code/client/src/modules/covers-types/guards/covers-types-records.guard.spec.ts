import { TestBed } from '@angular/core/testing';
import { CoversTypesRecordsGuard } from './covers-types-records.guard';

const LOG_PREFIX: string = "[Covers Types Records Guards]";

describe('Covers Types Records Guards', () => {

    let coversTypesRecordsGuard: CoversTypesRecordsGuard;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [],
            providers: [CoversTypesRecordsGuard],
        });
        coversTypesRecordsGuard = TestBed.inject(CoversTypesRecordsGuard);
    });

    describe('canActivate', () => {
        it('should return an Observable<boolean>', () => {
            coversTypesRecordsGuard.canActivate().subscribe(response => {
                expect(response).toEqual(true);
            });
        });
    });
});

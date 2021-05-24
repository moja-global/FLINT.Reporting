import { TestBed } from '@angular/core/testing';
import { UnitsRecordsGuard } from './units-records.guard';

const LOG_PREFIX: string = "[Units Records Guards]";

describe('Units Records Guards', () => {

    let unitsRecordsGuard: UnitsRecordsGuard;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [],
            providers: [UnitsRecordsGuard],
        });
        unitsRecordsGuard = TestBed.inject(UnitsRecordsGuard);
    });

    describe('canActivate', () => {
        it('should return an Observable<boolean>', () => {
            unitsRecordsGuard.canActivate().subscribe(response => {
                expect(response).toEqual(true);
            });
        });
    });
});

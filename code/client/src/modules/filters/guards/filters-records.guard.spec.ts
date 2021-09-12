import { TestBed } from '@angular/core/testing';
import { FiltersRecordsGuard } from './filters-records.guard';

const LOG_PREFIX: string = "[Filters Records Guards]";

describe('Filters Records Guards', () => {

    let filtersRecordsGuard: FiltersRecordsGuard;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [],
            providers: [FiltersRecordsGuard],
        });
        filtersRecordsGuard = TestBed.inject(FiltersRecordsGuard);
    });

    describe('canActivate', () => {
        it('should return an Observable<boolean>', () => {
            filtersRecordsGuard.canActivate().subscribe(response => {
                expect(response).toEqual(true);
            });
        });
    });
});

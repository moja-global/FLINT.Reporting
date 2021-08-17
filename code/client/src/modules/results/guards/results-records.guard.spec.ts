import { TestBed } from '@angular/core/testing';
import { ResultsRecordsGuard } from './results-records.guard';

const LOG_PREFIX: string = "[Results Records Guards]";

describe('Results Records Guards', () => {

    let resultsRecordsGuard: ResultsRecordsGuard;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [],
            providers: [ResultsRecordsGuard],
        });
        resultsRecordsGuard = TestBed.inject(ResultsRecordsGuard);
    });

    describe('canActivate', () => {
        it('should return an Observable<boolean>', () => {
            resultsRecordsGuard.canActivate().subscribe(response => {
                expect(response).toEqual(true);
            });
        });
    });
});

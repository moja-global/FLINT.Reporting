import { TestBed } from '@angular/core/testing';
import { ReportingVariablesRecordsGuard } from './reporting-variables-records.guard';

const LOG_PREFIX: string = "[Reporting Variables Records Guards]";

describe('Reporting Variables Records Guards', () => {

    let reportingVariablesRecordsGuard: ReportingVariablesRecordsGuard;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [],
            providers: [ReportingVariablesRecordsGuard],
        });
        reportingVariablesRecordsGuard = TestBed.inject(ReportingVariablesRecordsGuard);
    });

    describe('canActivate', () => {
        it('should return an Observable<boolean>', () => {
            reportingVariablesRecordsGuard.canActivate().subscribe(response => {
                expect(response).toEqual(true);
            });
        });
    });
});

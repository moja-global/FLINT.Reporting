import { TestBed } from '@angular/core/testing';
import { ReportingFrameworksRecordsGuard } from './reporting-frameworks-records.guard';

const LOG_PREFIX: string = "[Reporting Frameworks Records Guards]";

describe('Reporting Frameworks Records Guards', () => {

    let reportingFrameworksRecordsGuard: ReportingFrameworksRecordsGuard;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [],
            providers: [ReportingFrameworksRecordsGuard],
        });
        reportingFrameworksRecordsGuard = TestBed.inject(ReportingFrameworksRecordsGuard);
    });

    describe('canActivate', () => {
        it('should return an Observable<boolean>', () => {
            reportingFrameworksRecordsGuard.canActivate().subscribe(response => {
                expect(response).toEqual(true);
            });
        });
    });
});

import { TestBed } from '@angular/core/testing';
import { ReportingTablesRecordsGuard } from './reporting-tables-records.guard';

const LOG_PREFIX: string = "[Reporting Tables Records Guards]";

describe('Reporting Tables Records Guards', () => {

    let reportingTablesRecordsGuard: ReportingTablesRecordsGuard;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [],
            providers: [ReportingTablesRecordsGuard],
        });
        reportingTablesRecordsGuard = TestBed.inject(ReportingTablesRecordsGuard);
    });

    describe('canActivate', () => {
        it('should return an Observable<boolean>', () => {
            reportingTablesRecordsGuard.canActivate().subscribe(response => {
                expect(response).toEqual(true);
            });
        });
    });
});

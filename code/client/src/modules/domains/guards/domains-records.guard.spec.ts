import { TestBed } from '@angular/core/testing';
import { DomainsRecordsGuard } from './domains-records.guard';

const LOG_PREFIX: string = "[Domains Records Guards]";

describe('Domains Records Guards', () => {

    let domainsRecordsGuard: DomainsRecordsGuard;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [],
            providers: [DomainsRecordsGuard],
        });
        domainsRecordsGuard = TestBed.inject(DomainsRecordsGuard);
    });

    describe('canActivate', () => {
        it('should return an Observable<boolean>', () => {
            domainsRecordsGuard.canActivate().subscribe(response => {
                expect(response).toEqual(true);
            });
        });
    });
});

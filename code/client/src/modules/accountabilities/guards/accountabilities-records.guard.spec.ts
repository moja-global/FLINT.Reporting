import { TestBed } from '@angular/core/testing';
import { AccountabilitiesRecordsGuard } from './accountabilities-records.guard';

const LOG_PREFIX: string = "[Accountabilities Records Guards]";

describe('Accountabilities Records Guards', () => {

    let accountabilitiesRecordsGuard: AccountabilitiesRecordsGuard;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [],
            providers: [AccountabilitiesRecordsGuard],
        });
        accountabilitiesRecordsGuard = TestBed.inject(AccountabilitiesRecordsGuard);
    });

    describe('canActivate', () => {
        it('should return an Observable<boolean>', () => {
            accountabilitiesRecordsGuard.canActivate().subscribe(response => {
                expect(response).toEqual(true);
            });
        });
    });
});

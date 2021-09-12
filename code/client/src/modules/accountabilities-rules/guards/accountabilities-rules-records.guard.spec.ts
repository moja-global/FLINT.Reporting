import { TestBed } from '@angular/core/testing';
import { AccountabilitiesRulesRecordsGuard } from './accountabilities-rules-records.guard';

const LOG_PREFIX: string = "[Accountabilities Rules Records Guards]";

describe(' Accountabilities Rules Records Guards', () => {

    let accountabilitiesRulesRecordsGuard: AccountabilitiesRulesRecordsGuard;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [],
            providers: [AccountabilitiesRulesRecordsGuard],
        });
        accountabilitiesRulesRecordsGuard = TestBed.inject(AccountabilitiesRulesRecordsGuard);
    });

    describe('canActivate', () => {
        it('should return an Observable<boolean>', () => {
            accountabilitiesRulesRecordsGuard.canActivate().subscribe(response => {
                expect(response).toEqual(true);
            });
        });
    });
});

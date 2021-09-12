import { TestBed } from '@angular/core/testing';
import { AccountabilitiesTypesRecordsGuard } from './accountabilities-types-records.guard';

const LOG_PREFIX: string = "[Accountabilities Types Records Guards]";

describe('Accountabilities Types Records Guards', () => {

    let accountabilitiesTypesRecordsGuard: AccountabilitiesTypesRecordsGuard;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [],
            providers: [AccountabilitiesTypesRecordsGuard],
        });
        accountabilitiesTypesRecordsGuard = TestBed.inject(AccountabilitiesTypesRecordsGuard);
    });

    describe('canActivate', () => {
        it('should return an Observable<boolean>', () => {
            accountabilitiesTypesRecordsGuard.canActivate().subscribe(response => {
                expect(response).toEqual(true);
            });
        });
    });
});

import { TestBed } from '@angular/core/testing';
import { PartyTypesRecordsGuard } from './party-types-records.guard';

const LOG_PREFIX: string = "[Party Types Records Guards]";

describe('Party Types Records Guards', () => {

    let partyTypesRecordsGuard: PartyTypesRecordsGuard;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [],
            providers: [PartyTypesRecordsGuard],
        });
        partyTypesRecordsGuard = TestBed.inject(PartyTypesRecordsGuard);
    });

    describe('canActivate', () => {
        it('should return an Observable<boolean>', () => {
            partyTypesRecordsGuard.canActivate().subscribe(response => {
                expect(response).toEqual(true);
            });
        });
    });
});

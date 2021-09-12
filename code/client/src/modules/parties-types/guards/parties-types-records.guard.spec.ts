import { TestBed } from '@angular/core/testing';
import { PartiesTypesRecordsGuard } from './parties-types-records.guard';

const LOG_PREFIX: string = "[Parties Types Records Guards]";

describe('Parties Types Records Guards', () => {

    let partiesTypesRecordsGuard: PartiesTypesRecordsGuard;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [],
            providers: [PartiesTypesRecordsGuard],
        });
        partiesTypesRecordsGuard = TestBed.inject(PartiesTypesRecordsGuard);
    });

    describe('canActivate', () => {
        it('should return an Observable<boolean>', () => {
            partiesTypesRecordsGuard.canActivate().subscribe(response => {
                expect(response).toEqual(true);
            });
        });
    });
});

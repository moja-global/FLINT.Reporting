import { TestBed } from '@angular/core/testing';
import { PartiesRecordsGuard } from './parties-records.guard';

const LOG_PREFIX: string = "[Parties Records Guards]";

describe('Parties Records Guards', () => {

    let partiesRecordsGuard: PartiesRecordsGuard;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [],
            providers: [PartiesRecordsGuard],
        });
        partiesRecordsGuard = TestBed.inject(PartiesRecordsGuard);
    });

    describe('canActivate', () => {
        it('should return an Observable<boolean>', () => {
            partiesRecordsGuard.canActivate().subscribe(response => {
                expect(response).toEqual(true);
            });
        });
    });
});

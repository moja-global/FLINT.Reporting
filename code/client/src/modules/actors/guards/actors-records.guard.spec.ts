import { TestBed } from '@angular/core/testing';
import { ActorsRecordsGuard } from './actors-records.guard';

const LOG_PREFIX: string = "[Actors Records Guards]";

describe('Actors Records Guards', () => {

    let actorsRecordsGuard: ActorsRecordsGuard;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [],
            providers: [ActorsRecordsGuard],
        });
        actorsRecordsGuard = TestBed.inject(ActorsRecordsGuard);
    });

    describe('canActivate', () => {
        it('should return an Observable<boolean>', () => {
            actorsRecordsGuard.canActivate().subscribe(response => {
                expect(response).toEqual(true);
            });
        });
    });
});

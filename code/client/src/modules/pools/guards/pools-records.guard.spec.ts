import { TestBed } from '@angular/core/testing';
import { PoolsRecordsGuard } from './pools-records.guard';

const LOG_PREFIX: string = "[Pools Records Guards]";

describe('Pools Records Guards', () => {

    let poolsRecordsGuard: PoolsRecordsGuard;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [],
            providers: [PoolsRecordsGuard],
        });
        poolsRecordsGuard = TestBed.inject(PoolsRecordsGuard);
    });

    describe('canActivate', () => {
        it('should return an Observable<boolean>', () => {
            poolsRecordsGuard.canActivate().subscribe(response => {
                expect(response).toEqual(true);
            });
        });
    });
});

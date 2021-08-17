import { TestBed } from '@angular/core/testing';
import { SituationsRecordsGuard } from './situations-records.guard';

const LOG_PREFIX: string = "[Situations Records Guards]";

describe('Situations Records Guards', () => {

    let situationsRecordsGuard: SituationsRecordsGuard;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [],
            providers: [SituationsRecordsGuard],
        });
        situationsRecordsGuard = TestBed.inject(SituationsRecordsGuard);
    });

    describe('canActivate', () => {
        it('should return an Observable<boolean>', () => {
            situationsRecordsGuard.canActivate().subscribe(response => {
                expect(response).toEqual(true);
            });
        });
    });
});

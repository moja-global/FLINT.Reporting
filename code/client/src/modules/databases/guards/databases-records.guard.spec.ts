import { TestBed } from '@angular/core/testing';
import { DatabasesRecordsGuard } from './databases-records.guard';

const LOG_PREFIX: string = "[Databases Records Guards]";

describe('Databases Records Guards', () => {

    let databasesRecordsGuard: DatabasesRecordsGuard;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [],
            providers: [DatabasesRecordsGuard],
        });
        databasesRecordsGuard = TestBed.inject(DatabasesRecordsGuard);
    });

    describe('canActivate', () => {
        it('should return an Observable<boolean>', () => {
            databasesRecordsGuard.canActivate().subscribe(response => {
                expect(response).toEqual(true);
            });
        });
    });
});

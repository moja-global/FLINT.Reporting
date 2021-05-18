import { TestBed } from '@angular/core/testing';
import { CoverTypesRecordsGuard } from './cover-types-records.guard';

const LOG_PREFIX: string = "[Cover Types Records Guards]";

describe('Cover Types Records Guards', () => {

    let coverTypesRecordsGuard: CoverTypesRecordsGuard;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [],
            providers: [CoverTypesRecordsGuard],
        });
        coverTypesRecordsGuard = TestBed.inject(CoverTypesRecordsGuard);
    });

    describe('canActivate', () => {
        it('should return an Observable<boolean>', () => {
            coverTypesRecordsGuard.canActivate().subscribe(response => {
                expect(response).toEqual(true);
            });
        });
    });
});

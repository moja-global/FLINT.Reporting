import { TestBed } from '@angular/core/testing';
import { UnitCategoriesRecordsGuard } from './unit-categories-records.guard';

const LOG_PREFIX: string = "[Unit categories Records Guards]";

describe('Unit categories Records Guards', () => {

    let unitCategoriesRecordsGuard: UnitCategoriesRecordsGuard;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [],
            providers: [UnitCategoriesRecordsGuard],
        });
        unitCategoriesRecordsGuard = TestBed.inject(UnitCategoriesRecordsGuard);
    });

    describe('canActivate', () => {
        it('should return an Observable<boolean>', () => {
            unitCategoriesRecordsGuard.canActivate().subscribe(response => {
                expect(response).toEqual(true);
            });
        });
    });
});

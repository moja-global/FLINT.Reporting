import { TestBed } from '@angular/core/testing';
import { LandUseSubcategoriesRecordsGuard } from './land-use-subcategories-records.guard';

const LOG_PREFIX: string = "[Land Use Subcategories Records Guards]";

describe('Land Use Subcategories Records Guards', () => {

    let landUseSubcategoriesRecordsGuard: LandUseSubcategoriesRecordsGuard;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [],
            providers: [LandUseSubcategoriesRecordsGuard],
        });
        landUseSubcategoriesRecordsGuard = TestBed.inject(LandUseSubcategoriesRecordsGuard);
    });

    describe('canActivate', () => {
        it('should return an Observable<boolean>', () => {
            landUseSubcategoriesRecordsGuard.canActivate().subscribe(response => {
                expect(response).toEqual(true);
            });
        });
    });
});

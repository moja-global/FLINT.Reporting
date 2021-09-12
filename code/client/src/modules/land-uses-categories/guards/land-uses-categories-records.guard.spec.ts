import { TestBed } from '@angular/core/testing';
import { LandUsesCategoriesRecordsGuard } from './land-uses-categories-records.guard';

const LOG_PREFIX: string = "[Land Uses Categories Records Guards]";

describe(' Land Uses Categories Records Guards', () => {

    let landUsesCategoriesRecordsGuard: LandUsesCategoriesRecordsGuard;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [],
            providers: [LandUsesCategoriesRecordsGuard],
        });
        landUsesCategoriesRecordsGuard = TestBed.inject(LandUsesCategoriesRecordsGuard);
    });

    describe('canActivate', () => {
        it('should return an Observable<boolean>', () => {
            landUsesCategoriesRecordsGuard.canActivate().subscribe(response => {
                expect(response).toEqual(true);
            });
        });
    });
});

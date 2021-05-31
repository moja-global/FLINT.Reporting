import { TestBed } from '@angular/core/testing';
import { LandUseCategoriesRecordsGuard } from './land-use-categories-records.guard';

const LOG_PREFIX: string = "[Land Use Categories Records Guards]";

describe('Land Use Categories Records Guards', () => {

    let landUseCategoriesRecordsGuard: LandUseCategoriesRecordsGuard;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [],
            providers: [LandUseCategoriesRecordsGuard],
        });
        landUseCategoriesRecordsGuard = TestBed.inject(LandUseCategoriesRecordsGuard);
    });

    describe('canActivate', () => {
        it('should return an Observable<boolean>', () => {
            landUseCategoriesRecordsGuard.canActivate().subscribe(response => {
                expect(response).toEqual(true);
            });
        });
    });
});

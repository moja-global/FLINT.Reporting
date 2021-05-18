import { TestBed } from '@angular/core/testing';

import { HomeGuard } from './home.guard';

describe('Home Guards', () => {
    let errorGuard: HomeGuard;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [],
            providers: [HomeGuard],
        });
        errorGuard = TestBed.inject(HomeGuard);
    });

    describe('canActivate', () => {
        it('should return an Observable<boolean>', () => {
            errorGuard.canActivate().subscribe(response => {
                expect(response).toEqual(true);
            });
        });
    });
});

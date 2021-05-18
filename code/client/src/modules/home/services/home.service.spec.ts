import { TestBed } from '@angular/core/testing';

import { HomeService } from './home.service';

describe('HomeService', () => {
    let errorService: HomeService;

    beforeEach(() => {
        TestBed.configureTestingModule({
            providers: [HomeService],
        });
        errorService = TestBed.inject(HomeService);
    });

    describe('getHome$', () => {
        it('should return Observable<Home>', () => {
            errorService.getHome$().subscribe(response => {
                expect(response).toEqual({});
            });
        });
    });
});

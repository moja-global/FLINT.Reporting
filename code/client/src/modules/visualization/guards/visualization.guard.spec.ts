import { TestBed } from '@angular/core/testing';

import { VisualizationGuard } from './visualization.guard';

describe('Visualization Guards', () => {

    let visualizationGuard: VisualizationGuard;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [],
            providers: [
                VisualizationGuard,
            ],
        });
        visualizationGuard = TestBed.inject(VisualizationGuard);
    });

    describe('canActivate', () => {
        it('should return an Observable<boolean>', () => {
            visualizationGuard.canActivate().subscribe(response => {
                expect(response).toEqual(true);
            });
        });
    });

});

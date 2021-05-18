import { TestBed } from '@angular/core/testing';
import { FluxTypesRecordsGuard } from './flux-types-records.guard';

const LOG_PREFIX: string = "[Flux Types Records Guards]";

describe('Flux Types Records Guards', () => {

    let fluxTypesRecordsGuard: FluxTypesRecordsGuard;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [],
            providers: [FluxTypesRecordsGuard],
        });
        fluxTypesRecordsGuard = TestBed.inject(FluxTypesRecordsGuard);
    });

    describe('canActivate', () => {
        it('should return an Observable<boolean>', () => {
            fluxTypesRecordsGuard.canActivate().subscribe(response => {
                expect(response).toEqual(true);
            });
        });
    });
});

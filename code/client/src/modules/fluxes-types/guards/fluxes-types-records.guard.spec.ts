import { TestBed } from '@angular/core/testing';
import { FluxesTypesRecordsGuard } from './fluxes-types-records.guard';

const LOG_PREFIX: string = "[Fluxes Types Records Guards]";

describe('Fluxes Types Records Guards', () => {

    let fluxesTypesRecordsGuard: FluxesTypesRecordsGuard;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [],
            providers: [FluxesTypesRecordsGuard],
        });
        fluxesTypesRecordsGuard = TestBed.inject(FluxesTypesRecordsGuard);
    });

    describe('canActivate', () => {
        it('should return an Observable<boolean>', () => {
            fluxesTypesRecordsGuard.canActivate().subscribe(response => {
                expect(response).toEqual(true);
            });
        });
    });
});

import { TestBed } from '@angular/core/testing';
import { QuantityObservationsRecordsGuard } from './quantity-observations-records.guard';

const LOG_PREFIX: string = "[Quantity Observations Records Guards]";

describe('Quantity Observations Records Guards', () => {

    let quantityObservationsRecordsGuard: QuantityObservationsRecordsGuard;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [],
            providers: [QuantityObservationsRecordsGuard],
        });
        quantityObservationsRecordsGuard = TestBed.inject(QuantityObservationsRecordsGuard);
    });

    describe('canActivate', () => {
        it('should return an Observable<boolean>', () => {
            quantityObservationsRecordsGuard.canActivate().subscribe(response => {
                expect(response).toEqual(true);
            });
        });
    });
});

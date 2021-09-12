import { TestBed } from '@angular/core/testing';
import { FilterType } from '@common/models';
import { Filter } from '@common/models/filter.model';
import { isEmpty } from 'rxjs/operators';

import { FilterService } from './filter.service';

describe('FilterService', () => {

    let filtersService: FilterService;

    beforeEach(() => {
        TestBed.configureTestingModule({
            providers: [FilterService],
        });
        filtersService = TestBed.inject(FilterService);
    });

    describe('filters$', () => {
        it('should return Observable<Filter[]>', () => {
            filtersService.filters$.subscribe(response => {
                expect(response).toBeDefined();
            });
        });
    });


    describe('sendFilter', () => {
        it('should set the next filter', () => {

            // Define the filter
            let filter: Filter = { "type": FilterType.Error, "filter": "Some Error Filter" };

            // Subscribe to the filter service
            filtersService.filters$.subscribe(response => {

                // Assert that the next filter is the defined filter
                expect(response).toEqual(filter);
            });            

            // Send the filter
            filtersService.sendFilter(filter);



        });
    });

    describe('clearFilters', () => {

        it('should clear all filters', () => {

            // Define the filter
            let filter: Filter = { "type": FilterType.Error, "filter": "Some Error Filter" };

            // Send the filter
            filtersService.sendFilter(filter);

            // Clear the filters
            filtersService.clearFilters();

            // Assert that there are no filters
            filtersService.filters$.pipe(isEmpty()).subscribe(response => {
                expect(response).toEqual(true)
            });
        });
    });
});

import { TestBed } from '@angular/core/testing';

import { DateRangeSelectionService } from './date-range-selection.service';

describe('DateRangeSelectionService', () => {

    let dateRangeService: DateRangeSelectionService;

    beforeEach(() => {
        TestBed.configureTestingModule({
            providers: [DateRangeSelectionService],
        });
        dateRangeService = TestBed.inject(DateRangeSelectionService);
    });

});

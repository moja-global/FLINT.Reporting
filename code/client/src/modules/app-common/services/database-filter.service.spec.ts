import { TestBed } from '@angular/core/testing';
import { DatabaseFilterService } from './database-filter.service';

describe('DatabaseFilterService', () => {

    let themesService: DatabaseFilterService;

    beforeEach(() => {
        TestBed.configureTestingModule({
            providers: [DatabaseFilterService],
        });
        themesService = TestBed.inject(DatabaseFilterService);
    });

});

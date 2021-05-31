import { TestBed } from '@angular/core/testing';
import { take, toArray } from 'rxjs/operators';
import { ThemesService } from './themes.service';

describe('ThemesService', () => {

    let themesService: ThemesService;

    beforeEach(() => {
        TestBed.configureTestingModule({
            providers: [ThemesService],
        });
        themesService = TestBed.inject(ThemesService);
    });

    describe('themes$', () => {
        it('should return Observable<Theme[]>', () => {
            themesService.themes$.subscribe(response => {
                expect(response).toBeDefined();
            });
        });
    });


    describe('setTheme', () => {
        it('should set the next theme', () => {

            // Define theme
            let theme: string = "dark";

            // Subscribe to theme service
            themesService.themes$
            .pipe(
                take(2),
                toArray()
            )
            .subscribe(response => {

                expect(response.length).toEqual(2)
                expect(response[0]).toEqual("light");
                expect(response[1]).toEqual("dark");
            });           

            // Set theme
            themesService.theme = theme;



        });
    });

});

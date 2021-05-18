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

            // Define the theme
            let theme: string = "dark";

            // Subscribe to the theme service
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

            // Set the theme
            themesService.theme = theme;



        });
    });

});

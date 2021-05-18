import { HostListener, ChangeDetectionStrategy, Component, OnInit } from '@angular/core';


@Component({
    selector: 'sb-home',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './home.component.html',
    styleUrls: ['home.component.scss'],
})
export class HomeComponent implements OnInit {


    isMenuCollapsed = true;
    activeFragment = "home";
    monitorScroll: boolean = true;

    navigation: any;
    home: any;
    about: any;
    background: any;
    contacts: any;

    constructor() {
    }

    ngOnInit() {
        this.navigation = document.querySelector('.navbar');
        this.home = document.querySelector('#home');
        this.about = document.querySelector('#about');
        this.background = document.querySelector('#background');
        this.contacts = document.querySelector('#contacts');
    }


    scrollToElement(element: HTMLElement): void {

        //Temporarily halt scroll monitoring
        this.monitorScroll = false;
        
        switch (element.id) {
            case "home":
                this.activeFragment = "home";
                break;
            case "about":
                this.activeFragment = "about";
                break;
            case "projects":
                this.activeFragment = "projects";
                break;
            case "contacts":
                this.activeFragment = "contacts";
                break;
        }
        element.scrollIntoView({ behavior: "smooth", /*inline: "nearest"*/ });

        // Resume scroll monitoring
        this.monitorScroll = true;

    }


    @HostListener('window:scroll', ['$event'])
    onWindowScroll(e: any) {

        // Update Navbar Styling
        if (this.navigation != null) {
            if (window.pageYOffset > this.navigation.clientHeight) {
                this.navigation.classList.add('navbar-shrink');
            } else {
                this.navigation.classList.remove('navbar-shrink');
            }


        }

        // Update Active Navbar Link Styling
        if(this.monitorScroll) {
            if (this.isVisible(this.home)) {
                this.activeFragment = "home";
            } else if (this.isVisible(this.about)) {
                this.activeFragment = "about";
            } else if (this.isVisible(this.background)) {
                this.activeFragment = "background";
            } else if (this.isVisible(this.contacts)) {
                this.activeFragment = "contacts";
            } else {
                this.activeFragment = "";
            }
        }



    }


    isVisible(elem: any) {

        if (elem != null) {

            //console.log(elem.id);

            if (
                elem.offsetWidth +
                elem.offsetHeight +
                elem.getBoundingClientRect().height +
                elem.getBoundingClientRect().width ===
                0
            ) {
                return false;
            }

            const elemCenter = {
                x: elem.getBoundingClientRect().left + elem.offsetWidth / 2,
                y: elem.getBoundingClientRect().top + elem.offsetHeight / 2,
            };

            if (elemCenter.x < 0) {
                return false;
            }

            if (elemCenter.x > (document.documentElement.clientWidth || window.innerWidth)) {
                return false;
            }

            if (elemCenter.y < 0) {
                return false;
            }

            if (elemCenter.y > (document.documentElement.clientHeight || window.innerHeight)) {
                return false;
            }

            let pointContainer: any = document.elementFromPoint(elemCenter.x, elemCenter.y);
            do {
                if (pointContainer === elem) {
                    return true;
                }
            } while ((pointContainer = pointContainer.parentNode));

            return false;
        } else {
            return false;
        }
    }


}

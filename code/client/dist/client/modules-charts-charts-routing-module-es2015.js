(window["webpackJsonp"] = window["webpackJsonp"] || []).push([["modules-charts-charts-routing-module"],{

/***/ "pqGK":
/*!*****************************************************!*\
  !*** ./src/modules/charts/charts-routing.module.ts ***!
  \*****************************************************/
/*! exports provided: ROUTES, ChartsRoutingModule */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "ROUTES", function() { return ROUTES; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "ChartsRoutingModule", function() { return ChartsRoutingModule; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "mrSG");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "8Y7J");
/* harmony import */ var _angular_router__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! @angular/router */ "iInd");
/* harmony import */ var _charts_module__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ./charts.module */ "9OwE");
/* harmony import */ var _containers__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! ./containers */ "HP6o");

/* tslint:disable: ordered-imports*/


/* Module */

/* Containers */

/* Routes */
const ROUTES = [
    {
        path: '',
        canActivate: [],
        component: _containers__WEBPACK_IMPORTED_MODULE_4__["ChartsComponent"],
        data: {
            title: 'Charts - System',
            breadcrumbs: [
                {
                    text: 'Dashboard',
                    link: '/dashboard',
                },
                {
                    text: 'Charts',
                    active: true,
                },
            ],
        },
    },
];
let ChartsRoutingModule = class ChartsRoutingModule {
};
ChartsRoutingModule = Object(tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"])([
    Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["NgModule"])({
        imports: [_charts_module__WEBPACK_IMPORTED_MODULE_3__["ChartsModule"], _angular_router__WEBPACK_IMPORTED_MODULE_2__["RouterModule"].forChild(ROUTES)],
        exports: [_angular_router__WEBPACK_IMPORTED_MODULE_2__["RouterModule"]],
    })
], ChartsRoutingModule);



/***/ })

}]);
//# sourceMappingURL=modules-charts-charts-routing-module-es2015.js.map
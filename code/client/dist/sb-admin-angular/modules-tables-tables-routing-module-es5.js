(function () {
  function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

  (window["webpackJsonp"] = window["webpackJsonp"] || []).push([["modules-tables-tables-routing-module"], {
    /***/
    "XfXi":
    /*!*****************************************************!*\
      !*** ./src/modules/tables/tables-routing.module.ts ***!
      \*****************************************************/

    /*! exports provided: ROUTES, TablesRoutingModule */

    /***/
    function XfXi(module, __webpack_exports__, __webpack_require__) {
      "use strict";

      __webpack_require__.r(__webpack_exports__);
      /* harmony export (binding) */


      __webpack_require__.d(__webpack_exports__, "ROUTES", function () {
        return ROUTES;
      });
      /* harmony export (binding) */


      __webpack_require__.d(__webpack_exports__, "TablesRoutingModule", function () {
        return TablesRoutingModule;
      });
      /* harmony import */


      var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(
      /*! tslib */
      "mrSG");
      /* harmony import */


      var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(
      /*! @angular/core */
      "8Y7J");
      /* harmony import */


      var _angular_router__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(
      /*! @angular/router */
      "iInd");
      /* harmony import */


      var _tables_module__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(
      /*! ./tables.module */
      "Q+db");
      /* harmony import */


      var _containers__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(
      /*! ./containers */
      "xPiU");
      /* tslint:disable: ordered-imports*/

      /* Module */

      /* Containers */

      /* Routes */


      var ROUTES = [{
        path: '',
        canActivate: [],
        component: _containers__WEBPACK_IMPORTED_MODULE_4__["TablesComponent"],
        data: {
          title: 'Tables - System',
          breadcrumbs: [{
            text: 'Dashboard',
            link: '/dashboard'
          }, {
            text: 'Tables',
            active: true
          }]
        }
      }];

      var TablesRoutingModule = function TablesRoutingModule() {
        _classCallCheck(this, TablesRoutingModule);
      };

      TablesRoutingModule = Object(tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"])([Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["NgModule"])({
        imports: [_tables_module__WEBPACK_IMPORTED_MODULE_3__["TablesModule"], _angular_router__WEBPACK_IMPORTED_MODULE_2__["RouterModule"].forChild(ROUTES)],
        exports: [_angular_router__WEBPACK_IMPORTED_MODULE_2__["RouterModule"]]
      })], TablesRoutingModule);
      /***/
    }
  }]);
})();
//# sourceMappingURL=modules-tables-tables-routing-module-es5.js.map
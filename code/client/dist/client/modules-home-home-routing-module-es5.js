(function () {
  function _toConsumableArray(arr) { return _arrayWithoutHoles(arr) || _iterableToArray(arr) || _unsupportedIterableToArray(arr) || _nonIterableSpread(); }

  function _nonIterableSpread() { throw new TypeError("Invalid attempt to spread non-iterable instance.\nIn order to be iterable, non-array objects must have a [Symbol.iterator]() method."); }

  function _unsupportedIterableToArray(o, minLen) { if (!o) return; if (typeof o === "string") return _arrayLikeToArray(o, minLen); var n = Object.prototype.toString.call(o).slice(8, -1); if (n === "Object" && o.constructor) n = o.constructor.name; if (n === "Map" || n === "Set") return Array.from(o); if (n === "Arguments" || /^(?:Ui|I)nt(?:8|16|32)(?:Clamped)?Array$/.test(n)) return _arrayLikeToArray(o, minLen); }

  function _iterableToArray(iter) { if (typeof Symbol !== "undefined" && Symbol.iterator in Object(iter)) return Array.from(iter); }

  function _arrayWithoutHoles(arr) { if (Array.isArray(arr)) return _arrayLikeToArray(arr); }

  function _arrayLikeToArray(arr, len) { if (len == null || len > arr.length) len = arr.length; for (var i = 0, arr2 = new Array(len); i < len; i++) { arr2[i] = arr[i]; } return arr2; }

  function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

  function _defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

  function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

  (window["webpackJsonp"] = window["webpackJsonp"] || []).push([["modules-home-home-routing-module"], {
    /***/
    "Afob":
    /*!******************************************!*\
      !*** ./src/modules/home/guards/index.ts ***!
      \******************************************/

    /*! exports provided: guards, HomeGuard */

    /***/
    function Afob(module, __webpack_exports__, __webpack_require__) {
      "use strict";

      __webpack_require__.r(__webpack_exports__);
      /* harmony export (binding) */


      __webpack_require__.d(__webpack_exports__, "guards", function () {
        return guards;
      });
      /* harmony import */


      var _home_guard__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(
      /*! ./home.guard */
      "SMUz");
      /* harmony reexport (safe) */


      __webpack_require__.d(__webpack_exports__, "HomeGuard", function () {
        return _home_guard__WEBPACK_IMPORTED_MODULE_0__["HomeGuard"];
      });

      var guards = [_home_guard__WEBPACK_IMPORTED_MODULE_0__["HomeGuard"]];
      /***/
    },

    /***/
    "NI2U":
    /*!**************************************************************!*\
      !*** ./src/modules/home/containers/home/home.component.scss ***!
      \**************************************************************/

    /*! exports provided: default */

    /***/
    function NI2U(module, __webpack_exports__, __webpack_require__) {
      "use strict";

      __webpack_require__.r(__webpack_exports__);
      /* harmony default export */


      __webpack_exports__["default"] = "/* \n\nToC\n\n\t- Defaults\n    - Color system\n\t- Escaped Characters    \n\t- Options\n\t- Spacing\n\t- Body\n\t- Links\n\t- Headers\n\t- Paragraphs\n\t- Grids\t\n\t- Components\n\t- Typography\n\t- Tables\n\t- Buttons + Forms\n\t- Buttons\n\t- Forms\n\t- Z-Index\n\t- Navs\n\t- Navbars\n\t- Dropdowns\n\t- Pagination\n\t- Jumbotron\n\t- Cards\n\t- Tooltips\n\t- Popovers\n\t- Toasts\n\t- Badges\n\t- Modals\n\t- Alerts\n\t- Progress Bar\n\t- List Group\n\t- Image Thumbnails\n\t- Figures\n\t- Breadcrumbs\n\t- Carousel\n\t- Spinners\n\t- Close \n\t- Code\n\t- Utilities\n\t- Printing\n\n*/\n/* --------- Defaults  --------- */\n/*@import \"node_modules/bootstrap/scss/functions.scss\";\n@import \"node_modules/bootstrap/scss/variables.scss\";\n@import \"node_modules/bootstrap/scss/mixins.scss\";*/\n/* --------- Color system  --------- */\n/* --------- Escaped Characters  --------- */\n/* --------- Options  --------- */\n/* --------- Spacing  --------- */\n/*$spacers: map-merge(\n  (\n    0: 0,\n    1: ($spacer * .25),\n    2: ($spacer * .5),\n    3: $spacer,\n    4: ($spacer * 1.5),\n    5: ($spacer * 3)\n  ),\n  $spacers\n);*/\n/*$sizes: map-merge(\n  (\n    25: 25%,\n    50: 50%,\n    75: 75%,\n    100: 100%,\n    auto: auto\n  ),\n  $sizes\n);*/\n/* --------- Body  --------- */\n/* --------- Links  --------- */\n/* --------- Headers  --------- */\n/* --------- Paragraphs  --------- */\n/* --------- Grids  --------- */\n/*$grid-breakpoints: (\n    xs: 0,\n    sm: 576px,\n    md: 768px,\n    lg: 992px,\n    xl: 1200px,\n) !default;*/\n/*$container-max-widths: (\n  sm: 540px,\n  md: 720px,\n  lg: 960px,\n  xl: 1140px\n) !default;*/\n.container,\n.container-fluid {\n  padding-left: 1.5rem;\n  padding-right: 1.5rem;\n}\n/* --------- Components  --------- */\n/*$embed-responsive-aspect-ratios: join(\n  (\n    (21 9),\n    (16 9),\n    (4 3),\n    (1 1),\n  ),\n  $embed-responsive-aspect-ratios\n);*/\n/* --------- Typography  --------- */\n@font-face {\n  font-family: \"Cardo\";\n  src: url('Cardo-Regular.ttf') format(\"truetype\");\n  font-weight: 400;\n  font-style: normal;\n}\n@font-face {\n  font-family: \"Cardo\";\n  src: url('Cardo-Bold.ttf') format(\"truetype\");\n  font-weight: 700;\n  font-style: normal;\n}\n@font-face {\n  font-family: \"Cardo\";\n  src: url('Cardo-Italic.ttf') format(\"truetype\");\n  font-weight: 400;\n  font-style: italic;\n}\n@font-face {\n  font-family: \"Josefin Sans\";\n  src: url('JosefinSans-Thin.ttf') format(\"truetype\");\n  font-style: normal;\n  font-weight: 100;\n}\n@font-face {\n  font-family: \"Josefin Sans\";\n  src: url('JosefinSans-ThinItalic.ttf') format(\"truetype\");\n  font-style: italic;\n  font-weight: 100;\n}\n@font-face {\n  font-family: \"Josefin Sans\";\n  src: url('JosefinSans-ExtraLight.ttf') format(\"truetype\");\n  font-style: normal;\n  font-weight: 200;\n}\n@font-face {\n  font-family: \"Josefin Sans\";\n  src: url('JosefinSans-ExtraLightItalic.ttf') format(\"truetype\");\n  font-style: italic;\n  font-weight: 200;\n}\n@font-face {\n  font-family: \"Josefin Sans\";\n  src: url('JosefinSans-Light.ttf') format(\"truetype\");\n  font-style: normal;\n  font-weight: 300;\n}\n@font-face {\n  font-family: \"Josefin Sans\";\n  src: url('JosefinSans-LightItalic.ttf') format(\"truetype\");\n  font-style: italic;\n  font-weight: 300;\n}\n@font-face {\n  font-family: \"Josefin Sans\";\n  src: url('JosefinSans-Regular.ttf') format(\"truetype\");\n  font-style: normal;\n  font-weight: 400;\n}\n@font-face {\n  font-family: \"Josefin Sans\";\n  src: url('JosefinSans-Italic.ttf') format(\"truetype\");\n  font-style: italic;\n  font-weight: 400;\n}\n@font-face {\n  font-family: \"Josefin Sans\";\n  src: url('JosefinSans-Medium.ttf') format(\"truetype\");\n  font-style: normal;\n  font-weight: 500;\n}\n@font-face {\n  font-family: \"Josefin Sans\";\n  src: url('JosefinSans-MediumItalic.ttf') format(\"truetype\");\n  font-style: italic;\n  font-weight: 500;\n}\n@font-face {\n  font-family: \"Josefin Sans\";\n  src: url('JosefinSans-SemiBold.ttf') format(\"truetype\");\n  font-style: normal;\n  font-weight: 600;\n}\n@font-face {\n  font-family: \"Josefin Sans\";\n  src: url('JosefinSans-SemiBoldItalic.ttf') format(\"truetype\");\n  font-style: italic;\n  font-weight: 600;\n}\n@font-face {\n  font-family: \"Josefin Sans\";\n  src: url('JosefinSans-Bold.ttf') format(\"truetype\");\n  font-style: normal;\n  font-weight: 700;\n}\n@font-face {\n  font-family: \"Josefin Sans\";\n  src: url('JosefinSans-BoldItalic.ttf') format(\"truetype\");\n  font-style: italic;\n  font-weight: 700;\n}\n@font-face {\n  font-family: \"Lato\";\n  src: url('Lato-Thin.ttf') format(\"truetype\");\n  font-style: normal;\n  font-weight: 100;\n}\n@font-face {\n  font-family: \"Lato\";\n  src: url('Lato-ThinItalic.ttf') format(\"truetype\");\n  font-style: italic;\n  font-weight: 100;\n}\n@font-face {\n  font-family: \"Lato\";\n  src: url('Lato-Light.ttf') format(\"truetype\");\n  font-style: normal;\n  font-weight: 300;\n}\n@font-face {\n  font-family: \"Lato\";\n  src: url('Lato-LightItalic.ttf') format(\"truetype\");\n  font-style: italic;\n  font-weight: 300;\n}\n@font-face {\n  font-family: \"Lato\";\n  src: url('Lato-Regular.ttf') format(\"truetype\");\n  font-style: normal;\n  font-weight: 400;\n}\n@font-face {\n  font-family: \"Lato\";\n  src: url('Lato-Italic.ttf') format(\"truetype\");\n  font-style: italic;\n  font-weight: 400;\n}\n@font-face {\n  font-family: \"Lato\";\n  src: url('Lato-Bold.ttf') format(\"truetype\");\n  font-style: normal;\n  font-weight: 700;\n}\n@font-face {\n  font-family: \"Lato\";\n  src: url('Lato-BoldItalic.ttf') format(\"truetype\");\n  font-style: italic;\n  font-weight: 700;\n}\n@font-face {\n  font-family: \"Montserrat\";\n  src: url('Montserrat-Thin.ttf') format(\"truetype\");\n  font-style: normal;\n  font-weight: 100;\n}\n@font-face {\n  font-family: \"Montserrat\";\n  src: url('Montserrat-ThinItalic.ttf') format(\"truetype\");\n  font-style: italic;\n  font-weight: 100;\n}\n@font-face {\n  font-family: \"Montserrat\";\n  src: url('Montserrat-ExtraLight.ttf') format(\"truetype\");\n  font-style: normal;\n  font-weight: 200;\n}\n@font-face {\n  font-family: \"Montserrat\";\n  src: url('Montserrat-ExtraLightItalic.ttf') format(\"truetype\");\n  font-style: italic;\n  font-weight: 200;\n}\n@font-face {\n  font-family: \"Montserrat\";\n  src: url('Montserrat-Light.ttf') format(\"truetype\");\n  font-style: normal;\n  font-weight: 300;\n}\n@font-face {\n  font-family: \"Montserrat\";\n  src: url('Montserrat-LightItalic.ttf') format(\"truetype\");\n  font-style: italic;\n  font-weight: 300;\n}\n@font-face {\n  font-family: \"Montserrat\";\n  src: url('Montserrat-Regular.ttf') format(\"truetype\");\n  font-style: normal;\n  font-weight: 400;\n}\n@font-face {\n  font-family: \"Montserrat\";\n  src: url('Montserrat-Italic.ttf') format(\"truetype\");\n  font-style: italic;\n  font-weight: 400;\n}\n@font-face {\n  font-family: \"Montserrat\";\n  src: url('Montserrat-Medium.ttf') format(\"truetype\");\n  font-style: normal;\n  font-weight: 500;\n}\n@font-face {\n  font-family: \"Montserrat\";\n  src: url('Montserrat-MediumItalic.ttf') format(\"truetype\");\n  font-style: italic;\n  font-weight: 500;\n}\n@font-face {\n  font-family: \"Montserrat\";\n  src: url('Montserrat-SemiBold.ttf') format(\"truetype\");\n  font-style: normal;\n  font-weight: 600;\n}\n@font-face {\n  font-family: \"Montserrat\";\n  src: url('Montserrat-SemiBoldItalic.ttf') format(\"truetype\");\n  font-style: italic;\n  font-weight: 600;\n}\n@font-face {\n  font-family: \"Montserrat\";\n  src: url('Montserrat-Bold.ttf') format(\"truetype\");\n  font-style: normal;\n  font-weight: 700;\n}\n@font-face {\n  font-family: \"Montserrat\";\n  src: url('Montserrat-BoldItalic.ttf') format(\"truetype\");\n  font-style: italic;\n  font-weight: 700;\n}\n@font-face {\n  font-family: \"Open Sans\";\n  src: url('OpenSans-Light.ttf') format(\"truetype\");\n  font-style: normal;\n  font-weight: 300;\n}\n@font-face {\n  font-family: \"Open Sans\";\n  src: url('OpenSans-LightItalic.ttf') format(\"truetype\");\n  font-style: italic;\n  font-weight: 300;\n}\n@font-face {\n  font-family: \"Open Sans\";\n  src: url('OpenSans-Regular.ttf') format(\"truetype\");\n  font-style: normal;\n  font-weight: 400;\n}\n@font-face {\n  font-family: \"Open Sans\";\n  src: url('OpenSans-Italic.ttf') format(\"truetype\");\n  font-style: italic;\n  font-weight: 400;\n}\n@font-face {\n  font-family: \"Open Sans\";\n  src: url('OpenSans-Bold.ttf') format(\"truetype\");\n  font-style: normal;\n  font-weight: 700;\n}\n@font-face {\n  font-family: \"Open Sans\";\n  src: url('OpenSans-BoldItalic.ttf') format(\"truetype\");\n  font-style: italic;\n  font-weight: 700;\n}\n@font-face {\n  font-family: \"Open Sans\";\n  src: url('OpenSans-ExtraBold.ttf') format(\"truetype\");\n  font-style: normal;\n  font-weight: 900;\n}\n@font-face {\n  font-family: \"Open Sans\";\n  src: url('OpenSans-ExtraBoldItalic.ttf') format(\"truetype\");\n  font-style: italic;\n  font-weight: 900;\n}\n@font-face {\n  font-family: \"Raleway\";\n  src: url('Raleway-Thin.ttf') format(\"truetype\");\n  font-style: normal;\n  font-weight: 100;\n}\n@font-face {\n  font-family: \"Raleway\";\n  src: url('Raleway-ThinItalic.ttf') format(\"truetype\");\n  font-style: italic;\n  font-weight: 100;\n}\n@font-face {\n  font-family: \"Raleway\";\n  src: url('Raleway-ExtraLight.ttf') format(\"truetype\");\n  font-style: normal;\n  font-weight: 200;\n}\n@font-face {\n  font-family: \"Raleway\";\n  src: url('Raleway-ExtraLightItalic.ttf') format(\"truetype\");\n  font-style: italic;\n  font-weight: 200;\n}\n@font-face {\n  font-family: \"Raleway\";\n  src: url('Raleway-Light.ttf') format(\"truetype\");\n  font-style: normal;\n  font-weight: 300;\n}\n@font-face {\n  font-family: \"Raleway\";\n  src: url('Raleway-LightItalic.ttf') format(\"truetype\");\n  font-style: italic;\n  font-weight: 300;\n}\n@font-face {\n  font-family: \"Raleway\";\n  src: url('Raleway-Regular.ttf') format(\"truetype\");\n  font-style: normal;\n  font-weight: 400;\n}\n@font-face {\n  font-family: \"Raleway\";\n  src: url('Raleway-Italic.ttf') format(\"truetype\");\n  font-style: italic;\n  font-weight: 400;\n}\n@font-face {\n  font-family: \"Raleway\";\n  src: url('Raleway-Medium.ttf') format(\"truetype\");\n  font-style: normal;\n  font-weight: 500;\n}\n@font-face {\n  font-family: \"Raleway\";\n  src: url('Raleway-MediumItalic.ttf') format(\"truetype\");\n  font-style: italic;\n  font-weight: 500;\n}\n@font-face {\n  font-family: \"Raleway\";\n  src: url('Raleway-SemiBold.ttf') format(\"truetype\");\n  font-style: normal;\n  font-weight: 600;\n}\n@font-face {\n  font-family: \"Raleway\";\n  src: url('Raleway-SemiBoldItalic.ttf') format(\"truetype\");\n  font-style: italic;\n  font-weight: 600;\n}\n@font-face {\n  font-family: \"Raleway\";\n  src: url('Raleway-Bold.ttf') format(\"truetype\");\n  font-style: normal;\n  font-weight: 700;\n}\n@font-face {\n  font-family: \"Raleway\";\n  src: url('Raleway-BoldItalic.ttf') format(\"truetype\");\n  font-style: italic;\n  font-weight: 700;\n}\n@font-face {\n  font-family: \"Roboto\";\n  src: url('Roboto-Thin.ttf') format(\"truetype\");\n  font-style: normal;\n  font-weight: 100;\n}\n@font-face {\n  font-family: \"Roboto\";\n  src: url('Roboto-ThinItalic.ttf') format(\"truetype\");\n  font-style: italic;\n  font-weight: 100;\n}\n@font-face {\n  font-family: \"Roboto\";\n  src: url('Roboto-Light.ttf') format(\"truetype\");\n  font-style: normal;\n  font-weight: 300;\n}\n@font-face {\n  font-family: \"Roboto\";\n  src: url('Roboto-LightItalic.ttf') format(\"truetype\");\n  font-style: italic;\n  font-weight: 300;\n}\n@font-face {\n  font-family: \"Roboto\";\n  src: url('Roboto-Regular.ttf') format(\"truetype\");\n  font-style: normal;\n  font-weight: 400;\n}\n@font-face {\n  font-family: \"Roboto\";\n  src: url('Roboto-Italic.ttf') format(\"truetype\");\n  font-style: italic;\n  font-weight: 400;\n}\n@font-face {\n  font-family: \"Roboto\";\n  src: url('Roboto-Medium.ttf') format(\"truetype\");\n  font-style: normal;\n  font-weight: 500;\n}\n@font-face {\n  font-family: \"Roboto\";\n  src: url('Roboto-MediumItalic.ttf') format(\"truetype\");\n  font-style: italic;\n  font-weight: 500;\n}\n@font-face {\n  font-family: \"Roboto\";\n  src: url('Roboto-Bold.ttf') format(\"truetype\");\n  font-style: normal;\n  font-weight: 700;\n}\n@font-face {\n  font-family: \"Roboto\";\n  src: url('Roboto-BoldItalic.ttf') format(\"truetype\");\n  font-style: italic;\n  font-weight: 700;\n}\n@font-face {\n  font-family: \"Roboto Mono\";\n  src: url('RobotoMono-Thin.ttf') format(\"truetype\");\n  font-style: normal;\n  font-weight: 100;\n}\n@font-face {\n  font-family: \"Roboto Mono\";\n  src: url('RobotoMono-ThinItalic.ttf') format(\"truetype\");\n  font-style: italic;\n  font-weight: 100;\n}\n@font-face {\n  font-family: \"Roboto Mono\";\n  src: url('RobotoMono-ExtraLight.ttf') format(\"truetype\");\n  font-style: normal;\n  font-weight: 200;\n}\n@font-face {\n  font-family: \"Roboto Mono\";\n  src: url('RobotoMono-ExtraLightItalic.ttf') format(\"truetype\");\n  font-style: italic;\n  font-weight: 200;\n}\n@font-face {\n  font-family: \"Roboto Mono\";\n  src: url('RobotoMono-Light.ttf') format(\"truetype\");\n  font-style: normal;\n  font-weight: 300;\n}\n@font-face {\n  font-family: \"Roboto Mono\";\n  src: url('RobotoMono-LightItalic.ttf') format(\"truetype\");\n  font-style: italic;\n  font-weight: 300;\n}\n@font-face {\n  font-family: \"Roboto Mono\";\n  src: url('RobotoMono-Regular.ttf') format(\"truetype\");\n  font-style: normal;\n  font-weight: 400;\n}\n@font-face {\n  font-family: \"Roboto Mono\";\n  src: url('RobotoMono-Italic.ttf') format(\"truetype\");\n  font-style: italic;\n  font-weight: 400;\n}\n@font-face {\n  font-family: \"Roboto Mono\";\n  src: url('RobotoMono-Medium.ttf') format(\"truetype\");\n  font-style: normal;\n  font-weight: 500;\n}\n@font-face {\n  font-family: \"Roboto Mono\";\n  src: url('RobotoMono-MediumItalic.ttf') format(\"truetype\");\n  font-style: italic;\n  font-weight: 500;\n}\n@font-face {\n  font-family: \"Roboto Mono\";\n  src: url('RobotoMono-SemiBold.ttf') format(\"truetype\");\n  font-style: normal;\n  font-weight: 600;\n}\n@font-face {\n  font-family: \"Roboto Mono\";\n  src: url('RobotoMono-SemiBoldItalic.ttf') format(\"truetype\");\n  font-style: italic;\n  font-weight: 600;\n}\n@font-face {\n  font-family: \"Roboto Mono\";\n  src: url('RobotoMono-Bold.ttf') format(\"truetype\");\n  font-style: normal;\n  font-weight: 700;\n}\n@font-face {\n  font-family: \"Roboto Mono\";\n  src: url('RobotoMono-BoldItalic.ttf') format(\"truetype\");\n  font-style: italic;\n  font-weight: 700;\n}\n/* --------- Tables  --------- */\n/* --------- Buttons + Forms  --------- */\n/* --------- Buttons  --------- */\n/* --------- Forms  --------- */\n/*$custom-file-text: (\n  en: \"Browse\"\n) !default;*/\n/*$form-validation-states: map-merge(\n  (\n    \"valid\": (\n      \"color\": $form-feedback-valid-color,\n      \"icon\": $form-feedback-icon-valid\n    ),\n    \"invalid\": (\n      \"color\": $form-feedback-invalid-color,\n      \"icon\": $form-feedback-icon-invalid\n    ),\n  ),\n  $form-validation-states\n);*/\n/* --------- Z-Index  --------- */\n/* --------- Navbars  --------- */\n/* --------- Dropdowns  --------- */\n/* --------- Pagination  --------- */\n/* --------- Jumbotron  --------- */\n/* --------- Cards  --------- */\n/* --------- Tooltips  --------- */\n/* --------- Popovers  --------- */\n/* --------- Toasts  --------- */\n/* --------- Badges  --------- */\n/* --------- Modals  --------- */\n/* --------- Alerts  --------- */\n/* --------- Progress Bar  --------- */\n/* --------- List Group  --------- */\n/* --------- Image Thumbnails  --------- */\n/* --------- Figures  --------- */\n/* --------- Breadcrumbs  --------- */\n/* --------- Carousel  --------- */\n/* --------- Spinners  --------- */\n/* --------- Close  --------- */\n/* --------- Code  --------- */\n/* --------- Utilities  --------- */\n/* --------- Printing  --------- */\n.btn {\n  box-shadow: 0 0.1875rem 0.1875rem 0 rgba(0, 0, 0, 0.1) !important;\n  padding: 1.25rem 2rem;\n  font-family: \"Roboto\";\n  font-size: 80%;\n  text-transform: uppercase;\n  letter-spacing: 0.15rem;\n  border: 0;\n}\na:hover {\n  cursor: pointer;\n}\n/*# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIi4uLy4uLy4uLy4uLy4uLy4uLy4uLy4uLy4uL3N0eWxlcy9fdmFyaWFibGVzLnNjc3MiLCIuLi8uLi8uLi8uLi8uLi8uLi8uLi8uLi8uLi9zdHlsZXMvdmFyaWFibGVzL19zcGFjaW5nLnNjc3MiLCIuLi8uLi8uLi8uLi8uLi8uLi8uLi8uLi8uLi9zdHlsZXMvdmFyaWFibGVzL19ncmlkcy5zY3NzIiwiLi4vLi4vLi4vLi4vLi4vaG9tZS5jb21wb25lbnQuc2NzcyIsIi4uLy4uLy4uLy4uLy4uLy4uLy4uLy4uLy4uL3N0eWxlcy92YXJpYWJsZXMvX2NvbXBvbmVudHMuc2NzcyIsIi4uLy4uLy4uLy4uLy4uLy4uLy4uLy4uLy4uL3N0eWxlcy92YXJpYWJsZXMvX3R5cG9ncmFwaHkuc2NzcyIsIi4uLy4uLy4uLy4uLy4uLy4uLy4uLy4uLy4uL3N0eWxlcy92YXJpYWJsZXMvX2Zvcm1zLnNjc3MiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6IkFBQUE7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7OztDQUFBO0FBK0NBLGtDQUFBO0FBU0E7O21EQUFBO0FBS0Esc0NBQUE7QUFJQSw0Q0FBQTtBQUlBLGlDQUFBO0FBSUEsaUNBQUE7QUNqRUE7Ozs7Ozs7Ozs7R0FBQTtBQWNBOzs7Ozs7Ozs7R0FBQTtBRHVEQSw4QkFBQTtBQUlBLCtCQUFBO0FBSUEsaUNBQUE7QUFJQSxvQ0FBQTtBQUlBLCtCQUFBO0FFeEZBOzs7Ozs7WUFBQTtBQTBCQTs7Ozs7WUFBQTtBQVNBOztFQUVFLG9CQW5COEI7RUFvQjlCLHFCQXBCOEI7QUMwRWhDO0FIQUEsb0NBQUE7QUlqRUE7Ozs7Ozs7O0dBQUE7QUpxRUEsb0NBQUE7QUs5RkE7RUFDSSxvQkFBQTtFQUNBLGdEQUFBO0VBQ0EsZ0JBQUE7RUFDQSxrQkFBQTtBRndHSjtBRXJHQTtFQUNJLG9CQUFBO0VBQ0EsNkNBQUE7RUFDQSxnQkFBQTtFQUNBLGtCQUFBO0FGdUdKO0FFcEdBO0VBQ0ksb0JBQUE7RUFD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QkFBQTtBRmNKO0FFWEE7RUFDSSwwQkFBQTtFQUNBLGtEQUFBO0VBQ0Esa0JBQUE7RUFDQSxnQkFBQTtBRmFKO0FFVkE7RUFDSSwwQkFBQTtFQUNBLHdEQUFBO0VBQ0Esa0JBQUE7RUFDQSxnQkFBQTtBRllKO0FIbGdCQSxnQ0FBQTtBQUlBLHlDQUFBO0FBSUEsaUNBQUE7QUFJQSwrQkFBQTtBTStFQTs7WUFBQTtBQWtCQTs7Ozs7Ozs7Ozs7O0dBQUE7QU43RkEsaUNBQUE7QUFJQSxpQ0FBQTtBQUlBLG1DQUFBO0FBSUEsb0NBQUE7QUFJQSxtQ0FBQTtBQUlBLCtCQUFBO0FBSUEsa0NBQUE7QUFJQSxrQ0FBQTtBQUlBLGdDQUFBO0FBSUEsZ0NBQUE7QUFJQSxnQ0FBQTtBQUlBLGdDQUFBO0FBSUEsc0NBQUE7QUFJQSxvQ0FBQTtBQUlBLDBDQUFBO0FBSUEsaUNBQUE7QUFJQSxxQ0FBQTtBQUlBLGtDQUFBO0FBSUEsa0NBQUE7QUFJQSwrQkFBQTtBQUlBLDhCQUFBO0FBSUEsbUNBQUE7QUFJQSxrQ0FBQTtBRy9NQTtFQUNJLGlFQUFBO0VBQ0EscUJBQUE7RUFDQSxxQkVtbUIwQjtFRmxtQjFCLGNBQUE7RUFDQSx5QkFBQTtFQUNBLHVCQUFBO0VBQ0EsU0FBQTtBQXNwQko7QUFucEJBO0VBQ0ksZUFBQTtBQXNwQkoiLCJmaWxlIjoiaG9tZS5jb21wb25lbnQuc2NzcyIsInNvdXJjZXNDb250ZW50IjpbIi8qIFxuXG5Ub0NcblxuXHQtIERlZmF1bHRzXG4gICAgLSBDb2xvciBzeXN0ZW1cblx0LSBFc2NhcGVkIENoYXJhY3RlcnMgICAgXG5cdC0gT3B0aW9uc1xuXHQtIFNwYWNpbmdcblx0LSBCb2R5XG5cdC0gTGlua3Ncblx0LSBIZWFkZXJzXG5cdC0gUGFyYWdyYXBoc1xuXHQtIEdyaWRzXHRcblx0LSBDb21wb25lbnRzXG5cdC0gVHlwb2dyYXBoeVxuXHQtIFRhYmxlc1xuXHQtIEJ1dHRvbnMgKyBGb3Jtc1xuXHQtIEJ1dHRvbnNcblx0LSBGb3Jtc1xuXHQtIFotSW5kZXhcblx0LSBOYXZzXG5cdC0gTmF2YmFyc1xuXHQtIERyb3Bkb3duc1xuXHQtIFBhZ2luYXRpb25cblx0LSBKdW1ib3Ryb25cblx0LSBDYXJkc1xuXHQtIFRvb2x0aXBzXG5cdC0gUG9wb3ZlcnNcblx0LSBUb2FzdHNcblx0LSBCYWRnZXNcblx0LSBNb2RhbHNcblx0LSBBbGVydHNcblx0LSBQcm9ncmVzcyBCYXJcblx0LSBMaXN0IEdyb3VwXG5cdC0gSW1hZ2UgVGh1bWJuYWlsc1xuXHQtIEZpZ3VyZXNcblx0LSBCcmVhZGNydW1ic1xuXHQtIENhcm91c2VsXG5cdC0gU3Bpbm5lcnNcblx0LSBDbG9zZSBcblx0LSBDb2RlXG5cdC0gVXRpbGl0aWVzXG5cdC0gUHJpbnRpbmdcblxuKi9cblxuLyogLS0tLS0tLS0tIERlZmF1bHRzICAtLS0tLS0tLS0gKi9cblxuLy9BbGxcbi8vQGltcG9ydCBcIm5vZGVfbW9kdWxlcy9ib290c3RyYXAvc2Nzcy9ib290c3RyYXAuc2Nzc1wiO1xuQGltcG9ydCBcIm5vZGVfbW9kdWxlcy9ib290c3RyYXAvc2Nzcy9mdW5jdGlvbnMuc2Nzc1wiO1xuQGltcG9ydCBcIm5vZGVfbW9kdWxlcy9ib290c3RyYXAvc2Nzcy92YXJpYWJsZXMuc2Nzc1wiO1xuLy9AaW1wb3J0IFwibm9kZV9tb2R1bGVzL2Jvb3RzdHJhcC9zY3NzL21peGlucy5zY3NzXCI7XG5cbi8vIFJlcXVpcmVkXG4vKkBpbXBvcnQgXCJub2RlX21vZHVsZXMvYm9vdHN0cmFwL3Njc3MvZnVuY3Rpb25zLnNjc3NcIjtcbkBpbXBvcnQgXCJub2RlX21vZHVsZXMvYm9vdHN0cmFwL3Njc3MvdmFyaWFibGVzLnNjc3NcIjtcbkBpbXBvcnQgXCJub2RlX21vZHVsZXMvYm9vdHN0cmFwL3Njc3MvbWl4aW5zLnNjc3NcIjsqL1xuXG5cbi8qIC0tLS0tLS0tLSBDb2xvciBzeXN0ZW0gIC0tLS0tLS0tLSAqL1xuQGltcG9ydCBcIi4vdmFyaWFibGVzL2NvbG9yLXN5c3RlbS5zY3NzXCI7XG5cblxuLyogLS0tLS0tLS0tIEVzY2FwZWQgQ2hhcmFjdGVycyAgLS0tLS0tLS0tICovXG5AaW1wb3J0IFwiLi92YXJpYWJsZXMvZXNjYXBlZC1jaGFyYWN0ZXJzLnNjc3NcIjtcblxuXG4vKiAtLS0tLS0tLS0gT3B0aW9ucyAgLS0tLS0tLS0tICovXG5AaW1wb3J0IFwiLi92YXJpYWJsZXMvb3B0aW9ucy5zY3NzXCI7XG5cblxuLyogLS0tLS0tLS0tIFNwYWNpbmcgIC0tLS0tLS0tLSAqL1xuQGltcG9ydCBcIi4vdmFyaWFibGVzL3NwYWNpbmcuc2Nzc1wiO1xuXG5cbi8qIC0tLS0tLS0tLSBCb2R5ICAtLS0tLS0tLS0gKi9cbkBpbXBvcnQgXCIuL3ZhcmlhYmxlcy9ib2R5LnNjc3NcIjtcblxuXG4vKiAtLS0tLS0tLS0gTGlua3MgIC0tLS0tLS0tLSAqL1xuQGltcG9ydCBcIi4vdmFyaWFibGVzL2xpbmtzLnNjc3NcIjtcblxuXG4vKiAtLS0tLS0tLS0gSGVhZGVycyAgLS0tLS0tLS0tICovXG5AaW1wb3J0IFwiLi92YXJpYWJsZXMvaGVhZGVycy5zY3NzXCI7XG5cblxuLyogLS0tLS0tLS0tIFBhcmFncmFwaHMgIC0tLS0tLS0tLSAqL1xuQGltcG9ydCBcIi4vdmFyaWFibGVzL3BhcmFncmFwaHMuc2Nzc1wiO1xuXG5cbi8qIC0tLS0tLS0tLSBHcmlkcyAgLS0tLS0tLS0tICovXG5AaW1wb3J0IFwiLi92YXJpYWJsZXMvZ3JpZHMuc2Nzc1wiO1xuXG5cbi8qIC0tLS0tLS0tLSBDb21wb25lbnRzICAtLS0tLS0tLS0gKi9cbkBpbXBvcnQgXCIuL3ZhcmlhYmxlcy9jb21wb25lbnRzLnNjc3NcIjtcblxuXG4vKiAtLS0tLS0tLS0gVHlwb2dyYXBoeSAgLS0tLS0tLS0tICovXG5AaW1wb3J0IFwiLi92YXJpYWJsZXMvdHlwb2dyYXBoeS5zY3NzXCI7XG5cblxuLyogLS0tLS0tLS0tIFRhYmxlcyAgLS0tLS0tLS0tICovXG5AaW1wb3J0IFwiLi92YXJpYWJsZXMvdGFibGVzLnNjc3NcIjtcblxuXG4vKiAtLS0tLS0tLS0gQnV0dG9ucyArIEZvcm1zICAtLS0tLS0tLS0gKi9cbkBpbXBvcnQgXCIuL3ZhcmlhYmxlcy9fYnV0dG9ucy1hbmQtZm9ybXMuc2Nzc1wiO1xuXG5cbi8qIC0tLS0tLS0tLSBCdXR0b25zICAtLS0tLS0tLS0gKi9cbkBpbXBvcnQgXCIuL3ZhcmlhYmxlcy9idXR0b25zLnNjc3NcIjtcblxuXG4vKiAtLS0tLS0tLS0gRm9ybXMgIC0tLS0tLS0tLSAqL1xuQGltcG9ydCBcIi4vdmFyaWFibGVzL2Zvcm1zLnNjc3NcIjtcblxuXG4vKiAtLS0tLS0tLS0gWi1JbmRleCAgLS0tLS0tLS0tICovXG5AaW1wb3J0IFwiLi92YXJpYWJsZXMvei1pbmRleC5zY3NzXCI7XG5cblxuLyogLS0tLS0tLS0tIE5hdmJhcnMgIC0tLS0tLS0tLSAqL1xuQGltcG9ydCBcIi4vdmFyaWFibGVzL25hdmJhcnMuc2Nzc1wiO1xuXG5cbi8qIC0tLS0tLS0tLSBEcm9wZG93bnMgIC0tLS0tLS0tLSAqL1xuQGltcG9ydCBcIi4vdmFyaWFibGVzL2Ryb3Bkb3ducy5zY3NzXCI7XG5cblxuLyogLS0tLS0tLS0tIFBhZ2luYXRpb24gIC0tLS0tLS0tLSAqL1xuQGltcG9ydCBcIi4vdmFyaWFibGVzL3BhZ2luYXRpb24uc2Nzc1wiO1xuXG5cbi8qIC0tLS0tLS0tLSBKdW1ib3Ryb24gIC0tLS0tLS0tLSAqL1xuQGltcG9ydCBcIi4vdmFyaWFibGVzL2p1bWJvdHJvbi5zY3NzXCI7XG5cblxuLyogLS0tLS0tLS0tIENhcmRzICAtLS0tLS0tLS0gKi9cbkBpbXBvcnQgXCIuL3ZhcmlhYmxlcy9jYXJkcy5zY3NzXCI7XG5cblxuLyogLS0tLS0tLS0tIFRvb2x0aXBzICAtLS0tLS0tLS0gKi9cbkBpbXBvcnQgXCIuL3ZhcmlhYmxlcy90b29sdGlwcy5zY3NzXCI7XG5cblxuLyogLS0tLS0tLS0tIFBvcG92ZXJzICAtLS0tLS0tLS0gKi9cbkBpbXBvcnQgXCIuL3ZhcmlhYmxlcy9wb3BvdmVycy5zY3NzXCI7XG5cblxuLyogLS0tLS0tLS0tIFRvYXN0cyAgLS0tLS0tLS0tICovXG5AaW1wb3J0IFwiLi92YXJpYWJsZXMvdG9hc3RzLnNjc3NcIjtcblxuXG4vKiAtLS0tLS0tLS0gQmFkZ2VzICAtLS0tLS0tLS0gKi9cbkBpbXBvcnQgXCIuL3ZhcmlhYmxlcy9iYWRnZXMuc2Nzc1wiO1xuXG5cbi8qIC0tLS0tLS0tLSBNb2RhbHMgIC0tLS0tLS0tLSAqL1xuQGltcG9ydCBcIi4vdmFyaWFibGVzL21vZGFscy5zY3NzXCI7XG5cblxuLyogLS0tLS0tLS0tIEFsZXJ0cyAgLS0tLS0tLS0tICovXG5AaW1wb3J0IFwiLi92YXJpYWJsZXMvYWxlcnRzLnNjc3NcIjtcblxuXG4vKiAtLS0tLS0tLS0gUHJvZ3Jlc3MgQmFyICAtLS0tLS0tLS0gKi9cbkBpbXBvcnQgXCIuL3ZhcmlhYmxlcy9wcm9ncmVzcy1iYXIuc2Nzc1wiO1xuXG5cbi8qIC0tLS0tLS0tLSBMaXN0IEdyb3VwICAtLS0tLS0tLS0gKi9cbkBpbXBvcnQgXCIuL3ZhcmlhYmxlcy9saXN0LWdyb3VwLnNjc3NcIjtcblxuXG4vKiAtLS0tLS0tLS0gSW1hZ2UgVGh1bWJuYWlscyAgLS0tLS0tLS0tICovXG5AaW1wb3J0IFwiLi92YXJpYWJsZXMvaW1hZ2UtdGh1bWJuYWlscy5zY3NzXCI7XG5cblxuLyogLS0tLS0tLS0tIEZpZ3VyZXMgIC0tLS0tLS0tLSAqL1xuQGltcG9ydCBcIi4vdmFyaWFibGVzL2ZpZ3VyZXMuc2Nzc1wiO1xuXG5cbi8qIC0tLS0tLS0tLSBCcmVhZGNydW1icyAgLS0tLS0tLS0tICovXG5AaW1wb3J0IFwiLi92YXJpYWJsZXMvYnJlYWRjcnVtYnMuc2Nzc1wiO1xuXG5cbi8qIC0tLS0tLS0tLSBDYXJvdXNlbCAgLS0tLS0tLS0tICovXG5AaW1wb3J0IFwiLi92YXJpYWJsZXMvY2Fyb3VzZWwuc2Nzc1wiO1xuXG5cbi8qIC0tLS0tLS0tLSBTcGlubmVycyAgLS0tLS0tLS0tICovXG5AaW1wb3J0IFwiLi92YXJpYWJsZXMvc3Bpbm5lcnMuc2Nzc1wiO1xuXG5cbi8qIC0tLS0tLS0tLSBDbG9zZSAgLS0tLS0tLS0tICovXG5AaW1wb3J0IFwiLi92YXJpYWJsZXMvY2xvc2Uuc2Nzc1wiO1xuXG5cbi8qIC0tLS0tLS0tLSBDb2RlICAtLS0tLS0tLS0gKi9cbkBpbXBvcnQgXCIuL3ZhcmlhYmxlcy9jb2RlLnNjc3NcIjtcblxuXG4vKiAtLS0tLS0tLS0gVXRpbGl0aWVzICAtLS0tLS0tLS0gKi9cbkBpbXBvcnQgXCIuL3ZhcmlhYmxlcy91dGlsaXRpZXMuc2Nzc1wiO1xuXG5cbi8qIC0tLS0tLS0tLSBQcmludGluZyAgLS0tLS0tLS0tICovXG5AaW1wb3J0IFwiLi92YXJpYWJsZXMvcHJpbnRpbmcuc2Nzc1wiO1xuXG5cblxuXG4iLCIvLyBTcGFjaW5nXG4vL1xuLy8gQ29udHJvbCB0aGUgZGVmYXVsdCBzdHlsaW5nIG9mIG1vc3QgQm9vdHN0cmFwIGVsZW1lbnRzIGJ5IG1vZGlmeWluZyB0aGVzZVxuLy8gdmFyaWFibGVzLiBNb3N0bHkgZm9jdXNlZCBvbiBzcGFjaW5nLlxuLy8gWW91IGNhbiBhZGQgbW9yZSBlbnRyaWVzIHRvIHRoZSAkc3BhY2VycyBtYXAsIHNob3VsZCB5b3UgbmVlZCBtb3JlIHZhcmlhdGlvbi5cblxuLy8gJHNwYWNlcjogMXJlbTtcbi8vJHNwYWNlcnM6ICgpICFkZWZhdWx0O1xuLyokc3BhY2VyczogbWFwLW1lcmdlKFxuICAoXG4gICAgMDogMCxcbiAgICAxOiAoJHNwYWNlciAqIC4yNSksXG4gICAgMjogKCRzcGFjZXIgKiAuNSksXG4gICAgMzogJHNwYWNlcixcbiAgICA0OiAoJHNwYWNlciAqIDEuNSksXG4gICAgNTogKCRzcGFjZXIgKiAzKVxuICApLFxuICAkc3BhY2Vyc1xuKTsqL1xuXG4vLyBUaGlzIHZhcmlhYmxlIGFmZmVjdHMgdGhlIGAuaC0qYCBhbmQgYC53LSpgIGNsYXNzZXMuXG4vLyRzaXplczogKCkgIWRlZmF1bHQ7XG4vKiRzaXplczogbWFwLW1lcmdlKFxuICAoXG4gICAgMjU6IDI1JSxcbiAgICA1MDogNTAlLFxuICAgIDc1OiA3NSUsXG4gICAgMTAwOiAxMDAlLFxuICAgIGF1dG86IGF1dG9cbiAgKSxcbiAgJHNpemVzXG4pOyovXG5cbi8vIEFkanVzdCBjb2x1bW4gc3BhY2luZyBmb3Igc3ltbWV0cnlcbiRzcGFjZXI6IDFyZW07XG4kZ3JpZC1ndXR0ZXItd2lkdGg6ICRzcGFjZXIgKiAxLjU7XG4iLCIvLyBHcmlkIGJyZWFrcG9pbnRzXG4vL1xuLy8gRGVmaW5lIHRoZSBtaW5pbXVtIGRpbWVuc2lvbnMgYXQgd2hpY2ggeW91ciBsYXlvdXQgd2lsbCBjaGFuZ2UsXG4vLyBhZGFwdGluZyB0byBkaWZmZXJlbnQgc2NyZWVuIHNpemVzLCBmb3IgdXNlIGluIG1lZGlhIHF1ZXJpZXMuXG5cbi8qJGdyaWQtYnJlYWtwb2ludHM6IChcbiAgICB4czogMCxcbiAgICBzbTogNTc2cHgsXG4gICAgbWQ6IDc2OHB4LFxuICAgIGxnOiA5OTJweCxcbiAgICB4bDogMTIwMHB4LFxuKSAhZGVmYXVsdDsqL1xuXG4vLyBAaW5jbHVkZSBfYXNzZXJ0LWFzY2VuZGluZygkZ3JpZC1icmVha3BvaW50cywgXCIkZ3JpZC1icmVha3BvaW50c1wiKTtcbi8vIEBpbmNsdWRlIF9hc3NlcnQtc3RhcnRzLWF0LXplcm8oJGdyaWQtYnJlYWtwb2ludHMsIFwiJGdyaWQtYnJlYWtwb2ludHNcIik7XG5cblxuLy8gR3JpZCBjb2x1bW5zXG4vL1xuLy8gU2V0IHRoZSBudW1iZXIgb2YgY29sdW1ucyBhbmQgc3BlY2lmeSB0aGUgd2lkdGggb2YgdGhlIGd1dHRlcnMuXG5cbi8vJGdyaWQtY29sdW1uczogICAgICAgICAgICAgICAgMTIgIWRlZmF1bHQ7XG4vLyRncmlkLWd1dHRlci13aWR0aDogICAgICAgICAgIDMwcHggIWRlZmF1bHQ7XG4kZ3JpZC1ndXR0ZXItd2lkdGg6ICAgICAgICAgICAgICRzcGFjZXIgKiAxLjU7XG4vLyRncmlkLXJvdy1jb2x1bW5zOiAgICAgICAgICAgIDYgIWRlZmF1bHQ7XG5cblxuLy8gR3JpZCBjb250YWluZXJzXG4vL1xuLy8gRGVmaW5lIHRoZSBtYXhpbXVtIHdpZHRoIG9mIGAuY29udGFpbmVyYCBmb3IgZGlmZmVyZW50IHNjcmVlbiBzaXplcy5cblxuLyokY29udGFpbmVyLW1heC13aWR0aHM6IChcbiAgc206IDU0MHB4LFxuICBtZDogNzIwcHgsXG4gIGxnOiA5NjBweCxcbiAgeGw6IDExNDBweFxuKSAhZGVmYXVsdDsqL1xuXG4vLyBAaW5jbHVkZSBfYXNzZXJ0LWFzY2VuZGluZygkY29udGFpbmVyLW1heC13aWR0aHMsIFwiJGNvbnRhaW5lci1tYXgtd2lkdGhzXCIpO1xuXG4uY29udGFpbmVyLFxuLmNvbnRhaW5lci1mbHVpZCB7XG4gIHBhZGRpbmctbGVmdDogJGdyaWQtZ3V0dGVyLXdpZHRoO1xuICBwYWRkaW5nLXJpZ2h0OiAkZ3JpZC1ndXR0ZXItd2lkdGg7XG59IiwiQGltcG9ydCAnc3R5bGVzL3ZhcmlhYmxlcy5zY3NzJztcblxuLmJ0biB7XG4gICAgYm94LXNoYWRvdzogMCAwLjE4NzVyZW0gMC4xODc1cmVtIDAgcmdiYSgwLCAwLCAwLCAwLjEpICFpbXBvcnRhbnQ7XG4gICAgcGFkZGluZzogMS4yNXJlbSAycmVtO1xuICAgIGZvbnQtZmFtaWx5OiAkZm9udC1mYW1pbHktc3BlY2lhbDtcbiAgICBmb250LXNpemU6IDgwJTtcbiAgICB0ZXh0LXRyYW5zZm9ybTogdXBwZXJjYXNlO1xuICAgIGxldHRlci1zcGFjaW5nOiAwLjE1cmVtO1xuICAgIGJvcmRlcjogMDtcbn1cblxuYTpob3ZlcntcbiAgICBjdXJzb3I6IHBvaW50ZXI7XG59IiwiLy8gQ29tcG9uZW50c1xuLy9cbi8vIERlZmluZSBjb21tb24gcGFkZGluZyBhbmQgYm9yZGVyIHJhZGl1cyBzaXplcyBhbmQgbW9yZS5cblxuLy8kbGluZS1oZWlnaHQtbGc6ICAgICAgICAgICAgICAxLjUgIWRlZmF1bHQ7XG4vLyRsaW5lLWhlaWdodC1zbTogICAgICAgICAgICAgIDEuNSAhZGVmYXVsdDtcblxuLy8kYm9yZGVyLXdpZHRoOiAgICAgICAgICAgICAgICAxcHggIWRlZmF1bHQ7XG4vLyRib3JkZXItY29sb3I6ICAgICAgICAgICAgICAgICRncmF5LTMwMCAhZGVmYXVsdDtcblxuLy8kYm9yZGVyLXJhZGl1czogICAgICAgICAgICAgICAuMjVyZW0gIWRlZmF1bHQ7XG4vLyRib3JkZXItcmFkaXVzLWxnOiAgICAgICAgICAgIC4zcmVtICFkZWZhdWx0O1xuLy8kYm9yZGVyLXJhZGl1cy1zbTogICAgICAgICAgICAuMnJlbSAhZGVmYXVsdDtcblxuLy8kcm91bmRlZC1waWxsOiAgICAgICAgICAgICAgICA1MHJlbSAhZGVmYXVsdDtcblxuLy8kYm94LXNoYWRvdy1zbTogICAgICAgICAgICAgICAwIC4xMjVyZW0gLjI1cmVtIHJnYmEoLy8kYmxhY2ssIC4wNzUpICFkZWZhdWx0O1xuLy8kYm94LXNoYWRvdzogICAgICAgICAgICAgICAgICAwIC41cmVtIDFyZW0gcmdiYSgvLyRibGFjaywgLjE1KSAhZGVmYXVsdDtcbi8vJGJveC1zaGFkb3ctbGc6ICAgICAgICAgICAgICAgMCAxcmVtIDNyZW0gcmdiYSgvLyRibGFjaywgLjE3NSkgIWRlZmF1bHQ7XG5cbi8vJGNvbXBvbmVudC1hY3RpdmUtY29sb3I6ICAgICAgJHdoaXRlICFkZWZhdWx0O1xuLy8kY29tcG9uZW50LWFjdGl2ZS1iZzogICAgICAgICB0aGVtZS1jb2xvcihcInByaW1hcnlcIikgIWRlZmF1bHQ7XG5cbi8vJGNhcmV0LXdpZHRoOiAgICAgICAgICAgICAgICAgLjNlbSAhZGVmYXVsdDtcbi8vJGNhcmV0LXZlcnRpY2FsLWFsaWduOiAgICAgICAgJGNhcmV0LXdpZHRoICogLjg1ICFkZWZhdWx0O1xuLy8kY2FyZXQtc3BhY2luZzogICAgICAgICAgICAgICAkY2FyZXQtd2lkdGggKiAuODUgIWRlZmF1bHQ7XG5cbi8vJHRyYW5zaXRpb24tYmFzZTogICAgICAgICAgICAgYWxsIC4ycyBlYXNlLWluLW91dCAhZGVmYXVsdDtcbi8vJHRyYW5zaXRpb24tZmFkZTogICAgICAgICAgICAgb3BhY2l0eSAuMTVzIGxpbmVhciAhZGVmYXVsdDtcbi8vJHRyYW5zaXRpb24tY29sbGFwc2U6ICAgICAgICAgaGVpZ2h0IC4zNXMgZWFzZSAhZGVmYXVsdDtcblxuLy8kZW1iZWQtcmVzcG9uc2l2ZS1hc3BlY3QtcmF0aW9zOiAoKSAhZGVmYXVsdDtcbi8qJGVtYmVkLXJlc3BvbnNpdmUtYXNwZWN0LXJhdGlvczogam9pbihcbiAgKFxuICAgICgyMSA5KSxcbiAgICAoMTYgOSksXG4gICAgKDQgMyksXG4gICAgKDEgMSksXG4gICksXG4gICRlbWJlZC1yZXNwb25zaXZlLWFzcGVjdC1yYXRpb3Ncbik7Ki8iLCIvLyBUeXBvZ3JhcGh5XG4vL1xuLy8gRm9udHNcblxuLy8gU2VyaWZzXG4vL1xuLy8gQ2FyZG9cbkBmb250LWZhY2Uge1xuICAgIGZvbnQtZmFtaWx5OiBcIkNhcmRvXCI7XG4gICAgc3JjOiB1cmwoXCIuLi8uLi9hc3NldHMvZm9udHMvQ2FyZG8vQ2FyZG8tUmVndWxhci50dGZcIikgZm9ybWF0KCd0cnVldHlwZScpO1xuICAgIGZvbnQtd2VpZ2h0OiA0MDA7XG4gICAgZm9udC1zdHlsZTogbm9ybWFsO1xufVxuXG5AZm9udC1mYWNlIHtcbiAgICBmb250LWZhbWlseTogXCJDYXJkb1wiO1xuICAgIHNyYzogdXJsKFwiLi4vLi4vYXNzZXRzL2ZvbnRzL0NhcmRvL0NhcmRvLUJvbGQudHRmXCIpIGZvcm1hdCgndHJ1ZXR5cGUnKTtcbiAgICBmb250LXdlaWdodDogNzAwO1xuICAgIGZvbnQtc3R5bGU6IG5vcm1hbDtcbn1cblxuQGZvbnQtZmFjZSB7XG4gICAgZm9udC1mYW1pbHk6IFwiQ2FyZG9cIjtcbiAgICBzcmM6IHVybChcIi4uLy4uL2Fzc2V0cy9mb250cy9DYXJkby9DYXJkby1JdGFsaWMudHRmXCIpIGZvcm1hdCgndHJ1ZXR5cGUnKTtcbiAgICBmb250LXdlaWdodDogNDAwO1xuICAgIGZvbnQtc3R5bGU6IGl0YWxpYztcbn1cblxuLy8gU2FucyAtIFNlcmlmXG4vL1xuLy8gSm9zZWZpbiBTYW5zXG5AZm9udC1mYWNlIHtcbiAgICBmb250LWZhbWlseTogXCJKb3NlZmluIFNhbnNcIjtcbiAgICBzcmM6IHVybChcIi4uLy4uL2Fzc2V0cy9mb250cy9Kb3NlZmluX1NhbnMvc3RhdGljL0pvc2VmaW5TYW5zLVRoaW4udHRmXCIpIGZvcm1hdChcInRydWV0eXBlXCIpO1xuICAgIGZvbnQtc3R5bGU6IG5vcm1hbDtcbiAgICBmb250LXdlaWdodDogMTAwO1xufVxuXG5AZm9udC1mYWNlIHtcbiAgICBmb250LWZhbWlseTogXCJKb3NlZmluIFNhbnNcIjtcbiAgICBzcmM6IHVybChcIi4uLy4uL2Fzc2V0cy9mb250cy9Kb3NlZmluX1NhbnMvc3RhdGljL0pvc2VmaW5TYW5zLVRoaW5JdGFsaWMudHRmXCIpIGZvcm1hdChcInRydWV0eXBlXCIpO1xuICAgIGZvbnQtc3R5bGU6IGl0YWxpYztcbiAgICBmb250LXdlaWdodDogMTAwO1xufVxuXG5AZm9udC1mYWNlIHtcbiAgICBmb250LWZhbWlseTogXCJKb3NlZmluIFNhbnNcIjtcbiAgICBzcmM6IHVybChcIi4uLy4uL2Fzc2V0cy9mb250cy9Kb3NlZmluX1NhbnMvc3RhdGljL0pvc2VmaW5TYW5zLUV4dHJhTGlnaHQudHRmXCIpIGZvcm1hdChcInRydWV0eXBlXCIpO1xuICAgIGZvbnQtc3R5bGU6IG5vcm1hbDtcbiAgICBmb250LXdlaWdodDogMjAwO1xufVxuXG5AZm9udC1mYWNlIHtcbiAgICBmb250LWZhbWlseTogXCJKb3NlZmluIFNhbnNcIjtcbiAgICBzcmM6IHVybChcIi4uLy4uL2Fzc2V0cy9mb250cy9Kb3NlZmluX1NhbnMvc3RhdGljL0pvc2VmaW5TYW5zLUV4dHJhTGlnaHRJdGFsaWMudHRmXCIpIGZvcm1hdChcInRydWV0eXBlXCIpO1xuICAgIGZvbnQtc3R5bGU6IGl0YWxpYztcbiAgICBmb250LXdlaWdodDogMjAwO1xufVxuXG5AZm9udC1mYWNlIHtcbiAgICBmb250LWZhbWlseTogXCJKb3NlZmluIFNhbnNcIjtcbiAgICBzcmM6IHVybChcIi4uLy4uL2Fzc2V0cy9mb250cy9Kb3NlZmluX1NhbnMvc3RhdGljL0pvc2VmaW5TYW5zLUxpZ2h0LnR0ZlwiKSBmb3JtYXQoXCJ0cnVldHlwZVwiKTtcbiAgICBmb250LXN0eWxlOiBub3JtYWw7XG4gICAgZm9udC13ZWlnaHQ6IDMwMDtcbn1cblxuQGZvbnQtZmFjZSB7XG4gICAgZm9udC1mYW1pbHk6IFwiSm9zZWZpbiBTYW5zXCI7XG4gICAgc3JjOiB1cmwoXCIuLi8uLi9hc3NldHMvZm9udHMvSm9zZWZpbl9TYW5zL3N0YXRpYy9Kb3NlZmluU2Fucy1MaWdodEl0YWxpYy50dGZcIikgZm9ybWF0KFwidHJ1ZXR5cGVcIik7XG4gICAgZm9udC1zdHlsZTogaXRhbGljO1xuICAgIGZvbnQtd2VpZ2h0OiAzMDA7XG59XG5cbkBmb250LWZhY2Uge1xuICAgIGZvbnQtZmFtaWx5OiBcIkpvc2VmaW4gU2Fuc1wiO1xuICAgIHNyYzogdXJsKFwiLi4vLi4vYXNzZXRzL2ZvbnRzL0pvc2VmaW5fU2Fucy9zdGF0aWMvSm9zZWZpblNhbnMtUmVndWxhci50dGZcIikgZm9ybWF0KFwidHJ1ZXR5cGVcIik7XG4gICAgZm9udC1zdHlsZTogbm9ybWFsO1xuICAgIGZvbnQtd2VpZ2h0OiA0MDA7XG59XG5cbkBmb250LWZhY2Uge1xuICAgIGZvbnQtZmFtaWx5OiBcIkpvc2VmaW4gU2Fuc1wiO1xuICAgIHNyYzogdXJsKFwiLi4vLi4vYXNzZXRzL2ZvbnRzL0pvc2VmaW5fU2Fucy9zdGF0aWMvSm9zZWZpblNhbnMtSXRhbGljLnR0ZlwiKSBmb3JtYXQoXCJ0cnVldHlwZVwiKTtcbiAgICBmb250LXN0eWxlOiBpdGFsaWM7XG4gICAgZm9udC13ZWlnaHQ6IDQwMDtcbn1cblxuQGZvbnQtZmFjZSB7XG4gICAgZm9udC1mYW1pbHk6IFwiSm9zZWZpbiBTYW5zXCI7XG4gICAgc3JjOiB1cmwoXCIuLi8uLi9hc3NldHMvZm9udHMvSm9zZWZpbl9TYW5zL3N0YXRpYy9Kb3NlZmluU2Fucy1NZWRpdW0udHRmXCIpIGZvcm1hdChcInRydWV0eXBlXCIpO1xuICAgIGZvbnQtc3R5bGU6IG5vcm1hbDtcbiAgICBmb250LXdlaWdodDogNTAwO1xufVxuXG5AZm9udC1mYWNlIHtcbiAgICBmb250LWZhbWlseTogXCJKb3NlZmluIFNhbnNcIjtcbiAgICBzcmM6IHVybChcIi4uLy4uL2Fzc2V0cy9mb250cy9Kb3NlZmluX1NhbnMvc3RhdGljL0pvc2VmaW5TYW5zLU1lZGl1bUl0YWxpYy50dGZcIikgZm9ybWF0KFwidHJ1ZXR5cGVcIik7XG4gICAgZm9udC1zdHlsZTogaXRhbGljO1xuICAgIGZvbnQtd2VpZ2h0OiA1MDA7XG59XG5cbkBmb250LWZhY2Uge1xuICAgIGZvbnQtZmFtaWx5OiBcIkpvc2VmaW4gU2Fuc1wiO1xuICAgIHNyYzogdXJsKFwiLi4vLi4vYXNzZXRzL2ZvbnRzL0pvc2VmaW5fU2Fucy9zdGF0aWMvSm9zZWZpblNhbnMtU2VtaUJvbGQudHRmXCIpIGZvcm1hdChcInRydWV0eXBlXCIpO1xuICAgIGZvbnQtc3R5bGU6IG5vcm1hbDtcbiAgICBmb250LXdlaWdodDogNjAwO1xufVxuXG5AZm9udC1mYWNlIHtcbiAgICBmb250LWZhbWlseTogXCJKb3NlZmluIFNhbnNcIjtcbiAgICBzcmM6IHVybChcIi4uLy4uL2Fzc2V0cy9mb250cy9Kb3NlZmluX1NhbnMvc3RhdGljL0pvc2VmaW5TYW5zLVNlbWlCb2xkSXRhbGljLnR0ZlwiKSBmb3JtYXQoXCJ0cnVldHlwZVwiKTtcbiAgICBmb250LXN0eWxlOiBpdGFsaWM7XG4gICAgZm9udC13ZWlnaHQ6IDYwMDtcbn1cblxuQGZvbnQtZmFjZSB7XG4gICAgZm9udC1mYW1pbHk6IFwiSm9zZWZpbiBTYW5zXCI7XG4gICAgc3JjOiB1cmwoXCIuLi8uLi9hc3NldHMvZm9udHMvSm9zZWZpbl9TYW5zL3N0YXRpYy9Kb3NlZmluU2Fucy1Cb2xkLnR0ZlwiKSBmb3JtYXQoXCJ0cnVldHlwZVwiKTtcbiAgICBmb250LXN0eWxlOiBub3JtYWw7XG4gICAgZm9udC13ZWlnaHQ6IDcwMDtcbn1cblxuQGZvbnQtZmFjZSB7XG4gICAgZm9udC1mYW1pbHk6IFwiSm9zZWZpbiBTYW5zXCI7XG4gICAgc3JjOiB1cmwoXCIuLi8uLi9hc3NldHMvZm9udHMvSm9zZWZpbl9TYW5zL3N0YXRpYy9Kb3NlZmluU2Fucy1Cb2xkSXRhbGljLnR0ZlwiKSBmb3JtYXQoXCJ0cnVldHlwZVwiKTtcbiAgICBmb250LXN0eWxlOiBpdGFsaWM7XG4gICAgZm9udC13ZWlnaHQ6IDcwMDtcbn1cbi8vIExhdG9cbkBmb250LWZhY2Uge1xuICAgIGZvbnQtZmFtaWx5OiBcIkxhdG9cIjtcbiAgICBzcmM6IHVybChcIi4uLy4uL2Fzc2V0cy9mb250cy9MYXRvL0xhdG8tVGhpbi50dGZcIikgZm9ybWF0KFwidHJ1ZXR5cGVcIik7XG4gICAgZm9udC1zdHlsZTogbm9ybWFsO1xuICAgIGZvbnQtd2VpZ2h0OiAxMDA7XG59XG5cbkBmb250LWZhY2Uge1xuICAgIGZvbnQtZmFtaWx5OiBcIkxhdG9cIjtcbiAgICBzcmM6IHVybChcIi4uLy4uL2Fzc2V0cy9mb250cy9MYXRvL0xhdG8tVGhpbkl0YWxpYy50dGZcIikgZm9ybWF0KFwidHJ1ZXR5cGVcIik7XG4gICAgZm9udC1zdHlsZTogaXRhbGljO1xuICAgIGZvbnQtd2VpZ2h0OiAxMDA7XG59XG5cbkBmb250LWZhY2Uge1xuICAgIGZvbnQtZmFtaWx5OiBcIkxhdG9cIjtcbiAgICBzcmM6IHVybChcIi4uLy4uL2Fzc2V0cy9mb250cy9MYXRvL0xhdG8tTGlnaHQudHRmXCIpIGZvcm1hdChcInRydWV0eXBlXCIpO1xuICAgIGZvbnQtc3R5bGU6IG5vcm1hbDtcbiAgICBmb250LXdlaWdodDogMzAwO1xufVxuXG5AZm9udC1mYWNlIHtcbiAgICBmb250LWZhbWlseTogXCJMYXRvXCI7XG4gICAgc3JjOiB1cmwoXCIuLi8uLi9hc3NldHMvZm9udHMvTGF0by9MYXRvLUxpZ2h0SXRhbGljLnR0ZlwiKSBmb3JtYXQoXCJ0cnVldHlwZVwiKTtcbiAgICBmb250LXN0eWxlOiBpdGFsaWM7XG4gICAgZm9udC13ZWlnaHQ6IDMwMDtcbn1cblxuQGZvbnQtZmFjZSB7XG4gICAgZm9udC1mYW1pbHk6IFwiTGF0b1wiO1xuICAgIHNyYzogdXJsKFwiLi4vLi4vYXNzZXRzL2ZvbnRzL0xhdG8vTGF0by1SZWd1bGFyLnR0ZlwiKSBmb3JtYXQoXCJ0cnVldHlwZVwiKTtcbiAgICBmb250LXN0eWxlOiBub3JtYWw7XG4gICAgZm9udC13ZWlnaHQ6IDQwMDtcbn1cblxuQGZvbnQtZmFjZSB7XG4gICAgZm9udC1mYW1pbHk6IFwiTGF0b1wiO1xuICAgIHNyYzogdXJsKFwiLi4vLi4vYXNzZXRzL2ZvbnRzL0xhdG8vTGF0by1JdGFsaWMudHRmXCIpIGZvcm1hdChcInRydWV0eXBlXCIpO1xuICAgIGZvbnQtc3R5bGU6IGl0YWxpYztcbiAgICBmb250LXdlaWdodDogNDAwO1xufVxuXG5AZm9udC1mYWNlIHtcbiAgICBmb250LWZhbWlseTogXCJMYXRvXCI7XG4gICAgc3JjOiB1cmwoXCIuLi8uLi9hc3NldHMvZm9udHMvTGF0by9MYXRvLUJvbGQudHRmXCIpIGZvcm1hdChcInRydWV0eXBlXCIpO1xuICAgIGZvbnQtc3R5bGU6IG5vcm1hbDtcbiAgICBmb250LXdlaWdodDogNzAwO1xufVxuXG5AZm9udC1mYWNlIHtcbiAgICBmb250LWZhbWlseTogXCJMYXRvXCI7XG4gICAgc3JjOiB1cmwoXCIuLi8uLi9hc3NldHMvZm9udHMvTGF0by9MYXRvLUJvbGRJdGFsaWMudHRmXCIpIGZvcm1hdChcInRydWV0eXBlXCIpO1xuICAgIGZvbnQtc3R5bGU6IGl0YWxpYztcbiAgICBmb250LXdlaWdodDogNzAwO1xufVxuLy9Nb250c2VycmF0XG5AZm9udC1mYWNlIHtcbiAgICBmb250LWZhbWlseTogXCJNb250c2VycmF0XCI7XG4gICAgc3JjOiB1cmwoXCIuLi8uLi9hc3NldHMvZm9udHMvTW9udHNlcnJhdC9Nb250c2VycmF0LVRoaW4udHRmXCIpIGZvcm1hdChcInRydWV0eXBlXCIpO1xuICAgIGZvbnQtc3R5bGU6IG5vcm1hbDtcbiAgICBmb250LXdlaWdodDogMTAwO1xufVxuXG5AZm9udC1mYWNlIHtcbiAgICBmb250LWZhbWlseTogXCJNb250c2VycmF0XCI7XG4gICAgc3JjOiB1cmwoXCIuLi8uLi9hc3NldHMvZm9udHMvTW9udHNlcnJhdC9Nb250c2VycmF0LVRoaW5JdGFsaWMudHRmXCIpIGZvcm1hdChcInRydWV0eXBlXCIpO1xuICAgIGZvbnQtc3R5bGU6IGl0YWxpYztcbiAgICBmb250LXdlaWdodDogMTAwO1xufVxuXG5AZm9udC1mYWNlIHtcbiAgICBmb250LWZhbWlseTogXCJNb250c2VycmF0XCI7XG4gICAgc3JjOiB1cmwoXCIuLi8uLi9hc3NldHMvZm9udHMvTW9udHNlcnJhdC9Nb250c2VycmF0LUV4dHJhTGlnaHQudHRmXCIpIGZvcm1hdChcInRydWV0eXBlXCIpO1xuICAgIGZvbnQtc3R5bGU6IG5vcm1hbDtcbiAgICBmb250LXdlaWdodDogMjAwO1xufVxuXG5AZm9udC1mYWNlIHtcbiAgICBmb250LWZhbWlseTogXCJNb250c2VycmF0XCI7XG4gICAgc3JjOiB1cmwoXCIuLi8uLi9hc3NldHMvZm9udHMvTW9udHNlcnJhdC9Nb250c2VycmF0LUV4dHJhTGlnaHRJdGFsaWMudHRmXCIpIGZvcm1hdChcInRydWV0eXBlXCIpO1xuICAgIGZvbnQtc3R5bGU6IGl0YWxpYztcbiAgICBmb250LXdlaWdodDogMjAwO1xufVxuXG5AZm9udC1mYWNlIHtcbiAgICBmb250LWZhbWlseTogXCJNb250c2VycmF0XCI7XG4gICAgc3JjOiB1cmwoXCIuLi8uLi9hc3NldHMvZm9udHMvTW9udHNlcnJhdC9Nb250c2VycmF0LUxpZ2h0LnR0ZlwiKSBmb3JtYXQoXCJ0cnVldHlwZVwiKTtcbiAgICBmb250LXN0eWxlOiBub3JtYWw7XG4gICAgZm9udC13ZWlnaHQ6IDMwMDtcbn1cblxuQGZvbnQtZmFjZSB7XG4gICAgZm9udC1mYW1pbHk6IFwiTW9udHNlcnJhdFwiO1xuICAgIHNyYzogdXJsKFwiLi4vLi4vYXNzZXRzL2ZvbnRzL01vbnRzZXJyYXQvTW9udHNlcnJhdC1MaWdodEl0YWxpYy50dGZcIikgZm9ybWF0KFwidHJ1ZXR5cGVcIik7XG4gICAgZm9udC1zdHlsZTogaXRhbGljO1xuICAgIGZvbnQtd2VpZ2h0OiAzMDA7XG59XG5cbkBmb250LWZhY2Uge1xuICAgIGZvbnQtZmFtaWx5OiBcIk1vbnRzZXJyYXRcIjtcbiAgICBzcmM6IHVybChcIi4uLy4uL2Fzc2V0cy9mb250cy9Nb250c2VycmF0L01vbnRzZXJyYXQtUmVndWxhci50dGZcIikgZm9ybWF0KFwidHJ1ZXR5cGVcIik7XG4gICAgZm9udC1zdHlsZTogbm9ybWFsO1xuICAgIGZvbnQtd2VpZ2h0OiA0MDA7XG59XG5cbkBmb250LWZhY2Uge1xuICAgIGZvbnQtZmFtaWx5OiBcIk1vbnRzZXJyYXRcIjtcbiAgICBzcmM6IHVybChcIi4uLy4uL2Fzc2V0cy9mb250cy9Nb250c2VycmF0L01vbnRzZXJyYXQtSXRhbGljLnR0ZlwiKSBmb3JtYXQoXCJ0cnVldHlwZVwiKTtcbiAgICBmb250LXN0eWxlOiBpdGFsaWM7XG4gICAgZm9udC13ZWlnaHQ6IDQwMDtcbn1cblxuQGZvbnQtZmFjZSB7XG4gICAgZm9udC1mYW1pbHk6IFwiTW9udHNlcnJhdFwiO1xuICAgIHNyYzogdXJsKFwiLi4vLi4vYXNzZXRzL2ZvbnRzL01vbnRzZXJyYXQvTW9udHNlcnJhdC1NZWRpdW0udHRmXCIpIGZvcm1hdChcInRydWV0eXBlXCIpO1xuICAgIGZvbnQtc3R5bGU6IG5vcm1hbDtcbiAgICBmb250LXdlaWdodDogNTAwO1xufVxuXG5AZm9udC1mYWNlIHtcbiAgICBmb250LWZhbWlseTogXCJNb250c2VycmF0XCI7XG4gICAgc3JjOiB1cmwoXCIuLi8uLi9hc3NldHMvZm9udHMvTW9udHNlcnJhdC9Nb250c2VycmF0LU1lZGl1bUl0YWxpYy50dGZcIikgZm9ybWF0KFwidHJ1ZXR5cGVcIik7XG4gICAgZm9udC1zdHlsZTogaXRhbGljO1xuICAgIGZvbnQtd2VpZ2h0OiA1MDA7XG59XG5cbkBmb250LWZhY2Uge1xuICAgIGZvbnQtZmFtaWx5OiBcIk1vbnRzZXJyYXRcIjtcbiAgICBzcmM6IHVybChcIi4uLy4uL2Fzc2V0cy9mb250cy9Nb250c2VycmF0L01vbnRzZXJyYXQtU2VtaUJvbGQudHRmXCIpIGZvcm1hdChcInRydWV0eXBlXCIpO1xuICAgIGZvbnQtc3R5bGU6IG5vcm1hbDtcbiAgICBmb250LXdlaWdodDogNjAwO1xufVxuXG5AZm9udC1mYWNlIHtcbiAgICBmb250LWZhbWlseTogXCJNb250c2VycmF0XCI7XG4gICAgc3JjOiB1cmwoXCIuLi8uLi9hc3NldHMvZm9udHMvTW9udHNlcnJhdC9Nb250c2VycmF0LVNlbWlCb2xkSXRhbGljLnR0ZlwiKSBmb3JtYXQoXCJ0cnVldHlwZVwiKTtcbiAgICBmb250LXN0eWxlOiBpdGFsaWM7XG4gICAgZm9udC13ZWlnaHQ6IDYwMDtcbn1cblxuQGZvbnQtZmFjZSB7XG4gICAgZm9udC1mYW1pbHk6IFwiTW9udHNlcnJhdFwiO1xuICAgIHNyYzogdXJsKFwiLi4vLi4vYXNzZXRzL2ZvbnRzL01vbnRzZXJyYXQvTW9udHNlcnJhdC1Cb2xkLnR0ZlwiKSBmb3JtYXQoXCJ0cnVldHlwZVwiKTtcbiAgICBmb250LXN0eWxlOiBub3JtYWw7XG4gICAgZm9udC13ZWlnaHQ6IDcwMDtcbn1cblxuQGZvbnQtZmFjZSB7XG4gICAgZm9udC1mYW1pbHk6IFwiTW9udHNlcnJhdFwiO1xuICAgIHNyYzogdXJsKFwiLi4vLi4vYXNzZXRzL2ZvbnRzL01vbnRzZXJyYXQvTW9udHNlcnJhdC1Cb2xkSXRhbGljLnR0ZlwiKSBmb3JtYXQoXCJ0cnVldHlwZVwiKTtcbiAgICBmb250LXN0eWxlOiBpdGFsaWM7XG4gICAgZm9udC13ZWlnaHQ6IDcwMDtcbn1cbi8vIE9wZW4gU2Fuc1xuQGZvbnQtZmFjZSB7XG4gICAgZm9udC1mYW1pbHk6IFwiT3BlbiBTYW5zXCI7XG4gICAgc3JjOiB1cmwoXCIuLi8uLi9hc3NldHMvZm9udHMvT3Blbl9TYW5zL09wZW5TYW5zLUxpZ2h0LnR0ZlwiKSBmb3JtYXQoXCJ0cnVldHlwZVwiKTtcbiAgICBmb250LXN0eWxlOiBub3JtYWw7XG4gICAgZm9udC13ZWlnaHQ6IDMwMDtcbn1cblxuQGZvbnQtZmFjZSB7XG4gICAgZm9udC1mYW1pbHk6IFwiT3BlbiBTYW5zXCI7XG4gICAgc3JjOiB1cmwoXCIuLi8uLi9hc3NldHMvZm9udHMvT3Blbl9TYW5zL09wZW5TYW5zLUxpZ2h0SXRhbGljLnR0ZlwiKSBmb3JtYXQoXCJ0cnVldHlwZVwiKTtcbiAgICBmb250LXN0eWxlOiBpdGFsaWM7XG4gICAgZm9udC13ZWlnaHQ6IDMwMDtcbn1cblxuQGZvbnQtZmFjZSB7XG4gICAgZm9udC1mYW1pbHk6IFwiT3BlbiBTYW5zXCI7XG4gICAgc3JjOiB1cmwoXCIuLi8uLi9hc3NldHMvZm9udHMvT3Blbl9TYW5zL09wZW5TYW5zLVJlZ3VsYXIudHRmXCIpIGZvcm1hdChcInRydWV0eXBlXCIpO1xuICAgIGZvbnQtc3R5bGU6IG5vcm1hbDtcbiAgICBmb250LXdlaWdodDogNDAwO1xufVxuXG5AZm9udC1mYWNlIHtcbiAgICBmb250LWZhbWlseTogXCJPcGVuIFNhbnNcIjtcbiAgICBzcmM6IHVybChcIi4uLy4uL2Fzc2V0cy9mb250cy9PcGVuX1NhbnMvT3BlblNhbnMtSXRhbGljLnR0ZlwiKSBmb3JtYXQoXCJ0cnVldHlwZVwiKTtcbiAgICBmb250LXN0eWxlOiBpdGFsaWM7XG4gICAgZm9udC13ZWlnaHQ6IDQwMDtcbn1cblxuQGZvbnQtZmFjZSB7XG4gICAgZm9udC1mYW1pbHk6IFwiT3BlbiBTYW5zXCI7XG4gICAgc3JjOiB1cmwoXCIuLi8uLi9hc3NldHMvZm9udHMvT3Blbl9TYW5zL09wZW5TYW5zLUJvbGQudHRmXCIpIGZvcm1hdChcInRydWV0eXBlXCIpO1xuICAgIGZvbnQtc3R5bGU6IG5vcm1hbDtcbiAgICBmb250LXdlaWdodDogNzAwO1xufVxuXG5AZm9udC1mYWNlIHtcbiAgICBmb250LWZhbWlseTogXCJPcGVuIFNhbnNcIjtcbiAgICBzcmM6IHVybChcIi4uLy4uL2Fzc2V0cy9mb250cy9PcGVuX1NhbnMvT3BlblNhbnMtQm9sZEl0YWxpYy50dGZcIikgZm9ybWF0KFwidHJ1ZXR5cGVcIik7XG4gICAgZm9udC1zdHlsZTogaXRhbGljO1xuICAgIGZvbnQtd2VpZ2h0OiA3MDA7XG59XG5cblxuQGZvbnQtZmFjZSB7XG4gICAgZm9udC1mYW1pbHk6IFwiT3BlbiBTYW5zXCI7XG4gICAgc3JjOiB1cmwoXCIuLi8uLi9hc3NldHMvZm9udHMvT3Blbl9TYW5zL09wZW5TYW5zLUV4dHJhQm9sZC50dGZcIikgZm9ybWF0KFwidHJ1ZXR5cGVcIik7XG4gICAgZm9udC1zdHlsZTogbm9ybWFsO1xuICAgIGZvbnQtd2VpZ2h0OiA5MDA7XG59XG5cbkBmb250LWZhY2Uge1xuICAgIGZvbnQtZmFtaWx5OiBcIk9wZW4gU2Fuc1wiO1xuICAgIHNyYzogdXJsKFwiLi4vLi4vYXNzZXRzL2ZvbnRzL09wZW5fU2Fucy9PcGVuU2Fucy1FeHRyYUJvbGRJdGFsaWMudHRmXCIpIGZvcm1hdChcInRydWV0eXBlXCIpO1xuICAgIGZvbnQtc3R5bGU6IGl0YWxpYztcbiAgICBmb250LXdlaWdodDogOTAwO1xufVxuLy8gUmFsZXdheVxuQGZvbnQtZmFjZSB7XG4gICAgZm9udC1mYW1pbHk6IFwiUmFsZXdheVwiO1xuICAgIHNyYzogdXJsKFwiLi4vLi4vYXNzZXRzL2ZvbnRzL1JhbGV3YXkvc3RhdGljL1JhbGV3YXktVGhpbi50dGZcIikgZm9ybWF0KFwidHJ1ZXR5cGVcIik7XG4gICAgZm9udC1zdHlsZTogbm9ybWFsO1xuICAgIGZvbnQtd2VpZ2h0OiAxMDA7XG59XG5cbkBmb250LWZhY2Uge1xuICAgIGZvbnQtZmFtaWx5OiBcIlJhbGV3YXlcIjtcbiAgICBzcmM6IHVybChcIi4uLy4uL2Fzc2V0cy9mb250cy9SYWxld2F5L3N0YXRpYy9SYWxld2F5LVRoaW5JdGFsaWMudHRmXCIpIGZvcm1hdChcInRydWV0eXBlXCIpO1xuICAgIGZvbnQtc3R5bGU6IGl0YWxpYztcbiAgICBmb250LXdlaWdodDogMTAwO1xufVxuXG5AZm9udC1mYWNlIHtcbiAgICBmb250LWZhbWlseTogXCJSYWxld2F5XCI7XG4gICAgc3JjOiB1cmwoXCIuLi8uLi9hc3NldHMvZm9udHMvUmFsZXdheS9zdGF0aWMvUmFsZXdheS1FeHRyYUxpZ2h0LnR0ZlwiKSBmb3JtYXQoXCJ0cnVldHlwZVwiKTtcbiAgICBmb250LXN0eWxlOiBub3JtYWw7XG4gICAgZm9udC13ZWlnaHQ6IDIwMDtcbn1cblxuQGZvbnQtZmFjZSB7XG4gICAgZm9udC1mYW1pbHk6IFwiUmFsZXdheVwiO1xuICAgIHNyYzogdXJsKFwiLi4vLi4vYXNzZXRzL2ZvbnRzL1JhbGV3YXkvc3RhdGljL1JhbGV3YXktRXh0cmFMaWdodEl0YWxpYy50dGZcIikgZm9ybWF0KFwidHJ1ZXR5cGVcIik7XG4gICAgZm9udC1zdHlsZTogaXRhbGljO1xuICAgIGZvbnQtd2VpZ2h0OiAyMDA7XG59XG5cbkBmb250LWZhY2Uge1xuICAgIGZvbnQtZmFtaWx5OiBcIlJhbGV3YXlcIjtcbiAgICBzcmM6IHVybChcIi4uLy4uL2Fzc2V0cy9mb250cy9SYWxld2F5L3N0YXRpYy9SYWxld2F5LUxpZ2h0LnR0ZlwiKSBmb3JtYXQoXCJ0cnVldHlwZVwiKTtcbiAgICBmb250LXN0eWxlOiBub3JtYWw7XG4gICAgZm9udC13ZWlnaHQ6IDMwMDtcbn1cblxuQGZvbnQtZmFjZSB7XG4gICAgZm9udC1mYW1pbHk6IFwiUmFsZXdheVwiO1xuICAgIHNyYzogdXJsKFwiLi4vLi4vYXNzZXRzL2ZvbnRzL1JhbGV3YXkvc3RhdGljL1JhbGV3YXktTGlnaHRJdGFsaWMudHRmXCIpIGZvcm1hdChcInRydWV0eXBlXCIpO1xuICAgIGZvbnQtc3R5bGU6IGl0YWxpYztcbiAgICBmb250LXdlaWdodDogMzAwO1xufVxuXG5AZm9udC1mYWNlIHtcbiAgICBmb250LWZhbWlseTogXCJSYWxld2F5XCI7XG4gICAgc3JjOiB1cmwoXCIuLi8uLi9hc3NldHMvZm9udHMvUmFsZXdheS9zdGF0aWMvUmFsZXdheS1SZWd1bGFyLnR0ZlwiKSBmb3JtYXQoXCJ0cnVldHlwZVwiKTtcbiAgICBmb250LXN0eWxlOiBub3JtYWw7XG4gICAgZm9udC13ZWlnaHQ6IDQwMDtcbn1cblxuQGZvbnQtZmFjZSB7XG4gICAgZm9udC1mYW1pbHk6IFwiUmFsZXdheVwiO1xuICAgIHNyYzogdXJsKFwiLi4vLi4vYXNzZXRzL2ZvbnRzL1JhbGV3YXkvc3RhdGljL1JhbGV3YXktSXRhbGljLnR0ZlwiKSBmb3JtYXQoXCJ0cnVldHlwZVwiKTtcbiAgICBmb250LXN0eWxlOiBpdGFsaWM7XG4gICAgZm9udC13ZWlnaHQ6IDQwMDtcbn1cblxuQGZvbnQtZmFjZSB7XG4gICAgZm9udC1mYW1pbHk6IFwiUmFsZXdheVwiO1xuICAgIHNyYzogdXJsKFwiLi4vLi4vYXNzZXRzL2ZvbnRzL1JhbGV3YXkvc3RhdGljL1JhbGV3YXktTWVkaXVtLnR0ZlwiKSBmb3JtYXQoXCJ0cnVldHlwZVwiKTtcbiAgICBmb250LXN0eWxlOiBub3JtYWw7XG4gICAgZm9udC13ZWlnaHQ6IDUwMDtcbn1cblxuQGZvbnQtZmFjZSB7XG4gICAgZm9udC1mYW1pbHk6IFwiUmFsZXdheVwiO1xuICAgIHNyYzogdXJsKFwiLi4vLi4vYXNzZXRzL2ZvbnRzL1JhbGV3YXkvc3RhdGljL1JhbGV3YXktTWVkaXVtSXRhbGljLnR0ZlwiKSBmb3JtYXQoXCJ0cnVldHlwZVwiKTtcbiAgICBmb250LXN0eWxlOiBpdGFsaWM7XG4gICAgZm9udC13ZWlnaHQ6IDUwMDtcbn1cblxuQGZvbnQtZmFjZSB7XG4gICAgZm9udC1mYW1pbHk6IFwiUmFsZXdheVwiO1xuICAgIHNyYzogdXJsKFwiLi4vLi4vYXNzZXRzL2ZvbnRzL1JhbGV3YXkvc3RhdGljL1JhbGV3YXktU2VtaUJvbGQudHRmXCIpIGZvcm1hdChcInRydWV0eXBlXCIpO1xuICAgIGZvbnQtc3R5bGU6IG5vcm1hbDtcbiAgICBmb250LXdlaWdodDogNjAwO1xufVxuXG5AZm9udC1mYWNlIHtcbiAgICBmb250LWZhbWlseTogXCJSYWxld2F5XCI7XG4gICAgc3JjOiB1cmwoXCIuLi8uLi9hc3NldHMvZm9udHMvUmFsZXdheS9zdGF0aWMvUmFsZXdheS1TZW1pQm9sZEl0YWxpYy50dGZcIikgZm9ybWF0KFwidHJ1ZXR5cGVcIik7XG4gICAgZm9udC1zdHlsZTogaXRhbGljO1xuICAgIGZvbnQtd2VpZ2h0OiA2MDA7XG59XG5cbkBmb250LWZhY2Uge1xuICAgIGZvbnQtZmFtaWx5OiBcIlJhbGV3YXlcIjtcbiAgICBzcmM6IHVybChcIi4uLy4uL2Fzc2V0cy9mb250cy9SYWxld2F5L3N0YXRpYy9SYWxld2F5LUJvbGQudHRmXCIpIGZvcm1hdChcInRydWV0eXBlXCIpO1xuICAgIGZvbnQtc3R5bGU6IG5vcm1hbDtcbiAgICBmb250LXdlaWdodDogNzAwO1xufVxuXG5AZm9udC1mYWNlIHtcbiAgICBmb250LWZhbWlseTogXCJSYWxld2F5XCI7XG4gICAgc3JjOiB1cmwoXCIuLi8uLi9hc3NldHMvZm9udHMvUmFsZXdheS9zdGF0aWMvUmFsZXdheS1Cb2xkSXRhbGljLnR0ZlwiKSBmb3JtYXQoXCJ0cnVldHlwZVwiKTtcbiAgICBmb250LXN0eWxlOiBpdGFsaWM7XG4gICAgZm9udC13ZWlnaHQ6IDcwMDtcbn1cbi8vUm9ib3RvXG4vL1JvYm90b1xuQGZvbnQtZmFjZSB7XG4gICAgZm9udC1mYW1pbHk6IFwiUm9ib3RvXCI7XG4gICAgc3JjOiB1cmwoXCIuLi8uLi9hc3NldHMvZm9udHMvUm9ib3RvL1JvYm90by1UaGluLnR0ZlwiKSBmb3JtYXQoXCJ0cnVldHlwZVwiKTtcbiAgICBmb250LXN0eWxlOiBub3JtYWw7XG4gICAgZm9udC13ZWlnaHQ6IDEwMDtcbn1cblxuQGZvbnQtZmFjZSB7XG4gICAgZm9udC1mYW1pbHk6IFwiUm9ib3RvXCI7XG4gICAgc3JjOiB1cmwoXCIuLi8uLi9hc3NldHMvZm9udHMvUm9ib3RvL1JvYm90by1UaGluSXRhbGljLnR0ZlwiKSBmb3JtYXQoXCJ0cnVldHlwZVwiKTtcbiAgICBmb250LXN0eWxlOiBpdGFsaWM7XG4gICAgZm9udC13ZWlnaHQ6IDEwMDtcbn1cblxuQGZvbnQtZmFjZSB7XG4gICAgZm9udC1mYW1pbHk6IFwiUm9ib3RvXCI7XG4gICAgc3JjOiB1cmwoXCIuLi8uLi9hc3NldHMvZm9udHMvUm9ib3RvL1JvYm90by1MaWdodC50dGZcIikgZm9ybWF0KFwidHJ1ZXR5cGVcIik7XG4gICAgZm9udC1zdHlsZTogbm9ybWFsO1xuICAgIGZvbnQtd2VpZ2h0OiAzMDA7XG59XG5cbkBmb250LWZhY2Uge1xuICAgIGZvbnQtZmFtaWx5OiBcIlJvYm90b1wiO1xuICAgIHNyYzogdXJsKFwiLi4vLi4vYXNzZXRzL2ZvbnRzL1JvYm90by9Sb2JvdG8tTGlnaHRJdGFsaWMudHRmXCIpIGZvcm1hdChcInRydWV0eXBlXCIpO1xuICAgIGZvbnQtc3R5bGU6IGl0YWxpYztcbiAgICBmb250LXdlaWdodDogMzAwO1xufVxuXG5AZm9udC1mYWNlIHtcbiAgICBmb250LWZhbWlseTogXCJSb2JvdG9cIjtcbiAgICBzcmM6IHVybChcIi4uLy4uL2Fzc2V0cy9mb250cy9Sb2JvdG8vUm9ib3RvLVJlZ3VsYXIudHRmXCIpIGZvcm1hdChcInRydWV0eXBlXCIpO1xuICAgIGZvbnQtc3R5bGU6IG5vcm1hbDtcbiAgICBmb250LXdlaWdodDogNDAwO1xufVxuXG5AZm9udC1mYWNlIHtcbiAgICBmb250LWZhbWlseTogXCJSb2JvdG9cIjtcbiAgICBzcmM6IHVybChcIi4uLy4uL2Fzc2V0cy9mb250cy9Sb2JvdG8vUm9ib3RvLUl0YWxpYy50dGZcIikgZm9ybWF0KFwidHJ1ZXR5cGVcIik7XG4gICAgZm9udC1zdHlsZTogaXRhbGljO1xuICAgIGZvbnQtd2VpZ2h0OiA0MDA7XG59XG5cbkBmb250LWZhY2Uge1xuICAgIGZvbnQtZmFtaWx5OiBcIlJvYm90b1wiO1xuICAgIHNyYzogdXJsKFwiLi4vLi4vYXNzZXRzL2ZvbnRzL1JvYm90by9Sb2JvdG8tTWVkaXVtLnR0ZlwiKSBmb3JtYXQoXCJ0cnVldHlwZVwiKTtcbiAgICBmb250LXN0eWxlOiBub3JtYWw7XG4gICAgZm9udC13ZWlnaHQ6IDUwMDtcbn1cblxuQGZvbnQtZmFjZSB7XG4gICAgZm9udC1mYW1pbHk6IFwiUm9ib3RvXCI7XG4gICAgc3JjOiB1cmwoXCIuLi8uLi9hc3NldHMvZm9udHMvUm9ib3RvL1JvYm90by1NZWRpdW1JdGFsaWMudHRmXCIpIGZvcm1hdChcInRydWV0eXBlXCIpO1xuICAgIGZvbnQtc3R5bGU6IGl0YWxpYztcbiAgICBmb250LXdlaWdodDogNTAwO1xufVxuXG5AZm9udC1mYWNlIHtcbiAgICBmb250LWZhbWlseTogXCJSb2JvdG9cIjtcbiAgICBzcmM6IHVybChcIi4uLy4uL2Fzc2V0cy9mb250cy9Sb2JvdG8vUm9ib3RvLUJvbGQudHRmXCIpIGZvcm1hdChcInRydWV0eXBlXCIpO1xuICAgIGZvbnQtc3R5bGU6IG5vcm1hbDtcbiAgICBmb250LXdlaWdodDogNzAwO1xufVxuXG5AZm9udC1mYWNlIHtcbiAgICBmb250LWZhbWlseTogXCJSb2JvdG9cIjtcbiAgICBzcmM6IHVybChcIi4uLy4uL2Fzc2V0cy9mb250cy9Sb2JvdG8vUm9ib3RvLUJvbGRJdGFsaWMudHRmXCIpIGZvcm1hdChcInRydWV0eXBlXCIpO1xuICAgIGZvbnQtc3R5bGU6IGl0YWxpYztcbiAgICBmb250LXdlaWdodDogNzAwO1xufVxuXG4vLyBNb25vc3BhY2Vcbi8vXG4vLyBSb2JvdG8gTW9ub1xuQGZvbnQtZmFjZSB7XG4gICAgZm9udC1mYW1pbHk6IFwiUm9ib3RvIE1vbm9cIjtcbiAgICBzcmM6IHVybChcIi4uLy4uL2Fzc2V0cy9mb250cy9Sb2JvdG9fTW9uby9zdGF0aWMvUm9ib3RvTW9uby1UaGluLnR0ZlwiKSBmb3JtYXQoXCJ0cnVldHlwZVwiKTtcbiAgICBmb250LXN0eWxlOiBub3JtYWw7XG4gICAgZm9udC13ZWlnaHQ6IDEwMDtcbn1cblxuQGZvbnQtZmFjZSB7XG4gICAgZm9udC1mYW1pbHk6IFwiUm9ib3RvIE1vbm9cIjtcbiAgICBzcmM6IHVybChcIi4uLy4uL2Fzc2V0cy9mb250cy9Sb2JvdG9fTW9uby9zdGF0aWMvUm9ib3RvTW9uby1UaGluSXRhbGljLnR0ZlwiKSBmb3JtYXQoXCJ0cnVldHlwZVwiKTtcbiAgICBmb250LXN0eWxlOiBpdGFsaWM7XG4gICAgZm9udC13ZWlnaHQ6IDEwMDtcbn1cblxuQGZvbnQtZmFjZSB7XG4gICAgZm9udC1mYW1pbHk6IFwiUm9ib3RvIE1vbm9cIjtcbiAgICBzcmM6IHVybChcIi4uLy4uL2Fzc2V0cy9mb250cy9Sb2JvdG9fTW9uby9zdGF0aWMvUm9ib3RvTW9uby1FeHRyYUxpZ2h0LnR0ZlwiKSBmb3JtYXQoXCJ0cnVldHlwZVwiKTtcbiAgICBmb250LXN0eWxlOiBub3JtYWw7XG4gICAgZm9udC13ZWlnaHQ6IDIwMDtcbn1cblxuQGZvbnQtZmFjZSB7XG4gICAgZm9udC1mYW1pbHk6IFwiUm9ib3RvIE1vbm9cIjtcbiAgICBzcmM6IHVybChcIi4uLy4uL2Fzc2V0cy9mb250cy9Sb2JvdG9fTW9uby9zdGF0aWMvUm9ib3RvTW9uby1FeHRyYUxpZ2h0SXRhbGljLnR0ZlwiKSBmb3JtYXQoXCJ0cnVldHlwZVwiKTtcbiAgICBmb250LXN0eWxlOiBpdGFsaWM7XG4gICAgZm9udC13ZWlnaHQ6IDIwMDtcbn1cblxuQGZvbnQtZmFjZSB7XG4gICAgZm9udC1mYW1pbHk6IFwiUm9ib3RvIE1vbm9cIjtcbiAgICBzcmM6IHVybChcIi4uLy4uL2Fzc2V0cy9mb250cy9Sb2JvdG9fTW9uby9zdGF0aWMvUm9ib3RvTW9uby1MaWdodC50dGZcIikgZm9ybWF0KFwidHJ1ZXR5cGVcIik7XG4gICAgZm9udC1zdHlsZTogbm9ybWFsO1xuICAgIGZvbnQtd2VpZ2h0OiAzMDA7XG59XG5cbkBmb250LWZhY2Uge1xuICAgIGZvbnQtZmFtaWx5OiBcIlJvYm90byBNb25vXCI7XG4gICAgc3JjOiB1cmwoXCIuLi8uLi9hc3NldHMvZm9udHMvUm9ib3RvX01vbm8vc3RhdGljL1JvYm90b01vbm8tTGlnaHRJdGFsaWMudHRmXCIpIGZvcm1hdChcInRydWV0eXBlXCIpO1xuICAgIGZvbnQtc3R5bGU6IGl0YWxpYztcbiAgICBmb250LXdlaWdodDogMzAwO1xufVxuXG5AZm9udC1mYWNlIHtcbiAgICBmb250LWZhbWlseTogXCJSb2JvdG8gTW9ub1wiO1xuICAgIHNyYzogdXJsKFwiLi4vLi4vYXNzZXRzL2ZvbnRzL1JvYm90b19Nb25vL3N0YXRpYy9Sb2JvdG9Nb25vLVJlZ3VsYXIudHRmXCIpIGZvcm1hdChcInRydWV0eXBlXCIpO1xuICAgIGZvbnQtc3R5bGU6IG5vcm1hbDtcbiAgICBmb250LXdlaWdodDogNDAwO1xufVxuXG5AZm9udC1mYWNlIHtcbiAgICBmb250LWZhbWlseTogXCJSb2JvdG8gTW9ub1wiO1xuICAgIHNyYzogdXJsKFwiLi4vLi4vYXNzZXRzL2ZvbnRzL1JvYm90b19Nb25vL3N0YXRpYy9Sb2JvdG9Nb25vLUl0YWxpYy50dGZcIikgZm9ybWF0KFwidHJ1ZXR5cGVcIik7XG4gICAgZm9udC1zdHlsZTogaXRhbGljO1xuICAgIGZvbnQtd2VpZ2h0OiA0MDA7XG59XG5cbkBmb250LWZhY2Uge1xuICAgIGZvbnQtZmFtaWx5OiBcIlJvYm90byBNb25vXCI7XG4gICAgc3JjOiB1cmwoXCIuLi8uLi9hc3NldHMvZm9udHMvUm9ib3RvX01vbm8vc3RhdGljL1JvYm90b01vbm8tTWVkaXVtLnR0ZlwiKSBmb3JtYXQoXCJ0cnVldHlwZVwiKTtcbiAgICBmb250LXN0eWxlOiBub3JtYWw7XG4gICAgZm9udC13ZWlnaHQ6IDUwMDtcbn1cblxuQGZvbnQtZmFjZSB7XG4gICAgZm9udC1mYW1pbHk6IFwiUm9ib3RvIE1vbm9cIjtcbiAgICBzcmM6IHVybChcIi4uLy4uL2Fzc2V0cy9mb250cy9Sb2JvdG9fTW9uby9zdGF0aWMvUm9ib3RvTW9uby1NZWRpdW1JdGFsaWMudHRmXCIpIGZvcm1hdChcInRydWV0eXBlXCIpO1xuICAgIGZvbnQtc3R5bGU6IGl0YWxpYztcbiAgICBmb250LXdlaWdodDogNTAwO1xufVxuXG5AZm9udC1mYWNlIHtcbiAgICBmb250LWZhbWlseTogXCJSb2JvdG8gTW9ub1wiO1xuICAgIHNyYzogdXJsKFwiLi4vLi4vYXNzZXRzL2ZvbnRzL1JvYm90b19Nb25vL3N0YXRpYy9Sb2JvdG9Nb25vLVNlbWlCb2xkLnR0ZlwiKSBmb3JtYXQoXCJ0cnVldHlwZVwiKTtcbiAgICBmb250LXN0eWxlOiBub3JtYWw7XG4gICAgZm9udC13ZWlnaHQ6IDYwMDtcbn1cblxuQGZvbnQtZmFjZSB7XG4gICAgZm9udC1mYW1pbHk6IFwiUm9ib3RvIE1vbm9cIjtcbiAgICBzcmM6IHVybChcIi4uLy4uL2Fzc2V0cy9mb250cy9Sb2JvdG9fTW9uby9zdGF0aWMvUm9ib3RvTW9uby1TZW1pQm9sZEl0YWxpYy50dGZcIikgZm9ybWF0KFwidHJ1ZXR5cGVcIik7XG4gICAgZm9udC1zdHlsZTogaXRhbGljO1xuICAgIGZvbnQtd2VpZ2h0OiA2MDA7XG59XG5cbkBmb250LWZhY2Uge1xuICAgIGZvbnQtZmFtaWx5OiBcIlJvYm90byBNb25vXCI7XG4gICAgc3JjOiB1cmwoXCIuLi8uLi9hc3NldHMvZm9udHMvUm9ib3RvX01vbm8vc3RhdGljL1JvYm90b01vbm8tQm9sZC50dGZcIikgZm9ybWF0KFwidHJ1ZXR5cGVcIik7XG4gICAgZm9udC1zdHlsZTogbm9ybWFsO1xuICAgIGZvbnQtd2VpZ2h0OiA3MDA7XG59XG5cbkBmb250LWZhY2Uge1xuICAgIGZvbnQtZmFtaWx5OiBcIlJvYm90byBNb25vXCI7XG4gICAgc3JjOiB1cmwoXCIuLi8uLi9hc3NldHMvZm9udHMvUm9ib3RvX01vbm8vc3RhdGljL1JvYm90b01vbm8tQm9sZEl0YWxpYy50dGZcIikgZm9ybWF0KFwidHJ1ZXR5cGVcIik7XG4gICAgZm9udC1zdHlsZTogaXRhbGljO1xuICAgIGZvbnQtd2VpZ2h0OiA3MDA7XG59XG5cbi8vIEZvbnQsIGxpbmUtaGVpZ2h0LCBhbmQgY29sb3IgZm9yIGJvZHkgdGV4dCwgaGVhZGluZ3MsIGFuZCBtb3JlLlxuXG4vLyBzdHlsZWxpbnQtZGlzYWJsZSB2YWx1ZS1rZXl3b3JkLWNhc2Vcbi8vJGZvbnQtZmFtaWx5LXNhbnMtc2VyaWY6ICAgICAgXCJKb3NlZmluIFNhbnNcIjtcbiRmb250LWZhbWlseS1zYW5zLXNlcmlmOiAgICAgIFwiUmFsZXdheVwiO1xuJGZvbnQtZmFtaWx5LW1vbm9zcGFjZTogICAgICAgXCJSb2JvdG8gTW9ub1wiO1xuJGZvbnQtZmFtaWx5LXNwZWNpYWw6ICAgICAgICAgXCJSb2JvdG9cIjtcbiRmb250LWZhbWlseS1iYXNlOiAgICAgICAgICAgICRmb250LWZhbWlseS1zYW5zLXNlcmlmO1xuXG4kYmFzZS1sZXR0ZXItc3BhY2luZzogMC4wNjI1ZW07XG5cbi8vIHN0eWxlbGludC1lbmFibGUgdmFsdWUta2V5d29yZC1jYXNlXG5cbiRmb250LXNpemUtYmFzZTogICAgICAgICAgICAgICAgMXJlbTsgLy8gQXNzdW1lcyB0aGUgYnJvd3NlciBkZWZhdWx0LCB0eXBpY2FsbHkgYDE2cHhgXG4vLyRmb250LXNpemUtbGc6ICAgICAgICAgICAgICAgICRmb250LXNpemUtYmFzZSAqIDEuMjUgIWRlZmF1bHQ7XG4vLyRmb250LXNpemUtc206ICAgICAgICAgICAgICAgICRmb250LXNpemUtYmFzZSAqIC44NzUgIWRlZmF1bHQ7XG5cbi8vJGZvbnQtd2VpZ2h0LWxpZ2h0ZXI6ICAgICAgICAgbGlnaHRlciAhZGVmYXVsdDtcbi8vJGZvbnQtd2VpZ2h0LWxpZ2h0OiAgICAgICAgICAgMzAwICFkZWZhdWx0O1xuLy8kZm9udC13ZWlnaHQtbm9ybWFsOiAgICAgICAgICA0MDAgIWRlZmF1bHQ7XG4vLyRmb250LXdlaWdodC1ib2xkOiAgICAgICAgICAgIDcwMCAhZGVmYXVsdDtcbi8vJGZvbnQtd2VpZ2h0LWJvbGRlcjogICAgICAgICAgYm9sZGVyICFkZWZhdWx0O1xuXG4kZm9udC13ZWlnaHQtYmFzZTogICAgICAgICAgICAgICRmb250LXdlaWdodC1ub3JtYWw7XG4vLyRsaW5lLWhlaWdodC1iYXNlOiAgICAgICAgICAgIDEuNSAhZGVmYXVsdDtcblxuLy8kaDEtZm9udC1zaXplOiAgICAgICAgICAgICAgICAkZm9udC1zaXplLWJhc2UgKiAyLjUgIWRlZmF1bHQ7XG4kaDEtZm9udC1zaXplOiAgICAgICAgICAgICAgICAgICRmb250LXNpemUtYmFzZSAqIDEuODc1O1xuLy8kaDItZm9udC1zaXplOiAgICAgICAgICAgICAgICAkZm9udC1zaXplLWJhc2UgKiAyICFkZWZhdWx0O1xuLy8kaDItZm9udC1zaXplOiAgICAgICAgICAgICAgICAkZm9udC1zaXplLWJhc2UgKiAyICFkZWZhdWx0O1xuLy8kaDMtZm9udC1zaXplOiAgICAgICAgICAgICAgICAkZm9udC1zaXplLWJhc2UgKiAxLjc1ICFkZWZhdWx0O1xuLy8kaDMtZm9udC1zaXplOiAgICAgICAgICAgICAgICAkZm9udC1zaXplLWJhc2UgKiAxLjc1ICFkZWZhdWx0O1xuLy8kaDQtZm9udC1zaXplOiAgICAgICAgICAgICAgICAkZm9udC1zaXplLWJhc2UgKiAxLjUgIWRlZmF1bHQ7XG4vLyRoNC1mb250LXNpemU6ICAgICAgICAgICAgICAgICRmb250LXNpemUtYmFzZSAqIDEuNSAhZGVmYXVsdDtcbi8vJGg1LWZvbnQtc2l6ZTogICAgICAgICAgICAgICAgJGZvbnQtc2l6ZS1iYXNlICogMS4yNSAhZGVmYXVsdDtcbi8vJGg1LWZvbnQtc2l6ZTogICAgICAgICAgICAgICAgJGZvbnQtc2l6ZS1iYXNlICogMS4yNSAhZGVmYXVsdDtcbi8vJGg2LWZvbnQtc2l6ZTogICAgICAgICAgICAgICAgJGZvbnQtc2l6ZS1iYXNlICFkZWZhdWx0O1xuLy8kaDYtZm9udC1zaXplOiAgICAgICAgICAgICAgICAkZm9udC1zaXplLWJhc2UgIWRlZmF1bHQ7XG5cbi8vJGhlYWRpbmdzLW1hcmdpbi1ib3R0b206ICAgICAgJHNwYWNlciAvIDIgIWRlZmF1bHQ7XG4kaGVhZGluZ3MtZm9udC1mYW1pbHk6ICAgICAgICAgIFwiUmFsZXdheVwiO1xuJGhlYWRpbmdzLWZvbnQtd2VpZ2h0OiAgICAgICAgICA0MDA7XG4vLyRoZWFkaW5ncy1saW5lLWhlaWdodDogICAgICAgIDEuMiAhZGVmYXVsdDtcbiRoZWFkaW5ncy1jb2xvcjogICAgICAgICAgICAgICAgJGdyYXktNzAwO1xuXG4vLyRkaXNwbGF5MS1zaXplOiAgICAgICAgICAgICAgIDZyZW0gIWRlZmF1bHQ7XG4vLyRkaXNwbGF5Mi1zaXplOiAgICAgICAgICAgICAgIDUuNXJlbSAhZGVmYXVsdDtcbi8vJGRpc3BsYXkzLXNpemU6ICAgICAgICAgICAgICAgNC41cmVtICFkZWZhdWx0O1xuLy8kZGlzcGxheTQtc2l6ZTogICAgICAgICAgICAgICAzLjVyZW0gIWRlZmF1bHQ7XG5cbi8vJGRpc3BsYXkxLXdlaWdodDogICAgICAgICAgICAgMzAwICFkZWZhdWx0O1xuLy8kZGlzcGxheTItd2VpZ2h0OiAgICAgICAgICAgICAzMDAgIWRlZmF1bHQ7XG4vLyRkaXNwbGF5My13ZWlnaHQ6ICAgICAgICAgICAgIDMwMCAhZGVmYXVsdDtcbi8vJGRpc3BsYXk0LXdlaWdodDogICAgICAgICAgICAgMzAwICFkZWZhdWx0O1xuLy8kZGlzcGxheS1saW5lLWhlaWdodDogICAgICAgICAkaGVhZGluZ3MtbGluZS1oZWlnaHQgIWRlZmF1bHQ7XG5cbi8vJGxlYWQtZm9udC1zaXplOiAgICAgICAgICAgICAgLy8kZm9udC1zaXplLWJhc2UgKiAxLjI1ICFkZWZhdWx0O1xuLy8kbGVhZC1mb250LXdlaWdodDogICAgICAgICAgICAzMDAgIWRlZmF1bHQ7XG5cbi8vJHNtYWxsLWZvbnQtc2l6ZTogICAgICAgICAgICAgODAlICFkZWZhdWx0O1xuXG4vLyR0ZXh0LW11dGVkOiAgICAgICAgICAgICAgICAgICRncmF5LTYwMCAhZGVmYXVsdDtcblxuLy8kYmxvY2txdW90ZS1zbWFsbC1jb2xvcjogICAgICAkZ3JheS02MDAgIWRlZmF1bHQ7XG4vLyRibG9ja3F1b3RlLXNtYWxsLWZvbnQtc2l6ZTogICRzbWFsbC1mb250LXNpemUgIWRlZmF1bHQ7XG4vLyRibG9ja3F1b3RlLWZvbnQtc2l6ZTogICAgICAgICRmb250LXNpemUtYmFzZSAqIDEuMjUgIWRlZmF1bHQ7XG5cbi8vJGhyLWJvcmRlci1jb2xvcjogICAgICAgICAgICAgcmdiYSgkYmxhY2ssIC4xKSAhZGVmYXVsdDtcbi8vJGhyLWJvcmRlci13aWR0aDogICAgICAgICAgICAgJGJvcmRlci13aWR0aCAhZGVmYXVsdDtcblxuLy8kbWFyay1wYWRkaW5nOiAgICAgICAgICAgICAgICAuMmVtICFkZWZhdWx0O1xuXG4vLyRkdC1mb250LXdlaWdodDogICAgICAgICAgICAgICRmb250LXdlaWdodC1ib2xkICFkZWZhdWx0O1xuXG4vLyRrYmQtYm94LXNoYWRvdzogICAgICAgICAgICAgIGluc2V0IDAgLS4xcmVtIDAgcmdiYSgvLyRibGFjaywgLjI1KSAhZGVmYXVsdDtcbi8vJG5lc3RlZC1rYmQtZm9udC13ZWlnaHQ6ICAgICAgJGZvbnQtd2VpZ2h0LWJvbGQgIWRlZmF1bHQ7XG5cbi8vJGxpc3QtaW5saW5lLXBhZGRpbmc6ICAgICAgICAgLjVyZW0gIWRlZmF1bHQ7XG5cbi8vJG1hcmstYmc6ICAgICAgICAgICAgICAgICAgICAgI2ZjZjhlMyAhZGVmYXVsdDtcblxuLy8kaHItbWFyZ2luLXk6ICAgICAgICAgICAgICAgICAkc3BhY2VyICFkZWZhdWx0O1xuIiwiLy8gRm9ybXNcblxuLy8kbGFiZWwtbWFyZ2luLWJvdHRvbTogICAgICAgICAgICAgICAgICAgLjVyZW0gIWRlZmF1bHQ7XG5cbi8vJGlucHV0LXBhZGRpbmcteTogICAgICAgICAgICAgICAgICAgICAgICRpbnB1dC1idG4tcGFkZGluZy15ICFkZWZhdWx0O1xuLy8kaW5wdXQtcGFkZGluZy14OiAgICAgICAgICAgICAgICAgICAgICAgJGlucHV0LWJ0bi1wYWRkaW5nLXggIWRlZmF1bHQ7XG4vLyRpbnB1dC1mb250LWZhbWlseTogICAgICAgICAgICAgICAgICAgICAkaW5wdXQtYnRuLWZvbnQtZmFtaWx5ICFkZWZhdWx0O1xuLy8kaW5wdXQtZm9udC1zaXplOiAgICAgICAgICAgICAgICAgICAgICAgJGlucHV0LWJ0bi1mb250LXNpemUgIWRlZmF1bHQ7XG4vLyRpbnB1dC1mb250LXdlaWdodDogICAgICAgICAgICAgICAgICAgICAkZm9udC13ZWlnaHQtYmFzZSAhZGVmYXVsdDtcbi8vJGlucHV0LWxpbmUtaGVpZ2h0OiAgICAgICAgICAgICAgICAgICAgICRpbnB1dC1idG4tbGluZS1oZWlnaHQgIWRlZmF1bHQ7XG5cbi8vJGlucHV0LXBhZGRpbmcteS1zbTogICAgICAgICAgICAgICAgICAgICRpbnB1dC1idG4tcGFkZGluZy15LXNtICFkZWZhdWx0O1xuLy8kaW5wdXQtcGFkZGluZy14LXNtOiAgICAgICAgICAgICAgICAgICAgJGlucHV0LWJ0bi1wYWRkaW5nLXgtc20gIWRlZmF1bHQ7XG4vLyRpbnB1dC1mb250LXNpemUtc206ICAgICAgICAgICAgICAgICAgICAkaW5wdXQtYnRuLWZvbnQtc2l6ZS1zbSAhZGVmYXVsdDtcbi8vJGlucHV0LWxpbmUtaGVpZ2h0LXNtOiAgICAgICAgICAgICAgICAgICRpbnB1dC1idG4tbGluZS1oZWlnaHQtc20gIWRlZmF1bHQ7XG5cbi8vJGlucHV0LXBhZGRpbmcteS1sZzogICAgICAgICAgICAgICAgICAgICRpbnB1dC1idG4tcGFkZGluZy15LWxnICFkZWZhdWx0O1xuLy8kaW5wdXQtcGFkZGluZy14LWxnOiAgICAgICAgICAgICAgICAgICAgJGlucHV0LWJ0bi1wYWRkaW5nLXgtbGcgIWRlZmF1bHQ7XG4vLyRpbnB1dC1mb250LXNpemUtbGc6ICAgICAgICAgICAgICAgICAgICAkaW5wdXQtYnRuLWZvbnQtc2l6ZS1sZyAhZGVmYXVsdDtcbi8vJGlucHV0LWxpbmUtaGVpZ2h0LWxnOiAgICAgICAgICAgICAgICAgICRpbnB1dC1idG4tbGluZS1oZWlnaHQtbGcgIWRlZmF1bHQ7XG5cbi8vJGlucHV0LWJnOiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICR3aGl0ZSAhZGVmYXVsdDtcbi8vJGlucHV0LWRpc2FibGVkLWJnOiAgICAgICAgICAgICAgICAgICAgICRncmF5LTIwMCAhZGVmYXVsdDtcblxuLy8kaW5wdXQtY29sb3I6ICAgICAgICAgICAgICAgICAgICAgICAgICAgJGdyYXktNzAwICFkZWZhdWx0O1xuLy8kaW5wdXQtYm9yZGVyLWNvbG9yOiAgICAgICAgICAgICAgICAgICAgJGdyYXktNDAwICFkZWZhdWx0O1xuLy8kaW5wdXQtYm9yZGVyLXdpZHRoOiAgICAgICAgICAgICAgICAgICAgJGlucHV0LWJ0bi1ib3JkZXItd2lkdGggIWRlZmF1bHQ7XG4vLyRpbnB1dC1ib3gtc2hhZG93OiAgICAgICAgICAgICAgICAgICAgICBpbnNldCAwIDFweCAxcHggcmdiYSgkYmxhY2ssIC4wNzUpICFkZWZhdWx0O1xuXG4vLyRpbnB1dC1ib3JkZXItcmFkaXVzOiAgICAgICAgICAgICAgICAgICAkYm9yZGVyLXJhZGl1cyAhZGVmYXVsdDtcbi8vJGlucHV0LWJvcmRlci1yYWRpdXMtbGc6ICAgICAgICAgICAgICAgICRib3JkZXItcmFkaXVzLWxnICFkZWZhdWx0O1xuLy8kaW5wdXQtYm9yZGVyLXJhZGl1cy1zbTogICAgICAgICAgICAgICAgJGJvcmRlci1yYWRpdXMtc20gIWRlZmF1bHQ7XG5cbi8vJGlucHV0LWZvY3VzLWJnOiAgICAgICAgICAgICAgICAgICAgICAgICRpbnB1dC1iZyAhZGVmYXVsdDtcbi8vJGlucHV0LWZvY3VzLWJvcmRlci1jb2xvcjogICAgICAgICAgICAgIGxpZ2h0ZW4oJGNvbXBvbmVudC1hY3RpdmUtYmcsIDI1JSkgIWRlZmF1bHQ7XG4vLyRpbnB1dC1mb2N1cy1jb2xvcjogICAgICAgICAgICAgICAgICAgICAkaW5wdXQtY29sb3IgIWRlZmF1bHQ7XG4vLyRpbnB1dC1mb2N1cy13aWR0aDogICAgICAgICAgICAgICAgICAgICAkaW5wdXQtYnRuLWZvY3VzLXdpZHRoICFkZWZhdWx0O1xuLy8kaW5wdXQtZm9jdXMtYm94LXNoYWRvdzogICAgICAgICAgICAgICAgJGlucHV0LWJ0bi1mb2N1cy1ib3gtc2hhZG93ICFkZWZhdWx0O1xuXG4vLyRpbnB1dC1wbGFjZWhvbGRlci1jb2xvcjogICAgICAgICAgICAgICAkZ3JheS02MDAgIWRlZmF1bHQ7XG4vLyRpbnB1dC1wbGFpbnRleHQtY29sb3I6ICAgICAgICAgICAgICAgICAkYm9keS1jb2xvciAhZGVmYXVsdDtcblxuLy8kaW5wdXQtaGVpZ2h0LWJvcmRlcjogICAgICAgICAgICAgICAgICAgJGlucHV0LWJvcmRlci13aWR0aCAqIDIgIWRlZmF1bHQ7XG5cbi8vJGlucHV0LWhlaWdodC1pbm5lcjogICAgICAgICAgICAgICAgICAgIGFkZCgkaW5wdXQtbGluZS1oZWlnaHQgKiAxZW0sICRpbnB1dC1wYWRkaW5nLXkgKiAyKSAhZGVmYXVsdDtcbi8vJGlucHV0LWhlaWdodC1pbm5lci1oYWxmOiAgICAgICAgICAgICAgIGFkZCgkaW5wdXQtbGluZS1oZWlnaHQgKiAuNWVtLCAkaW5wdXQtcGFkZGluZy15KSAhZGVmYXVsdDtcbi8vJGlucHV0LWhlaWdodC1pbm5lci1xdWFydGVyOiAgICAgICAgICAgIGFkZCgkaW5wdXQtbGluZS1oZWlnaHQgKiAuMjVlbSwgJGlucHV0LXBhZGRpbmcteSAvIDIpICFkZWZhdWx0O1xuXG4vLyRpbnB1dC1oZWlnaHQ6ICAgICAgICAgICAgICAgICAgICAgICAgICBhZGQoJGlucHV0LWxpbmUtaGVpZ2h0ICogMWVtLCBhZGQoJGlucHV0LXBhZGRpbmcteSAqIDIsICRpbnB1dC1oZWlnaHQtYm9yZGVyLCBmYWxzZSkpICFkZWZhdWx0O1xuLy8kaW5wdXQtaGVpZ2h0LXNtOiAgICAgICAgICAgICAgICAgICAgICAgYWRkKCRpbnB1dC1saW5lLWhlaWdodC1zbSAqIDFlbSwgYWRkKCRpbnB1dC1wYWRkaW5nLXktc20gKiAyLCAkaW5wdXQtaGVpZ2h0LWJvcmRlciwgZmFsc2UpKSAhZGVmYXVsdDtcbi8vJGlucHV0LWhlaWdodC1sZzogICAgICAgICAgICAgICAgICAgICAgIGFkZCgkaW5wdXQtbGluZS1oZWlnaHQtbGcgKiAxZW0sIGFkZCgkaW5wdXQtcGFkZGluZy15LWxnICogMiwgJGlucHV0LWhlaWdodC1ib3JkZXIsIGZhbHNlKSkgIWRlZmF1bHQ7XG5cbi8vJGlucHV0LXRyYW5zaXRpb246ICAgICAgICAgICAgICAgICAgICAgIGJvcmRlci1jb2xvciAuMTVzIGVhc2UtaW4tb3V0LCBib3gtc2hhZG93IC4xNXMgZWFzZS1pbi1vdXQgIWRlZmF1bHQ7XG5cbi8vJGZvcm0tdGV4dC1tYXJnaW4tdG9wOiAgICAgICAgICAgICAgICAgIC4yNXJlbSAhZGVmYXVsdDtcblxuLy8kZm9ybS1jaGVjay1pbnB1dC1ndXR0ZXI6ICAgICAgICAgICAgICAgMS4yNXJlbSAhZGVmYXVsdDtcbi8vJGZvcm0tY2hlY2staW5wdXQtbWFyZ2luLXk6ICAgICAgICAgICAgIC4zcmVtICFkZWZhdWx0O1xuLy8kZm9ybS1jaGVjay1pbnB1dC1tYXJnaW4teDogICAgICAgICAgICAgLjI1cmVtICFkZWZhdWx0O1xuXG4vLyRmb3JtLWNoZWNrLWlubGluZS1tYXJnaW4teDogICAgICAgICAgICAuNzVyZW0gIWRlZmF1bHQ7XG4vLyRmb3JtLWNoZWNrLWlubGluZS1pbnB1dC1tYXJnaW4teDogICAgICAuMzEyNXJlbSAhZGVmYXVsdDtcblxuLy8kZm9ybS1ncmlkLWd1dHRlci13aWR0aDogICAgICAgICAgICAgICAgMTBweCAhZGVmYXVsdDtcbi8vJGZvcm0tZ3JvdXAtbWFyZ2luLWJvdHRvbTogICAgICAgICAgICAgIDFyZW0gIWRlZmF1bHQ7XG5cbi8vJGlucHV0LWdyb3VwLWFkZG9uLWNvbG9yOiAgICAgICAgICAgICAgICRpbnB1dC1jb2xvciAhZGVmYXVsdDtcbi8vJGlucHV0LWdyb3VwLWFkZG9uLWJnOiAgICAgICAgICAgICAgICAgICRncmF5LTIwMCAhZGVmYXVsdDtcbi8vJGlucHV0LWdyb3VwLWFkZG9uLWJvcmRlci1jb2xvcjogICAgICAgICRpbnB1dC1ib3JkZXItY29sb3IgIWRlZmF1bHQ7XG5cbi8vJGN1c3RvbS1mb3Jtcy10cmFuc2l0aW9uOiAgICAgICAgICAgICAgIGJhY2tncm91bmQtY29sb3IgLjE1cyBlYXNlLWluLW91dCwgYm9yZGVyLWNvbG9yIC4xNXMgZWFzZS1pbi1vdXQsIGJveC1zaGFkb3cgLjE1cyBlYXNlLWluLW91dCAhZGVmYXVsdDtcblxuLy8kY3VzdG9tLWNvbnRyb2wtZ3V0dGVyOiAgICAgICAgICAgICAgICAgLjVyZW0gIWRlZmF1bHQ7XG4vLyRjdXN0b20tY29udHJvbC1zcGFjZXIteDogICAgICAgICAgICAgICAxcmVtICFkZWZhdWx0O1xuLy8kY3VzdG9tLWNvbnRyb2wtY3Vyc29yOiAgICAgICAgICAgICAgICAgbnVsbCAhZGVmYXVsdDtcblxuLy8kY3VzdG9tLWNvbnRyb2wtaW5kaWNhdG9yLXNpemU6ICAgICAgICAgMXJlbSAhZGVmYXVsdDtcbi8vJGN1c3RvbS1jb250cm9sLWluZGljYXRvci1iZzogICAgICAgICAgICRpbnB1dC1iZyAhZGVmYXVsdDtcblxuLy8kY3VzdG9tLWNvbnRyb2wtaW5kaWNhdG9yLWJnLXNpemU6ICAgICAgNTAlIDUwJSAhZGVmYXVsdDtcbi8vJGN1c3RvbS1jb250cm9sLWluZGljYXRvci1ib3gtc2hhZG93OiAgICRpbnB1dC1ib3gtc2hhZG93ICFkZWZhdWx0O1xuLy8kY3VzdG9tLWNvbnRyb2wtaW5kaWNhdG9yLWJvcmRlci1jb2xvcjogJGdyYXktNTAwICFkZWZhdWx0O1xuLy8kY3VzdG9tLWNvbnRyb2wtaW5kaWNhdG9yLWJvcmRlci13aWR0aDogJGlucHV0LWJvcmRlci13aWR0aCAhZGVmYXVsdDtcblxuLy8kY3VzdG9tLWNvbnRyb2wtbGFiZWwtY29sb3I6ICAgICAgICAgICAgbnVsbCAhZGVmYXVsdDtcblxuLy8kY3VzdG9tLWNvbnRyb2wtaW5kaWNhdG9yLWRpc2FibGVkLWJnOiAgICAgICAgICAkaW5wdXQtZGlzYWJsZWQtYmcgIWRlZmF1bHQ7XG4vLyRjdXN0b20tY29udHJvbC1sYWJlbC1kaXNhYmxlZC1jb2xvcjogICAgICAgICAgICRncmF5LTYwMCAhZGVmYXVsdDtcblxuLy8kY3VzdG9tLWNvbnRyb2wtaW5kaWNhdG9yLWNoZWNrZWQtY29sb3I6ICAgICAgICAkY29tcG9uZW50LWFjdGl2ZS1jb2xvciAhZGVmYXVsdDtcbi8vJGN1c3RvbS1jb250cm9sLWluZGljYXRvci1jaGVja2VkLWJnOiAgICAgICAgICAgJGNvbXBvbmVudC1hY3RpdmUtYmcgIWRlZmF1bHQ7XG4vLyRjdXN0b20tY29udHJvbC1pbmRpY2F0b3ItY2hlY2tlZC1kaXNhYmxlZC1iZzogIHJnYmEodGhlbWUtY29sb3IoXCJwcmltYXJ5XCIpLCAuNSkgIWRlZmF1bHQ7XG4vLyRjdXN0b20tY29udHJvbC1pbmRpY2F0b3ItY2hlY2tlZC1ib3gtc2hhZG93OiAgIG51bGwgIWRlZmF1bHQ7XG4vLyRjdXN0b20tY29udHJvbC1pbmRpY2F0b3ItY2hlY2tlZC1ib3JkZXItY29sb3I6IC8vJGN1c3RvbS1jb250cm9sLWluZGljYXRvci1jaGVja2VkLWJnICFkZWZhdWx0O1xuXG4vLyRjdXN0b20tY29udHJvbC1pbmRpY2F0b3ItZm9jdXMtYm94LXNoYWRvdzogICAgICRpbnB1dC1mb2N1cy1ib3gtc2hhZG93ICFkZWZhdWx0O1xuLy8kY3VzdG9tLWNvbnRyb2wtaW5kaWNhdG9yLWZvY3VzLWJvcmRlci1jb2xvcjogICAkaW5wdXQtZm9jdXMtYm9yZGVyLWNvbG9yICFkZWZhdWx0O1xuXG4vLyRjdXN0b20tY29udHJvbC1pbmRpY2F0b3ItYWN0aXZlLWNvbG9yOiAgICAgICAgICRjb21wb25lbnQtYWN0aXZlLWNvbG9yICFkZWZhdWx0O1xuLy8kY3VzdG9tLWNvbnRyb2wtaW5kaWNhdG9yLWFjdGl2ZS1iZzogICAgICAgICAgICBsaWdodGVuKCRjb21wb25lbnQtYWN0aXZlLWJnLCAzNSUpICFkZWZhdWx0O1xuLy8kY3VzdG9tLWNvbnRyb2wtaW5kaWNhdG9yLWFjdGl2ZS1ib3gtc2hhZG93OiAgICBudWxsICFkZWZhdWx0O1xuLy8kY3VzdG9tLWNvbnRyb2wtaW5kaWNhdG9yLWFjdGl2ZS1ib3JkZXItY29sb3I6ICAvLyRjdXN0b20tY29udHJvbC1pbmRpY2F0b3ItYWN0aXZlLWJnICFkZWZhdWx0O1xuXG4vLyRjdXN0b20tY2hlY2tib3gtaW5kaWNhdG9yLWJvcmRlci1yYWRpdXM6ICAgICAgICRib3JkZXItcmFkaXVzICFkZWZhdWx0O1xuLy8kY3VzdG9tLWNoZWNrYm94LWluZGljYXRvci1pY29uLWNoZWNrZWQ6ICAgICAgICB1cmwoXCJkYXRhOmltYWdlL3N2Zyt4bWwsPHN2ZyB4bWxucz0naHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmcnIHdpZHRoPSc4JyBoZWlnaHQ9JzgnIHZpZXdCb3g9JzAgMCA4IDgnPjxwYXRoIGZpbGw9JyN7Ly8kY3VzdG9tLWNvbnRyb2wtaW5kaWNhdG9yLWNoZWNrZWQtY29sb3J9JyBkPSdNNi41NjQuNzVsLTMuNTkgMy42MTItMS41MzgtMS41NUwwIDQuMjZsMi45NzQgMi45OUw4IDIuMTkzeicvPjwvc3ZnPlwiKSAhZGVmYXVsdDtcblxuLy8kY3VzdG9tLWNoZWNrYm94LWluZGljYXRvci1pbmRldGVybWluYXRlLWJnOiAgICAgICAgICAgJGNvbXBvbmVudC1hY3RpdmUtYmcgIWRlZmF1bHQ7XG4vLyRjdXN0b20tY2hlY2tib3gtaW5kaWNhdG9yLWluZGV0ZXJtaW5hdGUtY29sb3I6ICAgICAgICAvLyRjdXN0b20tY29udHJvbC1pbmRpY2F0b3ItY2hlY2tlZC1jb2xvciAhZGVmYXVsdDtcbi8vJGN1c3RvbS1jaGVja2JveC1pbmRpY2F0b3ItaWNvbi1pbmRldGVybWluYXRlOiAgICAgICAgIHVybChcImRhdGE6aW1hZ2Uvc3ZnK3htbCw8c3ZnIHhtbG5zPSdodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2Zycgd2lkdGg9JzQnIGhlaWdodD0nNCcgdmlld0JveD0nMCAwIDQgNCc+PHBhdGggc3Ryb2tlPScjey8vJGN1c3RvbS1jaGVja2JveC1pbmRpY2F0b3ItaW5kZXRlcm1pbmF0ZS1jb2xvcn0nIGQ9J00wIDJoNCcvPjwvc3ZnPlwiKSAhZGVmYXVsdDtcbi8vJGN1c3RvbS1jaGVja2JveC1pbmRpY2F0b3ItaW5kZXRlcm1pbmF0ZS1ib3gtc2hhZG93OiAgIG51bGwgIWRlZmF1bHQ7XG4vLyRjdXN0b20tY2hlY2tib3gtaW5kaWNhdG9yLWluZGV0ZXJtaW5hdGUtYm9yZGVyLWNvbG9yOiAvLyRjdXN0b20tY2hlY2tib3gtaW5kaWNhdG9yLWluZGV0ZXJtaW5hdGUtYmcgIWRlZmF1bHQ7XG5cbi8vJGN1c3RvbS1yYWRpby1pbmRpY2F0b3ItYm9yZGVyLXJhZGl1czogICAgICAgICAgNTAlICFkZWZhdWx0O1xuLy8kY3VzdG9tLXJhZGlvLWluZGljYXRvci1pY29uLWNoZWNrZWQ6ICAgICAgICAgICB1cmwoXCJkYXRhOmltYWdlL3N2Zyt4bWwsPHN2ZyB4bWxucz0naHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmcnIHdpZHRoPScxMicgaGVpZ2h0PScxMicgdmlld0JveD0nLTQgLTQgOCA4Jz48Y2lyY2xlIHI9JzMnIGZpbGw9JyN7Ly8kY3VzdG9tLWNvbnRyb2wtaW5kaWNhdG9yLWNoZWNrZWQtY29sb3J9Jy8+PC9zdmc+XCIpICFkZWZhdWx0O1xuXG4vLyRjdXN0b20tc3dpdGNoLXdpZHRoOiAgICAgICAgICAgICAgICAgICAgICAgICAgIC8vJGN1c3RvbS1jb250cm9sLWluZGljYXRvci1zaXplICogMS43NSAhZGVmYXVsdDtcbi8vJGN1c3RvbS1zd2l0Y2gtaW5kaWNhdG9yLWJvcmRlci1yYWRpdXM6ICAgICAgICAgLy8kY3VzdG9tLWNvbnRyb2wtaW5kaWNhdG9yLXNpemUgLyAyICFkZWZhdWx0O1xuLy8kY3VzdG9tLXN3aXRjaC1pbmRpY2F0b3Itc2l6ZTogICAgICAgICAgICAgICAgICBzdWJ0cmFjdCgvLyRjdXN0b20tY29udHJvbC1pbmRpY2F0b3Itc2l6ZSwgLy8kY3VzdG9tLWNvbnRyb2wtaW5kaWNhdG9yLWJvcmRlci13aWR0aCAqIDQpICFkZWZhdWx0O1xuXG4vLyRjdXN0b20tc2VsZWN0LXBhZGRpbmcteTogICAgICAgICAgICRpbnB1dC1wYWRkaW5nLXkgIWRlZmF1bHQ7XG4vLyRjdXN0b20tc2VsZWN0LXBhZGRpbmcteDogICAgICAgICAgICRpbnB1dC1wYWRkaW5nLXggIWRlZmF1bHQ7XG4vLyRjdXN0b20tc2VsZWN0LWZvbnQtZmFtaWx5OiAgICAgICAgICRpbnB1dC1mb250LWZhbWlseSAhZGVmYXVsdDtcbi8vJGN1c3RvbS1zZWxlY3QtZm9udC1zaXplOiAgICAgICAgICAgJGlucHV0LWZvbnQtc2l6ZSAhZGVmYXVsdDtcbi8vJGN1c3RvbS1zZWxlY3QtaGVpZ2h0OiAgICAgICAgICAgICAgJGlucHV0LWhlaWdodCAhZGVmYXVsdDtcbi8vJGN1c3RvbS1zZWxlY3QtaW5kaWNhdG9yLXBhZGRpbmc6ICAgMXJlbSAhZGVmYXVsdDsgLy8gRXh0cmEgcGFkZGluZyB0byBhY2NvdW50IGZvciB0aGUgcHJlc2VuY2Ugb2YgdGhlIGJhY2tncm91bmQtaW1hZ2UgYmFzZWQgaW5kaWNhdG9yXG4vLyRjdXN0b20tc2VsZWN0LWZvbnQtd2VpZ2h0OiAgICAgICAgICRpbnB1dC1mb250LXdlaWdodCAhZGVmYXVsdDtcbi8vJGN1c3RvbS1zZWxlY3QtbGluZS1oZWlnaHQ6ICAgICAgICAgJGlucHV0LWxpbmUtaGVpZ2h0ICFkZWZhdWx0O1xuLy8kY3VzdG9tLXNlbGVjdC1jb2xvcjogICAgICAgICAgICAgICAkaW5wdXQtY29sb3IgIWRlZmF1bHQ7XG4vLyRjdXN0b20tc2VsZWN0LWRpc2FibGVkLWNvbG9yOiAgICAgICRncmF5LTYwMCAhZGVmYXVsdDtcbi8vJGN1c3RvbS1zZWxlY3QtYmc6ICAgICAgICAgICAgICAgICAgJGlucHV0LWJnICFkZWZhdWx0O1xuLy8kY3VzdG9tLXNlbGVjdC1kaXNhYmxlZC1iZzogICAgICAgICAkZ3JheS0yMDAgIWRlZmF1bHQ7XG4vLyRjdXN0b20tc2VsZWN0LWJnLXNpemU6ICAgICAgICAgICAgIDhweCAxMHB4ICFkZWZhdWx0OyAvLyBJbiBwaXhlbHMgYmVjYXVzZSBpbWFnZSBkaW1lbnNpb25zXG4vLyRjdXN0b20tc2VsZWN0LWluZGljYXRvci1jb2xvcjogICAgICRncmF5LTgwMCAhZGVmYXVsdDtcbi8vJGN1c3RvbS1zZWxlY3QtaW5kaWNhdG9yOiAgICAgICAgICAgdXJsKFwiZGF0YTppbWFnZS9zdmcreG1sLDxzdmcgeG1sbnM9J2h0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnJyB3aWR0aD0nNCcgaGVpZ2h0PSc1JyB2aWV3Qm94PScwIDAgNCA1Jz48cGF0aCBmaWxsPScjey8vJGN1c3RvbS1zZWxlY3QtaW5kaWNhdG9yLWNvbG9yfScgZD0nTTIgMEwwIDJoNHptMCA1TDAgM2g0eicvPjwvc3ZnPlwiKSAhZGVmYXVsdDtcbi8vJGN1c3RvbS1zZWxlY3QtYmFja2dyb3VuZDogICAgICAgICAgZXNjYXBlLXN2ZygvLyRjdXN0b20tc2VsZWN0LWluZGljYXRvcikgbm8tcmVwZWF0IHJpZ2h0IC8vJGN1c3RvbS1zZWxlY3QtcGFkZGluZy14IGNlbnRlciAvIC8vJGN1c3RvbS1zZWxlY3QtYmctc2l6ZSAhZGVmYXVsdDsgLy8gVXNlZCBzbyB3ZSBjYW4gaGF2ZSBtdWx0aXBsZSBiYWNrZ3JvdW5kIGVsZW1lbnRzIChlLmcuLCBhcnJvdyBhbmQgZmVlZGJhY2sgaWNvbilcblxuLy8kY3VzdG9tLXNlbGVjdC1mZWVkYmFjay1pY29uLXBhZGRpbmctcmlnaHQ6IGFkZCgxZW0gKiAuNzUsICgyICogLy8kY3VzdG9tLXNlbGVjdC1wYWRkaW5nLXkgKiAuNzUpICsgLy8kY3VzdG9tLXNlbGVjdC1wYWRkaW5nLXggKyAvLyRjdXN0b20tc2VsZWN0LWluZGljYXRvci1wYWRkaW5nKSAhZGVmYXVsdDtcbi8vJGN1c3RvbS1zZWxlY3QtZmVlZGJhY2staWNvbi1wb3NpdGlvbjogICAgICBjZW50ZXIgcmlnaHQgKC8vJGN1c3RvbS1zZWxlY3QtcGFkZGluZy14ICsgLy8kY3VzdG9tLXNlbGVjdC1pbmRpY2F0b3ItcGFkZGluZykgIWRlZmF1bHQ7XG4vLyRjdXN0b20tc2VsZWN0LWZlZWRiYWNrLWljb24tc2l6ZTogICAgICAgICAgJGlucHV0LWhlaWdodC1pbm5lci1oYWxmICRpbnB1dC1oZWlnaHQtaW5uZXItaGFsZiAhZGVmYXVsdDtcblxuLy8kY3VzdG9tLXNlbGVjdC1ib3JkZXItd2lkdGg6ICAgICAgICAkaW5wdXQtYm9yZGVyLXdpZHRoICFkZWZhdWx0O1xuLy8kY3VzdG9tLXNlbGVjdC1ib3JkZXItY29sb3I6ICAgICAgICAkaW5wdXQtYm9yZGVyLWNvbG9yICFkZWZhdWx0O1xuLy8kY3VzdG9tLXNlbGVjdC1ib3JkZXItcmFkaXVzOiAgICAgICAkYm9yZGVyLXJhZGl1cyAhZGVmYXVsdDtcbi8vJGN1c3RvbS1zZWxlY3QtYm94LXNoYWRvdzogICAgICAgICAgaW5zZXQgMCAxcHggMnB4IHJnYmEoJGJsYWNrLCAuMDc1KSAhZGVmYXVsdDtcblxuLy8kY3VzdG9tLXNlbGVjdC1mb2N1cy1ib3JkZXItY29sb3I6ICAkaW5wdXQtZm9jdXMtYm9yZGVyLWNvbG9yICFkZWZhdWx0O1xuLy8kY3VzdG9tLXNlbGVjdC1mb2N1cy13aWR0aDogICAgICAgICAkaW5wdXQtZm9jdXMtd2lkdGggIWRlZmF1bHQ7XG4vLyRjdXN0b20tc2VsZWN0LWZvY3VzLWJveC1zaGFkb3c6ICAgIDAgMCAwIC8vJGN1c3RvbS1zZWxlY3QtZm9jdXMtd2lkdGggJGlucHV0LWJ0bi1mb2N1cy1jb2xvciAhZGVmYXVsdDtcblxuLy8kY3VzdG9tLXNlbGVjdC1wYWRkaW5nLXktc206ICAgICAgICAkaW5wdXQtcGFkZGluZy15LXNtICFkZWZhdWx0O1xuLy8kY3VzdG9tLXNlbGVjdC1wYWRkaW5nLXgtc206ICAgICAgICAkaW5wdXQtcGFkZGluZy14LXNtICFkZWZhdWx0O1xuLy8kY3VzdG9tLXNlbGVjdC1mb250LXNpemUtc206ICAgICAgICAkaW5wdXQtZm9udC1zaXplLXNtICFkZWZhdWx0O1xuLy8kY3VzdG9tLXNlbGVjdC1oZWlnaHQtc206ICAgICAgICAgICAkaW5wdXQtaGVpZ2h0LXNtICFkZWZhdWx0O1xuXG4vLyRjdXN0b20tc2VsZWN0LXBhZGRpbmcteS1sZzogICAgICAgICRpbnB1dC1wYWRkaW5nLXktbGcgIWRlZmF1bHQ7XG4vLyRjdXN0b20tc2VsZWN0LXBhZGRpbmcteC1sZzogICAgICAgICRpbnB1dC1wYWRkaW5nLXgtbGcgIWRlZmF1bHQ7XG4vLyRjdXN0b20tc2VsZWN0LWZvbnQtc2l6ZS1sZzogICAgICAgICRpbnB1dC1mb250LXNpemUtbGcgIWRlZmF1bHQ7XG4vLyRjdXN0b20tc2VsZWN0LWhlaWdodC1sZzogICAgICAgICAgICRpbnB1dC1oZWlnaHQtbGcgIWRlZmF1bHQ7XG5cbi8vJGN1c3RvbS1yYW5nZS10cmFjay13aWR0aDogICAgICAgICAgMTAwJSAhZGVmYXVsdDtcbi8vJGN1c3RvbS1yYW5nZS10cmFjay1oZWlnaHQ6ICAgICAgICAgLjVyZW0gIWRlZmF1bHQ7XG4vLyRjdXN0b20tcmFuZ2UtdHJhY2stY3Vyc29yOiAgICAgICAgIHBvaW50ZXIgIWRlZmF1bHQ7XG4vLyRjdXN0b20tcmFuZ2UtdHJhY2stYmc6ICAgICAgICAgICAgICRncmF5LTMwMCAhZGVmYXVsdDtcbi8vJGN1c3RvbS1yYW5nZS10cmFjay1ib3JkZXItcmFkaXVzOiAgMXJlbSAhZGVmYXVsdDtcbi8vJGN1c3RvbS1yYW5nZS10cmFjay1ib3gtc2hhZG93OiAgICAgaW5zZXQgMCAuMjVyZW0gLjI1cmVtIHJnYmEoJGJsYWNrLCAuMSkgIWRlZmF1bHQ7XG5cbi8vJGN1c3RvbS1yYW5nZS10aHVtYi13aWR0aDogICAgICAgICAgICAgICAgICAgMXJlbSAhZGVmYXVsdDtcbi8vJGN1c3RvbS1yYW5nZS10aHVtYi1oZWlnaHQ6ICAgICAgICAgICAgICAgICAgLy8kY3VzdG9tLXJhbmdlLXRodW1iLXdpZHRoICFkZWZhdWx0O1xuLy8kY3VzdG9tLXJhbmdlLXRodW1iLWJnOiAgICAgICAgICAgICAgICAgICAgICAkY29tcG9uZW50LWFjdGl2ZS1iZyAhZGVmYXVsdDtcbi8vJGN1c3RvbS1yYW5nZS10aHVtYi1ib3JkZXI6ICAgICAgICAgICAgICAgICAgMCAhZGVmYXVsdDtcbi8vJGN1c3RvbS1yYW5nZS10aHVtYi1ib3JkZXItcmFkaXVzOiAgICAgICAgICAgMXJlbSAhZGVmYXVsdDtcbi8vJGN1c3RvbS1yYW5nZS10aHVtYi1ib3gtc2hhZG93OiAgICAgICAgICAgICAgMCAuMXJlbSAuMjVyZW0gcmdiYSgkYmxhY2ssIC4xKSAhZGVmYXVsdDtcbi8vJGN1c3RvbS1yYW5nZS10aHVtYi1mb2N1cy1ib3gtc2hhZG93OiAgICAgICAgMCAwIDAgMXB4ICRib2R5LWJnLCAkaW5wdXQtZm9jdXMtYm94LXNoYWRvdyAhZGVmYXVsdDtcbi8vJGN1c3RvbS1yYW5nZS10aHVtYi1mb2N1cy1ib3gtc2hhZG93LXdpZHRoOiAgJGlucHV0LWZvY3VzLXdpZHRoICFkZWZhdWx0OyAvLyBGb3IgZm9jdXMgYm94IHNoYWRvdyBpc3N1ZSBpbiBJRS9FZGdlXG4vLyRjdXN0b20tcmFuZ2UtdGh1bWItYWN0aXZlLWJnOiAgICAgICAgICAgICAgIGxpZ2h0ZW4oJGNvbXBvbmVudC1hY3RpdmUtYmcsIDM1JSkgIWRlZmF1bHQ7XG4vLyRjdXN0b20tcmFuZ2UtdGh1bWItZGlzYWJsZWQtYmc6ICAgICAgICAgICAgICRncmF5LTUwMCAhZGVmYXVsdDtcblxuLy8kY3VzdG9tLWZpbGUtaGVpZ2h0OiAgICAgICAgICAgICAgICAkaW5wdXQtaGVpZ2h0ICFkZWZhdWx0O1xuLy8kY3VzdG9tLWZpbGUtaGVpZ2h0LWlubmVyOiAgICAgICAgICAkaW5wdXQtaGVpZ2h0LWlubmVyICFkZWZhdWx0O1xuLy8kY3VzdG9tLWZpbGUtZm9jdXMtYm9yZGVyLWNvbG9yOiAgICAkaW5wdXQtZm9jdXMtYm9yZGVyLWNvbG9yICFkZWZhdWx0O1xuLy8kY3VzdG9tLWZpbGUtZm9jdXMtYm94LXNoYWRvdzogICAgICAkaW5wdXQtZm9jdXMtYm94LXNoYWRvdyAhZGVmYXVsdDtcbi8vJGN1c3RvbS1maWxlLWRpc2FibGVkLWJnOiAgICAgICAgICAgJGlucHV0LWRpc2FibGVkLWJnICFkZWZhdWx0O1xuXG4vLyRjdXN0b20tZmlsZS1wYWRkaW5nLXk6ICAgICAgICAgICAgICRpbnB1dC1wYWRkaW5nLXkgIWRlZmF1bHQ7XG4vLyRjdXN0b20tZmlsZS1wYWRkaW5nLXg6ICAgICAgICAgICAgICRpbnB1dC1wYWRkaW5nLXggIWRlZmF1bHQ7XG4vLyRjdXN0b20tZmlsZS1saW5lLWhlaWdodDogICAgICAgICAgICRpbnB1dC1saW5lLWhlaWdodCAhZGVmYXVsdDtcbi8vJGN1c3RvbS1maWxlLWZvbnQtZmFtaWx5OiAgICAgICAgICAgJGlucHV0LWZvbnQtZmFtaWx5ICFkZWZhdWx0O1xuLy8kY3VzdG9tLWZpbGUtZm9udC13ZWlnaHQ6ICAgICAgICAgICAkaW5wdXQtZm9udC13ZWlnaHQgIWRlZmF1bHQ7XG4vLyRjdXN0b20tZmlsZS1jb2xvcjogICAgICAgICAgICAgICAgICRpbnB1dC1jb2xvciAhZGVmYXVsdDtcbi8vJGN1c3RvbS1maWxlLWJnOiAgICAgICAgICAgICAgICAgICAgJGlucHV0LWJnICFkZWZhdWx0O1xuLy8kY3VzdG9tLWZpbGUtYm9yZGVyLXdpZHRoOiAgICAgICAgICAkaW5wdXQtYm9yZGVyLXdpZHRoICFkZWZhdWx0O1xuLy8kY3VzdG9tLWZpbGUtYm9yZGVyLWNvbG9yOiAgICAgICAgICAkaW5wdXQtYm9yZGVyLWNvbG9yICFkZWZhdWx0O1xuLy8kY3VzdG9tLWZpbGUtYm9yZGVyLXJhZGl1czogICAgICAgICAkaW5wdXQtYm9yZGVyLXJhZGl1cyAhZGVmYXVsdDtcbi8vJGN1c3RvbS1maWxlLWJveC1zaGFkb3c6ICAgICAgICAgICAgJGlucHV0LWJveC1zaGFkb3cgIWRlZmF1bHQ7XG4vLyRjdXN0b20tZmlsZS1idXR0b24tY29sb3I6ICAgICAgICAgICRjdXN0b20tZmlsZS1jb2xvciAhZGVmYXVsdDtcbi8vJGN1c3RvbS1maWxlLWJ1dHRvbi1iZzogICAgICAgICAgICAgJGlucHV0LWdyb3VwLWFkZG9uLWJnICFkZWZhdWx0O1xuLyokY3VzdG9tLWZpbGUtdGV4dDogKFxuICBlbjogXCJCcm93c2VcIlxuKSAhZGVmYXVsdDsqL1xuXG5cbi8vIEZvcm0gdmFsaWRhdGlvblxuXG4vLyRmb3JtLWZlZWRiYWNrLW1hcmdpbi10b3A6ICAgICAgICAgICRmb3JtLXRleHQtbWFyZ2luLXRvcCAhZGVmYXVsdDtcbi8vJGZvcm0tZmVlZGJhY2stZm9udC1zaXplOiAgICAgICAgICAgJHNtYWxsLWZvbnQtc2l6ZSAhZGVmYXVsdDtcbi8vJGZvcm0tZmVlZGJhY2stdmFsaWQtY29sb3I6ICAgICAgICAgdGhlbWUtY29sb3IoXCJzdWNjZXNzXCIpICFkZWZhdWx0O1xuLy8kZm9ybS1mZWVkYmFjay1pbnZhbGlkLWNvbG9yOiAgICAgICB0aGVtZS1jb2xvcihcImRhbmdlclwiKSAhZGVmYXVsdDtcblxuLy8kZm9ybS1mZWVkYmFjay1pY29uLXZhbGlkLWNvbG9yOiAgICAkZm9ybS1mZWVkYmFjay12YWxpZC1jb2xvciAhZGVmYXVsdDtcbi8vJGZvcm0tZmVlZGJhY2staWNvbi12YWxpZDogICAgICAgICAgdXJsKFwiZGF0YTppbWFnZS9zdmcreG1sLDxzdmcgeG1sbnM9J2h0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnJyB3aWR0aD0nOCcgaGVpZ2h0PSc4JyB2aWV3Qm94PScwIDAgOCA4Jz48cGF0aCBmaWxsPScjeyRmb3JtLWZlZWRiYWNrLWljb24tdmFsaWQtY29sb3J9JyBkPSdNMi4zIDYuNzNMLjYgNC41M2MtLjQtMS4wNC40Ni0xLjQgMS4xLS44bDEuMSAxLjQgMy40LTMuOGMuNi0uNjMgMS42LS4yNyAxLjIuN2wtNCA0LjZjLS40My41LS44LjQtMS4xLjF6Jy8+PC9zdmc+XCIpICFkZWZhdWx0O1xuLy8kZm9ybS1mZWVkYmFjay1pY29uLWludmFsaWQtY29sb3I6ICAkZm9ybS1mZWVkYmFjay1pbnZhbGlkLWNvbG9yICFkZWZhdWx0O1xuLy8kZm9ybS1mZWVkYmFjay1pY29uLWludmFsaWQ6ICAgICAgICB1cmwoXCJkYXRhOmltYWdlL3N2Zyt4bWwsPHN2ZyB4bWxucz0naHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmcnIHdpZHRoPScxMicgaGVpZ2h0PScxMicgZmlsbD0nbm9uZScgc3Ryb2tlPScjeyRmb3JtLWZlZWRiYWNrLWljb24taW52YWxpZC1jb2xvcn0nIHZpZXdCb3g9JzAgMCAxMiAxMic+PGNpcmNsZSBjeD0nNicgY3k9JzYnIHI9JzQuNScvPjxwYXRoIHN0cm9rZS1saW5lam9pbj0ncm91bmQnIGQ9J001LjggMy42aC40TDYgNi41eicvPjxjaXJjbGUgY3g9JzYnIGN5PSc4LjInIHI9Jy42JyBmaWxsPScjeyRmb3JtLWZlZWRiYWNrLWljb24taW52YWxpZC1jb2xvcn0nIHN0cm9rZT0nbm9uZScvPjwvc3ZnPlwiKSAhZGVmYXVsdDtcblxuLy8kZm9ybS12YWxpZGF0aW9uLXN0YXRlczogKCkgIWRlZmF1bHQ7XG4vKiRmb3JtLXZhbGlkYXRpb24tc3RhdGVzOiBtYXAtbWVyZ2UoXG4gIChcbiAgICBcInZhbGlkXCI6IChcbiAgICAgIFwiY29sb3JcIjogJGZvcm0tZmVlZGJhY2stdmFsaWQtY29sb3IsXG4gICAgICBcImljb25cIjogJGZvcm0tZmVlZGJhY2staWNvbi12YWxpZFxuICAgICksXG4gICAgXCJpbnZhbGlkXCI6IChcbiAgICAgIFwiY29sb3JcIjogJGZvcm0tZmVlZGJhY2staW52YWxpZC1jb2xvcixcbiAgICAgIFwiaWNvblwiOiAkZm9ybS1mZWVkYmFjay1pY29uLWludmFsaWRcbiAgICApLFxuICApLFxuICAkZm9ybS12YWxpZGF0aW9uLXN0YXRlc1xuKTsqLyJdfQ== */";
      /***/
    },

    /***/
    "NWp7":
    /*!***************************************************!*\
      !*** ./src/modules/home/services/home.service.ts ***!
      \***************************************************/

    /*! exports provided: HomeService */

    /***/
    function NWp7(module, __webpack_exports__, __webpack_require__) {
      "use strict";

      __webpack_require__.r(__webpack_exports__);
      /* harmony export (binding) */


      __webpack_require__.d(__webpack_exports__, "HomeService", function () {
        return HomeService;
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


      var rxjs__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(
      /*! rxjs */
      "qCKp");

      var HomeService = /*#__PURE__*/function () {
        function HomeService() {
          _classCallCheck(this, HomeService);
        }

        _createClass(HomeService, [{
          key: "getHome$",
          value: function getHome$() {
            return Object(rxjs__WEBPACK_IMPORTED_MODULE_2__["of"])({});
          }
        }]);

        return HomeService;
      }();

      HomeService.ctorParameters = function () {
        return [];
      };

      HomeService = Object(tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"])([Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["Injectable"])(), Object(tslib__WEBPACK_IMPORTED_MODULE_0__["__metadata"])("design:paramtypes", [])], HomeService);
      /***/
    },

    /***/
    "PH8I":
    /*!*****************************************!*\
      !*** ./src/modules/home/home.module.ts ***!
      \*****************************************/

    /*! exports provided: HomeModule */

    /***/
    function PH8I(module, __webpack_exports__, __webpack_require__) {
      "use strict";

      __webpack_require__.r(__webpack_exports__);
      /* harmony export (binding) */


      __webpack_require__.d(__webpack_exports__, "HomeModule", function () {
        return HomeModule;
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


      var _angular_common__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(
      /*! @angular/common */
      "SVse");
      /* harmony import */


      var _angular_router__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(
      /*! @angular/router */
      "iInd");
      /* harmony import */


      var _angular_forms__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(
      /*! @angular/forms */
      "s7LF");
      /* harmony import */


      var _common_app_common_module__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__(
      /*! @common/app-common.module */
      "NMtB");
      /* harmony import */


      var _modules_navigation_navigation_module__WEBPACK_IMPORTED_MODULE_6__ = __webpack_require__(
      /*! @modules/navigation/navigation.module */
      "hex+");
      /* harmony import */


      var _components__WEBPACK_IMPORTED_MODULE_7__ = __webpack_require__(
      /*! ./components */
      "XoFJ");
      /* harmony import */


      var _containers__WEBPACK_IMPORTED_MODULE_8__ = __webpack_require__(
      /*! ./containers */
      "QX2z");
      /* harmony import */


      var _guards__WEBPACK_IMPORTED_MODULE_9__ = __webpack_require__(
      /*! ./guards */
      "Afob");
      /* harmony import */


      var _services__WEBPACK_IMPORTED_MODULE_10__ = __webpack_require__(
      /*! ./services */
      "fR7s");
      /* tslint:disable: ordered-imports*/

      /* Modules */

      /* Components */

      /* Containers */

      /* Guards */

      /* Services */


      var HomeModule = function HomeModule() {
        _classCallCheck(this, HomeModule);
      };

      HomeModule = Object(tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"])([Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["NgModule"])({
        imports: [_angular_common__WEBPACK_IMPORTED_MODULE_2__["CommonModule"], _angular_router__WEBPACK_IMPORTED_MODULE_3__["RouterModule"], _angular_forms__WEBPACK_IMPORTED_MODULE_4__["ReactiveFormsModule"], _angular_forms__WEBPACK_IMPORTED_MODULE_4__["FormsModule"], _common_app_common_module__WEBPACK_IMPORTED_MODULE_5__["AppCommonModule"], _modules_navigation_navigation_module__WEBPACK_IMPORTED_MODULE_6__["NavigationModule"]],
        providers: [].concat(_toConsumableArray(_services__WEBPACK_IMPORTED_MODULE_10__["services"]), _toConsumableArray(_guards__WEBPACK_IMPORTED_MODULE_9__["guards"])),
        declarations: [].concat(_toConsumableArray(_containers__WEBPACK_IMPORTED_MODULE_8__["containers"]), _toConsumableArray(_components__WEBPACK_IMPORTED_MODULE_7__["components"])),
        exports: [].concat(_toConsumableArray(_containers__WEBPACK_IMPORTED_MODULE_8__["containers"]), _toConsumableArray(_components__WEBPACK_IMPORTED_MODULE_7__["components"]))
      })], HomeModule);
      /***/
    },

    /***/
    "QX2z":
    /*!**********************************************!*\
      !*** ./src/modules/home/containers/index.ts ***!
      \**********************************************/

    /*! exports provided: containers, HomeComponent */

    /***/
    function QX2z(module, __webpack_exports__, __webpack_require__) {
      "use strict";

      __webpack_require__.r(__webpack_exports__);
      /* harmony export (binding) */


      __webpack_require__.d(__webpack_exports__, "containers", function () {
        return containers;
      });
      /* harmony import */


      var _home_home_component__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(
      /*! ./home/home.component */
      "WFGm");
      /* harmony reexport (safe) */


      __webpack_require__.d(__webpack_exports__, "HomeComponent", function () {
        return _home_home_component__WEBPACK_IMPORTED_MODULE_0__["HomeComponent"];
      });

      var containers = [_home_home_component__WEBPACK_IMPORTED_MODULE_0__["HomeComponent"]];
      /***/
    },

    /***/
    "SMUz":
    /*!***********************************************!*\
      !*** ./src/modules/home/guards/home.guard.ts ***!
      \***********************************************/

    /*! exports provided: HomeGuard */

    /***/
    function SMUz(module, __webpack_exports__, __webpack_require__) {
      "use strict";

      __webpack_require__.r(__webpack_exports__);
      /* harmony export (binding) */


      __webpack_require__.d(__webpack_exports__, "HomeGuard", function () {
        return HomeGuard;
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


      var rxjs__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(
      /*! rxjs */
      "qCKp");

      var HomeGuard = /*#__PURE__*/function () {
        function HomeGuard() {
          _classCallCheck(this, HomeGuard);
        }

        _createClass(HomeGuard, [{
          key: "canActivate",
          value: function canActivate() {
            return Object(rxjs__WEBPACK_IMPORTED_MODULE_2__["of"])(true);
          }
        }]);

        return HomeGuard;
      }();

      HomeGuard = Object(tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"])([Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["Injectable"])()], HomeGuard);
      /***/
    },

    /***/
    "WFGm":
    /*!************************************************************!*\
      !*** ./src/modules/home/containers/home/home.component.ts ***!
      \************************************************************/

    /*! exports provided: HomeComponent */

    /***/
    function WFGm(module, __webpack_exports__, __webpack_require__) {
      "use strict";

      __webpack_require__.r(__webpack_exports__);
      /* harmony export (binding) */


      __webpack_require__.d(__webpack_exports__, "HomeComponent", function () {
        return HomeComponent;
      });
      /* harmony import */


      var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(
      /*! tslib */
      "mrSG");
      /* harmony import */


      var _raw_loader_home_component_html__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(
      /*! raw-loader!./home.component.html */
      "pNCK");
      /* harmony import */


      var _home_component_scss__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(
      /*! ./home.component.scss */
      "NI2U");
      /* harmony import */


      var _angular_core__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(
      /*! @angular/core */
      "8Y7J");

      var HomeComponent = /*#__PURE__*/function () {
        function HomeComponent() {
          _classCallCheck(this, HomeComponent);

          this.isMenuCollapsed = true;
          this.activeFragment = "home";
          this.monitorScroll = true;
        }

        _createClass(HomeComponent, [{
          key: "ngOnInit",
          value: function ngOnInit() {
            this.navigation = document.querySelector('.navbar');
            this.home = document.querySelector('#home');
            this.about = document.querySelector('#about');
            this.background = document.querySelector('#background');
            this.contacts = document.querySelector('#contacts');
          }
        }, {
          key: "scrollToElement",
          value: function scrollToElement(element) {
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

            element.scrollIntoView({
              behavior: "smooth"
            }); // Resume scroll monitoring

            this.monitorScroll = true;
          }
        }, {
          key: "onWindowScroll",
          value: function onWindowScroll(e) {
            // Update Navbar Styling
            if (this.navigation != null) {
              if (window.pageYOffset > this.navigation.clientHeight) {
                this.navigation.classList.add('navbar-shrink');
              } else {
                this.navigation.classList.remove('navbar-shrink');
              }
            } // Update Active Navbar Link Styling


            if (this.monitorScroll) {
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
        }, {
          key: "isVisible",
          value: function isVisible(elem) {
            if (elem != null) {
              //console.log(elem.id);
              if (elem.offsetWidth + elem.offsetHeight + elem.getBoundingClientRect().height + elem.getBoundingClientRect().width === 0) {
                return false;
              }

              var elemCenter = {
                x: elem.getBoundingClientRect().left + elem.offsetWidth / 2,
                y: elem.getBoundingClientRect().top + elem.offsetHeight / 2
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

              var pointContainer = document.elementFromPoint(elemCenter.x, elemCenter.y);

              do {
                if (pointContainer === elem) {
                  return true;
                }
              } while (pointContainer = pointContainer.parentNode);

              return false;
            } else {
              return false;
            }
          }
        }]);

        return HomeComponent;
      }();

      HomeComponent.ctorParameters = function () {
        return [];
      };

      HomeComponent.propDecorators = {
        onWindowScroll: [{
          type: _angular_core__WEBPACK_IMPORTED_MODULE_3__["HostListener"],
          args: ['window:scroll', ['$event']]
        }]
      };
      HomeComponent = Object(tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"])([Object(_angular_core__WEBPACK_IMPORTED_MODULE_3__["Component"])({
        selector: 'sb-home',
        changeDetection: _angular_core__WEBPACK_IMPORTED_MODULE_3__["ChangeDetectionStrategy"].OnPush,
        template: _raw_loader_home_component_html__WEBPACK_IMPORTED_MODULE_1__["default"],
        styles: [_home_component_scss__WEBPACK_IMPORTED_MODULE_2__["default"]]
      }), Object(tslib__WEBPACK_IMPORTED_MODULE_0__["__metadata"])("design:paramtypes", [])], HomeComponent);
      /***/
    },

    /***/
    "XoFJ":
    /*!**********************************************!*\
      !*** ./src/modules/home/components/index.ts ***!
      \**********************************************/

    /*! exports provided: components */

    /***/
    function XoFJ(module, __webpack_exports__, __webpack_require__) {
      "use strict";

      __webpack_require__.r(__webpack_exports__);
      /* harmony export (binding) */


      __webpack_require__.d(__webpack_exports__, "components", function () {
        return components;
      });

      var components = [];
      /***/
    },

    /***/
    "fR7s":
    /*!********************************************!*\
      !*** ./src/modules/home/services/index.ts ***!
      \********************************************/

    /*! exports provided: services, HomeService */

    /***/
    function fR7s(module, __webpack_exports__, __webpack_require__) {
      "use strict";

      __webpack_require__.r(__webpack_exports__);
      /* harmony export (binding) */


      __webpack_require__.d(__webpack_exports__, "services", function () {
        return services;
      });
      /* harmony import */


      var _home_service__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(
      /*! ./home.service */
      "NWp7");
      /* harmony reexport (safe) */


      __webpack_require__.d(__webpack_exports__, "HomeService", function () {
        return _home_service__WEBPACK_IMPORTED_MODULE_0__["HomeService"];
      });

      var services = [_home_service__WEBPACK_IMPORTED_MODULE_0__["HomeService"]];
      /***/
    },

    /***/
    "pNCK":
    /*!****************************************************************************************************!*\
      !*** ./node_modules/raw-loader/dist/cjs.js!./src/modules/home/containers/home/home.component.html ***!
      \****************************************************************************************************/

    /*! exports provided: default */

    /***/
    function pNCK(module, __webpack_exports__, __webpack_require__) {
      "use strict";

      __webpack_require__.r(__webpack_exports__);
      /* harmony default export */


      __webpack_exports__["default"] = "<sb-layout-home>\n\n    <nav class=\"navbar navbar-expand-lg navbar-light fixed-top\" id=\"navbar-main\">\n        <div class=\"container\">\n            <a class=\"navbar-brand\" routerLink=\"/\">SYSTEM</a>\n            <button class=\"navbar-toggler navbar-toggler-right\" type=\"button\"\n                (click)=\"isMenuCollapsed = !isMenuCollapsed\" aria-controls=\"navbarResponsive\" aria-expanded=\"false\"\n                aria-label=\"Toggle navigation\">\n                <fa-icon class=\"ml-1\" [icon]='[\"fas\", \"bars\"]'></fa-icon>\n            </button>\n            <div class=\"collapse navbar-collapse\" [ngbCollapse]=\"isMenuCollapsed\">\n                <ul class=\"navbar-nav ml-auto\">\n                    <li class=\"nav-item\"><a class=\"nav-link js-scroll-trigger\"\n                            [ngClass]=\"{'active' : activeFragment == 'home'}\" (click)=\"scrollToElement(home)\">Home</a>\n                    </li>\n                    <li class=\"nav-item\"><a class=\"nav-link js-scroll-trigger\"\n                            [ngClass]=\"{'active' : activeFragment == 'about'}\"\n                            (click)=\"scrollToElement(about)\">About</a></li>\n                    <li class=\"nav-item\"><a class=\"nav-link js-scroll-trigger\"\n                            [ngClass]=\"{'active' : activeFragment == 'background'}\"\n                            (click)=\"scrollToElement(background)\">Background</a></li>\n                    <li class=\"nav-item\"><a class=\"nav-link\" routerLink=\"/dashboard\">Dashboard</a>\n                    </li>\n                    <li class=\"nav-item\"><a class=\"nav-link js-scroll-trigger\"\n                            [ngClass]=\"{'active' : activeFragment == 'contacts'}\"\n                            (click)=\"scrollToElement(contacts)\">Contacts</a></li>\n                </ul>\n            </div>\n        </div>\n    </nav>\n\n    <!-- Masthead-->\n    <header class=\"masthead\" id=\"home\">\n        <div class=\"container d-flex h-100 align-items-center\">\n            <div class=\"mx-auto text-center\">\n                <h1 class=\"mx-auto my-0 text-uppercase\">System</h1>\n                <h2 class=\"text-white-50 mx-auto mt-2 mb-5\">A single sentence introducing the system or welcoming users to the system</h2>\n                <a class=\"btn btn-primary js-scroll-trigger\" (click)=\"scrollToElement(about)\">Get Started</a>\n            </div>\n        </div>\n    </header>\n    <!-- About-->\n    <section class=\"about-section text-center\" id=\"about\" #about>\n        <div class=\"container\">\n            <div class=\"row\">\n                <div class=\"col-lg-9 mx-auto\">\n                    <h2 class=\"text-white mb-4\">About</h2>\n                    <p class=\"text-white\">\n                        \n                        A few lines detailing what the system is about and laying down the next steps for its users or would-be users\n                    </p>\n                </div>\n            </div>\n            <img class=\"img-fluid\" src=\"assets/img/demo-image-2.png\" alt=\"\" />\n        </div>\n    </section>\n    <!-- Background-->\n    <section class=\"projects-section bg-light mt-1\" id=\"background\" #background>\n        <div class=\"container\">\n            <h2 class=\"text-black text-center mb-4\">Background</h2>\n            <!-- Background Row 1-->\n            <div class=\"row justify-content-center no-gutters mb-5 mb-lg-0\">\n                <div class=\"col-lg-6\"><img class=\"img-fluid\" src=\"assets/img/demo-image-3.jpg\" alt=\"\" /></div>\n                <div class=\"col-lg-6\">\n                    <div class=\"bg-primary text-center h-100 project\">\n                        <div class=\"d-flex h-100\">\n                            <div class=\"project-text w-100 my-auto text-center text-lg-left\">\n                                <h4 class=\"text-white\">Introduction</h4>\n                                <p class=\"mb-0 text-white\">\n                                    A brief introduction of the circumstances that triggered the system development project\n                                </p>\n                                <p></p>\n                                <hr class=\"d-none d-lg-block mb-0 ml-0\" />\n                            </div>\n                        </div>\n                    </div>\n                </div>\n            </div>\n            <!-- Background Row 2-->\n            <div class=\"row justify-content-center no-gutters mb-5 mb-lg-0\">\n                <div class=\"col-lg-6\"><img class=\"img-fluid\" src=\"assets/img/demo-image-4.jpg\" alt=\"\" /></div>\n                <div class=\"col-lg-6 order-lg-first\">\n                    <div class=\"bg-primary text-center h-100 project\">\n                        <div class=\"d-flex h-100\">\n                            <div class=\"project-text w-100 my-auto text-center text-lg-right\">\n                                <h4 class=\"text-white\">The Project</h4>\n                                <p class=\"mb-0 text-white\">\n                                    A few statements about the circumstances that triggered the system development exercise.\n                                </p>\n                                <p></p>\n                                <hr class=\"d-none d-lg-block mb-0 mr-0\" />\n                            </div>\n                        </div>\n                    </div>\n                </div>\n            </div>\n            <!-- Background Row 3-->\n            <div class=\"row justify-content-center no-gutters mb-5 mb-lg-0\">\n                <div class=\"col-lg-6\"><img class=\"img-fluid\" src=\"assets/img/demo-image-5.jpg\" alt=\"\" /></div>\n                <div class=\"col-lg-6\">\n                    <div class=\"bg-primary text-center h-100 project\">\n                        <div class=\"d-flex h-100\">\n                            <div class=\"project-text w-100 my-auto text-center text-lg-left\">\n                                <h4 class=\"text-white\">The System</h4>\n                                <p class=\"mb-0 text-white\">\n                                    A few statements about the system that was developed as a result.\n                                </p>\n                                <p></p>\n                                <hr class=\"d-none d-lg-block mb-0 ml-0\" />\n                            </div>\n                        </div>\n                    </div>\n                </div>\n            </div>\n            <!-- Background Row 4-->\n            <div class=\"row justify-content-center no-gutters mb-5 mb-lg-0\">\n                <div class=\"col-lg-6\"><img class=\"img-fluid\" src=\"assets/img/demo-image-6.jpg\" alt=\"\" /></div>\n                <div class=\"col-lg-6 order-lg-first\">\n                    <div class=\"bg-primary text-center h-100 project\">\n                        <div class=\"d-flex h-100\">\n                            <div class=\"project-text w-100 my-auto text-center text-lg-right\">\n                                <h4 class=\"text-white\">Status</h4>\n                                <p class=\"mb-0 text-white\">\n                                    A few statements as to the status of the developed system.\n                                </p>\n                                <p></p>\n                                <hr class=\"d-none d-lg-block mb-0 mr-0\" />\n                            </div>\n                        </div>\n                    </div>\n                </div>\n            </div>\n        </div>\n    </section>\n\n    <!-- Dashboard Link-->\n    <section class=\"signup-section\" id=\"signup\" #signup>\n        <div class=\"container\">\n            <div class=\"row\">\n                <div class=\"col-md-10 col-lg-8 mx-auto text-center\">\n                    <fa-icon class=\"fa-2x mb-2 text-white\" [icon]='[\"far\", \"paper-plane\"]'></fa-icon>\n                    <h2 class=\"text-white mb-5\">A call for the users to take action</h2>\n                    <a class=\"btn btn-primary mx-auto\" routerLink=\"/dashboard\">Open Dashboard</a>\n                    <!--<button class=\"btn btn-primary mx-auto\" type=\"submit\">Dashboard</button>-->\n                </div>\n            </div>\n        </div>\n    </section>\n    <!-- Contact-->\n    <section class=\"contact-section bg-primary\" id=\"contacts\" #contacts>\n        <div class=\"container\">\n            <div class=\"row\">\n                <div class=\"col-md-4 mb-3 mb-md-0\">\n                    <div class=\"card py-4 h-100\">\n                        <div class=\"card-body text-center\">\n                            <i class=\"fas fa-map-marked-alt text-primary mb-2\"></i>\n                            <h4 class=\"text-uppercase m-0\">\n                                <a href=\"https://www.miles.co.ke\" class=\"text-primary\"\n                                    target=\"_blank\">Partner 1</a>\n                            </h4>\n                            <hr class=\"my-4 mx-auto\" />\n                            <div class=\"small text-black-50\">\n                                <img class=\"img-fluid\" src=\"assets/img/tiger.png\" alt=\"\" />\n                            </div>\n                        </div>\n                    </div>\n                </div>\n                <div class=\"col-md-4 mb-3 mb-md-0\">\n                    <div class=\"card py-4 h-100\">\n                        <div class=\"card-body text-center\">\n                            <i class=\"fas fa-envelope text-primary mb-2\"></i>\n                            <h4 class=\"text-uppercase m-0\">\n                                <a href=\"http://www.miles.co.ke\" class=\"text-primary\" target=\"_blank\">Partner 2</a>\n                            </h4>\n                            <hr class=\"my-4 mx-auto\" />\n                            <div class=\"small text-black-50\">\n                                <img class=\"img-fluid\" src=\"assets/img/leo.png\" alt=\"\" />\n                            </div>\n                        </div>\n                    </div>\n                </div>\n                <div class=\"col-md-4 mb-3 mb-md-0\">\n                    <div class=\"card py-4 h-100\">\n                        <div class=\"card-body text-center\">\n                            <i class=\"fas fa-mobile-alt text-primary mb-2\"></i>\n                            <h4 class=\"text-uppercase m-0\">\n                                <a href=\"https://miles.co.ke/\" class=\"text-primary\" target=\"_blank\">Partner 3</a>\n                            </h4>\n                            <hr class=\"my-4 mx-auto bg-primary\" />\n                            <div class=\"small text-black-50\">\n                                <img class=\"img-fluid\" src=\"assets/img/jaguar.png\" alt=\"\" />\n                            </div>\n                        </div>\n                    </div>\n                </div>\n            </div>\n        </div>\n    </section>\n    <!-- Footer-->\n    <footer class=\"footer bg-primary small text-center text-white\">\n        <div class=\"container\">Copyright &copy; The Second Mile 2021</div>\n    </footer>\n</sb-layout-home>";
      /***/
    },

    /***/
    "pdzT":
    /*!*************************************************!*\
      !*** ./src/modules/home/home-routing.module.ts ***!
      \*************************************************/

    /*! exports provided: ROUTES, HomeRoutingModule */

    /***/
    function pdzT(module, __webpack_exports__, __webpack_require__) {
      "use strict";

      __webpack_require__.r(__webpack_exports__);
      /* harmony export (binding) */


      __webpack_require__.d(__webpack_exports__, "ROUTES", function () {
        return ROUTES;
      });
      /* harmony export (binding) */


      __webpack_require__.d(__webpack_exports__, "HomeRoutingModule", function () {
        return HomeRoutingModule;
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


      var _home_module__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(
      /*! ./home.module */
      "PH8I");
      /* harmony import */


      var _containers__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(
      /*! ./containers */
      "QX2z");
      /* tslint:disable: ordered-imports*/

      /* Module */

      /* Containers */

      /* Routes */


      var ROUTES = [{
        path: '',
        canActivate: [],
        component: _containers__WEBPACK_IMPORTED_MODULE_4__["HomeComponent"],
        data: {
          title: 'Home'
        }
      }];

      var HomeRoutingModule = function HomeRoutingModule() {
        _classCallCheck(this, HomeRoutingModule);
      };

      HomeRoutingModule = Object(tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"])([Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["NgModule"])({
        imports: [_home_module__WEBPACK_IMPORTED_MODULE_3__["HomeModule"], _angular_router__WEBPACK_IMPORTED_MODULE_2__["RouterModule"].forChild(ROUTES)],
        exports: [_angular_router__WEBPACK_IMPORTED_MODULE_2__["RouterModule"]]
      })], HomeRoutingModule);
      /***/
    }
  }]);
})();
//# sourceMappingURL=modules-home-home-routing-module-es5.js.map
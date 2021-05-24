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

  (window["webpackJsonp"] = window["webpackJsonp"] || []).push([["default~modules-unit-categories-unit-categories-routing-module~modules-units-units-routing-module"], {
    /***/
    "3E5Y":
    /*!******************************************************************************!*\
      !*** ./src/modules/unit-categories/services/unit-categories-data.service.ts ***!
      \******************************************************************************/

    /*! exports provided: UnitCategoriesDataService */

    /***/
    function E5Y(module, __webpack_exports__, __webpack_require__) {
      "use strict";

      __webpack_require__.r(__webpack_exports__);
      /* harmony export (binding) */


      __webpack_require__.d(__webpack_exports__, "UnitCategoriesDataService", function () {
        return UnitCategoriesDataService;
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
      /* harmony import */


      var _angular_common_http__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(
      /*! @angular/common/http */
      "IheW");
      /* harmony import */


      var ngx_logger__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(
      /*! ngx-logger */
      "P4hB");
      /* harmony import */


      var environments_environment__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__(
      /*! environments/environment */
      "AytR");
      /* harmony import */


      var _common_models_message_type_model__WEBPACK_IMPORTED_MODULE_6__ = __webpack_require__(
      /*! @common/models/message.type.model */
      "nB5A");
      /* harmony import */


      var _common_services__WEBPACK_IMPORTED_MODULE_7__ = __webpack_require__(
      /*! @common/services */
      "gNCb");
      /* harmony import */


      var rxjs_operators__WEBPACK_IMPORTED_MODULE_8__ = __webpack_require__(
      /*! rxjs/operators */
      "kU1M");

      var LOG_PREFIX = "[Unit Categories Data Service]";
      var API_PREFIX = "api/v1/unit_categories";
      var HEADERS = {
        'Content-Type': 'application/json'
      };

      var UnitCategoriesDataService = /*#__PURE__*/function () {
        function UnitCategoriesDataService(http, connectivityStatusService, messageService, zone, log) {
          var _this = this;

          _classCallCheck(this, UnitCategoriesDataService);

          this.http = http;
          this.connectivityStatusService = connectivityStatusService;
          this.messageService = messageService;
          this.zone = zone;
          this.log = log; // The base url of the server

          this._baseUrl = environments_environment__WEBPACK_IMPORTED_MODULE_5__["environment"].baseUrl; // The local data cache

          this._cache = {
            unitCategories: []
          }; // The observables that allow subscribers to keep tabs of the current status 
          // of unit categories records in the data store

          this._unitCategoriesSubject$ = new rxjs__WEBPACK_IMPORTED_MODULE_2__["BehaviorSubject"]([]);
          this.unitCategories$ = this._unitCategoriesSubject$.asObservable(); // The observable that we will use to opt out of initialization subscriptions 
          // once we are done with them

          this._done$ = new rxjs__WEBPACK_IMPORTED_MODULE_2__["Subject"](); // The api that we'll use to communicate data store changes when components that 
          // subscribe to this service are outside the current ngzone

          this.bc = new BroadcastChannel("unit-categories-data-channel");
          /**
           * Publish information to current (listening) ui
           * @param event
           */

          this.handleEvent = function (event) {
            _this.zone.run(function () {
              return _this._unitCategoriesSubject$.next(event.data.newValue);
            });
          }; // Subscribe to connectivity status notifications


          this.log.trace("".concat(LOG_PREFIX, " Subscribing to connectivity status notifications"));
          this.connectivityStatusService.online$.pipe(Object(rxjs_operators__WEBPACK_IMPORTED_MODULE_8__["takeUntil"])(this._done$)).subscribe(function (online) {
            // Check if the user is online
            _this.log.trace("".concat(LOG_PREFIX, " Checking if the user is online"));

            _this.log.debug("".concat(LOG_PREFIX, " User is online = ").concat(online));

            if (online) {
              // Initialize data
              _this.log.trace("".concat(LOG_PREFIX, " Initializing data"));

              _this.getAllUnitCategories().pipe(Object(rxjs_operators__WEBPACK_IMPORTED_MODULE_8__["first"])()) // This will automatically complete (and therefore unsubscribe) after the first value has been emitted.
              .subscribe(function (response) {
                // Data initialization complete
                _this.log.trace("".concat(LOG_PREFIX, " Data initialization complete"));
              }); // Unsubscribe from connectivity status notifications


              _this.log.trace("".concat(LOG_PREFIX, " Unsubscribing from connectivity status notifications"));

              _this._done$.next();

              _this._done$.complete();
            }
          }); //Note: "bc.onmessage" isn't invoked on sender ui

          this.bc.onmessage = this.zone.run(function () {
            return _this.handleEvent;
          });
        }
        /**
         * Creates and adds an instance of a new Unit Category record to the local cache and then broadcasts the changes to all subscribers
         *
         * @param unitCategory The details of the Unit Category record to be created - with the id and version details missing
         */


        _createClass(UnitCategoriesDataService, [{
          key: "createUnitCategory",
          value: function createUnitCategory(unitCategory) {
            var _this2 = this;

            this.log.trace("".concat(LOG_PREFIX, " Entering createUnitCategory()"));
            this.log.debug("".concat(LOG_PREFIX, " UnitCategory = ").concat(JSON.stringify(unitCategory))); // Make a HTTP POST Request to create the record

            this.log.debug("".concat(LOG_PREFIX, " Making a HTTP POST Request to ").concat(this._baseUrl, "/").concat(API_PREFIX, " to create the record"));
            return this.http.post("".concat(this._baseUrl, "/").concat(API_PREFIX), JSON.stringify(unitCategory), {
              headers: new _angular_common_http__WEBPACK_IMPORTED_MODULE_3__["HttpHeaders"](HEADERS)
            }).pipe(Object(rxjs_operators__WEBPACK_IMPORTED_MODULE_8__["tap"])(function (data) {
              // Unit Category record Creation was successful
              _this2.log.trace("".concat(LOG_PREFIX, " Record Creation was successful"));

              _this2.log.debug("".concat(LOG_PREFIX, " Created Unit Category record = ").concat(JSON.stringify(data))); // Add the newly created Unit Category record to the Local Cache


              _this2.log.trace("".concat(LOG_PREFIX, " Adding the newly created Unit Category record to the Local Cache"));

              _this2._cache.unitCategories.push(data); // Create an up to date copy of the Unit Categories records


              _this2.log.trace("".concat(LOG_PREFIX, " Creating an up to date copy of the Unit Categories records"));

              var copy = Object.assign({}, _this2._cache).unitCategories; // Broadcast the up to date copy of the Unit Categories records to the current listener

              _this2.log.trace("".concat(LOG_PREFIX, " Broadcasting the up to date copy of the Unit Categories records to the current listener"));

              _this2._unitCategoriesSubject$.next(copy); // Broadcast the up to date copy of the Unit Categories records to the other listeners


              _this2.log.trace("".concat(LOG_PREFIX, " Broadcasting the up to date copy of the Unit Categories records to the other listeners"));

              _this2.bc.postMessage({
                newValue: copy
              }); // Send a message that states that the Unit Category record Creation was successful


              _this2.log.trace("".concat(LOG_PREFIX, " Sending a message that states that the Unit Category record Creation was successful"));

              _this2.messageService.sendMessage({
                "type": _common_models_message_type_model__WEBPACK_IMPORTED_MODULE_6__["MessageType"].Success,
                "message": "The Unit Category record Creation was successful"
              });
            }), Object(rxjs_operators__WEBPACK_IMPORTED_MODULE_8__["catchError"])(function (error) {
              // Unit Category record Creation was unsuccessful
              _this2.log.error("".concat(LOG_PREFIX, " Unit Category record Creation was unsuccessful: ").concat(error.statusText || "See Server Logs for more details")); // Send a message that states that the Unit Category record Creation was unsuccessful


              _this2.log.trace("".concat(LOG_PREFIX, " Sending a message that states that the Unit Category record Creation was unsuccessful"));

              _this2.messageService.sendMessage({
                "type": _common_models_message_type_model__WEBPACK_IMPORTED_MODULE_6__["MessageType"].Error,
                "message": "The Unit Category record Creation was unsuccessful"
              });

              return Object(rxjs__WEBPACK_IMPORTED_MODULE_2__["throwError"])(error);
            }));
          }
          /**
           * Retrieves and adds a single Unit Category record to the local cache and then broadcasts the changes to all subscribers
           *
           * @param unitCategoryId The Unique Identifier of the Unit Category record
           */

        }, {
          key: "getUnitCategory",
          value: function getUnitCategory(unitCategoryId) {
            var _this3 = this;

            this.log.trace("".concat(LOG_PREFIX, " Entering getUnitCategory()"));
            this.log.debug("".concat(LOG_PREFIX, " UnitCategory Id = ").concat(unitCategoryId)); // Make a HTTP GET Request to retrieve the record

            this.log.debug("".concat(LOG_PREFIX, " Making a HTTP GET Request to ").concat(this._baseUrl, "/").concat(API_PREFIX, "/ids/").concat(unitCategoryId, " to retrieve the record"));
            return this.http.get("".concat(this._baseUrl, "/").concat(API_PREFIX, "/ids/").concat(unitCategoryId), {
              headers: new _angular_common_http__WEBPACK_IMPORTED_MODULE_3__["HttpHeaders"](HEADERS)
            }).pipe(Object(rxjs_operators__WEBPACK_IMPORTED_MODULE_8__["tap"])(function (data) {
              // Unit Category record Retrieval was successful
              _this3.log.trace("".concat(LOG_PREFIX, " Unit Category record Retrieval was successful"));

              _this3.log.debug("".concat(LOG_PREFIX, " Retrieved Unit Category record = ").concat(JSON.stringify(data))); // Search for the Unit Category record in the Local Cache and return its index


              _this3.log.trace("".concat(LOG_PREFIX, " Searching for the Unit Category record in the Local Cache and returning its index"));

              var index = _this3._cache.unitCategories.findIndex(function (d) {
                return d.id === data.id;
              });

              _this3.log.debug("".concat(LOG_PREFIX, " Unit Category record Index = ").concat(index)); // If the record was found (index != -1), update it, else, add it to the Local Storage


              if (index != -1) {
                // The Unit Category record was found in the Local Cache
                _this3.log.trace("".concat(LOG_PREFIX, " The Unit Category record was found in the Local Cache")); // Update the local Unit Category record


                _this3.log.trace("".concat(LOG_PREFIX, " Updating the local Unit Category record"));

                _this3._cache.unitCategories[index] = data;
              } else {
                // The Unit Category record was not found in the Local Cache
                _this3.log.trace("".concat(LOG_PREFIX, " The Unit Category record was not found in the Local Cache")); // Add the Unit Category record to the Local Cache


                _this3.log.trace("".concat(LOG_PREFIX, " Adding the Unit Category record to the Local Cache"));

                _this3._cache.unitCategories.push(data);
              } // Create an up to date copy of the Unit Categories records


              _this3.log.trace("".concat(LOG_PREFIX, " Creating an up to date copy of the Unit Categories records"));

              var copy = Object.assign({}, _this3._cache).unitCategories; // Broadcast the up to date copy of the Unit Categories records to the current listener

              _this3.log.trace("".concat(LOG_PREFIX, " Broadcasting the up to date copy of the Unit Categories records to the current listener"));

              _this3._unitCategoriesSubject$.next(copy); // Broadcast the up to date copy of the Unit Categories records to the other listeners


              _this3.log.trace("".concat(LOG_PREFIX, " Broadcasting the up to date copy of the Unit Categories records to the other listeners"));

              _this3.bc.postMessage({
                newValue: copy
              }); // Send a message that states that the Unit Category record Retrieval was successful


              _this3.log.trace("".concat(LOG_PREFIX, " Sending a message that states that the Unit Category record Retrieval was successful"));

              _this3.messageService.sendMessage({
                "type": _common_models_message_type_model__WEBPACK_IMPORTED_MODULE_6__["MessageType"].Success,
                "message": "The Unit Category record Retrieval was successful"
              });
            }), Object(rxjs_operators__WEBPACK_IMPORTED_MODULE_8__["catchError"])(function (error) {
              // Unit Category record Retrieval was unsuccessful
              _this3.log.error("".concat(LOG_PREFIX, " Unit Category record Retrieval was unsuccessful: ").concat(error.statusText || "See Server Logs for more details")); // Send a message that states that the Unit Category record Retrieval was unsuccessful


              _this3.log.trace("".concat(LOG_PREFIX, " Sending a message that states that the Unit Category record Retrieval was unsuccessful"));

              _this3.messageService.sendMessage({
                "type": _common_models_message_type_model__WEBPACK_IMPORTED_MODULE_6__["MessageType"].Error,
                "message": "The Unit Category record Retrieval was unsuccessful"
              });

              return Object(rxjs__WEBPACK_IMPORTED_MODULE_2__["throwError"])(error);
            }));
          }
          /**
           * Retrieves and adds all or a subset of all Unit Categories records to the local cache and then broadcasts the changes to all subscribers
           *
           * @param filters Optional query parameters used in filtering the retrieved records
           */

        }, {
          key: "getAllUnitCategories",
          value: function getAllUnitCategories(filters) {
            var _this4 = this;

            this.log.trace("".concat(LOG_PREFIX, " Entering getAllUnitCategories()"));
            this.log.debug("".concat(LOG_PREFIX, " Filters = ").concat(JSON.stringify(filters))); // Make a HTTP GET Request to retrieve the records

            this.log.debug("".concat(LOG_PREFIX, " Making a HTTP GET Request to ").concat(this._baseUrl, "/").concat(API_PREFIX, "/all to retrieve the records"));
            return this.http.get("".concat(this._baseUrl, "/").concat(API_PREFIX, "/all"), {
              headers: new _angular_common_http__WEBPACK_IMPORTED_MODULE_3__["HttpHeaders"](HEADERS),
              params: filters == null ? {} : filters
            }).pipe(Object(rxjs_operators__WEBPACK_IMPORTED_MODULE_8__["tap"])(function (data) {
              // Unit Categories records Retrieval was successful
              _this4.log.trace("".concat(LOG_PREFIX, " Unit Categories records Retrieval was successful"));

              _this4.log.debug("".concat(LOG_PREFIX, " Retrieved Unit Categories records = ").concat(JSON.stringify(data))); // Update the Unit Categories records in the Local Cache to the newly pulled Unit Categories records


              _this4.log.trace("".concat(LOG_PREFIX, " Updating the Unit Categories records in the Local Cache to the newly pulled Unit Categories records"));

              _this4._cache.unitCategories = data; // Create an up to date copy of the Unit Categories records

              _this4.log.trace("".concat(LOG_PREFIX, " Creating an up to date copy of the Unit Categories records"));

              var copy = Object.assign({}, _this4._cache).unitCategories; // Broadcast the up to date copy of the Unit Categories records to the current listener

              _this4.log.trace("".concat(LOG_PREFIX, " Broadcasting the up to date copy of the Unit Categories records to the current listener"));

              _this4._unitCategoriesSubject$.next(copy); // Broadcast the up to date copy of the Unit Categories records to the other listeners


              _this4.log.trace("".concat(LOG_PREFIX, " Broadcasting the up to date copy of the Unit Categories records to the other listeners"));

              _this4.bc.postMessage({
                newValue: copy
              }); // Send a message that states that the Unit Categories records Retrieval was successful


              _this4.log.trace("".concat(LOG_PREFIX, " Sending a message that states that the Unit Categories records Retrieval was successful"));

              _this4.messageService.sendMessage({
                "type": _common_models_message_type_model__WEBPACK_IMPORTED_MODULE_6__["MessageType"].Success,
                "message": "The Unit Categories records Retrieval was successful"
              });
            }), Object(rxjs_operators__WEBPACK_IMPORTED_MODULE_8__["catchError"])(function (error) {
              // Unit Categories records Retrieval was unsuccessful
              _this4.log.error("".concat(LOG_PREFIX, " Unit Categories records Retrieval was unsuccessful: ").concat(error.statusText || "See Server Logs for more details")); // Send a message that states that the Unit Categories records Retrieval was unsuccessful


              _this4.log.trace("".concat(LOG_PREFIX, " Sending a message that states that the Unit Categories records Retrieval was unsuccessful"));

              _this4.messageService.sendMessage({
                "type": _common_models_message_type_model__WEBPACK_IMPORTED_MODULE_6__["MessageType"].Error,
                "message": "The Unit Categories records Retrieval was unsuccessful"
              });

              return Object(rxjs__WEBPACK_IMPORTED_MODULE_2__["throwError"])(error);
            }));
          }
          /**
           * Updates a single Unit Category record and its corresponding counterpart in the local cache and then broadcasts the changes to all subscribers
           *
           * @param unitCategory The details of the Unit Category record to be updated
           */

        }, {
          key: "updateUnitCategory",
          value: function updateUnitCategory(unitCategory) {
            var _this5 = this;

            this.log.trace("".concat(LOG_PREFIX, " Entering updateUnitCategory()"));
            this.log.debug("".concat(LOG_PREFIX, " UnitCategory = ").concat(JSON.stringify(unitCategory))); // Make a HTTP POST Request to retrieve the records

            this.log.debug("".concat(LOG_PREFIX, " Making a HTTP POST Request to ").concat(this._baseUrl, "/").concat(API_PREFIX, " to update the record"));
            return this.http.put("".concat(this._baseUrl, "/").concat(API_PREFIX), JSON.stringify(unitCategory), {
              headers: new _angular_common_http__WEBPACK_IMPORTED_MODULE_3__["HttpHeaders"](HEADERS)
            }).pipe(Object(rxjs_operators__WEBPACK_IMPORTED_MODULE_8__["tap"])(function (data) {
              // Unit Category record Update was successful
              _this5.log.trace("".concat(LOG_PREFIX, " Unit Category record Update was successful"));

              _this5.log.debug("".concat(LOG_PREFIX, " Updated Unit Category record = ").concat(JSON.stringify(data))); // Search for the locally stored Unit Category record


              _this5.log.trace("".concat(LOG_PREFIX, " Searching for the locally stored Unit Category record"));

              var index = _this5._cache.unitCategories.findIndex(function (d) {
                return d.id === data.id;
              });

              _this5.log.debug("".concat(LOG_PREFIX, " Updated Unit Category record Index = ").concat(index)); // If the record was found (index != -1), update it in the Local Cache


              if (index != -1) {
                // Update the local Unit Category record
                _this5.log.trace("".concat(LOG_PREFIX, " Updating the locally stored Unit Category record"));

                _this5._cache.unitCategories[index] = data; // Create an up to date copy of the Unit Categories records

                _this5.log.trace("".concat(LOG_PREFIX, " Creating an up to date copy of the Unit Categories records"));

                var copy = Object.assign({}, _this5._cache).unitCategories; // Broadcast the up to date copy of the Unit Categories records to the current listener

                _this5.log.trace("".concat(LOG_PREFIX, " Broadcasting the up to date copy of the Unit Categories records to the current listener"));

                _this5._unitCategoriesSubject$.next(copy); // Broadcast the up to date copy of the Unit Categories records to the other listeners


                _this5.log.trace("".concat(LOG_PREFIX, " Broadcasting the up to date copy of the Unit Categories records to the other listeners"));

                _this5.bc.postMessage({
                  newValue: copy
                }); // Send a message that states that the Unit Category record Update was successful


                _this5.log.trace("".concat(LOG_PREFIX, " Sending a message that states that the Unit Category record Update was successful"));

                _this5.messageService.sendMessage({
                  "type": _common_models_message_type_model__WEBPACK_IMPORTED_MODULE_6__["MessageType"].Success,
                  "message": "The Unit Category record Update was successful"
                });
              } else {
                // Local Cache Update was unsuccessful
                _this5.log.error("".concat(LOG_PREFIX, " Local Cache Update was unsuccessful: Unit Category record is missing in the Local Cache")); // Send a message that states that the Local Cache Update was unsuccessful


                _this5.log.trace("".concat(LOG_PREFIX, " Sending a message that states that the Local Cache Update was unsuccessful"));

                _this5.messageService.sendMessage({
                  "type": _common_models_message_type_model__WEBPACK_IMPORTED_MODULE_6__["MessageType"].Error,
                  "message": "Unit Categories records Local Cache Update was unsuccessful"
                });
              }
            }), Object(rxjs_operators__WEBPACK_IMPORTED_MODULE_8__["catchError"])(function (error) {
              // Unit Category record Update was unsuccessful
              _this5.log.error("".concat(LOG_PREFIX, " Unit Category record Update was unsuccessful: ").concat(error.statusText || "See Server Logs for more details")); // Send a message that states that the Unit Category record Update was unsuccessful


              _this5.log.trace("".concat(LOG_PREFIX, " Sending a message that states that the Unit Category record Update was unsuccessful"));

              _this5.messageService.sendMessage({
                "type": _common_models_message_type_model__WEBPACK_IMPORTED_MODULE_6__["MessageType"].Error,
                "message": "The Unit Category record Update was unsuccessful"
              });

              return Object(rxjs__WEBPACK_IMPORTED_MODULE_2__["throwError"])(error);
            }));
          }
          /**
           * Deletes a single Unit Category record and its corresponding counterpart in the local cache and then broadcasts the changes to all subscribers
           *
           * @param unitCategoryId The Unique Identifier of the record
           * @returns The total count of deleted records - which should be 1 in this case if the delete operation was successful
           */

        }, {
          key: "deleteUnitCategory",
          value: function deleteUnitCategory(unitCategoryId) {
            var _this6 = this;

            this.log.trace("".concat(LOG_PREFIX, " Entering deleteUnitCategory()"));
            this.log.debug("".concat(LOG_PREFIX, " UnitCategory Id = ").concat(unitCategoryId)); // Make a HTTP DELETE Request to retrieve the records

            this.log.debug("".concat(LOG_PREFIX, " Making a HTTP DELETE Request to ").concat(this._baseUrl, "/").concat(API_PREFIX, "/ids/").concat(unitCategoryId, " to delete the record"));
            return this.http["delete"]("".concat(this._baseUrl, "/").concat(API_PREFIX, "/ids/").concat(unitCategoryId), {
              headers: new _angular_common_http__WEBPACK_IMPORTED_MODULE_3__["HttpHeaders"](HEADERS)
            }).pipe(Object(rxjs_operators__WEBPACK_IMPORTED_MODULE_8__["tap"])(function (count) {
              // Mark the deletion successful if and only if exactly 1 record was deleted
              if (count == 1) {
                // Unit Category record Deletion was successful
                _this6.log.trace("".concat(LOG_PREFIX, " Unit Category record Deletion was successful")); // Search for the deleted Unit Category record in the Local Cache


                _this6.log.trace("".concat(LOG_PREFIX, " Searching for the deleted Unit Category record in the Local Cache"));

                var index = _this6._cache.unitCategories.findIndex(function (d) {
                  return d.id == unitCategoryId;
                });

                _this6.log.debug("".concat(LOG_PREFIX, " Deleted Unit Category record Index = ").concat(index)); // If the record was found (index != -1), remove it from the Local Cache


                if (index != -1) {
                  // Remove the deleted Unit Category record from the Local Cache
                  _this6.log.trace("".concat(LOG_PREFIX, " Removing the deleted Unit Category record from the Local Cache"));

                  _this6._cache.unitCategories.splice(index, 1); // Create an up to date copy of the Unit Categories records


                  _this6.log.trace("".concat(LOG_PREFIX, " Creating an up to date copy of the Unit Categories records"));

                  var copy = Object.assign({}, _this6._cache).unitCategories; // Broadcast the up to date copy of the Unit Categories records to the current listener

                  _this6.log.trace("".concat(LOG_PREFIX, " Broadcasting the up to date copy of the Unit Categories records to the current listener"));

                  _this6._unitCategoriesSubject$.next(copy); // Broadcast the up to date copy of the Unit Categories records to the other listeners


                  _this6.log.trace("".concat(LOG_PREFIX, " Broadcasting the up to date copy of the Unit Categories records to the other listeners"));

                  _this6.bc.postMessage({
                    newValue: copy
                  }); // Send a message that states that the Unit Category record Deletion was successful


                  _this6.log.trace("".concat(LOG_PREFIX, " Sending a message that states that the Unit Category record Deletion was successful"));

                  _this6.messageService.sendMessage({
                    "type": _common_models_message_type_model__WEBPACK_IMPORTED_MODULE_6__["MessageType"].Success,
                    "message": "The Unit Category record Deletion was successful"
                  });
                } else {
                  // Local Cache Update was unsuccessful
                  _this6.log.error("".concat(LOG_PREFIX, " Local Cache Update was unsuccessful: Unit Category record is missing in the Local Cache")); // Send a message that states that the Local Cache Update was unsuccessful


                  _this6.log.trace("".concat(LOG_PREFIX, " Sending a message that states that the Local Cache Update was unsuccessful"));

                  _this6.messageService.sendMessage({
                    "type": _common_models_message_type_model__WEBPACK_IMPORTED_MODULE_6__["MessageType"].Error,
                    "message": "Unit Categories records Local Cache Update was unsuccessful"
                  });
                }
              } else {
                // Unit Category record Deletion was unsuccessful
                _this6.log.error("".concat(LOG_PREFIX, " Unit Category record Deletion was unsuccessful: Expecting 1 record to be deleted instead of ").concat(count)); // Send a message that states that the Unit Category record Deletion was unsuccessful


                _this6.log.trace("".concat(LOG_PREFIX, " Sending a message that states that the Unit Category record Deletion was unsuccessful"));

                _this6.messageService.sendMessage({
                  "type": _common_models_message_type_model__WEBPACK_IMPORTED_MODULE_6__["MessageType"].Error,
                  "message": "The Unit Category record Deletion was unsuccessful"
                });
              }
            }), Object(rxjs_operators__WEBPACK_IMPORTED_MODULE_8__["catchError"])(function (error) {
              // Unit Category record Deletion was unsuccessful
              _this6.log.error("".concat(LOG_PREFIX, " Unit Category record Deletion was unsuccessful: ").concat(error.statusText || "See Server Logs for more details")); // Send a message that states that the Unit Category record Deletion was unsuccessful


              _this6.log.trace("".concat(LOG_PREFIX, " Sending a message that states that the Unit Category record Deletion was unsuccessful"));

              _this6.messageService.sendMessage({
                "type": _common_models_message_type_model__WEBPACK_IMPORTED_MODULE_6__["MessageType"].Error,
                "message": "The Unit Category record Deletion was unsuccessful"
              });

              return Object(rxjs__WEBPACK_IMPORTED_MODULE_2__["throwError"])(error);
            }));
          }
          /**
           * Use BehaviorSubject's getter property named value to get the most recent value passed through it.
           */

        }, {
          key: "records",
          get: function get() {
            return this._unitCategoriesSubject$.value;
          }
        }]);

        return UnitCategoriesDataService;
      }();

      UnitCategoriesDataService.ctorParameters = function () {
        return [{
          type: _angular_common_http__WEBPACK_IMPORTED_MODULE_3__["HttpClient"]
        }, {
          type: _common_services__WEBPACK_IMPORTED_MODULE_7__["ConnectivityStatusService"]
        }, {
          type: _common_services__WEBPACK_IMPORTED_MODULE_7__["MessageService"]
        }, {
          type: _angular_core__WEBPACK_IMPORTED_MODULE_1__["NgZone"]
        }, {
          type: ngx_logger__WEBPACK_IMPORTED_MODULE_4__["NGXLogger"]
        }];
      };

      UnitCategoriesDataService = Object(tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"])([Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["Injectable"])({
        providedIn: 'root'
      }), Object(tslib__WEBPACK_IMPORTED_MODULE_0__["__metadata"])("design:paramtypes", [_angular_common_http__WEBPACK_IMPORTED_MODULE_3__["HttpClient"], _common_services__WEBPACK_IMPORTED_MODULE_7__["ConnectivityStatusService"], _common_services__WEBPACK_IMPORTED_MODULE_7__["MessageService"], _angular_core__WEBPACK_IMPORTED_MODULE_1__["NgZone"], ngx_logger__WEBPACK_IMPORTED_MODULE_4__["NGXLogger"]])], UnitCategoriesDataService);
      /***/
    },

    /***/
    "CUCj":
    /*!*******************************************************!*\
      !*** ./src/modules/unit-categories/services/index.ts ***!
      \*******************************************************/

    /*! exports provided: services, UnitCategoriesDataService, UnitCategoriesRecordsTabulationService */

    /***/
    function CUCj(module, __webpack_exports__, __webpack_require__) {
      "use strict";

      __webpack_require__.r(__webpack_exports__);
      /* harmony export (binding) */


      __webpack_require__.d(__webpack_exports__, "services", function () {
        return services;
      });
      /* harmony import */


      var _unit_categories_data_service__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(
      /*! ./unit-categories-data.service */
      "3E5Y");
      /* harmony import */


      var _unit_categories_records_tabulation_service__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(
      /*! ./unit-categories-records-tabulation.service */
      "tqN6");
      /* harmony reexport (safe) */


      __webpack_require__.d(__webpack_exports__, "UnitCategoriesDataService", function () {
        return _unit_categories_data_service__WEBPACK_IMPORTED_MODULE_0__["UnitCategoriesDataService"];
      });
      /* harmony reexport (safe) */


      __webpack_require__.d(__webpack_exports__, "UnitCategoriesRecordsTabulationService", function () {
        return _unit_categories_records_tabulation_service__WEBPACK_IMPORTED_MODULE_1__["UnitCategoriesRecordsTabulationService"];
      });

      var services = [_unit_categories_data_service__WEBPACK_IMPORTED_MODULE_0__["UnitCategoriesDataService"], _unit_categories_records_tabulation_service__WEBPACK_IMPORTED_MODULE_1__["UnitCategoriesRecordsTabulationService"]];
      /***/
    },

    /***/
    "nB5A":
    /*!*************************************************************!*\
      !*** ./src/modules/app-common/models/message.type.model.ts ***!
      \*************************************************************/

    /*! exports provided: MessageType */

    /***/
    function nB5A(module, __webpack_exports__, __webpack_require__) {
      "use strict";

      __webpack_require__.r(__webpack_exports__);
      /* harmony export (binding) */


      __webpack_require__.d(__webpack_exports__, "MessageType", function () {
        return MessageType;
      });

      var MessageType;

      (function (MessageType) {
        MessageType[MessageType["Success"] = 0] = "Success";
        MessageType[MessageType["Error"] = 1] = "Error";
        MessageType[MessageType["Info"] = 2] = "Info";
        MessageType[MessageType["Warning"] = 3] = "Warning";
      })(MessageType || (MessageType = {}));
      /***/

    },

    /***/
    "tqN6":
    /*!********************************************************************************************!*\
      !*** ./src/modules/unit-categories/services/unit-categories-records-tabulation.service.ts ***!
      \********************************************************************************************/

    /*! exports provided: UnitCategoriesRecordsTabulationService */

    /***/
    function tqN6(module, __webpack_exports__, __webpack_require__) {
      "use strict";

      __webpack_require__.r(__webpack_exports__);
      /* harmony export (binding) */


      __webpack_require__.d(__webpack_exports__, "UnitCategoriesRecordsTabulationService", function () {
        return UnitCategoriesRecordsTabulationService;
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


      var _unit_categories_data_service__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(
      /*! ./unit-categories-data.service */
      "3E5Y");
      /* harmony import */


      var ngx_logger__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(
      /*! ngx-logger */
      "P4hB");
      /* harmony import */


      var rxjs__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(
      /*! rxjs */
      "qCKp");

      var LOG_PREFIX = "[Unit Categories Records Tabulation Service]";

      var UnitCategoriesRecordsTabulationService = /*#__PURE__*/function () {
        function UnitCategoriesRecordsTabulationService(unitCategoriesDataService, log) {
          var _this7 = this;

          _classCallCheck(this, UnitCategoriesRecordsTabulationService);

          this.unitCategoriesDataService = unitCategoriesDataService;
          this.log = log; // The observables that will be updated / broadcasted whenever 
          // a background task is started and completed  

          this._loadingSubject$ = new rxjs__WEBPACK_IMPORTED_MODULE_4__["BehaviorSubject"](true);
          this._loading$ = this._loadingSubject$.asObservable(); // The first set of observables that will be updated / broadcasted whenever 
          // Unit Categories records are transformed as per the user defined search 
          // or sort criteria    

          this._unitCategoriesSubject$ = new rxjs__WEBPACK_IMPORTED_MODULE_4__["BehaviorSubject"]([]);
          this._unitCategories$ = this._unitCategoriesSubject$.asObservable(); // The second set of observables that will be updated / broadcasted whenever 
          // Unit Categories records are transformed as per the user defined search 
          // or sort criteria

          this._totalSubject$ = new rxjs__WEBPACK_IMPORTED_MODULE_4__["BehaviorSubject"](0);
          this._total$ = this._totalSubject$.asObservable(); // The user defined search or sort criteria.
          // Determines which & how many Unit Categories records should be displayed

          this._state = {
            page: 1,
            pageSize: 4,
            searchTerm: '',
            sortColumn: '',
            sortDirection: ''
          }; // A common gathering point for all the component's subscriptions.
          // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   

          this._subscriptions = [];

          this._subscriptions.push(this.unitCategoriesDataService.unitCategories$.subscribe(function (unitCategories) {
            _this7._transform(unitCategories);
          }));
        }

        _createClass(UnitCategoriesRecordsTabulationService, [{
          key: "ngOnDestroy",
          value: function ngOnDestroy() {
            this._subscriptions.forEach(function (s) {
              return s.unsubscribe();
            });
          }
          /**
           * Returns an observable containing Unit Categories records that have been filtered as per the user defined criteria
           */

        }, {
          key: "_set",

          /**
           * Utility method for all the class setters.
           * Does the actual updating of details / transforming of data
           * @param patch the partially updated details
           */
          value: function _set(patch) {
            // Update the state
            Object.assign(this._state, patch); // Transform the Unit Categories records

            this._transform(this.unitCategoriesDataService.records);
          }
          /**
           * Compares two values to find out if the first value preceeds the second.
           * When comparing string values, please note that this method is case sensitive
           *
           * @param v1 The first value
           * @param v2 The second value
           * @returns -1 if v1 preceeds v2, 0 if v1 is equal to v2 or 1 if v1 is greater than v2
           */

        }, {
          key: "compare",
          value: function compare(v1, v2) {
            this.log.trace("".concat(LOG_PREFIX, " Comparing two values to find out if the first value preceeds the second"));
            return v1 == undefined || v1 == null || v2 == undefined || v2 == null ? 0 : v1 < v2 ? -1 : v1 > v2 ? 1 : 0;
          }
          /**
           * Sorts Unit Categories Records
           *
           * @param unitCategories The Unit Categories records to sort
           * @param column The table column to sort the records by
           * @param direction The desired sort direction - ascending or descending
           * @returns The sorted Unit Categories records
           */

        }, {
          key: "sort",
          value: function sort(unitCategories, column, direction) {
            var _this8 = this;

            this.log.trace("".concat(LOG_PREFIX, " Sorting Unit Categories records"));

            if (direction === '' || column == null) {
              return unitCategories;
            } else {
              return _toConsumableArray(unitCategories).sort(function (a, b) {
                var res = _this8.compare(a[column], b[column]);

                return direction === 'asc' ? res : -res;
              });
            }
          }
          /**
           * Checks if search string is present in Unit Category record
           *
           * @param unitCategory The Unit Category record
           * @param term The Search String
           * @returns A boolean result indicating whether or not a match was found
           */

        }, {
          key: "matches",
          value: function matches(unitCategory, term) {
            this.log.trace("".concat(LOG_PREFIX, " Checking if search string is present in Unit Category record"));

            if (unitCategory != null && unitCategory != undefined) {
              // Try locating the search string in the Unit Category's name
              if (unitCategory.name != null && unitCategory.name != undefined) {
                if (unitCategory.name.toLowerCase().includes(term.toLowerCase())) {
                  return true;
                }
              }
            }

            return false;
          }
          /**
           * Paginates Unit Categories Records
           *
           * @param unitCategories The Unit Categories records to paginate
           * @returns The paginated Unit Categories records
           */

        }, {
          key: "paginate",
          value: function paginate(unitCategories, page, pageSize) {
            this.log.trace("".concat(LOG_PREFIX, " Paginating Unit Categories records"));
            return unitCategories.slice((page - 1) * pageSize, (page - 1) * pageSize + pageSize);
          }
          /**
           * Updates the index of the Unit Categories Records
           *
           * @param unitCategories The Unit Categories records to sort
           * @returns The newly indexed Unit Categories records
           */

        }, {
          key: "index",
          value: function index(unitCategories) {
            this.log.trace("".concat(LOG_PREFIX, " Indexing Unit Categories records"));
            var pos = 0;
            return unitCategories.map(function (d) {
              d.pos = ++pos;
              return d;
            });
          }
          /**
           * Sorts, filters and paginates Unit Categories records
           *
           * @param records the original Unit Categories records
           */

        }, {
          key: "_transform",
          value: function _transform(records) {
            var _this9 = this;

            // Flag
            this._loadingSubject$.next(true);

            if (records.length != 0) {
              this.log.trace("".concat(LOG_PREFIX, " Sorting, filtering and paginating Unit Categories records"));
              var _this$_state = this._state,
                  sortColumn = _this$_state.sortColumn,
                  sortDirection = _this$_state.sortDirection,
                  pageSize = _this$_state.pageSize,
                  page = _this$_state.page,
                  searchTerm = _this$_state.searchTerm; // Sort

              var transformed = this.sort(records, sortColumn, sortDirection); // Filter

              transformed = transformed.filter(function (unitCategory) {
                return _this9.matches(unitCategory, searchTerm);
              });
              var total = transformed.length; // Index

              transformed = this.index(transformed); // Paginate

              transformed = this.paginate(transformed, page, pageSize); // Broadcast

              this._unitCategoriesSubject$.next(transformed);

              this._totalSubject$.next(total);
            } else {
              // Broadcast
              this._unitCategoriesSubject$.next([]);

              this._totalSubject$.next(0);
            } // Flag


            this._loadingSubject$.next(false);
          }
        }, {
          key: "unitCategories$",
          get: function get() {
            this.log.trace("".concat(LOG_PREFIX, " Getting unitCategories$ observable"));
            this.log.debug("".concat(LOG_PREFIX, " Current unitCategories$ observable value = ").concat(JSON.stringify(this._unitCategoriesSubject$.value)));
            return this._unitCategories$;
          }
          /**
           * Returns an observable containing the total number of Unit Categories records that have been filtered as per the user defined criteria
           */

        }, {
          key: "total$",
          get: function get() {
            this.log.trace("".concat(LOG_PREFIX, " Getting total$ observable"));
            this.log.debug("".concat(LOG_PREFIX, " Current total$ observable value = ").concat(JSON.stringify(this._totalSubject$.value)));
            return this._total$;
          }
          /**
           * Returns an observable containing a boolean flag that indicates whether or not a data operation exercise (sorting, searching etc.) is currently underway
           */

        }, {
          key: "loading$",
          get: function get() {
            this.log.trace("".concat(LOG_PREFIX, " Getting loading$ observable"));
            this.log.debug("".concat(LOG_PREFIX, " Current loading$ observable value = ").concat(JSON.stringify(this._loadingSubject$.value)));
            return this._loading$;
          }
          /**
           * Returns the currently active page
           */

        }, {
          key: "page",
          get: function get() {
            this.log.trace("".concat(LOG_PREFIX, " Getting page detail"));
            this.log.debug("".concat(LOG_PREFIX, " Current page detail value = ").concat(JSON.stringify(this._state.page)));
            return this._state.page;
          }
          /**
           * Updates the currently active page detail and then triggers data transformation
           */
          ,
          set: function set(page) {
            this.log.trace("".concat(LOG_PREFIX, " Setting page detail to ").concat(JSON.stringify(page)));

            this._set({
              page: page
            });
          }
          /**
           * Returns the currently set page size
           */

        }, {
          key: "pageSize",
          get: function get() {
            this.log.trace("".concat(LOG_PREFIX, " Getting page size detail"));
            this.log.debug("".concat(LOG_PREFIX, " Current page size detail = ").concat(JSON.stringify(this._state.pageSize)));
            return this._state.pageSize;
          }
          /**
           * Updates the desired page size detail and then triggers data transformation
           */
          ,
          set: function set(pageSize) {
            this.log.debug("".concat(LOG_PREFIX, " Setting page size to ").concat(JSON.stringify(pageSize)));

            this._set({
              pageSize: pageSize
            });
          }
          /**
           * Gets the currently entered search term
           */

        }, {
          key: "searchTerm",
          get: function get() {
            this.log.debug("".concat(LOG_PREFIX, " Getting search term detail"));
            this.log.debug("".concat(LOG_PREFIX, " Current search term detail = ").concat(JSON.stringify(this._state.searchTerm)));
            return this._state.searchTerm;
          }
          /**
           * Updates the search term detail and then triggers data transformation
           */
          ,
          set: function set(searchTerm) {
            this.log.debug("".concat(LOG_PREFIX, " Setting search term to ").concat(JSON.stringify(searchTerm)));

            this._set({
              searchTerm: searchTerm
            });
          }
          /**
           * Updates the sort column detail and then triggers data transformation
           */

        }, {
          key: "sortColumn",
          set: function set(sortColumn) {
            this.log.debug("".concat(LOG_PREFIX, " Setting sort column to ").concat(JSON.stringify(sortColumn)));

            this._set({
              sortColumn: sortColumn
            });
          }
          /**
           * Updates the sort direction detail and then triggers data transformation
           */

        }, {
          key: "sortDirection",
          set: function set(sortDirection) {
            this.log.debug("".concat(LOG_PREFIX, " Setting sort direction to ").concat(JSON.stringify(sortDirection)));

            this._set({
              sortDirection: sortDirection
            });
          }
        }]);

        return UnitCategoriesRecordsTabulationService;
      }();

      UnitCategoriesRecordsTabulationService.ctorParameters = function () {
        return [{
          type: _unit_categories_data_service__WEBPACK_IMPORTED_MODULE_2__["UnitCategoriesDataService"]
        }, {
          type: ngx_logger__WEBPACK_IMPORTED_MODULE_3__["NGXLogger"]
        }];
      };

      UnitCategoriesRecordsTabulationService = Object(tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"])([Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["Injectable"])({
        providedIn: 'root'
      }), Object(tslib__WEBPACK_IMPORTED_MODULE_0__["__metadata"])("design:paramtypes", [_unit_categories_data_service__WEBPACK_IMPORTED_MODULE_2__["UnitCategoriesDataService"], ngx_logger__WEBPACK_IMPORTED_MODULE_3__["NGXLogger"]])], UnitCategoriesRecordsTabulationService);
      /***/
    }
  }]);
})();
//# sourceMappingURL=default~modules-unit-categories-unit-categories-routing-module~modules-units-units-routing-module-es5.js.map
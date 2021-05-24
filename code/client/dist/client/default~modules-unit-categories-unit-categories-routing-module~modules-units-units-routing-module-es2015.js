(window["webpackJsonp"] = window["webpackJsonp"] || []).push([["default~modules-unit-categories-unit-categories-routing-module~modules-units-units-routing-module"],{

/***/ "3E5Y":
/*!******************************************************************************!*\
  !*** ./src/modules/unit-categories/services/unit-categories-data.service.ts ***!
  \******************************************************************************/
/*! exports provided: UnitCategoriesDataService */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "UnitCategoriesDataService", function() { return UnitCategoriesDataService; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "mrSG");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "8Y7J");
/* harmony import */ var rxjs__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! rxjs */ "qCKp");
/* harmony import */ var _angular_common_http__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! @angular/common/http */ "IheW");
/* harmony import */ var ngx_logger__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! ngx-logger */ "P4hB");
/* harmony import */ var environments_environment__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__(/*! environments/environment */ "AytR");
/* harmony import */ var _common_models_message_type_model__WEBPACK_IMPORTED_MODULE_6__ = __webpack_require__(/*! @common/models/message.type.model */ "nB5A");
/* harmony import */ var _common_services__WEBPACK_IMPORTED_MODULE_7__ = __webpack_require__(/*! @common/services */ "gNCb");
/* harmony import */ var rxjs_operators__WEBPACK_IMPORTED_MODULE_8__ = __webpack_require__(/*! rxjs/operators */ "kU1M");









const LOG_PREFIX = "[Unit Categories Data Service]";
const API_PREFIX = "api/v1/unit_categories";
const HEADERS = { 'Content-Type': 'application/json' };
let UnitCategoriesDataService = class UnitCategoriesDataService {
    constructor(http, connectivityStatusService, messageService, zone, log) {
        this.http = http;
        this.connectivityStatusService = connectivityStatusService;
        this.messageService = messageService;
        this.zone = zone;
        this.log = log;
        // The base url of the server
        this._baseUrl = environments_environment__WEBPACK_IMPORTED_MODULE_5__["environment"].baseUrl;
        // The local data cache
        this._cache = { unitCategories: [] };
        // The observables that allow subscribers to keep tabs of the current status 
        // of unit categories records in the data store
        this._unitCategoriesSubject$ = new rxjs__WEBPACK_IMPORTED_MODULE_2__["BehaviorSubject"]([]);
        this.unitCategories$ = this._unitCategoriesSubject$.asObservable();
        // The observable that we will use to opt out of initialization subscriptions 
        // once we are done with them
        this._done$ = new rxjs__WEBPACK_IMPORTED_MODULE_2__["Subject"]();
        // The api that we'll use to communicate data store changes when components that 
        // subscribe to this service are outside the current ngzone
        this.bc = new BroadcastChannel("unit-categories-data-channel");
        /**
         * Publish information to current (listening) ui
         * @param event
         */
        this.handleEvent = (event) => {
            this.zone.run(() => this._unitCategoriesSubject$.next(event.data.newValue));
        };
        // Subscribe to connectivity status notifications
        this.log.trace(`${LOG_PREFIX} Subscribing to connectivity status notifications`);
        this.connectivityStatusService.online$
            .pipe(Object(rxjs_operators__WEBPACK_IMPORTED_MODULE_8__["takeUntil"])(this._done$))
            .subscribe(online => {
            // Check if the user is online
            this.log.trace(`${LOG_PREFIX} Checking if the user is online`);
            this.log.debug(`${LOG_PREFIX} User is online = ${online}`);
            if (online) {
                // Initialize data
                this.log.trace(`${LOG_PREFIX} Initializing data`);
                this.getAllUnitCategories()
                    .pipe(Object(rxjs_operators__WEBPACK_IMPORTED_MODULE_8__["first"])()) // This will automatically complete (and therefore unsubscribe) after the first value has been emitted.
                    .subscribe((response => {
                    // Data initialization complete
                    this.log.trace(`${LOG_PREFIX} Data initialization complete`);
                }));
                // Unsubscribe from connectivity status notifications
                this.log.trace(`${LOG_PREFIX} Unsubscribing from connectivity status notifications`);
                this._done$.next();
                this._done$.complete();
            }
        });
        //Note: "bc.onmessage" isn't invoked on sender ui
        this.bc.onmessage = this.zone.run(() => this.handleEvent);
    }
    /**
     * Creates and adds an instance of a new Unit Category record to the local cache and then broadcasts the changes to all subscribers
     *
     * @param unitCategory The details of the Unit Category record to be created - with the id and version details missing
     */
    createUnitCategory(unitCategory) {
        this.log.trace(`${LOG_PREFIX} Entering createUnitCategory()`);
        this.log.debug(`${LOG_PREFIX} UnitCategory = ${JSON.stringify(unitCategory)}`);
        // Make a HTTP POST Request to create the record
        this.log.debug(`${LOG_PREFIX} Making a HTTP POST Request to ${this._baseUrl}/${API_PREFIX} to create the record`);
        return this.http.post(`${this._baseUrl}/${API_PREFIX}`, JSON.stringify(unitCategory), { headers: new _angular_common_http__WEBPACK_IMPORTED_MODULE_3__["HttpHeaders"](HEADERS) })
            .pipe(Object(rxjs_operators__WEBPACK_IMPORTED_MODULE_8__["tap"])((data) => {
            // Unit Category record Creation was successful
            this.log.trace(`${LOG_PREFIX} Record Creation was successful`);
            this.log.debug(`${LOG_PREFIX} Created Unit Category record = ${JSON.stringify(data)}`);
            // Add the newly created Unit Category record to the Local Cache
            this.log.trace(`${LOG_PREFIX} Adding the newly created Unit Category record to the Local Cache`);
            this._cache.unitCategories.push(data);
            // Create an up to date copy of the Unit Categories records
            this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Unit Categories records`);
            const copy = Object.assign({}, this._cache).unitCategories;
            // Broadcast the up to date copy of the Unit Categories records to the current listener
            this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Unit Categories records to the current listener`);
            this._unitCategoriesSubject$.next(copy);
            // Broadcast the up to date copy of the Unit Categories records to the other listeners
            this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Unit Categories records to the other listeners`);
            this.bc.postMessage({ newValue: copy });
            // Send a message that states that the Unit Category record Creation was successful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Unit Category record Creation was successful`);
            this.messageService.sendMessage({ "type": _common_models_message_type_model__WEBPACK_IMPORTED_MODULE_6__["MessageType"].Success, "message": "The Unit Category record Creation was successful" });
        }), Object(rxjs_operators__WEBPACK_IMPORTED_MODULE_8__["catchError"])((error) => {
            // Unit Category record Creation was unsuccessful
            this.log.error(`${LOG_PREFIX} Unit Category record Creation was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);
            // Send a message that states that the Unit Category record Creation was unsuccessful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Unit Category record Creation was unsuccessful`);
            this.messageService.sendMessage({ "type": _common_models_message_type_model__WEBPACK_IMPORTED_MODULE_6__["MessageType"].Error, "message": "The Unit Category record Creation was unsuccessful" });
            return Object(rxjs__WEBPACK_IMPORTED_MODULE_2__["throwError"])(error);
        }));
    }
    /**
     * Retrieves and adds a single Unit Category record to the local cache and then broadcasts the changes to all subscribers
     *
     * @param unitCategoryId The Unique Identifier of the Unit Category record
     */
    getUnitCategory(unitCategoryId) {
        this.log.trace(`${LOG_PREFIX} Entering getUnitCategory()`);
        this.log.debug(`${LOG_PREFIX} UnitCategory Id = ${unitCategoryId}`);
        // Make a HTTP GET Request to retrieve the record
        this.log.debug(`${LOG_PREFIX} Making a HTTP GET Request to ${this._baseUrl}/${API_PREFIX}/ids/${unitCategoryId} to retrieve the record`);
        return this.http.get(`${this._baseUrl}/${API_PREFIX}/ids/${unitCategoryId}`, { headers: new _angular_common_http__WEBPACK_IMPORTED_MODULE_3__["HttpHeaders"](HEADERS) })
            .pipe(Object(rxjs_operators__WEBPACK_IMPORTED_MODULE_8__["tap"])((data) => {
            // Unit Category record Retrieval was successful
            this.log.trace(`${LOG_PREFIX} Unit Category record Retrieval was successful`);
            this.log.debug(`${LOG_PREFIX} Retrieved Unit Category record = ${JSON.stringify(data)}`);
            // Search for the Unit Category record in the Local Cache and return its index
            this.log.trace(`${LOG_PREFIX} Searching for the Unit Category record in the Local Cache and returning its index`);
            let index = this._cache.unitCategories.findIndex(d => d.id === data.id);
            this.log.debug(`${LOG_PREFIX} Unit Category record Index = ${index}`);
            // If the record was found (index != -1), update it, else, add it to the Local Storage
            if (index != -1) {
                // The Unit Category record was found in the Local Cache
                this.log.trace(`${LOG_PREFIX} The Unit Category record was found in the Local Cache`);
                // Update the local Unit Category record
                this.log.trace(`${LOG_PREFIX} Updating the local Unit Category record`);
                this._cache.unitCategories[index] = data;
            }
            else {
                // The Unit Category record was not found in the Local Cache
                this.log.trace(`${LOG_PREFIX} The Unit Category record was not found in the Local Cache`);
                // Add the Unit Category record to the Local Cache
                this.log.trace(`${LOG_PREFIX} Adding the Unit Category record to the Local Cache`);
                this._cache.unitCategories.push(data);
            }
            // Create an up to date copy of the Unit Categories records
            this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Unit Categories records`);
            const copy = Object.assign({}, this._cache).unitCategories;
            // Broadcast the up to date copy of the Unit Categories records to the current listener
            this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Unit Categories records to the current listener`);
            this._unitCategoriesSubject$.next(copy);
            // Broadcast the up to date copy of the Unit Categories records to the other listeners
            this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Unit Categories records to the other listeners`);
            this.bc.postMessage({ newValue: copy });
            // Send a message that states that the Unit Category record Retrieval was successful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Unit Category record Retrieval was successful`);
            this.messageService.sendMessage({ "type": _common_models_message_type_model__WEBPACK_IMPORTED_MODULE_6__["MessageType"].Success, "message": "The Unit Category record Retrieval was successful" });
        }), Object(rxjs_operators__WEBPACK_IMPORTED_MODULE_8__["catchError"])((error) => {
            // Unit Category record Retrieval was unsuccessful
            this.log.error(`${LOG_PREFIX} Unit Category record Retrieval was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);
            // Send a message that states that the Unit Category record Retrieval was unsuccessful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Unit Category record Retrieval was unsuccessful`);
            this.messageService.sendMessage({ "type": _common_models_message_type_model__WEBPACK_IMPORTED_MODULE_6__["MessageType"].Error, "message": "The Unit Category record Retrieval was unsuccessful" });
            return Object(rxjs__WEBPACK_IMPORTED_MODULE_2__["throwError"])(error);
        }));
    }
    /**
     * Retrieves and adds all or a subset of all Unit Categories records to the local cache and then broadcasts the changes to all subscribers
     *
     * @param filters Optional query parameters used in filtering the retrieved records
     */
    getAllUnitCategories(filters) {
        this.log.trace(`${LOG_PREFIX} Entering getAllUnitCategories()`);
        this.log.debug(`${LOG_PREFIX} Filters = ${JSON.stringify(filters)}`);
        // Make a HTTP GET Request to retrieve the records
        this.log.debug(`${LOG_PREFIX} Making a HTTP GET Request to ${this._baseUrl}/${API_PREFIX}/all to retrieve the records`);
        return this.http.get(`${this._baseUrl}/${API_PREFIX}/all`, { headers: new _angular_common_http__WEBPACK_IMPORTED_MODULE_3__["HttpHeaders"](HEADERS), params: filters == null ? {} : filters })
            .pipe(Object(rxjs_operators__WEBPACK_IMPORTED_MODULE_8__["tap"])((data) => {
            // Unit Categories records Retrieval was successful
            this.log.trace(`${LOG_PREFIX} Unit Categories records Retrieval was successful`);
            this.log.debug(`${LOG_PREFIX} Retrieved Unit Categories records = ${JSON.stringify(data)}`);
            // Update the Unit Categories records in the Local Cache to the newly pulled Unit Categories records
            this.log.trace(`${LOG_PREFIX} Updating the Unit Categories records in the Local Cache to the newly pulled Unit Categories records`);
            this._cache.unitCategories = data;
            // Create an up to date copy of the Unit Categories records
            this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Unit Categories records`);
            const copy = Object.assign({}, this._cache).unitCategories;
            // Broadcast the up to date copy of the Unit Categories records to the current listener
            this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Unit Categories records to the current listener`);
            this._unitCategoriesSubject$.next(copy);
            // Broadcast the up to date copy of the Unit Categories records to the other listeners
            this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Unit Categories records to the other listeners`);
            this.bc.postMessage({ newValue: copy });
            // Send a message that states that the Unit Categories records Retrieval was successful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Unit Categories records Retrieval was successful`);
            this.messageService.sendMessage({ "type": _common_models_message_type_model__WEBPACK_IMPORTED_MODULE_6__["MessageType"].Success, "message": "The Unit Categories records Retrieval was successful" });
        }), Object(rxjs_operators__WEBPACK_IMPORTED_MODULE_8__["catchError"])((error) => {
            // Unit Categories records Retrieval was unsuccessful
            this.log.error(`${LOG_PREFIX} Unit Categories records Retrieval was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);
            // Send a message that states that the Unit Categories records Retrieval was unsuccessful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Unit Categories records Retrieval was unsuccessful`);
            this.messageService.sendMessage({ "type": _common_models_message_type_model__WEBPACK_IMPORTED_MODULE_6__["MessageType"].Error, "message": "The Unit Categories records Retrieval was unsuccessful" });
            return Object(rxjs__WEBPACK_IMPORTED_MODULE_2__["throwError"])(error);
        }));
    }
    /**
     * Updates a single Unit Category record and its corresponding counterpart in the local cache and then broadcasts the changes to all subscribers
     *
     * @param unitCategory The details of the Unit Category record to be updated
     */
    updateUnitCategory(unitCategory) {
        this.log.trace(`${LOG_PREFIX} Entering updateUnitCategory()`);
        this.log.debug(`${LOG_PREFIX} UnitCategory = ${JSON.stringify(unitCategory)}`);
        // Make a HTTP POST Request to retrieve the records
        this.log.debug(`${LOG_PREFIX} Making a HTTP POST Request to ${this._baseUrl}/${API_PREFIX} to update the record`);
        return this.http.put(`${this._baseUrl}/${API_PREFIX}`, JSON.stringify(unitCategory), { headers: new _angular_common_http__WEBPACK_IMPORTED_MODULE_3__["HttpHeaders"](HEADERS) })
            .pipe(Object(rxjs_operators__WEBPACK_IMPORTED_MODULE_8__["tap"])((data) => {
            // Unit Category record Update was successful
            this.log.trace(`${LOG_PREFIX} Unit Category record Update was successful`);
            this.log.debug(`${LOG_PREFIX} Updated Unit Category record = ${JSON.stringify(data)}`);
            // Search for the locally stored Unit Category record
            this.log.trace(`${LOG_PREFIX} Searching for the locally stored Unit Category record`);
            let index = this._cache.unitCategories.findIndex(d => d.id === data.id);
            this.log.debug(`${LOG_PREFIX} Updated Unit Category record Index = ${index}`);
            // If the record was found (index != -1), update it in the Local Cache
            if (index != -1) {
                // Update the local Unit Category record
                this.log.trace(`${LOG_PREFIX} Updating the locally stored Unit Category record`);
                this._cache.unitCategories[index] = data;
                // Create an up to date copy of the Unit Categories records
                this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Unit Categories records`);
                const copy = Object.assign({}, this._cache).unitCategories;
                // Broadcast the up to date copy of the Unit Categories records to the current listener
                this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Unit Categories records to the current listener`);
                this._unitCategoriesSubject$.next(copy);
                // Broadcast the up to date copy of the Unit Categories records to the other listeners
                this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Unit Categories records to the other listeners`);
                this.bc.postMessage({ newValue: copy });
                // Send a message that states that the Unit Category record Update was successful
                this.log.trace(`${LOG_PREFIX} Sending a message that states that the Unit Category record Update was successful`);
                this.messageService.sendMessage({ "type": _common_models_message_type_model__WEBPACK_IMPORTED_MODULE_6__["MessageType"].Success, "message": "The Unit Category record Update was successful" });
            }
            else {
                // Local Cache Update was unsuccessful
                this.log.error(`${LOG_PREFIX} Local Cache Update was unsuccessful: Unit Category record is missing in the Local Cache`);
                // Send a message that states that the Local Cache Update was unsuccessful
                this.log.trace(`${LOG_PREFIX} Sending a message that states that the Local Cache Update was unsuccessful`);
                this.messageService.sendMessage({ "type": _common_models_message_type_model__WEBPACK_IMPORTED_MODULE_6__["MessageType"].Error, "message": "Unit Categories records Local Cache Update was unsuccessful" });
            }
        }), Object(rxjs_operators__WEBPACK_IMPORTED_MODULE_8__["catchError"])((error) => {
            // Unit Category record Update was unsuccessful
            this.log.error(`${LOG_PREFIX} Unit Category record Update was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);
            // Send a message that states that the Unit Category record Update was unsuccessful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Unit Category record Update was unsuccessful`);
            this.messageService.sendMessage({ "type": _common_models_message_type_model__WEBPACK_IMPORTED_MODULE_6__["MessageType"].Error, "message": "The Unit Category record Update was unsuccessful" });
            return Object(rxjs__WEBPACK_IMPORTED_MODULE_2__["throwError"])(error);
        }));
    }
    /**
     * Deletes a single Unit Category record and its corresponding counterpart in the local cache and then broadcasts the changes to all subscribers
     *
     * @param unitCategoryId The Unique Identifier of the record
     * @returns The total count of deleted records - which should be 1 in this case if the delete operation was successful
     */
    deleteUnitCategory(unitCategoryId) {
        this.log.trace(`${LOG_PREFIX} Entering deleteUnitCategory()`);
        this.log.debug(`${LOG_PREFIX} UnitCategory Id = ${unitCategoryId}`);
        // Make a HTTP DELETE Request to retrieve the records
        this.log.debug(`${LOG_PREFIX} Making a HTTP DELETE Request to ${this._baseUrl}/${API_PREFIX}/ids/${unitCategoryId} to delete the record`);
        return this.http.delete(`${this._baseUrl}/${API_PREFIX}/ids/${unitCategoryId}`, { headers: new _angular_common_http__WEBPACK_IMPORTED_MODULE_3__["HttpHeaders"](HEADERS) })
            .pipe(Object(rxjs_operators__WEBPACK_IMPORTED_MODULE_8__["tap"])((count) => {
            // Mark the deletion successful if and only if exactly 1 record was deleted
            if (count == 1) {
                // Unit Category record Deletion was successful
                this.log.trace(`${LOG_PREFIX} Unit Category record Deletion was successful`);
                // Search for the deleted Unit Category record in the Local Cache
                this.log.trace(`${LOG_PREFIX} Searching for the deleted Unit Category record in the Local Cache`);
                let index = this._cache.unitCategories.findIndex(d => d.id == unitCategoryId);
                this.log.debug(`${LOG_PREFIX} Deleted Unit Category record Index = ${index}`);
                // If the record was found (index != -1), remove it from the Local Cache
                if (index != -1) {
                    // Remove the deleted Unit Category record from the Local Cache
                    this.log.trace(`${LOG_PREFIX} Removing the deleted Unit Category record from the Local Cache`);
                    this._cache.unitCategories.splice(index, 1);
                    // Create an up to date copy of the Unit Categories records
                    this.log.trace(`${LOG_PREFIX} Creating an up to date copy of the Unit Categories records`);
                    const copy = Object.assign({}, this._cache).unitCategories;
                    // Broadcast the up to date copy of the Unit Categories records to the current listener
                    this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Unit Categories records to the current listener`);
                    this._unitCategoriesSubject$.next(copy);
                    // Broadcast the up to date copy of the Unit Categories records to the other listeners
                    this.log.trace(`${LOG_PREFIX} Broadcasting the up to date copy of the Unit Categories records to the other listeners`);
                    this.bc.postMessage({ newValue: copy });
                    // Send a message that states that the Unit Category record Deletion was successful
                    this.log.trace(`${LOG_PREFIX} Sending a message that states that the Unit Category record Deletion was successful`);
                    this.messageService.sendMessage({ "type": _common_models_message_type_model__WEBPACK_IMPORTED_MODULE_6__["MessageType"].Success, "message": "The Unit Category record Deletion was successful" });
                }
                else {
                    // Local Cache Update was unsuccessful
                    this.log.error(`${LOG_PREFIX} Local Cache Update was unsuccessful: Unit Category record is missing in the Local Cache`);
                    // Send a message that states that the Local Cache Update was unsuccessful
                    this.log.trace(`${LOG_PREFIX} Sending a message that states that the Local Cache Update was unsuccessful`);
                    this.messageService.sendMessage({ "type": _common_models_message_type_model__WEBPACK_IMPORTED_MODULE_6__["MessageType"].Error, "message": "Unit Categories records Local Cache Update was unsuccessful" });
                }
            }
            else {
                // Unit Category record Deletion was unsuccessful
                this.log.error(`${LOG_PREFIX} Unit Category record Deletion was unsuccessful: Expecting 1 record to be deleted instead of ${count}`);
                // Send a message that states that the Unit Category record Deletion was unsuccessful
                this.log.trace(`${LOG_PREFIX} Sending a message that states that the Unit Category record Deletion was unsuccessful`);
                this.messageService.sendMessage({ "type": _common_models_message_type_model__WEBPACK_IMPORTED_MODULE_6__["MessageType"].Error, "message": "The Unit Category record Deletion was unsuccessful" });
            }
        }), Object(rxjs_operators__WEBPACK_IMPORTED_MODULE_8__["catchError"])((error) => {
            // Unit Category record Deletion was unsuccessful
            this.log.error(`${LOG_PREFIX} Unit Category record Deletion was unsuccessful: ${error.statusText || "See Server Logs for more details"}`);
            // Send a message that states that the Unit Category record Deletion was unsuccessful
            this.log.trace(`${LOG_PREFIX} Sending a message that states that the Unit Category record Deletion was unsuccessful`);
            this.messageService.sendMessage({ "type": _common_models_message_type_model__WEBPACK_IMPORTED_MODULE_6__["MessageType"].Error, "message": "The Unit Category record Deletion was unsuccessful" });
            return Object(rxjs__WEBPACK_IMPORTED_MODULE_2__["throwError"])(error);
        }));
    }
    /**
     * Use BehaviorSubject's getter property named value to get the most recent value passed through it.
     */
    get records() {
        return this._unitCategoriesSubject$.value;
    }
};
UnitCategoriesDataService.ctorParameters = () => [
    { type: _angular_common_http__WEBPACK_IMPORTED_MODULE_3__["HttpClient"] },
    { type: _common_services__WEBPACK_IMPORTED_MODULE_7__["ConnectivityStatusService"] },
    { type: _common_services__WEBPACK_IMPORTED_MODULE_7__["MessageService"] },
    { type: _angular_core__WEBPACK_IMPORTED_MODULE_1__["NgZone"] },
    { type: ngx_logger__WEBPACK_IMPORTED_MODULE_4__["NGXLogger"] }
];
UnitCategoriesDataService = Object(tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"])([
    Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["Injectable"])({
        providedIn: 'root'
    }),
    Object(tslib__WEBPACK_IMPORTED_MODULE_0__["__metadata"])("design:paramtypes", [_angular_common_http__WEBPACK_IMPORTED_MODULE_3__["HttpClient"],
        _common_services__WEBPACK_IMPORTED_MODULE_7__["ConnectivityStatusService"],
        _common_services__WEBPACK_IMPORTED_MODULE_7__["MessageService"],
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["NgZone"],
        ngx_logger__WEBPACK_IMPORTED_MODULE_4__["NGXLogger"]])
], UnitCategoriesDataService);



/***/ }),

/***/ "CUCj":
/*!*******************************************************!*\
  !*** ./src/modules/unit-categories/services/index.ts ***!
  \*******************************************************/
/*! exports provided: services, UnitCategoriesDataService, UnitCategoriesRecordsTabulationService */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "services", function() { return services; });
/* harmony import */ var _unit_categories_data_service__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./unit-categories-data.service */ "3E5Y");
/* harmony import */ var _unit_categories_records_tabulation_service__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ./unit-categories-records-tabulation.service */ "tqN6");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "UnitCategoriesDataService", function() { return _unit_categories_data_service__WEBPACK_IMPORTED_MODULE_0__["UnitCategoriesDataService"]; });

/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "UnitCategoriesRecordsTabulationService", function() { return _unit_categories_records_tabulation_service__WEBPACK_IMPORTED_MODULE_1__["UnitCategoriesRecordsTabulationService"]; });



const services = [_unit_categories_data_service__WEBPACK_IMPORTED_MODULE_0__["UnitCategoriesDataService"], _unit_categories_records_tabulation_service__WEBPACK_IMPORTED_MODULE_1__["UnitCategoriesRecordsTabulationService"]];




/***/ }),

/***/ "nB5A":
/*!*************************************************************!*\
  !*** ./src/modules/app-common/models/message.type.model.ts ***!
  \*************************************************************/
/*! exports provided: MessageType */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "MessageType", function() { return MessageType; });
var MessageType;
(function (MessageType) {
    MessageType[MessageType["Success"] = 0] = "Success";
    MessageType[MessageType["Error"] = 1] = "Error";
    MessageType[MessageType["Info"] = 2] = "Info";
    MessageType[MessageType["Warning"] = 3] = "Warning";
})(MessageType || (MessageType = {}));


/***/ }),

/***/ "tqN6":
/*!********************************************************************************************!*\
  !*** ./src/modules/unit-categories/services/unit-categories-records-tabulation.service.ts ***!
  \********************************************************************************************/
/*! exports provided: UnitCategoriesRecordsTabulationService */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "UnitCategoriesRecordsTabulationService", function() { return UnitCategoriesRecordsTabulationService; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "mrSG");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "8Y7J");
/* harmony import */ var _unit_categories_data_service__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ./unit-categories-data.service */ "3E5Y");
/* harmony import */ var ngx_logger__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ngx-logger */ "P4hB");
/* harmony import */ var rxjs__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! rxjs */ "qCKp");





const LOG_PREFIX = "[Unit Categories Records Tabulation Service]";
let UnitCategoriesRecordsTabulationService = class UnitCategoriesRecordsTabulationService {
    constructor(unitCategoriesDataService, log) {
        this.unitCategoriesDataService = unitCategoriesDataService;
        this.log = log;
        // The observables that will be updated / broadcasted whenever 
        // a background task is started and completed  
        this._loadingSubject$ = new rxjs__WEBPACK_IMPORTED_MODULE_4__["BehaviorSubject"](true);
        this._loading$ = this._loadingSubject$.asObservable();
        // The first set of observables that will be updated / broadcasted whenever 
        // Unit Categories records are transformed as per the user defined search 
        // or sort criteria    
        this._unitCategoriesSubject$ = new rxjs__WEBPACK_IMPORTED_MODULE_4__["BehaviorSubject"]([]);
        this._unitCategories$ = this._unitCategoriesSubject$.asObservable();
        // The second set of observables that will be updated / broadcasted whenever 
        // Unit Categories records are transformed as per the user defined search 
        // or sort criteria
        this._totalSubject$ = new rxjs__WEBPACK_IMPORTED_MODULE_4__["BehaviorSubject"](0);
        this._total$ = this._totalSubject$.asObservable();
        // The user defined search or sort criteria.
        // Determines which & how many Unit Categories records should be displayed
        this._state = { page: 1, pageSize: 4, searchTerm: '', sortColumn: '', sortDirection: '' };
        // A common gathering point for all the component's subscriptions.
        // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
        this._subscriptions = [];
        this._subscriptions.push(this.unitCategoriesDataService.unitCategories$
            .subscribe((unitCategories) => {
            this._transform(unitCategories);
        }));
    }
    ngOnDestroy() {
        this._subscriptions.forEach((s) => s.unsubscribe());
    }
    /**
     * Returns an observable containing Unit Categories records that have been filtered as per the user defined criteria
     */
    get unitCategories$() {
        this.log.trace(`${LOG_PREFIX} Getting unitCategories$ observable`);
        this.log.debug(`${LOG_PREFIX} Current unitCategories$ observable value = ${JSON.stringify(this._unitCategoriesSubject$.value)}`);
        return this._unitCategories$;
    }
    /**
     * Returns an observable containing the total number of Unit Categories records that have been filtered as per the user defined criteria
     */
    get total$() {
        this.log.trace(`${LOG_PREFIX} Getting total$ observable`);
        this.log.debug(`${LOG_PREFIX} Current total$ observable value = ${JSON.stringify(this._totalSubject$.value)}`);
        return this._total$;
    }
    /**
     * Returns an observable containing a boolean flag that indicates whether or not a data operation exercise (sorting, searching etc.) is currently underway
     */
    get loading$() {
        this.log.trace(`${LOG_PREFIX} Getting loading$ observable`);
        this.log.debug(`${LOG_PREFIX} Current loading$ observable value = ${JSON.stringify(this._loadingSubject$.value)}`);
        return this._loading$;
    }
    /**
     * Returns the currently active page
     */
    get page() {
        this.log.trace(`${LOG_PREFIX} Getting page detail`);
        this.log.debug(`${LOG_PREFIX} Current page detail value = ${JSON.stringify(this._state.page)}`);
        return this._state.page;
    }
    /**
     * Updates the currently active page detail and then triggers data transformation
     */
    set page(page) {
        this.log.trace(`${LOG_PREFIX} Setting page detail to ${JSON.stringify(page)}`);
        this._set({ page });
    }
    /**
     * Returns the currently set page size
     */
    get pageSize() {
        this.log.trace(`${LOG_PREFIX} Getting page size detail`);
        this.log.debug(`${LOG_PREFIX} Current page size detail = ${JSON.stringify(this._state.pageSize)}`);
        return this._state.pageSize;
    }
    /**
     * Updates the desired page size detail and then triggers data transformation
     */
    set pageSize(pageSize) {
        this.log.debug(`${LOG_PREFIX} Setting page size to ${JSON.stringify(pageSize)}`);
        this._set({ pageSize });
    }
    /**
     * Gets the currently entered search term
     */
    get searchTerm() {
        this.log.debug(`${LOG_PREFIX} Getting search term detail`);
        this.log.debug(`${LOG_PREFIX} Current search term detail = ${JSON.stringify(this._state.searchTerm)}`);
        return this._state.searchTerm;
    }
    /**
     * Updates the search term detail and then triggers data transformation
     */
    set searchTerm(searchTerm) {
        this.log.debug(`${LOG_PREFIX} Setting search term to ${JSON.stringify(searchTerm)}`);
        this._set({ searchTerm });
    }
    /**
     * Updates the sort column detail and then triggers data transformation
     */
    set sortColumn(sortColumn) {
        this.log.debug(`${LOG_PREFIX} Setting sort column to ${JSON.stringify(sortColumn)}`);
        this._set({ sortColumn });
    }
    /**
     * Updates the sort direction detail and then triggers data transformation
     */
    set sortDirection(sortDirection) {
        this.log.debug(`${LOG_PREFIX} Setting sort direction to ${JSON.stringify(sortDirection)}`);
        this._set({ sortDirection });
    }
    /**
     * Utility method for all the class setters.
     * Does the actual updating of details / transforming of data
     * @param patch the partially updated details
     */
    _set(patch) {
        // Update the state
        Object.assign(this._state, patch);
        // Transform the Unit Categories records
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
    compare(v1, v2) {
        this.log.trace(`${LOG_PREFIX} Comparing two values to find out if the first value preceeds the second`);
        return (v1 == undefined || v1 == null || v2 == undefined || v2 == null) ? 0 : v1 < v2 ? -1 : v1 > v2 ? 1 : 0;
    }
    /**
     * Sorts Unit Categories Records
     *
     * @param unitCategories The Unit Categories records to sort
     * @param column The table column to sort the records by
     * @param direction The desired sort direction - ascending or descending
     * @returns The sorted Unit Categories records
     */
    sort(unitCategories, column, direction) {
        this.log.trace(`${LOG_PREFIX} Sorting Unit Categories records`);
        if (direction === '' || column == null) {
            return unitCategories;
        }
        else {
            return [...unitCategories].sort((a, b) => {
                const res = this.compare(a[column], b[column]);
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
    matches(unitCategory, term) {
        this.log.trace(`${LOG_PREFIX} Checking if search string is present in Unit Category record`);
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
    paginate(unitCategories, page, pageSize) {
        this.log.trace(`${LOG_PREFIX} Paginating Unit Categories records`);
        return unitCategories.slice((page - 1) * pageSize, (page - 1) * pageSize + pageSize);
    }
    /**
     * Updates the index of the Unit Categories Records
     *
     * @param unitCategories The Unit Categories records to sort
     * @returns The newly indexed Unit Categories records
     */
    index(unitCategories) {
        this.log.trace(`${LOG_PREFIX} Indexing Unit Categories records`);
        let pos = 0;
        return unitCategories.map(d => {
            d.pos = ++pos;
            return d;
        });
    }
    /**
     * Sorts, filters and paginates Unit Categories records
     *
     * @param records the original Unit Categories records
     */
    _transform(records) {
        // Flag
        this._loadingSubject$.next(true);
        if (records.length != 0) {
            this.log.trace(`${LOG_PREFIX} Sorting, filtering and paginating Unit Categories records`);
            const { sortColumn, sortDirection, pageSize, page, searchTerm } = this._state;
            // Sort
            let transformed = this.sort(records, sortColumn, sortDirection);
            // Filter
            transformed = transformed.filter(unitCategory => this.matches(unitCategory, searchTerm));
            const total = transformed.length;
            // Index
            transformed = this.index(transformed);
            // Paginate
            transformed = this.paginate(transformed, page, pageSize);
            // Broadcast
            this._unitCategoriesSubject$.next(transformed);
            this._totalSubject$.next(total);
        }
        else {
            // Broadcast
            this._unitCategoriesSubject$.next([]);
            this._totalSubject$.next(0);
        }
        // Flag
        this._loadingSubject$.next(false);
    }
};
UnitCategoriesRecordsTabulationService.ctorParameters = () => [
    { type: _unit_categories_data_service__WEBPACK_IMPORTED_MODULE_2__["UnitCategoriesDataService"] },
    { type: ngx_logger__WEBPACK_IMPORTED_MODULE_3__["NGXLogger"] }
];
UnitCategoriesRecordsTabulationService = Object(tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"])([
    Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["Injectable"])({ providedIn: 'root' }),
    Object(tslib__WEBPACK_IMPORTED_MODULE_0__["__metadata"])("design:paramtypes", [_unit_categories_data_service__WEBPACK_IMPORTED_MODULE_2__["UnitCategoriesDataService"],
        ngx_logger__WEBPACK_IMPORTED_MODULE_3__["NGXLogger"]])
], UnitCategoriesRecordsTabulationService);



/***/ })

}]);
//# sourceMappingURL=default~modules-unit-categories-unit-categories-routing-module~modules-units-units-routing-module-es2015.js.map
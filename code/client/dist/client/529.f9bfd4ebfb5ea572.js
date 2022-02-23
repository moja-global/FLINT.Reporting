"use strict";(self.webpackChunkclient=self.webpackChunkclient||[]).push([[529],{4575:(v,f,a)=>{a.d(f,{k:()=>$});class ${constructor(d){Object.assign(this,d)}}},1771:(v,f,a)=>{a.d(f,{LR:()=>$.L,QV:()=>h,uZ:()=>S});var $=a(1710),R=a(4499),d=a(2383),w=a(1585);const r="[Reporting Frameworks Records Tabulation Service]";let h=(()=>{class p{constructor(t,o){this.reportingFrameworksDataService=t,this.log=o,this._state={page:1,pageSize:4,searchTerm:"",sortColumn:"",sortDirection:""},this._loadingSubject$=new R.X(!1),this._loading$=this._loadingSubject$.asObservable(),this._reportingFrameworksSubject$=new R.X([]),this._reportingFrameworks$=this._reportingFrameworksSubject$.asObservable(),this._totalSubject$=new R.X(0),this._total$=this._totalSubject$.asObservable(),this._subscriptions=[],this._subscriptions.push(this.reportingFrameworksDataService.reportingFrameworks$.subscribe(i=>{this.transform(i)}))}ngOnDestroy(){this.log.trace(`${r} Destroying Service`),this._subscriptions.forEach(t=>t.unsubscribe())}get reportingFrameworks$(){return this.log.trace(`${r} Getting reportingFrameworks$ observable`),this.log.debug(`${r} Current reportingFrameworks$ observable value = ${JSON.stringify(this._reportingFrameworksSubject$.value)}`),this._reportingFrameworks$}get total$(){return this.log.trace(`${r} Getting total$ observable`),this.log.debug(`${r} Current total$ observable value = ${JSON.stringify(this._totalSubject$.value)}`),this._total$}get loading$(){return this.log.trace(`${r} Getting loading$ observable`),this.log.debug(`${r} Current loading$ observable value = ${JSON.stringify(this._loadingSubject$.value)}`),this._loading$}get page(){return this.log.trace(`${r} Getting page detail`),this.log.debug(`${r} Current page detail value = ${JSON.stringify(this._state.page)}`),this._state.page}set page(t){this.log.trace(`${r} Setting page detail to ${JSON.stringify(t)}`),this.set({page:t})}get pageSize(){return this.log.trace(`${r} Getting page size detail`),this.log.debug(`${r} Current page size detail = ${JSON.stringify(this._state.pageSize)}`),this._state.pageSize}set pageSize(t){this.log.debug(`${r} Setting page size to ${JSON.stringify(t)}`),this.set({pageSize:t})}get searchTerm(){return this.log.debug(`${r} Getting search term detail`),this.log.debug(`${r} Current search term detail = ${JSON.stringify(this._state.searchTerm)}`),this._state.searchTerm}set searchTerm(t){this.log.debug(`${r} Setting search term to ${JSON.stringify(t)}`),this.set({searchTerm:t})}set sortColumn(t){this.log.debug(`${r} Setting sort column to ${JSON.stringify(t)}`),this.set({sortColumn:t})}set sortDirection(t){this.log.debug(`${r} Setting sort direction to ${JSON.stringify(t)}`),this.set({sortDirection:t})}compare(t,o){return this.log.trace(`${r} Comparing two values to find out if the first value preceeds the second`),null==t||null==t||null==o||null==o?0:t<o?-1:t>o?1:0}sort(t,o,i){return this.log.trace(`${r} Sorting Reporting Frameworks Records`),""===i||null==o?t:[...t].sort((F,e)=>{const l=this.compare(F[o],e[o]);return"asc"===i?l:-l})}matches(t,o){return this.log.trace(`${r} Checking if the search string is present in the Reporting Framework record`),!(null==t||null==t||!(null!=t.name&&null!=t.name&&t.name.toLowerCase().includes(o.toLowerCase())||null!=t.description&&null!=t.description&&t.description.toLowerCase().includes(o.toLowerCase())))}index(t){this.log.trace(`${r} Indexing Reporting Frameworks Records`);let o=0;return t.map(i=>(i.pos=++o,i))}paginate(t,o,i){return this.log.trace(`${r} Paginating Reporting Frameworks Records`),t.slice((o-1)*i,(o-1)*i+i)}set(t){Object.assign(this._state,t),this.transform(this.reportingFrameworksDataService.records)}transform(t){if(this._loadingSubject$.next(!0),0!=t.length){this.log.trace(`${r} Sorting, filtering and paginating Reporting Frameworks Records`),this.log.debug(`${r} Reporting Frameworks Records before transformation = ${JSON.stringify(t)}`);const{sortColumn:o,sortDirection:i,pageSize:F,page:e,searchTerm:l}=this._state;let g=this.sort(t,o,i);this.log.debug(`${r} Reporting Frameworks Records after 'Sort' Transformation = ${JSON.stringify(g)}`),g=g.filter(m=>this.matches(m,l));const b=g.length;this.log.debug(`${r} Reporting Frameworks Records after 'Filter by Search Term' Transformation = ${JSON.stringify(g)}`),g=this.index(g),this.log.debug(`${r} Reporting Frameworks Records after 'Index' Transformation = ${JSON.stringify(g)}`),g=this.paginate(g,e,F),this.log.debug(`${r} Reporting Frameworks Records after 'Paginate' Transformation = ${JSON.stringify(g)}`),this._reportingFrameworksSubject$.next(Object.assign([],g)),this._totalSubject$.next(b)}else this._reportingFrameworksSubject$.next([]),this._totalSubject$.next(0);this._loadingSubject$.next(!1)}}return p.\u0275fac=function(t){return new(t||p)(d.LFG($.L),d.LFG(w.Kf))},p.\u0275prov=d.Yz7({token:p,factory:p.\u0275fac,providedIn:"root"}),p})();const S=[$.L,h]},1710:(v,f,a)=>{a.d(f,{L:()=>b});var $=a(4499),R=a(273),d=a(9433),w=a(4522),r=a(8260),h=a(824),S=a(6620),p=a(6263),_=a(6047),t=a(7384),o=a(2835),i=a(2383),F=a(1585);const e="[Reporting Frameworks Data Service]",l="api/v1/reporting_frameworks",g={"Content-Type":"application/json"};let b=(()=>{class m{constructor(n,s,c,u,C){this.http=n,this.connectivityStatusService=s,this.messageService=c,this.zone=u,this.log=C,this._cache={reportingFrameworks:[]},this._reportingFrameworksSubject$=new $.X([]),this.reportingFrameworks$=this._reportingFrameworksSubject$.asObservable(),this._done$=new R.x,this.bc=new BroadcastChannel("reporting-frameworks-data-channel"),this.handleEvent=k=>{this.zone.run(()=>this._reportingFrameworksSubject$.next(k.data.newValue))},this.log.trace(`${e} Subscribing to connectivity status notifications`),this.connectivityStatusService.online$.pipe((0,p.R)(this._done$)).subscribe(k=>{this.log.trace(`${e} Checking if the user is online`),this.log.debug(`${e} User is online = ${k}`),k&&(this.log.trace(`${e} Initializing data`),this.getAllReportingFrameworks().pipe((0,_.P)()).subscribe(O=>{this.log.trace(`${e} Data initialization complete`)}),this.log.trace(`${e} Unsubscribing from connectivity status notifications`),this._done$.next(!0),this._done$.complete())}),this.bc.onmessage=this.zone.run(()=>this.handleEvent)}createReportingFramework(n){return this.log.trace(`${e} Entering createReportingFramework()`),this.log.debug(`${e} Reporting Framework = ${JSON.stringify(n)}`),this.log.debug(`${e} Making a HTTP POST Request to ${r.N.reportingFrameworksBaseUrl}/${l} to create the record`),this.http.post(`${r.N.reportingFrameworksBaseUrl}/${l}`,JSON.stringify(n),{headers:new w.WM(g)}).pipe((0,t.b)(s=>{this.log.trace(`${e} Record Creation was successful`),this.log.debug(`${e} Created Reporting Framework record = ${JSON.stringify(s)}`),this.log.trace(`${e} Adding the newly created Reporting Framework record to the Local Cache`),this._cache.reportingFrameworks.push(s),this.log.trace(`${e} Creating an up to date copy of the Reporting Frameworks Records`);const c=Object.assign({},this._cache).reportingFrameworks;this.log.trace(`${e} Broadcasting the up to date copy of the Reporting Frameworks Records to the current listener`),this._reportingFrameworksSubject$.next(c),this.log.trace(`${e} Broadcasting the up to date copy of the Reporting Frameworks Records to the other listeners`),this.bc.postMessage({newValue:c}),this.log.trace(`${e} Sending a message that states that the Reporting Framework record Creation was successful`),this.messageService.sendMessage({type:h.C.Success,message:"The Reporting Framework record Creation was successful"})}),(0,o.K)(s=>(this.log.error(`${e} Reporting Framework record Creation was unsuccessful: ${s.statusText||"See Server Logs for more details"}`),this.log.trace(`${e} Sending a message that states that the Reporting Framework record Creation was unsuccessful`),this.messageService.sendMessage({type:h.C.Error,message:"The Reporting Framework record Creation was unsuccessful"}),(0,d._)(s))))}getAllReportingFrameworks(n){return this.log.trace(`${e} Entering getAllReportingFrameworks()`),this.log.debug(`${e} Filters = ${JSON.stringify(n)}`),this.log.debug(`${e} Making a HTTP GET Request to ${r.N.reportingFrameworksBaseUrl}/${l}/all to retrieve the records`),this.http.get(`${r.N.reportingFrameworksBaseUrl}/${l}/all`,{headers:new w.WM(g),params:null==n?{}:n}).pipe((0,t.b)(s=>{this.log.trace(`${e} Reporting Frameworks Records Retrieval was successful`),this.log.debug(`${e} Retrieved Reporting Frameworks Records = ${JSON.stringify(s)}`),this.log.trace(`${e} Updating the Reporting Frameworks Records in the Local Cache to the newly pulled Reporting Frameworks Records`),this._cache.reportingFrameworks=s,this.log.trace(`${e} Creating an up to date copy of the Reporting Frameworks Records`);const c=Object.assign({},this._cache).reportingFrameworks;this.log.trace(`${e} Broadcasting the up to date copy of the Reporting Frameworks Records to the current listener`),this._reportingFrameworksSubject$.next(c),this.log.trace(`${e} Broadcasting the up to date copy of the Reporting Frameworks Records to the other listeners`),this.bc.postMessage({newValue:c}),this.log.trace(`${e} Sending a message that states that the Reporting Frameworks Records Retrieval was successful`),this.messageService.sendMessage({type:h.C.Success,message:"The Reporting Frameworks Records Retrieval was successful"})}),(0,o.K)(s=>(this.log.error(`${e} Reporting Frameworks Records Retrieval was unsuccessful: ${s.statusText||"See Server Logs for more details"}`),this.log.trace(`${e} Sending a message that states that the Reporting Frameworks Records Retrieval was unsuccessful`),this.messageService.sendMessage({type:h.C.Error,message:"The Reporting Frameworks Records Retrieval was unsuccessful"}),(0,d._)(s))))}updateReportingFramework(n){return this.log.trace(`${e} Entering updateReportingFramework()`),this.log.debug(`${e} Reporting Framework = ${JSON.stringify(n)}`),this.log.debug(`${e} Making a HTTP POST Request to ${r.N.reportingFrameworksBaseUrl}/${l} to update the record`),this.http.put(`${r.N.reportingFrameworksBaseUrl}/${l}`,JSON.stringify(n),{headers:new w.WM(g)}).pipe((0,t.b)(s=>{this.log.trace(`${e} Reporting Framework record Update was successful`),this.log.debug(`${e} Updated Reporting Framework record = ${JSON.stringify(s)}`),this.log.trace(`${e} Searching for the locally stored Reporting Framework record`);let c=this._cache.reportingFrameworks.findIndex(u=>u.id===s.id);if(this.log.debug(`${e} Updated Reporting Framework record Index = ${c}`),-1!=c){this.log.trace(`${e} Updating the locally stored Reporting Framework record`),this._cache.reportingFrameworks[c]=s,this.log.trace(`${e} Creating an up to date copy of the Reporting Frameworks Records`);const u=Object.assign({},this._cache).reportingFrameworks;this.log.trace(`${e} Broadcasting the up to date copy of the Reporting Frameworks Records to the current listener`),this._reportingFrameworksSubject$.next(u),this.log.trace(`${e} Broadcasting the up to date copy of the Reporting Frameworks Records to the other listeners`),this.bc.postMessage({newValue:u}),this.log.trace(`${e} Sending a message that states that the Reporting Framework record Update was successful`),this.messageService.sendMessage({type:h.C.Success,message:"The Reporting Framework record Update was successful"})}else this.log.error(`${e} Local Cache Update was unsuccessful: Reporting Framework record is missing in the Local Cache`),this.log.trace(`${e} Sending a message that states that the Local Cache Update was unsuccessful`),this.messageService.sendMessage({type:h.C.Error,message:"Reporting Frameworks Records Local Cache Update was unsuccessful"})}),(0,o.K)(s=>(this.log.error(`${e} Reporting Framework record Update was unsuccessful: ${s.statusText||"See Server Logs for more details"}`),this.log.trace(`${e} Sending a message that states that the Reporting Framework record Update was unsuccessful`),this.messageService.sendMessage({type:h.C.Error,message:"The Reporting Framework record Update was unsuccessful"}),(0,d._)(s))))}deleteReportingFramework(n){return this.log.trace(`${e} Entering deleteReportingFramework()`),this.log.debug(`${e} Reporting Framework Id = ${n}`),this.log.debug(`${e} Making a HTTP DELETE Request to ${r.N.reportingFrameworksBaseUrl}/${l}/ids/${n} to delete the record`),this.http.delete(`${r.N.reportingFrameworksBaseUrl}/${l}/ids/${n}`,{headers:new w.WM(g)}).pipe((0,t.b)(s=>{if(1==s){this.log.trace(`${e} Reporting Framework record Deletion was successful`),this.log.trace(`${e} Searching for the deleted Reporting Framework record in the Local Cache`);let c=this._cache.reportingFrameworks.findIndex(u=>u.id==n);if(this.log.debug(`${e} Deleted Reporting Framework record Index = ${c}`),-1!=c){this.log.trace(`${e} Removing the deleted Reporting Framework record from the Local Cache`),this._cache.reportingFrameworks.splice(c,1),this.log.trace(`${e} Creating an up to date copy of the Reporting Frameworks Records`);const u=Object.assign({},this._cache).reportingFrameworks;this.log.trace(`${e} Broadcasting the up to date copy of the Reporting Frameworks Records to the current listener`),this._reportingFrameworksSubject$.next(u),this.log.trace(`${e} Broadcasting the up to date copy of the Reporting Frameworks Records to the other listeners`),this.bc.postMessage({newValue:u}),this.log.trace(`${e} Sending a message that states that the Reporting Framework record Deletion was successful`),this.messageService.sendMessage({type:h.C.Success,message:"The Reporting Framework record Deletion was successful"})}else this.log.error(`${e} Local Cache Update was unsuccessful: Reporting Framework record is missing in the Local Cache`),this.log.trace(`${e} Sending a message that states that the Local Cache Update was unsuccessful`),this.messageService.sendMessage({type:h.C.Error,message:"Reporting Frameworks Records Local Cache Update was unsuccessful"})}else this.log.error(`${e} Reporting Framework record Deletion was unsuccessful: Expecting 1 record to be deleted instead of ${s}`),this.log.trace(`${e} Sending a message that states that the Reporting Framework record Deletion was unsuccessful`),this.messageService.sendMessage({type:h.C.Error,message:"The Reporting Framework record Deletion was unsuccessful"})}),(0,o.K)(s=>(this.log.error(`${e} Reporting Framework record Deletion was unsuccessful: ${s.statusText||"See Server Logs for more details"}`),this.log.trace(`${e} Sending a message that states that the Reporting Framework record Deletion was unsuccessful`),this.messageService.sendMessage({type:h.C.Error,message:"The Reporting Framework record Deletion was unsuccessful"}),(0,d._)(s))))}get records(){return this._reportingFrameworksSubject$.value}}return m.\u0275fac=function(n){return new(n||m)(i.LFG(w.eN),i.LFG(S.qZ),i.LFG(S.ez),i.LFG(i.R0b),i.LFG(F.Kf))},m.\u0275prov=i.Yz7({token:m,factory:m.\u0275fac,providedIn:"root"}),m})()}}]);
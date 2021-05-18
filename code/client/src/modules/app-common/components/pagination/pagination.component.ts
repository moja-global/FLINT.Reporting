import { AfterViewInit, ChangeDetectionStrategy, Component, EventEmitter, Output} from '@angular/core';
import { NGXLogger } from 'ngx-logger';
import { BehaviorSubject} from 'rxjs';

const LOG_PREFIX: string = "[Pagination Component]";

@Component({
  selector: 'sb-pagination',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './pagination.component.html',
  styleUrls: ['pagination.component.scss'],
})
export class PaginationComponent implements AfterViewInit {

  // Instantiate the current page number field
  private _page: number = 1;

  // Instantiate the desired page size field
  private _pageSize: number = 4;

  // Instantiate an observable field of the total records.
  // This field's value will change everytime the user filters the records.
  private _totalSubject$ = new BehaviorSubject<number>(0);
  readonly total$ = this._totalSubject$.asObservable();  

  // Instantiate Page Changes Events Emitter.
  @Output() public pageChanged: EventEmitter<number> = new EventEmitter<number>();  

  // Instantiate Page Size Changes Events Emitter.
  @Output() public pageSizeChanged: EventEmitter<number> = new EventEmitter<number>(); 
  
  constructor(private log: NGXLogger) { }

  ngAfterViewInit() {}

  ngOnDestroy() {}

  /**
   * Initializes the component's page and page size fields
   * @param page The initial page number
   * @param pageSize The initial page size
   */
  public initialize(page: number, pageSize: number): void {

    this.log.debug(`${LOG_PREFIX} Initializing Page to ${page}`);
    this.page = page;

    this.log.debug(`${LOG_PREFIX} Initializing Page Size to ${pageSize}`);
    this.pageSize = pageSize;
  }

  /**
   * Returns the current page number
   */
  public get page(){

    this.log.debug(`${LOG_PREFIX} Returning ${this._page} as Page`);
    return this._page;
  }

  /**
   * Sets and broadcasts the newly set page number
   */
  public set page(page: number){

    this.log.debug(`${LOG_PREFIX} Changing Page to ${JSON.stringify(page)}`);
    this._page = page;
    this.pageChanged.emit(page);
  }
  
  /**
   * Returns the current page size
   */  
  public get pageSize(){

    this.log.debug(`${LOG_PREFIX} Returning ${this._pageSize} as Page Size`);
    return this._pageSize;
  }

  /**
   * Sets and broadcasts the newly set page size
   */
  public set pageSize(pageSize: number){   

    this.log.debug(`${LOG_PREFIX} Changing Page Size to ${JSON.stringify(pageSize)}`);
    this._pageSize = pageSize;
    this.pageSizeChanged.emit(pageSize);
  }
  
  /**
   * Sets the total number of records
   */
  public set total(total: number) {
    
    this.log.debug(`${LOG_PREFIX} Setting total to ${JSON.stringify(total)}`);
    this._totalSubject$.next(total);
  }  

}

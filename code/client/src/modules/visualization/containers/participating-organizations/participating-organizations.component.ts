import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  HostListener,
  OnInit
} from '@angular/core';
import {
  OrganizationsDataService
} from '@modules/visualization/services';
import { NGXLogger } from 'ngx-logger';
import { BehaviorSubject, Observable, Subscription } from 'rxjs';
import {
  Group,
  Organization,
  OrganizationType
} from '@modules/visualization/models';

const LOG_PREFIX: string = "[Issue Counts Organization Component]";

@Component({
  selector: 'sb-participating-organizations',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './participating-organizations.component.html',
  styleUrls: ['participating-organizations.component.scss'],
})
export class ParticipatingOrganizationsComponent implements OnInit {

  organizationTypeId: number = -1;


  private _organizations$: BehaviorSubject<Organization[]> = new BehaviorSubject<Organization[]>([]);
  readonly organizations$: Observable<Organization[]> = this._organizations$.asObservable();

  totalCount!: number;

  // Instantiate a central gathering point for all the component's subscriptions.
  // Makes it easier to unsubscribe from all subscriptions when the component is destroyed.   
  private _subscriptions: Subscription[] = [];

  constructor(
    private cd: ChangeDetectorRef,
    private organizationsDataService: OrganizationsDataService,
    private log: NGXLogger) { }

  ngOnInit() {
    this.organizationsDataService.getOrganizations$().subscribe(results => {
      this.totalCount = results.length;
      this._organizations$.next(Object.assign([], results));
    });
  }

  @HostListener('window:beforeunload')
  ngOnDestroy() {

    this.log.trace(`${LOG_PREFIX} Destroying Component`);

    // Clear all subscriptions
    this.log.trace(`${LOG_PREFIX} Clearing all subscriptions`);
    this._subscriptions.forEach((s) => s.unsubscribe());
  }

  getOrganizationThumbnailDetails(organization: Organization): { img: string, title: string, subtitle: string, link: string } {
    return {
      img: "/assets/logos/" + organization.id + ".png",
      title: organization.name,
      subtitle: organization.plural,
      link: organization.website
    };
  }

  onFilterByOrganizationType(organizationTypeId: number) {
    this.organizationTypeId = organizationTypeId;
    if(organizationTypeId == -1) {
      this.organizationsDataService.getOrganizations$()
      .subscribe(results => {
        this.totalCount = results.length;
        this._organizations$.next(Object.assign([], results));
      });
    } else {
      this.organizationsDataService.getOrganizationsByOrganizationType$(organizationTypeId)
      .subscribe(results => {
        this.totalCount = results.length;
        this._organizations$.next(Object.assign([], results));
      });
    }

    this.cd.detectChanges();
  }

}

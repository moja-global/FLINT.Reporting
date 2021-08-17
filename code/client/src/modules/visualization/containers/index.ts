import { ActorCountsComponent } from './actors-counts/actors-counts.component';
import { IssuesCountsComponent } from './issue-counts/issue-counts.component';
import { IssueGraphsComponent } from './issue-graphs/issue-graphs.component';
import { ParticipatingOrganizationsComponent } from './participating-organizations/participating-organizations.component';
import { ParticipationGraphsComponent } from './participation-graphs/participation-graphs.component';

export const containers = [
    ActorCountsComponent,
    IssuesCountsComponent,
    IssueGraphsComponent,
    ParticipatingOrganizationsComponent,
    ParticipationGraphsComponent
];

export * from './actors-counts/actors-counts.component';
export * from './issue-counts/issue-counts.component';
export * from './issue-graphs/issue-graphs.component';
export * from './participating-organizations/participating-organizations.component';
export * from './participation-graphs/participation-graphs.component';
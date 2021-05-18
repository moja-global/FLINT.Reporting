import { SortDirection } from "@common/directives/sortable.directive";

export interface State {
    page: number;
    pageSize: number;
    searchTerm: string;
    sortColumn: string;
    sortDirection: SortDirection;
}

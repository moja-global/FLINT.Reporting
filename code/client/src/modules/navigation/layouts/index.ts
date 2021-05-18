import { LayoutAuthComponent } from './layout-auth/layout-auth.component';
import { LayoutDashboardComponent } from './layout-dashboard/layout-dashboard.component';
import { LayoutErrorComponent } from './layout-error/layout-error.component';
import { LayoutMainComponent } from './layout-main/layout-main.component';

export const layouts = [
    LayoutAuthComponent,
    LayoutDashboardComponent,  
    LayoutErrorComponent,  
    LayoutMainComponent];


export * from './layout-auth/layout-auth.component';
export * from './layout-dashboard/layout-dashboard.component';
export * from './layout-error/layout-error.component';
export * from './layout-main/layout-main.component';

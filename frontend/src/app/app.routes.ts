import { Routes } from '@angular/router';
import { Dashboard } from './components/dashboard/dashboard.component';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './pages/login/login';
import { roleGuard } from './guards/role.guard';
import { TavoliComponent } from './components/tavoli/tavoli.component';
import { MenuComponent } from './components/menu/menu.component';
import { ComandeComponent } from './components/comande/comande.component';
import { CassaDashboardComponent } from './components/cassa-dashboard/cassa-dashboard.component';
import { StaffManagementComponent } from './components/staff-management/staff-management.component';

export const routes: Routes = [
    {
        path: '',
        component: HomeComponent
    },
    {
        path: 'login',
        component: LoginComponent
    },
    {
        path: 'dashboard',
        component: Dashboard,
        canActivate: [roleGuard]
    },
    {
        path: 'tavoli',
        component: TavoliComponent,
        canActivate: [roleGuard]
    },
    {
        path: 'ordini',
        component: ComandeComponent,
        canActivate: [roleGuard]
    },
    {
        path: 'menu',
        component: MenuComponent,
        canActivate: [roleGuard]
    },
    {
        path: 'cassa',
        component: CassaDashboardComponent,
        canActivate: [roleGuard]
    },
    {
        path: 'gestione-staff',
        component: StaffManagementComponent,
        canActivate: [roleGuard],
        data: { roles: ['ADMIN'] }
    },
    {
        path: '**',
        redirectTo: ''
    }
];

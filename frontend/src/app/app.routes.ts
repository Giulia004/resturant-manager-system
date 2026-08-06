import { Routes } from '@angular/router';
import { Dashboard } from './components/dashboard/dashboard.component';
import { RegisterComponent } from './pages/register/register.component';
import { HomeComponent } from './components/home/home';
import { LoginComponent } from './pages/login/login';
import { roleGuard } from './guards/role.guard';
import { TavoliComponent } from './components/tavoli/tavoli.component';
import { MenuComponent } from './components/menu/menu.component';

export const routes: Routes = [
    {
        path: '',
        component:HomeComponent
    },
    {
        path: 'register',
        component:RegisterComponent
    },
    {
        path: 'login',
        component:LoginComponent
    },
    {
        path: 'dashboard',
        component: Dashboard,
        canActivate:[roleGuard]
    },
    {
        path: 'tavoli',
        component: TavoliComponent,
        canActivate:[roleGuard]
    },
    {
        path: 'menu',
        component: MenuComponent,
        canActivate:[roleGuard]
    },
    {
        path: '**',
        redirectTo:''
    }
];

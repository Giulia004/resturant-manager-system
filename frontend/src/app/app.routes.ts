import { Routes } from '@angular/router';
import { Dashboard } from './components/dashboard/dashboard.component';
import { RegisterComponent } from './pages/register/register.component';
import { HomeComponent } from './components/home/home';
import { LoginComponent } from './pages/login/login';

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
        component:Dashboard
    }, {
        path: '**',
        redirectTo:''
    }
];

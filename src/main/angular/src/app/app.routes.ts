import {Routes} from '@angular/router';
import {Register} from './components/register/register';
import {Login} from './components/login/login';
import {Sidenav} from './components/sidenav/sidenav';
import {AuthGuard} from './services/AuthGuard';
import {VehicleTable} from './components/vehicle-table/vehicle-table';
import {PostTable} from './components/post-table/post-table';
import {BankTable} from './components/bank-table/bank-table';

export const routes: Routes = [
  { path: 'auth/register', component: Register },
  { path: 'auth/login', component: Login },
  {
    path: 'api',
    component: Sidenav,
    canActivate: [AuthGuard],
    children: [
      { path: 'vehicles', component: VehicleTable },
      { path: 'posts', component: PostTable },
      { path: 'banks', component: BankTable },
      { path: '', redirectTo: 'vehicles', pathMatch: 'full' }
    ]
  },
  { path: '', redirectTo: 'auth/login', pathMatch: 'full' }
];

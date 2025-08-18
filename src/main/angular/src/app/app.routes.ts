import {Routes} from '@angular/router';
import {Register} from './components/auth/register/register';
import {Login} from './components/auth/login/login';
import {AuthGuard} from './services/AuthGuard';
import {VehicleTable} from './components/vehicle/vehicle-table/vehicle-table';
import {PostTable} from './components/post/post-table/post-table';
import {BranchTable} from './components/branch/branch-table/branch-table';
import { BranchDetail } from './components/branch/branch-detail/branch-detail';
import {Container} from './components/container/container';
import {BankDetail} from './components/bank/bank-detail/bank-detail';

export const routes: Routes = [
  { path: 'auth/register', component: Register },
  { path: 'auth/login', component: Login },
  {
    path: 'api',
    component: Container,
    canActivate: [AuthGuard],
    children: [
      { path: 'vehicles', component: VehicleTable },
      { path: 'posts', component: PostTable },
      { path: 'banks', component: BankDetail },
      { path: 'branches', component: BranchTable },
      { path: 'branches/:id', component: BranchDetail},
      { path: '', redirectTo: 'vehicles', pathMatch: 'full' }
    ]
  },
  { path: '', redirectTo: 'auth/login', pathMatch: 'full' }
];

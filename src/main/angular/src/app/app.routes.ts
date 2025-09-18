import {Routes} from '@angular/router';
import {Register} from './components/auth/register/register';
import {Login} from './components/auth/login/login';
import {AuthGuard} from './services/AuthGuard';
import {VehicleTable} from './components/vehicle/vehicle-table/vehicle-table';
import {PostTable} from './components/post/post-table/post-table';
import {Branches} from './components/branch/branches/branches';
import { BranchDetail } from './components/branch/branch-detail/branch-detail';
import {Container} from './components/container/container';
import {InstituteDetail} from './components/bank/institute-detail/institute-detail';
import {Dashboard} from './components/dashboard/dashboard';
import {Institutes} from './components/bank/institutes/institutes';
import {Clients} from './components/client/clients/clients';
import {Accounts} from './components/account/accounts/accounts';
import {Cards} from './components/card/cards/cards';
import {Transactions} from './components/transaction/transactions/transactions';
import {UsersDetails} from './components/auth/users-details/users-details';
import {Profile} from './components/auth/profile/profile';

export const routes: Routes = [
  { path: '', redirectTo: 'auth/login', pathMatch: 'full' },
  { path: 'auth/login', component: Login },
  { path: 'auth/register', component: Register },
  {
    path: 'api',
    component: Container,
    canActivate: [AuthGuard],
    children: [
      { path: 'dashboard', component: Dashboard },
     /* { path: 'vehicles', component: VehicleTable },
      { path: 'posts', component: PostTable },*/

      { path: 'institute', component: Institutes },
      { path: 'branches', component: Branches },
      { path: 'clients', component: Clients },
      { path: 'accounts', component: Accounts },
      { path: 'cards', component: Cards },
      { path: 'transactions', component: Transactions },
      { path: 'institute/:id/details', component: InstituteDetail},
      { path: 'branches/:id', component: BranchDetail},
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  },
  {
    path: 'auth',
    component: Container,
    canActivate: [AuthGuard],
    children: [
      { path: 'profile', component: Profile},
      { path: 'users', component: UsersDetails},
      ]
  }

];

import {Routes} from '@angular/router';
import {RegisterComponent} from './components/auth/register/register-component';
import {LoginComponent} from './components/auth/login/login-component';
import {AuthGuard} from './services/AuthGuard';
import {BranchListComponent} from './components/branch/branch-list/branch-list-component';
import {BranchDetailComponent} from './components/branch/branch-detail/branch-detail-component';
import {ContainerComponent} from './components/container/container-component';
import {InstituteDetailComponent} from './components/institute/institute-detail/institute-detail-component';
import {DashboardComponent} from './components/dashboard/dashboard-component';
import {InstituteListComponent} from './components/institute/institute-list/institute-list-component';
import {ClientListComponent} from './components/client/client-list/client-list-component';
import {AccountListComponent} from './components/account/account-list/account-list-component';
import {CardComponent} from './components/card/card-list/card-component';
import {TransactionListComponent} from './components/transaction/transaction-list/transaction-list-component';
import {UserDetailComponent} from './components/auth/user-detail/user-detail-component';
import {ProfileComponent} from './components/auth/profile/profile-component';
import {AddressListComponent} from './components/address/address-list/address-list.component';
import {AddressDetailComponent} from './components/address/address-detail/address-detail-component';

export const routes: Routes = [
  {path: '', redirectTo: 'auth/login', pathMatch: 'full'},
  {path: 'auth/login', component: LoginComponent},
  {path: 'auth/register', component: RegisterComponent},
  {
    path: 'api',
    component: ContainerComponent,
    canActivate: [AuthGuard],
    children: [
      {path: 'dashboard', component: DashboardComponent},

      {path: 'institute', component: InstituteListComponent},
      {path: 'institute/:id/details', component: InstituteDetailComponent},
      {path: 'institute/:id/branches', component: InstituteDetailComponent},

      {path: 'branches', component: BranchListComponent},
      {path: 'branches/:id', component: BranchDetailComponent},

      {path: 'addresses', component: AddressListComponent},
      {path: 'addresses/:id/details', component: AddressDetailComponent},

      {path: 'clients', component: ClientListComponent},

      {path: 'accounts', component: AccountListComponent},

      {path: 'cards', component: CardComponent},

      {path: 'transactions', component: TransactionListComponent},

      {path: '', redirectTo: 'dashboard', pathMatch: 'full'}
    ]
  },
  {
    path: 'auth',
    component: ContainerComponent,
    canActivate: [AuthGuard],
    children: [
      {path: 'profile', component: ProfileComponent},
      {path: 'users', component: UserDetailComponent},
    ]
  }

];

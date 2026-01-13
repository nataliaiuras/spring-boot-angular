import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserService } from '../../services/user/user-service';
import { BranchService } from '../../services/branch/branch-service';
import { InstituteService } from '../../services/institute/institute-service';
import { Subscription } from 'rxjs';
import { RouterModule } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDividerModule } from '@angular/material/divider';
import {AccountService} from '../../services/account/account-service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatCardModule,
    MatIconModule,
    MatButtonModule,
    MatProgressSpinnerModule,
    MatDividerModule
  ],
  templateUrl: './dashboard-component.html',
  styleUrl: './dashboard-component.css'
})
export class DashboardComponent implements OnInit, OnDestroy {
  // Statistics
  userCount = 0;
  branchCount = 0;
  instituteCount = 0;
  accountCount = 0;

  // Loading states
  loadingUsers = true;
  loadingBranches = true;
  loadingInstitutes = true;
  loadingAccounts = true;

  // Current user
  currentUser: any = null;

  // System info
  currentDateTime = '';

  // Subscriptions to manage
  private subscriptions: Subscription[] = [];

  constructor(
    private userService: UserService,
    private branchService: BranchService,
    private instituteService: InstituteService,
    private accountService: AccountService
  ) {}

  ngOnInit(): void {
    this.loadDashboardData();
    this.updateDateTime();

    setInterval(() => this.updateDateTime(), 60000);
  }


  updateDateTime(): void {
    this.currentDateTime = new Date().toLocaleString();
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }


  loadDashboardData(): void {
    this.userService.getCurrentUser();
    const userSub = this.userService.currentUser$.subscribe(user => {
      this.currentUser = user;
    });
    this.subscriptions.push(userSub);

    const usersSub = this.userService.getAllUsers().subscribe({
      next: (response) => {
        if (response.success && response.data) {
          this.userCount = response.data.totalElements || 0;
        }
        this.loadingUsers = false;
      },
      error: (error) => {
        console.error('Error loading users:', error);
        this.loadingUsers = false;
      }
    });
    this.subscriptions.push(usersSub);



    const instituteSub = this.instituteService.getAllInstitutes().subscribe({
      next: (response) => {
        if (response.success && response.data) {
          this.instituteCount = response.data.totalElements || 0;
        }
        this.loadingInstitutes = false;
      },
      error: (error) => {
        console.error('Error loading institutes:', error);
        this.loadingInstitutes = false;
      }
    });
    this.subscriptions.push(instituteSub);

   /* this.instituteService.getAllInstitutes();
    const instituteSub = this.instituteService.institute$.subscribe(institutes => {
      this.instituteCount = institutes.length;
      this.loadingInstitutes = false;
    });
    this.subscriptions.push(instituteSub);*/


    const branchSub = this.branchService.getAllBranches().subscribe({
      next: (response) => {
        if (response.success && response.data) {
          this.branchCount = response.data.totalElements || 0;
        }
        this.loadingBranches = false;
      },
      error: (error) => {
        console.error('Error loading branches:', error);
        this.loadingBranches = false;
      }
    });
    this.subscriptions.push(branchSub);


   /* this.branchService.getBranches();
    const branchSub = this.branchService.branches$.subscribe(branches => {
      this.branchCount = branches.length;
      this.loadingBranches = false;
    });
    this.subscriptions.push(branchSub);*/


    this.accountService.getAccounts();
    const accountSub = this.accountService.accounts$.subscribe(accounts => {
      this.accountCount = accounts.length;
      this.loadingAccounts = false;
    });
    this.subscriptions.push(accountSub);

  }


}

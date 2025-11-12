import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserService } from '../../services/user/user-service';
import { BranchService } from '../../services/branch/branch-service';
import { InstituteService } from '../../services/institute/institute-service';
import { VehicleService } from '../../services/vehicle/vehicle-service';
import { Subscription } from 'rxjs';
import { RouterModule } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDividerModule } from '@angular/material/divider';

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
  vehicleCount = 0;

  // Loading states
  loadingUsers = true;
  loadingBranches = true;
  loadingInstitutes = true;
  loadingVehicles = true;

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
    private vehicleService: VehicleService
  ) {}

  ngOnInit(): void {
    this.loadDashboardData();
    this.updateDateTime();

    // Update the date/time every minute
    setInterval(() => this.updateDateTime(), 60000);
  }

  updateDateTime(): void {
    this.currentDateTime = new Date().toLocaleString();
  }

  ngOnDestroy(): void {
    // Clean up subscriptions to prevent memory leaks
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  loadDashboardData(): void {
    // Load current user
    this.userService.getCurrentUser();
    const userSub = this.userService.currentUser$.subscribe(user => {
      this.currentUser = user;
    });
    this.subscriptions.push(userSub);

    // Load users count
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

    this.instituteService.getAllInstitutes();
    const instituteSub = this.instituteService.institute$.subscribe(institutes => {
      this.instituteCount = institutes.length;
      this.loadingInstitutes = false;
    });
    this.subscriptions.push(instituteSub);

    // Load branches
    this.branchService.getBranches();
    const branchSub = this.branchService.branches$.subscribe(branches => {
      this.branchCount = branches.length;
      this.loadingBranches = false;
    });
    this.subscriptions.push(branchSub);



    // Load vehicles
    this.vehicleService.getVehicles();
    const vehicleSub = this.vehicleService.vehicles$.subscribe(vehicles => {
      this.vehicleCount = vehicles.length;
      this.loadingVehicles = false;
    });
    this.subscriptions.push(vehicleSub);
  }
}

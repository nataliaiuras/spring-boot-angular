import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {UserService} from '../../../services/user/user-service';
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef,
  MatHeaderRow,
  MatHeaderRowDef,
  MatTable,
  MatTableDataSource
} from '@angular/material/table';

@Component({
  selector: 'app-users-details',
  imports: [CommonModule, MatCell, MatCellDef, MatColumnDef, MatHeaderCell, MatTable, MatHeaderRow, MatHeaderRowDef, MatHeaderCellDef],
  templateUrl: './user-detail-component.html',
  styleUrl: './user-detail-component.css'
})
export class UserDetailComponent implements OnInit {
  displayedColumns = ['id', 'username', 'role', 'enabled', 'lastModifiedDate', 'lastModifiedDate', 'version'];


  dataSource = new MatTableDataSource<User>([]);
  users: User[] = [];
  loading = false;
  error: string | null = null;
  currentPage = 0;
  totalPages = 0;
  totalElements = 0;
  pageSize = 10;


  constructor(
    private readonly userService: UserService
  ) {
  }

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(page: number = 0, size: number = 10): void {
    this.loading = true;
    this.error = null;

    this.userService.getAllUsers().subscribe({
      next: (response) => {
        if (response.success && response.data) {
          this.users = response.data.content;
          this.currentPage = response.data.number;
          this.totalPages = response.data.totalPages;
          this.totalElements = response.data.totalElements;
          this.pageSize = response.data.size;

        } else {
          this.error = 'Failed to load users';
        }
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading users:', err);
        this.error = 'Error loading users. Please try again.';
        this.loading = false;
      }
    });
  }
}

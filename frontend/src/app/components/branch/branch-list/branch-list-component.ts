import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef,
  MatHeaderRow,
  MatHeaderRowDef,
  MatRow,
  MatRowDef,
  MatTable,
  MatTableDataSource
} from '@angular/material/table';
import {MatPaginator} from '@angular/material/paginator';
import {MatDialog} from '@angular/material/dialog';
import {BranchFormComponent} from '../branch-form/branch-form-component';
import {MatButton} from '@angular/material/button';
import {BranchService} from '../../../services/branch/branch-service';
import {BranchResponse} from '../../../core/models/response/branch/branch-response';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-branch-list',
  imports: [
    MatButton,
    MatCell,
    MatCellDef,
    MatColumnDef,
    MatHeaderCell,
    MatHeaderRow,
    MatHeaderRowDef,
    MatPaginator,
    MatRow,
    MatRowDef,
    MatTable,
    MatHeaderCellDef,
  ],
  templateUrl: './branch-list-component.html',
  styleUrl: './branch-list-component.css'
})
export class BranchListComponent implements OnInit, AfterViewInit {

  displayedColumns = ['id', 'branchCode', 'locationCode', 'bicCode', 'name', 'email', 'phoneNumber'];
  dataSource = new MatTableDataSource<BranchResponse>([]);

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(
    private readonly branchService: BranchService,
    private readonly dialog: MatDialog) {
  }

  ngOnInit() {
    this.loadBranches();
  }

  private loadBranches(): void {
    this.branchService.branches$.subscribe({
      next: (branches) => {
        console.log('BranchListComponent loaded:', branches);
        this.dataSource.data = branches;
      },
      error: (error) => {
        console.error('Error loading branches:', error);
      }
    });
    this.branchService.getBranches();
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }

  openDetail2(branch?: BranchListComponent) {
    /* this.branchService.getBranch(branch.);*/

  }

  openForm(branch?: BranchListComponent) {
    const dialogRef = this.dialog.open(BranchFormComponent, {
      width: '600px',
      data: branch ? {...branch} : null
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        if (branch) {
          this.branchService.updateBranch(result);
        } else {
          this.branchService.createBranch(result);
        }
      }
    });
  }

  deleteBranch(id: number) {
    this.branchService.deleteBranch(id);
  }

  openDetail(id: number) {
    this.branchService.getBranch(id);
    //  this.router.navigate(['api/branches/', id]);
  }
}

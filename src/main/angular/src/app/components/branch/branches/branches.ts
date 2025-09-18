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
import {BranchForm} from '../branch-form/branch-form';
import {MatButton} from '@angular/material/button';
import {BranchService} from '../../../services/branch/branch-service';
import {Branch} from '../../../models/branch';

@Component({
  selector: 'app-branch-table',
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
  templateUrl: './branches.html',
  styleUrl: './branches.css'
})
export class Branches implements OnInit, AfterViewInit {
  displayedColumns = ['id', 'branchCode', 'locationCode', 'bicCode', 'name', 'email', 'phoneNumber'];

  dataSource = new MatTableDataSource<Branch>([]);

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(private readonly branchService: BranchService, private readonly dialog: MatDialog) {
  }

  ngOnInit() {
    this.loadBranches();
  }

  private loadBranches(): void {
    this.branchService.branches$.subscribe({
      next: (branches) => {
        console.log('Branches loaded:', branches);
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

  openDetail2(branch?: Branches) {
    /* this.branchService.getBranch(branch.);*/

  }

  openForm(branch?: Branches) {
    const dialogRef = this.dialog.open(BranchForm, {
      width: '600px',
      data: branch ? {...branch} : null
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        if (branch) {
          this.branchService.updateBranch(result);
        } else {
          this.branchService.addBranch(result);
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

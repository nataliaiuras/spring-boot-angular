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
import {MatButton, MatIconButton} from '@angular/material/button';
import {MatIcon} from '@angular/material/icon';
import {Router, RouterLinkActive} from '@angular/router';
import {BranchService} from '../../../services/branch/branch-service';
import {Branch} from '../../../models/branch';
import {MatListItem} from '@angular/material/list';

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
    MatIcon,
    MatIconButton,
    MatPaginator,
    MatRow,
    MatRowDef,
    MatTable,
    MatHeaderCellDef,
    MatListItem,
    RouterLinkActive,
  ],
  templateUrl: './branch-table.html',
  styleUrl: './branch-table.css'
})
export class BranchTable implements OnInit, AfterViewInit {
  displayedColumns = ['id', 'name', 'bicCode', 'swiftCode', 'telephoneNumber', 'email', 'address', 'actions'];

  dataSource = new MatTableDataSource<Branch>([]);

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(private branchService: BranchService, private dialog: MatDialog) {
  }

  ngOnInit() {
    this.branchService.getBranches();
    this.branchService.branches$.subscribe(branches => {
      this.dataSource.data = branches;
    });
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }

  openDetail2(branch?: BranchTable) {
   /* this.branchService.getBranch(branch.);*/

  }

  openForm(branch?: BranchTable) {
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

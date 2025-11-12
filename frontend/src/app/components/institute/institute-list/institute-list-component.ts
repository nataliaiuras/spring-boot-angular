import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {InstituteResponse} from '../../../core/models/response/institute/institute.response';
import {InstituteService} from '../../../services/institute/institute-service';
import {MatDialog} from '@angular/material/dialog';
import {InstituteFormComponent} from '../institute-form/institute-form-component';
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef,
  MatHeaderRow,
  MatHeaderRowDef, MatRow, MatRowDef,
  MatTable,
  MatTableDataSource
} from '@angular/material/table';
import {MatButton, MatIconButton} from '@angular/material/button';
import {MatPaginator} from '@angular/material/paginator';
import {Router} from '@angular/router';
import {MatIcon} from '@angular/material/icon';
import {MatTooltip} from '@angular/material/tooltip';

@Component({
  selector: 'app-institutes',
  imports: [
    MatButton,
    MatIconButton,
    MatTable,
    MatColumnDef,
    MatHeaderCell,
    MatCell,
    MatHeaderCellDef,
    MatCellDef,
    MatRow,
    MatRowDef,
    MatHeaderRow,
    MatHeaderRowDef,
    MatPaginator,
    MatIcon,
    MatTooltip,
  ],
  templateUrl: './institute-list-component.html',
  styleUrl: './institute-list-component.css'
})
export class InstituteListComponent implements OnInit, AfterViewInit {
  displayedColumns = ['id', 'bankCode', 'name', 'website'];
  dataSource = new MatTableDataSource<InstituteResponse>([]);

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(
    private readonly instituteService: InstituteService,
    private readonly dialog: MatDialog,
    private readonly router: Router) {
  }

  ngOnInit() {
    this.instituteService.loadInstitutes();
    this.instituteService.institute$.subscribe({
      next: (institutes) => {
        this.dataSource.data = institutes;
      }
    });
  }


ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }

  openForm(institute?: InstituteResponse) {
    const dialogRef = this.dialog.open(InstituteFormComponent, {
      width: '400px',
      data: institute ? {...institute} : null
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        if (institute) {
          this.instituteService.updateInstitute(result.getId, result);
        } else {
          this.instituteService.createInstitute(result);
        }
      }
    });
  }

  getInstitute(institute:any) {
    this.router.navigate(['/api/institute', institute.id, 'details']);
  }

  deleteInstitute(institute: InstituteResponse) {
    if (confirm(`Are you sure you want to delete ${institute.name}?`)) {
      this.instituteService.deleteInstitute(institute.id).subscribe({
        next: (response) => {
          if (response.success) {
            console.log('Institute deleted successfully');
          }
        },
        error: (error) => {
          console.error('Error deleting institute:', error);
        }
      });
    }
  }
}

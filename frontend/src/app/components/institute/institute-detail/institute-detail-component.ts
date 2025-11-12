import {Component, OnDestroy, OnInit, signal} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {InstituteService} from '../../../services/institute/institute-service';
import {InstituteResponse} from '../../../core/models/response/institute/institute.response';
import {MatDialog} from '@angular/material/dialog';
import {InstituteFormComponent} from '../institute-form/institute-form-component';
import {CommonModule} from '@angular/common';
import {finalize, Subject, takeUntil} from 'rxjs';
import {BranchResponse} from '../../../core/models/response/branch/branch-response';
import {AddressResponse} from '../../../core/models/response/address/address-response';
import {FormGroup} from '@angular/forms';
import {InstituteDetailResponse} from '../../../core/models/response/institute/institute-detail.response';
import {MatCard, MatCardActions, MatCardContent, MatCardHeader, MatCardTitle} from '@angular/material/card';
import {MatIcon} from '@angular/material/icon';
import {MatButton} from '@angular/material/button';
import {
  MatAccordion,
  MatExpansionPanel, MatExpansionPanelDescription,
  MatExpansionPanelHeader,
  MatExpansionPanelTitle
} from '@angular/material/expansion';

@Component({
  selector: 'app-institute-detail',
  imports: [
    CommonModule,
    MatCard,
    MatCardActions,
    MatIcon,
    MatCardContent,
    MatCardTitle,
    MatCardHeader,
    MatButton,
    MatAccordion,
    MatExpansionPanel,
    MatExpansionPanelTitle,
    MatExpansionPanelHeader
  ],
  /* changeDetection: ChangeDetectionStrategy.OnPush,*/
  templateUrl: './institute-detail-component.html',
  styleUrl: './institute-detail-component.css'
})
export class InstituteDetailComponent implements OnInit, OnDestroy {
  institute: InstituteResponse | null = null;
  instituteDetail: InstituteDetailResponse | null = null;
  address: AddressResponse | null = null;
  branches: BranchResponse[] = [];
  instituteForm!: FormGroup;
  isEditMode = false;
  loading = true;
  error = false;
  private readonly destroy$ = new Subject<void>();
  readonly panelOpenState = signal(false);


  constructor(
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly instituteService: InstituteService,
    private readonly dialog: MatDialog
  ) {
  }

  ngOnInit(): void {
    this.loadInstituteDetails();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }


  private loadInstituteDetails(): void {
    const instituteId = this.route.snapshot.paramMap.get('id');

    if (!instituteId) {
      this.error = true;
      this.loading = false;
      return;
    }

    this.loading = true;
    this.error = false;

    this.instituteService.getDetailedInstitute(Number(instituteId))
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => this.loading = false)
      )
      .subscribe({
        next: (response) => {
          if (response.success && response.data) {
            this.instituteDetail = response.data;
            this.branches = response.data.branches;
          }
          console.log('Institute loaded:', response);
        },
        error: (error) => {
          console.error('Error loading institute:', error);
          this.error = true;
        }
      });
  }


  openUpdateForm(): void {
    if (!this.instituteDetail) return;

    const dialogRef = this.dialog.open(InstituteFormComponent, {
      width: '400px',
      data: {...this.instituteDetail}
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        const id = +this.route.snapshot.params['id']; // Convert to number with +
        this.instituteService.updateInstitute(id, result);
        // Reload the institute after update
        this.loadInstituteDetails();
      }
    });
  }

  deleteInstitute(): void {
    if (!this.instituteDetail) return;

    if (confirm('Are you sure you want to delete this institute?')) {
      this.instituteService.deleteInstitute(this.instituteDetail.id);
      this.router.navigate(['/api/institute']);
    }
  }

  getBranches() {
    if (!this.instituteDetail) return;
    return this.instituteDetail.branches
  }

  goBack(): void {
    this.router.navigate(['/api/institute']);
  }
}

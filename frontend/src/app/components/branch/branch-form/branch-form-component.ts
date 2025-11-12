import {Component, Inject} from '@angular/core';
import {MatButton} from '@angular/material/button';
import {MAT_DIALOG_DATA, MatDialogActions, MatDialogRef, MatDialogTitle} from '@angular/material/dialog';
import {MatError, MatFormField, MatInput, MatLabel} from '@angular/material/input';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {BranchResponse} from '../../../core/models/response/branch/branch-response';
import {MatOption} from '@angular/material/core';
import {MatSelect} from '@angular/material/select';

@Component({
  selector: 'app-branch-form',
  imports: [
    MatButton,
    MatDialogActions,
    MatDialogTitle,
    MatError,
    MatFormField,
    MatInput,
    MatLabel,
    MatOption,
    MatSelect,
    ReactiveFormsModule
  ],
  templateUrl: './branch-form-component.html',
  styleUrl: './branch-form-component.css'
})
export class BranchFormComponent {

  branchForm: FormGroup;

  constructor(
    private readonly fb: FormBuilder,
    private readonly dialogRef: MatDialogRef<BranchFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: BranchResponse | null
  ) {
    this.branchForm = this.fb.group({
      id: [data?.id ?? null],
      bicCode: [data?.bicCode ?? '', Validators.required],
      swiftCode: [data?.locationCode ?? '', Validators.required],
      name: [data?.name ?? '', Validators.required],
      email: [data?.email ?? '', Validators.required],
      telephoneNumber: [data?.phoneNumber ?? '', Validators.required],
   /*   address: [data?.address ?? '', Validators.required]*/
    });
  }

  onSubmit() {
    if (this.branchForm.valid) {
      this.dialogRef.close(this.branchForm.value);
    }
  }

  onCancel() {
    this.dialogRef.close();
  }
}

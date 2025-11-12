import {Component, Inject} from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {MatButton} from "@angular/material/button";
import {MAT_DIALOG_DATA, MatDialogActions, MatDialogRef, MatDialogTitle} from "@angular/material/dialog";
import {MatError, MatFormField, MatInput, MatLabel} from "@angular/material/input";
import {InstituteResponse} from '../../../core/models/response/institute/institute.response';

@Component({
  selector: 'app-bank-form',
  imports: [
    FormsModule,
    MatButton,
    MatDialogActions,
    MatDialogTitle,
    MatError,
    MatFormField,
    MatInput,
    MatLabel,
    ReactiveFormsModule
  ],
  templateUrl: './institute-form-component.html',
  styleUrl: './institute-form-component.css'
})
export class InstituteFormComponent {
  instituteForm: FormGroup;

  constructor(
    private readonly fb: FormBuilder,
    private readonly dialogRef: MatDialogRef<InstituteFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: InstituteResponse | null
  ) {
    this.instituteForm = this.fb.group({
     /* id: [data?.id ?? null],*/
      bankCode: [data?.bankCode ?? '', Validators.required],
      name: [data?.name ?? '', Validators.required],
      website: [data?.website ?? '', [Validators.required]]
    });
  }

  onSubmit() {
    if (this.instituteForm.valid) {
      this.dialogRef.close(this.instituteForm.value);
    }
  }

  onCancel() {
    this.dialogRef.close();
  }
}

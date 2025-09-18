import {Component, Inject} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {MAT_DIALOG_DATA, MatDialogActions, MatDialogRef, MatDialogTitle} from '@angular/material/dialog';
import {MatError, MatFormField, MatInput, MatLabel} from '@angular/material/input';
import {MatButton} from '@angular/material/button';
import {MatSelect} from '@angular/material/select';
import {MatOption} from '@angular/material/core';
import {Vehicle} from '../../../models/vehicle';

@Component({
  selector: 'app-vehicle-form',
  templateUrl: './vehicle-form.html',
  imports: [
    MatDialogTitle,
    ReactiveFormsModule,
    MatFormField,
    MatLabel,
    MatInput,
    MatDialogActions,
    MatButton,
    MatError,
    MatOption,
    MatSelect,
  ],
  styleUrls: ['./vehicle-form.css'],
  standalone: true,
})
export class VehicleForm {
  vehicleForm: FormGroup;

  constructor(
    private readonly fb: FormBuilder,
    private readonly dialogRef: MatDialogRef<VehicleForm>,
    @Inject(MAT_DIALOG_DATA) public data: Vehicle | null
  ) {
    this.vehicleForm = this.fb.group({
      id: [data?.id ?? null],
      brand: [data?.brand ?? '', Validators.required],
      model: [data?.model ?? '', Validators.required],
      yearProd: [data?.yearProd ?? '', [Validators.required, Validators.min(1900), Validators.max(new Date().getFullYear())]],
      color: [data?.color ?? '', Validators.required]
    });
  }

  onSubmit() {
    if (this.vehicleForm.valid) {
      this.dialogRef.close(this.vehicleForm.value);
    }
  }

  onCancel() {
    this.dialogRef.close();
  }
}

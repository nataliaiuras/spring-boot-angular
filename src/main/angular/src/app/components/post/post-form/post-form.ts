import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogActions, MatDialogRef, MatDialogTitle } from '@angular/material/dialog';
import { MatError, MatFormField, MatInput, MatLabel } from '@angular/material/input';
import { NgIf } from '@angular/common';
import { MatButton } from '@angular/material/button';
import { Post } from '../../../models/post';

@Component({
  selector: 'app-post-form',
  templateUrl: './post-form.html',
  imports: [
    MatDialogTitle,
    ReactiveFormsModule,
    MatFormField,
    MatLabel,
    MatInput,
    NgIf,
    MatDialogActions,
    MatButton,
    MatError
  ],
  styleUrls: ['./post-form.css'],
  standalone: true,
})
export class PostForm {
  postForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<PostForm>,
    @Inject(MAT_DIALOG_DATA) public data: Post | null
  ) {
    const currentDate = new Date().toISOString();

    this.postForm = this.fb.group({
      id: [data?.id || null],
      title: [data?.title || '', Validators.required],
      content: [data?.content || '', Validators.required],
      author: [data?.author || '', Validators.required],
      createdAt: [data?.createdAt || currentDate]
    });
  }

  onSubmit() {
    if (this.postForm.valid) {
      this.dialogRef.close(this.postForm.value);
    }
  }

  onCancel() {
    this.dialogRef.close();
  }
}

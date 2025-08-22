import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';

@Component({
  selector: 'app-edit-modal',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatInputModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule
  ],
  templateUrl: './edit-modal.html',
  styleUrls: ['./edit-modal.scss']
})
export class EditModal {
  title: string = 'Edit';
  data: any = {};
  fields: { key: string; label: string; type?: string; options?: any[] }[] = [];
  isEditMode: boolean = true; // default true

  form: FormGroup;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<EditModal>,
    @Inject(MAT_DIALOG_DATA) public dialogData: any
  ) {
    this.title = dialogData.title;
    this.data = dialogData.data || {};
    this.fields = dialogData.fields || [];
    this.isEditMode = dialogData.isEditMode ?? true; // ispravno preuzimanje iz data
    this.form = this.fb.group({});
    this.initForm();
  }

  initForm() {
    const group: any = {};
    this.fields.forEach(f => {
      group[f.key] = [this.data[f.key] ?? ''];
    });
    this.form = this.fb.group(group);
  }

  isSelectField(field: any): boolean {
    return Array.isArray(field.options) && field.options.length > 0;
  }

  onSave() {
    if (this.form.valid) this.dialogRef.close(this.form.value);
  }

  onCancel() {
    this.dialogRef.close(null);
  }
}
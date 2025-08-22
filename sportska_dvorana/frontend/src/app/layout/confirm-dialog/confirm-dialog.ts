import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';

export interface ConfirmDialogData {
  title: string;
  message: string;
  confirmText?: string;
  cancelText?: string;
  infoOnly?: boolean; 
}

@Component({
  selector: 'app-confirm-dialog',
  standalone: true,
  imports: [MatDialogModule, MatButtonModule, CommonModule],
  template: `
  <div class="dialogWrapper">
    <h2 mat-dialog-title>{{ data.title }}</h2>
    <mat-dialog-content>{{ data.message }}</mat-dialog-content>
    <mat-dialog-actions align="center">
      <button *ngIf="!data.infoOnly"
              mat-raised-button
              class="confirm-btn"
              [mat-dialog-close]="true">
        {{ data.confirmText || 'Potvrdi' }}
      </button>
      <button mat-raised-button [mat-dialog-close]="false" [ngClass]="data.infoOnly ? 'confirm-btn' : ''">
        {{ data.cancelText || (data.infoOnly ? 'Ok' : 'Otkaži') }}
      </button>
    </mat-dialog-actions>
  </div>
  `,
  styles: [`
    @use '@angular/material' as mat;

    .dialogWrapper {
      text-align: center;
    }


    h2[mat-dialog-title] {
      font-weight: 600;
      margin-bottom: 1rem;
    }

    mat-dialog-content {
      font-size: 1rem;
      margin-bottom: 1.5rem;
    }

    mat-dialog-actions button {
      min-width: 90px;
    }

    .confirm-btn {
      background-color: #00696a !important;
      color: white !important;
    }

    .confirm-btn:hover {
      background-color: #004b4c;
    }
  `]
})
export class ConfirmDialog {
  constructor(
    public dialogRef: MatDialogRef<ConfirmDialog>,
    @Inject(MAT_DIALOG_DATA) public data: ConfirmDialogData
  ) {}
}
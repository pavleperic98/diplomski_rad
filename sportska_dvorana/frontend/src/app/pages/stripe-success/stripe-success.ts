import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-stripe-success',
  standalone: true,
  imports: [CommonModule, MatButtonModule, MatCardModule, MatIconModule],
  template: `
    <div class="success-wrapper">
      <mat-card class="success-card">
        <mat-icon class="success-icon">check_circle</mat-icon>
        <h2>Plaćanje uspešno!</h2>
        <p>Vaša rezervacija je sada plaćena.</p>
        <button mat-raised-button class="orangeBtn" (click)="goToReservations()">
          Pogledaj rezervacije
        </button>
      </mat-card>
    </div>
  `,
  styles: [`
    .success-wrapper {
      display: flex;
      justify-content: center;
      align-items: center;
      min-height: 100vh;
      background-color: var(--mat-sys-on-primary);
    }

    .success-card {
      display: flex;
      flex-direction: column;
      align-items: center;
      text-align: center;
      padding: 3rem 2rem;
      margin-bottom: 8rem;
      border-radius: 12px;
      background-color: var(--mat-sys-primary);
      color: white;
      box-shadow: 0 8px 16px rgba(0,0,0,0.2);
      max-width: 400px;
      width: 100%;
    }

    .success-icon {
      color: #bbff00;
      margin-bottom: 1rem;
      height: 50px !important;
      width: 50px  !important;
      font-size: 48px;
      
    }

    h2 {
      margin: 1rem 0 0.5rem 0;
      font-size: 2rem;
    }

    p {
      margin-bottom: 2rem;
      font-size: 1.2rem;
      color: #ddd;
    }

    .orangeBtn {
      background-color: #ff8c00;
      color: white;
      width: 100%;
      font-weight: 500;
    }

    .orangeBtn:hover {
      background-color: #d87802;
    }

    @media (max-width: 768px) {
      .success-card {
        padding: 2rem 1.5rem;
      }
      h2 { font-size: 1.8rem; }
      p { font-size: 1rem; }
      .success-icon { font-size: 3.5rem; }
    }
  `]
})
export class StripeSuccess implements OnInit {
  reservationId: number | null = null;

  constructor(private route: ActivatedRoute) {}

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.reservationId = params['reservationId'] ? +params['reservationId'] : null;
      if (this.reservationId) console.log('Plaćanje završeno za rezervaciju:', this.reservationId);
    });
  }

  goToReservations() {
    window.location.href = '/reservations';
  }
}
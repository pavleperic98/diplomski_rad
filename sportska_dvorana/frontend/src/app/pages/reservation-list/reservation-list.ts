import { CommonModule, NgClass, NgFor } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog } from '@angular/material/dialog';
import { HttpClient } from '@angular/common/http';
import { ConfirmDialog } from '../../layout/confirm-dialog/confirm-dialog';
import { AuthService } from '../../auth/auth.service';
import { environment } from '../../../env';
import { StripeService } from '../stripe-success/stripe.service'; 
import { ActivatedRoute } from '@angular/router';

interface Reservation {
  reservationId: number;
  hallId: number;
  hallName: string;
  sportId: number;
  sportName: string;
  userId: number;
  userFullName: string;
  statusName: string;
  paymentStatus: string | null;
  date: string;
  timeFrom: string;
  timeTo: string;
  finalPrice: number;
}

@Component({
  selector: 'app-reservation-list',
  standalone: true,
  imports: [
    CommonModule,
    NgFor,
    NgClass,
    MatButtonModule,
    MatCardModule,
    MatIconModule,
  ],
  templateUrl: './reservation-list.html',
  styleUrls: ['./reservation-list.scss']
})
export class ReservationList implements OnInit {
  reservations: Reservation[] = [];
  backendURL = environment.apiUrl;
  selectedReservation: Reservation | null = null;

  constructor(
    private http: HttpClient,
    private dialog: MatDialog,
    private authService: AuthService,
    private stripeService: StripeService,
    private route: ActivatedRoute,
  ) {}

  ngOnInit() {
    this.checkPaymentResult();
    this.loadUserReservations();
  }

  private loadUserReservations() {
    this.authService.getCurrentUser().subscribe({
      next: user => this.loadReservations(user.userId),
      error: err => console.error('Greška pri dohvatanju korisnika:', err)
    });
  }

  private checkPaymentResult() {
    this.route.queryParams.subscribe(params => {
      if (params['success'] === 'true') {
        this.dialog.open(ConfirmDialog, {
          data: {
            title: 'Plaćanje uspešno',
            message: 'Vaša rezervacija je uspešno plaćena!',
            infoOnly: true
          }
        }).afterClosed().subscribe(() => this.loadUserReservations());
      } else if (params['canceled'] === 'true') {
        this.dialog.open(ConfirmDialog, {
          data: {
            title: 'Plaćanje otkazano',
            message: 'Transakcija nije izvršena.',
            infoOnly: true
          }
        });
      }
    });
  }

  private loadReservations(userId: number) {
    this.http.get<Reservation[]>(`${this.backendURL}/reservation/user/${userId}`)
      .subscribe({
        next: data => this.reservations = data,
        error: err => console.error('Greška pri učitavanju rezervacija:', err)
      });
  }

  statusClass(status: string): string {
    switch (status.toLowerCase()) {
      case 'kreirana': return 'kreirana';
      case 'placena': return 'placena';
      case 'zavrsena': return 'zavrsena';
      case 'otkazana': return 'otkazana';
      default: return '';
    }
  }

  async goToCheckout(reservation: Reservation) {
    try {
      await this.stripeService.createCheckout(reservation.reservationId, reservation.finalPrice);
    } catch (err) {
      console.error('Greška pri plaćanju:', err);
      this.dialog.open(ConfirmDialog, {
        data: {
          title: 'Greška',
          message: 'Došlo je do greške pri pokretanju plaćanja.',
          infoOnly: true
        }
      });
    }
  }

  cancelReservation(reservationId: number) {
    const dialogRef = this.dialog.open(ConfirmDialog, {
      width: '400px',
      data: {
        title: 'Potvrda otkazivanja',
        message: 'Da li ste sigurni da želite da otkažete ovu rezervaciju?',
        cancelText: 'Nazad',
        confirmText: 'Otkaži',
        infoOnly: false
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.http.put(`${this.backendURL}/reservation/${reservationId}/status`, { statusId: 4 })
          .subscribe({
            next: () => {
              const reservation = this.reservations.find(r => r.reservationId === reservationId);
              if (reservation) reservation.statusName = 'otkazana';

              this.dialog.open(ConfirmDialog, {
                data: {
                  title: 'Rezervacija otkazana',
                  message: 'Uspešno ste otkazali rezervaciju.',
                  infoOnly: true
                }
              });
            },
            error: err => {
              console.error('Greška pri otkazivanju rezervacije:', err);
              this.dialog.open(ConfirmDialog, {
                data: {
                  title: 'Greška',
                  message: 'Došlo je do greške pri otkazivanju rezervacije.',
                  infoOnly: true
                }
              });
            }
          });
      }
    });
  }
}
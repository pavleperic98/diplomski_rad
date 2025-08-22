import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { FormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { HttpClient } from '@angular/common/http';
import { formatDate } from '@angular/common';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSelectModule } from '@angular/material/select';

interface User {
  userId: number;
  firstName: string;
  lastName: string;
  email?: string;
}

interface Hall {
  hallId: number;
  name: string;
  description: string;
  capacity: number;
  pricePerHour: number;
  sports: { sportId: number; sport: string }[];
  sportIcons: string[];      
  images: string[];     
}

interface Reservation {
  reservationId: number;
  user: {
    firstName: string,
    lastName: string
  }
  hall: {
    hallId: number;
    name: string;
  };
  status: {
    statusId: number;
    status: string;
  };
  date: string;
  timeFrom: string;
  timeTo: string;
}

@Component({
  standalone: true,
  selector: 'app-hall-details',
  imports: [
    CommonModule,
    RouterModule,
    MatButtonModule,
    MatCardModule,
    MatIconModule,
    MatDividerModule,
    MatDatepickerModule,
    MatNativeDateModule,
    FormsModule,
    MatInputModule,
    MatFormFieldModule,
    MatProgressSpinnerModule,
    MatSelectModule,
  ],
  templateUrl: './hall-details.html',
  styleUrls: ['./hall-details.scss']
})
export class HallDetails implements OnInit {
  hallId!: number;
  hall: Hall | null = null;
  selectedDate: Date = new Date();
  selectedSportId: number | null = null;
  sports: {sportId: number, sport: string}[] = [];
  reservations: Reservation[] = [];
  isLoading = false;
  errorMessage = '';
  successMessage = '';

  selectedStartTime: string | null = null;  
  selectedDuration: number = 1;             
  workingHoursStart = 8;
  workingHoursEnd = 22;
  currentUser: User | null = null;

  preloadedSportIcons: string[] = [];
  preloadedImages: string[] = [];

  // Carousel
  currentImageIndex = 0;

  constructor(private route: ActivatedRoute, private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (!id || isNaN(Number(id))) {
      this.errorMessage = 'Invalid hall ID';
      return;
    }
    this.hallId = Number(id);

    // Dohvati prosleđene ikonice i slike ako postoje
    const state = history.state as { sportIcons?: string[]; images?: string[] };
    this.preloadedSportIcons = state.sportIcons || [];
    this.preloadedImages = state.images || [];

    console.log(this.preloadedImages);

    this.loadCurrentUser();
  }

  loadCurrentUser(): void {
    this.http.get<User>('http://localhost:8080/api/auth/me').subscribe({
      next: (user) => {
        this.currentUser = user;
        this.loadHallDetails();
        this.loadReservations();
      },
      error: (err) => {
        console.error('Neuspešno učitavanje korisnika:', err);
        this.errorMessage = 'Neuspešno učitavanje podataka o korisniku. Molimo ulogujte se.';
      }
    });
  }

  loadHallDetails(): void {
    this.http.get<Hall>(`http://localhost:8080/api/hall/${this.hallId}`).subscribe({
      next: (data) => {
        this.hall = {
          ...data,
          sportIcons: this.preloadedSportIcons,
          images: this.preloadedImages,
        };
        this.sports = this.hall.sports;
        if (this.sports.length > 0) this.selectedSportId = this.sports[0].sportId;

        console.log()
      },
      error: (err) => {
        console.error('Error loading hall details:', err);
        this.errorMessage = 'Failed to load hall details';
      }
    });
  }

  loadReservations(): void {
    const formattedDate = formatDate(this.selectedDate, 'yyyy-MM-dd', 'en-US');
    this.isLoading = true;

    this.http.get<Reservation[]>(`http://localhost:8080/api/reservation/filter?hallId=${this.hallId}&date=${formattedDate}`)
      .subscribe({
        next: (data) => {
          this.reservations = data;
          this.isLoading = false;
        },
        error: (err) => {
          this.errorMessage = 'Neuspešno učitavanje rezervacija';
          this.isLoading = false;
        }
      });
  }

  dateChanged(): void {
    this.loadReservations();
  }

  getSelectedSportName(): string {
    const sport = this.sports.find(s => s.sportId === this.selectedSportId);
    return sport ? sport.sport : '';
  }

  getPossibleStartTimes(): string[] {
    const times: string[] = [];
    const maxStartHour = this.workingHoursEnd - this.selectedDuration;
    
    for (let hour = this.workingHoursStart; hour <= maxStartHour; hour++) {
        times.push(hour.toString().padStart(2, '0') + ':00');
    }
    return times;
  }

  isTimeSlotAvailable(startTime: string, duration: number): boolean {
    if (!this.reservations || this.reservations.length === 0) return true;

    const [startH, startM] = startTime.split(':').map(Number);
    const startMinutes = startH * 60 + startM;
    const endMinutes = startMinutes + duration * 60;

    if (startH < this.workingHoursStart || (startH + duration) > this.workingHoursEnd) return false;

    for (const res of this.reservations) {
      if (res.status.statusId !== 1) continue;
      const [resStartH, resStartM] = res.timeFrom.split(':').map(Number);
      const [resEndH, resEndM] = res.timeTo.split(':').map(Number);
      const resStart = resStartH * 60 + resStartM;
      const resEnd = resEndH * 60 + resEndM;

      if (!(endMinutes <= resStart || startMinutes >= resEnd)) return false;
    }
    return true;
  }

  selectStartTime(time: string | null): void {
    if (!time) return;

    if (this.isTimeSlotAvailable(time, this.selectedDuration)) {
      this.selectedStartTime = time;
      this.errorMessage = '';
      this.successMessage = '';
    } else {
      this.errorMessage = 'Izabrani termin nije slobodan za ovaj vremenski interval.';
      this.selectedStartTime = null;
      this.successMessage = '';
    }
  }

  getCalculatedEndTime(): string {
    if (!this.selectedStartTime) return '';
    const [h, m] = this.selectedStartTime.split(':').map(Number);
    const endH = h + this.selectedDuration;
    return `${endH.toString().padStart(2, '0')}:${m.toString().padStart(2, '0')}`;
  }

  confirmReservation(): void {
    if (!this.selectedStartTime || !this.selectedSportId) {
      this.errorMessage = 'Molimo izaberite vreme i sport';
      return;
    }

    if (!this.currentUser) {
      this.errorMessage = 'Niste ulogovani. Molimo prijavite se.';
      return;
    }

    const payload = {
      hallId: this.hallId,
      sportId: this.selectedSportId,
      userId: this.currentUser.userId,
      paymenId: null,
      date: formatDate(this.selectedDate, 'yyyy-MM-dd', 'en-US'),
      timeFrom: this.selectedStartTime + ':00',
      timeTo: this.getCalculatedEndTime() + ':00',
      statusId: 1,
      finalPrice: this.calculateTotalPrice()
    };

    console.log('Reservation payload:', payload);

    this.isLoading = true;
    this.http.post('http://localhost:8080/api/reservation', payload).subscribe({
      next: () => {
        this.successMessage = 'Rezervacija uspešno kreirana!';
        this.errorMessage = '';
        this.loadReservations();
        this.selectedStartTime = null;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Reservation error:', err);
        this.errorMessage = err.error?.message || 'Došlo je do greške pri kreiranju rezervacije.';
        this.successMessage = '';
        this.isLoading = false;
      }
    });
  }

  calculateTotalPrice(): number {
    if (!this.hall || !this.selectedDuration) return 0;
    return this.hall.pricePerHour * this.selectedDuration;
  }

  onDurationChange(): void {
    if (this.selectedStartTime && 
        !this.isTimeSlotAvailable(this.selectedStartTime, this.selectedDuration)) {
        this.selectedStartTime = null;
        this.errorMessage = 'Selected duration makes current time slot unavailable';
    }
  }

  getEndTime(startTime: string, duration: number): string {
    const [h, m] = startTime.split(':').map(Number);
    const endH = h + duration;
    return `${endH.toString().padStart(2, '0')}:${m.toString().padStart(2, '0')}`;
  }

  formatTime(timeString: string): string {
    return timeString.substring(0, 5); 
  }

  // CAROUSEL
  prevImage(): void {
    if (!this.hall?.images) return;
    this.currentImageIndex = (this.currentImageIndex - 1 + this.hall.images.length) % this.hall.images.length;
  }

  nextImage(): void {
    if (!this.hall?.images) return;
    this.currentImageIndex = (this.currentImageIndex + 1) % this.hall.images.length;
  }

  goBack(): void {
    this.router.navigate(['/halls']);
  }
}
import { Component } from '@angular/core';
import { AdminSection } from '../admin.section.model'; 
import { AdminTable } from '../admin-table/admin-table';
import { CommonModule, NgFor } from '@angular/common';
import { MatSidenavContainer, MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { AuthService } from '../../auth/auth.service';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.scss'],
  standalone: true,
  imports: [CommonModule, MatSidenavModule, MatListModule, MatButtonModule, MatIconModule, NgFor, AdminTable, MatSidenavContainer]
})
export class Dashboard {
  sections: AdminSection[] = [

    {
  label: 'Users',
  icon: 'people',
  apiEndpoint: 'user',
  columns: [
    { key: 'userId', label: 'ID'},
    { key: 'firstName', label: 'Ime' },
    { key: 'lastName', label: 'Prezime' },
    { key: 'username', label: 'Korisničko ime' },
    { key: 'email', label: 'Email' },
    { key: 'phoneNumber', label: 'Telefon' },
    { key: 'birthDate', label: 'Datum rođenja' },
    { key: 'roleId', label: 'Uloga' }
  ]
},
      {
      label: 'Statuses',
      icon: 'check_circle',
      apiEndpoint: 'status',
      columns: [
        { key: 'statusId', label: 'ID'},
        { key: 'status', label: 'Naziv' }
      ]
    },
    {
      label: 'Sports',
      icon: 'sports_soccer',
      apiEndpoint: 'sport',
      columns: [
        { key: 'sportId', label: 'ID'},
        { key: 'sport', label: 'Naziv'},
        { key: 'halls', label: 'Sala'}
      ]
    },
    {
      label: 'Reservations',
      icon: 'bookmark',
      apiEndpoint: 'reservation',
      columns: [
        { key: 'reservationId', label: 'ID'},
        { key: 'hallName', label: 'Sala' },
        { key: 'sportName', label: 'Sport' },
        { key: 'userFullName', label: 'Korisnik' },
        { key: 'statusName', label: 'Status' },
        { key: 'dateTime', label: 'Termin' }, 
        { key: 'finalPrice', label: 'Cena' }
      ]
    },
    {
      label: 'Halls',
      icon: 'house',
      apiEndpoint: 'hall',
      columns: [
        { key: 'hallId', label: 'ID'},
        { key: 'name', label: 'Naziv'},
        { key: 'capacity', label: 'Kapacitet'},
        { key: 'pricePerHour', label: 'Cena po satu'},
        { key: 'sports', label: 'Sportovi'}
      ]
    },
    {
      label: 'Payments',
      icon: 'payment',
      apiEndpoint: 'payment',
      columns: [
        { key: 'paymentId', label: 'ID'},
        { key: 'stripeId', label: 'Stripe ID' },
        { key: 'amount', label: 'Iznos' },
        { key: 'currency', label: 'Valuta' },
        { key: 'paymentStatus', label: 'Status plaćanja' },
        { key: 'reservationId', label: 'Rezervacija' }
      ]
    }
  ];

  selectedSection: AdminSection | null = null;
  currentUser: any;

  constructor(private authService: AuthService) {}

  ngOnInit() {
    this.authService.getCurrentUser().subscribe({
      next: user => this.currentUser = user,
      error: err => console.error(err)
    });
  }

  selectSection(section: AdminSection) {
    this.selectedSection = section;
  }

  logout() {
    this.authService.logout();
  }
}
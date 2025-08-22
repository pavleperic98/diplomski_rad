import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../env';

export interface Reservation {
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

@Injectable({
  providedIn: 'root'
})
export class ReservationService {
  private baseUrl = `${environment.apiUrl}/reservation`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<Reservation[]> {
    return this.http.get<Reservation[]>(this.baseUrl);
  }

  getById(id: number): Observable<Reservation> {
    return this.http.get<Reservation>(`${this.baseUrl}/${id}`);
  }

  create(reservation: Partial<Reservation>): Observable<Reservation> {
    return this.http.post<Reservation>(this.baseUrl, reservation);
  }

  update(id: number, reservation: Partial<Reservation>): Observable<Reservation> {
    return this.http.put<Reservation>(`${this.baseUrl}/${id}`, reservation);
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`);
  }
}
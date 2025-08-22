import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../env';

export interface HallSport {
  sportId: number;
  sport: string;
}

export interface Hall {
  hallId: number;
  name: string;
  capacity: number;
  description: string;
  pricePerHour: number;
  sports: HallSport[];
}

@Injectable({
  providedIn: 'root'
})
export class HallService {
  private baseUrl = `${environment.apiUrl}/hall`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<Hall[]> {
    return this.http.get<Hall[]>(this.baseUrl);
  }

  getById(id: number): Observable<Hall> {
    return this.http.get<Hall>(`${this.baseUrl}/${id}`);
  }

  create(hall: Partial<Hall>): Observable<Hall> {
    return this.http.post<Hall>(this.baseUrl, hall);
  }

  update(id: number, hall: Partial<Hall>): Observable<Hall> {
    return this.http.put<Hall>(`${this.baseUrl}/${id}`, hall);
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`);
  }
}
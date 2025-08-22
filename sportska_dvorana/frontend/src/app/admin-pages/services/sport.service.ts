import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../env';
import { Observable } from 'rxjs';

export interface Sport {
  sportId: number;
  sport: string;
  hallIds: number[];
}

@Injectable({
  providedIn: 'root',
})
export class SportService {
  private apiUrl = `${environment.apiUrl}/sport`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<Sport[]> {
    return this.http.get<Sport[]>(this.apiUrl);
  }

  getById(id: number): Observable<Sport> {
    return this.http.get<Sport>(`${this.apiUrl}/${id}`);
  }

  create(data: Partial<Sport>): Observable<Sport> {
    return this.http.post<Sport>(this.apiUrl, data);
  }

  update(id: number, data: Partial<Sport>): Observable<Sport> {
    return this.http.put<Sport>(`${this.apiUrl}/${id}`, data);
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }
}
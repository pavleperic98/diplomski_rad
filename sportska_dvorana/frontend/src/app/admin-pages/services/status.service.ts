import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../env';

export interface Status {
  id: number;
  name: string;
  description?: string;
}

@Injectable({
  providedIn: 'root'
})
export class StatusService {
  private baseUrl = `${environment.apiUrl}/status`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<Status[]> {
    return this.http.get<Status[]>(this.baseUrl);
  }

  getById(id: number): Observable<Status> {
    return this.http.get<Status>(`${this.baseUrl}/${id}`);
  }

  create(status: Status): Observable<any> {
    return this.http.post(this.baseUrl, status);
  }

  update(id: number, status: Status): Observable<any> {
    return this.http.put(`${this.baseUrl}/${id}`, status);
  }

  delete(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`);
  }
}
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { environment } from '../../../env';

@Injectable({
  providedIn: 'root',
})
export class StripeService {
  backendURL = environment.apiUrl + '/stripe'; 

  constructor(private http: HttpClient) {}

  async createCheckout(reservationId: number, amount: number): Promise<void> {
    try {
      const params = new HttpParams()
        .set('reservationId', reservationId.toString())
        .set('amount', amount.toString());

      const url = await firstValueFrom(
        this.http.post(this.backendURL + '/checkout', null, {
          params,
          responseType: 'text',
        })
      );

      window.location.href = url; 
    } catch (err) {
      console.error('Greška prilikom kreiranja checkout-a:', err);
      throw err;
    }
  }
}
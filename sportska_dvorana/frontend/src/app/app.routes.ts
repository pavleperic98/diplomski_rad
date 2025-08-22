import { Routes } from '@angular/router';
import { Welcome } from './pages/welcome/welcome';
import { HallList } from './pages/hall-list/hall-list';
import { HallDetails } from './pages/hall-details/hall-details';
import { ReservationList } from './pages/reservation-list/reservation-list';
import { StripeSuccess } from './pages/stripe-success/stripe-success';
import { Dashboard } from './admin-pages/dashboard/dashboard';

export const routes: Routes = [
  { 
    path: 'auth', 
    loadChildren: () => import('./auth/auth.routes').then((m) => m.AUTH_ROUTES)
  },
  {
    path: 'admin',
    loadChildren: () => import('./admin-pages/admin.routes').then(m => m.ADMIN_ROUTES),
  },
  { path: 'welcome', component: Welcome },
  { path: 'halls', component: HallList },
  { path: 'halls/:id', component: HallDetails },
  { path: 'reservations', component: ReservationList },
  { path: 'payment-success', component: StripeSuccess },

  { path: '', redirectTo: '/welcome', pathMatch: 'full' }
];
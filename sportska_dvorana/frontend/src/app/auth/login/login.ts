import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { AuthService } from '../auth.service';
import { MatIconModule } from '@angular/material/icon';
import { switchMap } from 'rxjs';

@Component({
  standalone: true,
  selector: 'app-login',
  templateUrl: './login.html',
  styleUrls: ['./login.scss'],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    MatInputModule,
    MatButtonModule,
    MatCardModule,
    MatIconModule
  ],
})
export class Login {
  loginForm: FormGroup;
  errorMessage: string | null = null; 

  constructor(private fb: FormBuilder, private auth: AuthService, private router: Router) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
    });
  }

  onSubmit(): void {
    this.loginForm.markAllAsTouched();

    if (this.loginForm.invalid) {
      this.errorMessage = 'Molimo popunite sva obavezna polja.';
      return;
    }

    this.errorMessage = null;


    this.auth.login(this.loginForm.value).pipe(
      switchMap(res => {
        this.auth.setToken(res.token);
        return this.auth.getCurrentUser(); 
      })
    ).subscribe({
      next: (user) => {

      
        if (user.roleId === 1) {
          this.router.navigate(['/admin']);
        } else {
          this.router.navigate(['/welcome']);
        }
      },
      error: (err) => {
        this.errorMessage = 'Login neuspešan. Pokušajte ponovo.'; 
        console.error(err);
      }
    });


  }
}
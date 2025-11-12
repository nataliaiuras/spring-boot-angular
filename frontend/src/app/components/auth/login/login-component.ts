import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {FormsModule} from '@angular/forms';
import {AuthService} from '../../../services/auth/auth-service';
import {NgIf} from '@angular/common';
import {catchError, Observable, of} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {MatCardModule} from '@angular/material/card';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';


interface LoginResponse {
  token: string;
}

@Component({
  selector: 'app-login',
  imports: [
    FormsModule,
    NgIf,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule
  ],
  templateUrl: './login-component.html',
  styleUrl: './login-component.css',
  standalone: true
})
export class LoginComponent implements OnInit{

  credentials = {
    username: '',
    password: ''
  };
  message = '';

  constructor(
    private readonly authService: AuthService,
    private readonly router: Router,
    private readonly route: ActivatedRoute,
  ) { }

  ngOnInit() {
    localStorage.removeItem('token');
  }

  // login(): void {
  //   this.authService.login(this.credentials).subscribe({
  //     next: (response) => {
  //       // const returnUrl = this.route.snapshot.queryParams['returnUrl'] || 'api/banks';
  //       // this.router.navigate([returnUrl]);
  //       this.router.navigate(['/api/vehicles'])
  //     },
  //     error: (error) => {
  //       console.error('LoginComponent error:', error);
  //       this.message = 'Invalid username or password';
  //     }
  //   });
  // }


/*  login() {
    this.authService.login(this.credentials).subscribe({
      next: (response) => {
        if (response.token) {
          sessionStorage.setItem('token', response.token);
          this.authService.setToken(response.token);
        }
        this.router.navigate(['/api/banks'])
      },
      error: (error) => {
        this.message = 'LoginComponent failed';
      }
    });
  }*/

  login() {
    this.authService.login(this.credentials).subscribe({
      next: (response) => {
        console.log('LoginComponent response:', response); // Debug log

        // Access the nested token: response.data.token instead of response.token
        if (response.data && response.data.token) {
          console.log('Token received:', response.data.token);
          sessionStorage.setItem('token', response.data.token);
          this.authService.setToken(response.data.token);

          console.log('Token stored:', sessionStorage.getItem('token'));
        } else {
          console.log('No token found in response data!');
        }
        this.router.navigate(['/api/dashboard'])
      },
      error: (error) => {
        console.error('LoginComponent error:', error);
        this.message = 'LoginComponent failed';
      }
    });
  }








}

import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {FormsModule} from '@angular/forms';
import {AuthService} from '../../services/auth/auth';
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
  templateUrl: './login.html',
  styleUrl: './login.css',
  standalone: true
})
export class Login implements OnInit{

/*  credentials = { username: '', password: '' };
  message = '';

  constructor(private authService: AuthService, private router: Router) { }

  login() {
    this.authService.login(this.credentials)
      .subscribe(response => {
      localStorage.setItem('username', response);
      this.router.navigate(['']);
    }, error => {
      console.error('Login error: ', error);
      this.message = 'Invalid username or password';
    });
  }*/

  credentials = {
    username: '',
    password: ''
  };
  message = '';

  constructor(
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute,
    private http: HttpClient
  ) { }

  ngOnInit() {
    localStorage.removeItem('token');
  }

  login(): void {
    this.authService.login(this.credentials).subscribe({
      next: (response) => {
        // Get return url from route parameters or default to '/'
        const returnUrl = this.route.snapshot.queryParams['returnUrl'] || 'api/vehicles';
        this.router.navigate([returnUrl]);
      },
      error: (error) => {
        console.error('Login error:', error);
        this.message = 'Invalid username or password';
      }
    });
  }


  /*login() {
    const url = 'http://localhost:8080/auth/login';

    this.http.post<LoginResponse>(url, {
      username: this.credentials.username,
      password: this.credentials.password
    })
      .pipe(
        catchError(error => {
          console.error('Login failed:', error);
          this.message = 'Invalid username or password';
          return of(null);
        })
      )
      .subscribe(response => {
        if (response?.token) {
          sessionStorage.setItem('token', response.token);
          this.router.navigate(['']);
        } else {
          this.message = 'Invalid username or password';
        }
      });
  }*/
}

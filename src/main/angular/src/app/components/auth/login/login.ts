import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {FormsModule} from '@angular/forms';
import {AuthService} from '../../../services/auth/auth';
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

  login(): void {
    this.authService.login(this.credentials).subscribe({
      next: (response) => {
        const returnUrl = this.route.snapshot.queryParams['returnUrl'] || 'api/posts';
        this.router.navigate([returnUrl]);
      },
      error: (error) => {
        console.error('Login error:', error);
        this.message = 'Invalid username or password';
      }
    });
  }

}

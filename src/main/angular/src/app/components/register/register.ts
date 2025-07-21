import {Component, Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {AuthService} from '../../services/auth/auth';
import {FormsModule} from '@angular/forms';
import {NgIf} from '@angular/common';
import {MatCardModule} from '@angular/material/card';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';

@Component({
  selector: 'app-register',
  templateUrl: './register.html',
  imports: [
    FormsModule,
    NgIf,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule
  ],
  styleUrls: ['./register.css'],
  standalone: true
})
export class Register {

  user = { username: '', password: '', email: '' };
  message = '';

  constructor(private authService: AuthService, private router: Router) { }

  register() {
    this.authService.register(this.user).subscribe(() => {
      this.message = 'Registration successful';
      this.router.navigate(['/login']);
    }, error => {
      console.error('Registration error: ', error);
      this.message = 'Registration failed';
    });
  }
}

import {Component, OnInit} from '@angular/core';
import {UserService} from '../../../services/user/user-service';
import {DatePipe, NgIf} from '@angular/common';

@Component({
  selector: 'app-profile',
  imports: [
    NgIf,
    DatePipe
  ],
  templateUrl: './profile-component.html',
  styleUrl: './profile-component.css'
})
export class ProfileComponent implements OnInit {
  currentUser: User | null = null;
  loading = true;

  constructor(private readonly userService: UserService) {}

  ngOnInit() {
    console.log('ProfileComponent component initialized');

    // Load current user
    this.userService.getCurrentUser();

    // Subscribe to user changes
    this.userService.currentUser$.subscribe({
      next: (user) => {
        console.log('User data received in profile:', user);
        this.currentUser = user;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error in profile component:', error);
        this.loading = false;
      }
    });
  }
}


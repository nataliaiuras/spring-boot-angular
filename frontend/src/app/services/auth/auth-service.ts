import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject, map, Observable} from 'rxjs';
import {Router} from '@angular/router';

export interface UserProfile {
  username: string;
  email: string;
  role: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly baseUrl = 'http://localhost:8080';
  isAuthenticatedSubject = new BehaviorSubject<boolean>(false);
  private readonly userProfileSubject = new BehaviorSubject<UserProfile | null>(null);
  userProfile$ = this.userProfileSubject.asObservable();

  constructor(
    private readonly http: HttpClient,
    private readonly router: Router
  ) {
    this.isAuthenticatedSubject.next(!!sessionStorage.getItem('token'));
    if (sessionStorage.getItem('token')) {
      console.log("Token found, fetching user profile", )
      this.getUserProfile();
    }
  }

  register(user: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/auth/register`, user);
  }


  login(credentials: { username: string; password: string }): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/auth/login`, credentials)
      .pipe(
        map(response => {
          if (response.token) {
            sessionStorage.setItem('token', response.token);
            this.isAuthenticatedSubject.next(true);
            this.getUserProfile();
          }
          return response;
        })
      );
  }

  isAuthenticated(): Observable<boolean> {
    return this.isAuthenticatedSubject.asObservable();
  }

  getToken(): string | null {
    const token = sessionStorage.getItem('token');
    if (!token || token === 'null' || token.trim() === '') {
      return null;
    }
    return token;
  }

  logout(): void {
    sessionStorage.removeItem('token');
    this.isAuthenticatedSubject.next(false);
    this.userProfileSubject.next(null);
    this.router.navigate(['/auth/login']);
  }

  getUserProfile(): void {
    const token = sessionStorage.getItem('token');
    if (token) {
      this.http.get<UserProfile>(`${this.baseUrl}/auth/profile`, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).subscribe({
        next: (profile) => {
          this.userProfileSubject.next(profile);
        },
        error: (error) => {
          console.error('Error fetching user profile:', error);
          if (error.status === 401) {
            this.logout();
          }
        }
      });
    }
  }
  setToken(token: string): void {
    sessionStorage.setItem('token', token);
  }

  removeToken(): void {
    sessionStorage.removeItem('token');
  }

  isLoggedIn(): boolean {
    return this.getToken() !== null;
  }

}

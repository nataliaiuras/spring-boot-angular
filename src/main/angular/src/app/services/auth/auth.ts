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

  private baseUrl = 'http://localhost:8080';
  isAuthenticatedSubject = new BehaviorSubject<boolean>(false);
  private userProfileSubject = new BehaviorSubject<UserProfile | null>(null);
  userProfile$ = this.userProfileSubject.asObservable();

  constructor(private http: HttpClient,
              private router: Router
  ) {
    this.isAuthenticatedSubject.next(!!sessionStorage.getItem('token'));
    if (sessionStorage.getItem('token')) {
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
            // Fetch user profile after successful login
            this.getUserProfile();
          }
          return response;
        })
      );
  }


  /*  login(credentials: {username: string, password: string}): Observable<any> {
      return this.http.post(`${this.baseUrl}/auth/login`, {
        username: credentials.username,
        password: credentials.password
      });
    }*/

  /* login(credentials: any): Observable<any> {
     return this.http.post(`${this.baseUrl}/login`, credentials, { responseType: 'text' });
   }*/

  isAuthenticated(): Observable<boolean> {
    return this.isAuthenticatedSubject.asObservable();
  }

  getToken(): string | null {
    return sessionStorage.getItem('token');
  }

/*  logout(): Observable<any> {
    return this.http.post(`${this.baseUrl}/auth/logout`, {});
  }*/

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
          // If there's an error (e.g., token expired), log out the user
          if (error.status === 401) {
            this.logout();
          }
        }
      });
    }
  }
}

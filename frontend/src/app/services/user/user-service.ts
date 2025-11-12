import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {Page} from '../../core/models/response/general/page';
import {ApiResponse} from '../../core/models/response/general/api-response';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private readonly baseUrl = 'http://localhost:8080/auth';
  private readonly currentUserSubject = new BehaviorSubject<User | null>(null);

  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(private readonly http: HttpClient) {
  }

  getCurrentUser(): void {
    const token = sessionStorage.getItem('token');

    // First, get current user info from token or a 'me' endpoint
    this.http.get<ApiResponse<User>>(`${this.baseUrl}/profile`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    }).subscribe({
      next: (response) => {
        if (response.success && response.data) {
          console.log('Current user loaded:', response.data);
          this.currentUserSubject.next(response.data);
        }
      },
      error: (error) => {
        console.error('Error loading current user:', error);
        this.currentUserSubject.next(null);
      }
    });
  }

  getUserById(id: number): Observable<ApiResponse<Page<User>>> {
    const token = sessionStorage.getItem('token');
    return this.http.get<ApiResponse<Page<User>>>(`${this.baseUrl}/users/${id}`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });
  }

  getAllUsers(): Observable<ApiResponse<Page<User>>> {
    const token = sessionStorage.getItem('token');
    return this.http.get<ApiResponse<Page<User>>>(`${this.baseUrl}/users`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });
  }
}

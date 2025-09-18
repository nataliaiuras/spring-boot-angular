import {Injectable} from '@angular/core';
import {BehaviorSubject, finalize, Observable, tap} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {Institute} from '../../models/institute';

@Injectable({
  providedIn: 'root'
})
export class InstituteService {
  private readonly baseUrl = 'http://localhost:8080/api/institute';
  private readonly instituteSubject = new BehaviorSubject<Institute[]>([]);
  institute$ = this.instituteSubject.asObservable();

  constructor(private http: HttpClient) {
  }

  getInstitutes(): void {
    const token = sessionStorage.getItem('token');
    this.http.get<ApiResponse<PageResponse<Institute>>>(this.baseUrl, {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    }).subscribe(
      {
        next: (response) => {
          console.log('Institutes API response:', response); // Debug log

          if (response.success && response.data && response.data.content) {
            console.log('Institutes data:', response.data.content); // Debug log
            this.instituteSubject.next(response.data.content);
          } else {
            console.error('Invalid response structure:', response);
            this.instituteSubject.next([]);
          }
        },
        error: (error) => {
          console.error('Error fetching institutes:', error);
          this.instituteSubject.next([]);
        }
      }
    );
  }

  getInstitute(id: number): Observable<ApiResponse<Institute>> {
    const token = sessionStorage.getItem('token');
    return this.http.get<ApiResponse<Institute>>(`${this.baseUrl}/${id}/details`, {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    });
  }

  addInstitute(institute: Institute): void {
    const token = sessionStorage.getItem('token');
    const {id, ...instituteWithoutId} = institute;
    this.http.post<Institute>(this.baseUrl, instituteWithoutId, {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    })
      .subscribe(
        (newInstitute) => {
          console.log('Institute added successfully:', instituteWithoutId);
          this.getInstitutes();
        },
        (error) => {
          console.error('Error adding institute:', error);
        }
      );
  }

  updateInstitute(institute: Institute): void {
    const token = sessionStorage.getItem('token');
    this.http.put(`${this.baseUrl}/${institute.id}`, institute, {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    })
      .pipe(
        tap(() => console.log('Institute updated on server.')),
        finalize(() => this.getInstitutes())
      ).subscribe(
      () => {
        console.log('Institute updated successful and list will be refreshed.');
      },
      error => {
        console.error('Error updating institute:', error);
      }
    );
  }

  deleteInstitute(id: number): void {
    const token = sessionStorage.getItem('token');
    this.http.delete(`${this.baseUrl}/${id}`, {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    })
      .pipe(
        tap(() => console.log('Institute deleted on server.')),
        finalize(() => this.getInstitutes())
      )
      .subscribe(
        () => {
          console.log('Institute deletion successful and list will be refreshed.');
        },
        error => {
          console.error('Error deleting institute:', error);
        }
      );
  }
}

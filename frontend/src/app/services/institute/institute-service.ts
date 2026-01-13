import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable, tap} from 'rxjs';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {InstituteResponse} from '../../core/models/response/institute/institute.response';
import {InstituteCreateRequest} from '../../core/models/request/institute/institute-create-request';
import {InstituteDetailResponse} from '../../core/models/response/institute/institute-detail.response';
import {InstituteUpdateRequest} from '../../core/models/request/institute/institute-update-request';
import {BranchResponse} from '../../core/models/response/branch/branch-response';
import {Page} from '../../core/models/response/general/page';
import {ApiResponse} from '../../core/models/response/general/api-response';

@Injectable({
  providedIn: 'root'
})
export class InstituteService {
  private readonly baseUrl = 'http://localhost:8080/api/institute';
  private readonly instituteSubject = new BehaviorSubject<InstituteResponse[]>([]);
  institute$ = this.instituteSubject.asObservable();

  constructor(private readonly http: HttpClient) {
  }

  private getHeaders(): HttpHeaders {
    const token = sessionStorage.getItem('token');
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept': 'application/json',
      'Authorization': `Bearer ${token}`
    });
  }

  getAllInstitutes(page: number = 0, size: number = 10, sortBy: string = 'id', sortDir: string = 'asc'): Observable<ApiResponse<Page<InstituteResponse>>> {
    const params = {
      page: page.toString(),
      size: size.toString(),
      sortBy,
      sortDir
    };
    return this.http.get<ApiResponse<Page<InstituteResponse>>>(this.baseUrl, {
      params,
      headers: this.getHeaders()
    });
  }


  loadInstitutes(): void {
    this.getAllInstitutes().subscribe({
      next: (response) => {
        if (response.success && response.data?.content) {
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
    });
  }

  getInstitute(id: number): Observable<ApiResponse<InstituteResponse>> {
    return this.http.get<ApiResponse<InstituteResponse>>(`${this.baseUrl}/${id}`, {
      headers: this.getHeaders()
    });
  }

  getDetailedInstitute(id: number): Observable<ApiResponse<InstituteDetailResponse>> {
    return this.http.get<ApiResponse<InstituteDetailResponse>>(`${this.baseUrl}/${id}/details`, {
      headers: this.getHeaders()
    });
  }

  createInstitute(request: InstituteCreateRequest): Observable<ApiResponse<InstituteDetailResponse>> {
    return this.http.post<ApiResponse<InstituteDetailResponse>>(this.baseUrl, request, {
      headers: this.getHeaders()
    }).pipe(
      tap((response) => {
        if (response.success) {
          console.log('Institute created successfully');
          this.loadInstitutes();
        }
      })
    );
  }

  updateInstitute(id: number, request: InstituteUpdateRequest): Observable<ApiResponse<InstituteDetailResponse>> {
    return this.http.put<ApiResponse<InstituteDetailResponse>>(`${this.baseUrl}/${id}`, request, {
      headers: this.getHeaders()
    }).pipe(
      tap((response) => {
        if (response.success) {
          console.log('Institute updated successfully');
          this.loadInstitutes();
        }
      })
    );
  }

  patchInstitute(id: number, request: InstituteUpdateRequest): Observable<ApiResponse<InstituteDetailResponse>> {
    return this.http.patch<ApiResponse<InstituteDetailResponse>>(`${this.baseUrl}/${id}`, request, {
      headers: this.getHeaders()
    }).pipe(
      tap((response) => {
        if (response.success) {
          console.log('Institute patched successfully');
          this.loadInstitutes();
        }
      })
    );
  }

  deleteInstitute(id: number): Observable<ApiResponse<string>> {
    return this.http.delete<ApiResponse<string>>(`${this.baseUrl}/${id}`, {
      headers: this.getHeaders()
    }).pipe(
      tap((response) => {
        if (response.success) {
          console.log('Institute deleted successfully');
          this.loadInstitutes();
        } else {
          console.error('Error deleting institute:', response);
        }
      })
    );
  }

  getBranches(id: number, page: number = 0, size: number = 10, sortBy: string = 'id', sortDir: string = 'asc'): Observable<ApiResponse<Page<BranchResponse>>> {
    const params = {
      page: page.toString(),
      size: size.toString(),
      sortBy,
      sortDir
    };
    return this.http.get<ApiResponse<Page<BranchResponse>>>(`${this.baseUrl}/${id}/branches`, {
      params,
      headers: this.getHeaders()
    });
  }
}

import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable, tap} from 'rxjs';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {AccountResponse} from '../../core/models/response/account/account-response';
import {AccountCreateRequest} from '../../core/models/request/account/account-create-request';
import {AccountDetailResponse} from '../../core/models/response/account/account-detail-response';
import {AccountUpdateRequest} from '../../core/models/request/account/account-update-request';
import {Page} from '../../core/models/response/general/page';
import {ApiResponse} from '../../core/models/response/general/api-response';
import {BranchResponse} from '../../core/models/response/branch/branch-response';

@Injectable({
  providedIn: 'root'
})
export class AccountService {
  private readonly baseUrl = 'http://localhost:8080/api/accounts';
  private readonly accountSubject = new BehaviorSubject<AccountResponse[]>([]);
  accounts$ = this.accountSubject.asObservable();

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

  getAccounts(): void {
    const token = sessionStorage.getItem('token');
    this.http.get<AccountResponse[]>(this.baseUrl, {
      headers: this.getHeaders()
    }).subscribe(accounts => this.accountSubject.next(accounts));
  }

  getAllAccounts(page: number = 0, size: number = 10, sortBy: string = 'id', sortDir: string = 'asc'): Observable<ApiResponse<Page<AccountResponse>>> {
    const params = {
      page: page.toString(),
      size: size.toString(),
      sortBy,
      sortDir
    };
    return this.http.get<ApiResponse<Page<AccountResponse>>>(this.baseUrl, {
      params,
      headers: this.getHeaders()
    });
  }


  loadAccounts(): void {
    this.getAllAccounts().subscribe({
      next: (response) => {
        if (response.success && response.data?.content) {
          this.accountSubject.next(response.data.content);
        } else {
          console.error('Invalid response structure:', response);
          this.accountSubject.next([]);
        }
      },
      error: (error) => {
        console.error('Error fetching accounts:', error);
        this.accountSubject.next([]);
      }
    });
  }

  getAccount(id: number): Observable<ApiResponse<AccountResponse>> {
    return this.http.get<ApiResponse<AccountResponse>>(`${this.baseUrl}/${id}`, {
      headers: this.getHeaders()
    });
  }

  getDetailedAccount(id: number): Observable<ApiResponse<AccountDetailResponse>> {
    return this.http.get<ApiResponse<AccountDetailResponse>>(`${this.baseUrl}/${id}/details`, {
      headers: this.getHeaders()
    });
  }

  createAccount(request: AccountCreateRequest): Observable<ApiResponse<AccountDetailResponse>> {
    return this.http.post<ApiResponse<AccountDetailResponse>>(this.baseUrl, request, {
      headers: this.getHeaders()
    }).pipe(
      tap((response) => {
        if (response.success) {
          console.log('Account created successfully');
          this.loadAccounts();
        }
      })
    );
  }

  updateAccount(id: number, request: AccountUpdateRequest): Observable<ApiResponse<AccountDetailResponse>> {
    return this.http.put<ApiResponse<AccountDetailResponse>>(`${this.baseUrl}/${id}`, request, {
      headers: this.getHeaders()
    }).pipe(
      tap((response) => {
        if (response.success) {
          console.log('Account updated successfully');
          this.loadAccounts();
        }
      })
    );
  }

  patchAccount(id: number, request: AccountUpdateRequest): Observable<ApiResponse<AccountDetailResponse>> {
    return this.http.patch<ApiResponse<AccountDetailResponse>>(`${this.baseUrl}/${id}`, request, {
      headers: this.getHeaders()
    }).pipe(
      tap((response) => {
        if (response.success) {
          console.log('Account patched successfully');
          this.loadAccounts();
        }
      })
    );
  }

  deleteAccount(id: number): Observable<ApiResponse<string>> {
    return this.http.delete<ApiResponse<string>>(`${this.baseUrl}/${id}`, {
      headers: this.getHeaders()
    }).pipe(
      tap((response) => {
        if (response.success) {
          console.log('Account deleted successfully');
          this.loadAccounts();
        } else {
          console.error('Error deleting account:', response);
        }
      })
    );
  }

}

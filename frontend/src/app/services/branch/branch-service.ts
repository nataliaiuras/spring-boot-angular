import {Injectable} from '@angular/core';
import {BehaviorSubject, finalize, Observable, tap} from 'rxjs';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {BranchResponse} from '../../core/models/response/branch/branch-response';
import {CustomerResponse} from '../../core/models/response/customer/customer-response';
import {AddressResponse} from '../../core/models/response/address/address-response';

@Injectable({
  providedIn: 'root'
})
export class BranchService {
  private readonly baseUrl = 'http://localhost:8080/api/branches';
  private readonly branchesSubject = new BehaviorSubject<BranchResponse[]>([]);
  branches$ = this.branchesSubject.asObservable();

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

  getBranches(): void {
    const token = sessionStorage.getItem('token');
    this.http.get<BranchResponse[]>(this.baseUrl, {
      headers: this.getHeaders()
    }).subscribe(branches => this.branchesSubject.next(branches));
  }

  getBranch(id: number): Observable<BranchResponse> {
    const token = sessionStorage.getItem('token');
    return this.http.get<BranchResponse>(`${this.baseUrl}/${id}`, {
      headers: this.getHeaders()
    });
  }

  createBranch(branch: BranchResponse): void {
    const token = sessionStorage.getItem('token');
    const {id, ...branchWithoutId} = branch;
    this.http.post<BranchResponse>(this.baseUrl, branchWithoutId, {
      headers: this.getHeaders()
    })
      .subscribe(
        (newBranch) => {
          console.log('Branch added successfully:', branchWithoutId);
          this.getBranches();
        },
        (error) => {
          console.error('Error adding branch:', error);
        }
      );
  }

  updateBranch(branch: BranchResponse): void {
    const token = sessionStorage.getItem('token');
    this.http.put(`${this.baseUrl}/${branch.id}`, branch, {
      headers: this.getHeaders()
    })
      .pipe(
        tap(() => console.log('Branch updated on server.')),
        finalize(() => this.getBranches())
      ).subscribe({
      error(error) {
        console.error('Error updating branch:', error);
      },
      complete() {
        console.log('Branch updated successful and list will be refreshed.');
      },
    })
  }

  deleteBranch(id: number): void {
    const token = sessionStorage.getItem('token');
    this.http.delete(`${this.baseUrl}/${id}`, {
      headers: this.getHeaders()
    })
      .pipe(
        tap(() => console.log('Branch deleted on server.')),
        finalize(() => this.getBranches())
      )
      .subscribe(
        () => {
          console.log('Branch deletion successful and list will be refreshed.');
        },
        error => {
          console.error('Error deleting branch:', error);
        }
      );
  }

  getCustomers(id: number): Observable<CustomerResponse> {
    const token = sessionStorage.getItem('token');
    return this.http.get<CustomerResponse>(`${this.baseUrl}/${id}/customers`, {
      headers: this.getHeaders()
    });
  }


  getAddress(id: number): Observable<AddressResponse> {
    const token = sessionStorage.getItem('token');
    return this.http.get<AddressResponse>(`${this.baseUrl}/${id}/address`, {
      headers: this.getHeaders()
    });
  }
}

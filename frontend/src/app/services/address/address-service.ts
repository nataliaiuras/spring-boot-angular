import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable, tap} from 'rxjs';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Page} from '../../core/models/response/general/page';
import {AddressResponse} from '../../core/models/response/address/address-response';
import {AddressDetailResponse} from '../../core/models/response/address/address-detail-response';
import {AddressCreateRequest} from '../../core/models/request/address/address-create-request';
import {AddressUpdateRequest} from '../../core/models/request/address/address-update-request';
import {ApiResponse} from '../../core/models/response/general/api-response';


@Injectable({
  providedIn: 'root'
})
export class AddressService {
  private readonly baseUrl = 'http://localhost:8080/api/addresses';
  private readonly addressSubject = new BehaviorSubject<AddressResponse[]>([]);
  addresses$ = this.addressSubject.asObservable();

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


  /*  getAddresses(page: number, size: number): Observable<{ data: AddressResponse[], total: number }> {
      const params = new HttpParams()
        .set('page', page.toString())
        .set('size', size.toString());

      return this.http.get<{ data: AddressResponse[], total: number }>(this.baseUrl, { params,
        headers: this.getHeaders() });
    }*/

  getAllAddresses(page: number = 0, size: number = 10, sortBy: string = 'id', sortDir: string = 'asc'): Observable<ApiResponse<Page<AddressResponse>>> {
    const params = {
      page: page.toString(),
      size: size.toString(),
      sortBy,
      sortDir
    };
    return this.http.get<ApiResponse<Page<AddressResponse>>>(this.baseUrl, {
      params,
      headers: this.getHeaders()
    });
  }

  loadAddress(): void {
    this.getAllAddresses().subscribe({
      next: (response) => {
        if (response.success && response.data?.content) {
          this.addressSubject.next(response.data.content);
        } else {
          console.error('Invalid response structure:', response);
          this.addressSubject.next([]);
        }
      },
      error: (error) => {
        console.error('Error fetching addresss:', error);
        this.addressSubject.next([]);
      }
    });
  }

  getAddress(id: number): Observable<ApiResponse<AddressResponse>> {
    return this.http.get<ApiResponse<AddressResponse>>(`${this.baseUrl}/${id}`, {
      headers: this.getHeaders()
    });
  }

  getDetailedAddress(id: number): Observable<ApiResponse<AddressDetailResponse>> {
    return this.http.get<ApiResponse<AddressDetailResponse>>(`${this.baseUrl}/${id}/details`, {
      headers: this.getHeaders()
    });
  }

  createAddress(request: AddressCreateRequest): Observable<ApiResponse<AddressDetailResponse>> {
    return this.http.post<ApiResponse<AddressDetailResponse>>(this.baseUrl, request, {
      headers: this.getHeaders()
    }).pipe(
      tap((response) => {
        if (response.success) {
          console.log('Address created successfully');
          this.loadAddress();
        }
      })
    );
  }

  updateAddress(id: number, request: AddressUpdateRequest): Observable<ApiResponse<AddressDetailResponse>> {
    return this.http.put<ApiResponse<AddressDetailResponse>>(`${this.baseUrl}/${id}`, request, {
      headers: this.getHeaders()
    }).pipe(
      tap((response) => {
        if (response.success) {
          console.log('Address updated successfully');
          this.loadAddress();
        }
      })
    );
  }

  patchAddress(id: number, request: AddressUpdateRequest): Observable<ApiResponse<AddressDetailResponse>> {
    return this.http.patch<ApiResponse<AddressDetailResponse>>(`${this.baseUrl}/${id}`, request, {
      headers: this.getHeaders()
    }).pipe(
      tap((response) => {
        if (response.success) {
          console.log('Address patched successfully');
          this.loadAddress();
        }
      })
    );
  }

  deleteAddress(id: number): Observable<ApiResponse<string>> {
    return this.http.delete<ApiResponse<string>>(`${this.baseUrl}/${id}`, {
      headers: this.getHeaders()
    }).pipe(
      tap((response) => {
        if (response.success) {
          console.log('Address deleted successfully');
          this.loadAddress();
        } else {
          console.error('Error deleting address:', response);
        }
      })
    );
  }
}

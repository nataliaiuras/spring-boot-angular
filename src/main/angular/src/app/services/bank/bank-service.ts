import { Injectable } from '@angular/core';
import {BehaviorSubject, finalize, Observable, tap} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {Bank} from '../../models/bank';

@Injectable({
  providedIn: 'root'
})
export class BankService {
  private baseUrl = 'http://localhost:8080/api/banks';
  private banksSubject = new BehaviorSubject<Bank[]>([]);
  banks$ = this.banksSubject.asObservable();

  constructor(private http: HttpClient) {
  }

  getBanks(): void {
    const token = sessionStorage.getItem('token');
    this.http.get<Bank[]>(this.baseUrl, {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    }).subscribe(banks => this.banksSubject.next(banks));
  }

  getBank(id: number): Observable<Bank> {
    const token = sessionStorage.getItem('token');
    return this.http.get<Bank>(`${this.baseUrl}/${id}`, {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    });
  }

  addBank(bank: Bank): void {
    const token = sessionStorage.getItem('token');
    const {id, ...bankWithoutId} = bank;
    this.http.post<Bank>(this.baseUrl, bankWithoutId, {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    })
      .subscribe(
        (newBank) => {
          console.log('Bank added successfully:', bankWithoutId);
          this.getBanks();
        },
        (error) => {
          console.error('Error adding bank:', error);
        }
      );
  }

  updateBank(bank: Bank): void {
    const token = sessionStorage.getItem('token');
    this.http.put(`${this.baseUrl}/${bank.id}`, bank, {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    })
      .pipe(
        tap(() => console.log('Bank updated on server.')),
        finalize(() => this.getBanks())
      ).subscribe(
      () => {
        console.log('Bank updated successful and list will be refreshed.');
      },
      error => {
        console.error('Error updating bank:', error);
      }
    );
  }

  deleteBank(id: number): void {
    const token = sessionStorage.getItem('token');
    this.http.delete(`${this.baseUrl}/${id}`, {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    })
      .pipe(
        tap(() => console.log('Bank deleted on server.')),
        finalize(() => this.getBanks())
      )
      .subscribe(
        () => {
          console.log('Bank deletion successful and list will be refreshed.');
        },
        error => {
          console.error('Error deleting bank:', error);
        }
      );
  }
}

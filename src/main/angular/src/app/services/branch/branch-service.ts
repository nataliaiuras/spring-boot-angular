import { Injectable } from '@angular/core';
import {BehaviorSubject, finalize, Observable, tap} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {Branch} from '../../models/branch';

@Injectable({
  providedIn: 'root'
})
export class BranchService {
  private readonly baseUrl = 'http://localhost:8080/api/branches';
  private readonly branchesSubject = new BehaviorSubject<Branch[]>([]);
  branches$ = this.branchesSubject.asObservable();

 /* private readonly dataSource = new BehaviorSubject<Branch[]>([]);
  currentData = this.dataSource.asObservable();

  changeData(data: Branch[] = []) {
    this.dataSource.next(data);
  }*/

  constructor(private readonly http: HttpClient) {
  }

  getBranches(): void {
    const token = sessionStorage.getItem('token');
    this.http.get<Branch[]>(this.baseUrl, {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    }).subscribe(branches => this.branchesSubject.next(branches));
  }

  getBranch(id: number): Observable<Branch> {
    const token = sessionStorage.getItem('token');
    return this.http.get<Branch>(`${this.baseUrl}/${id}`, {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    });
  }

  addBranch(branch: Branch): void {
    const token = sessionStorage.getItem('token');
    const {id, ...branchWithoutId} = branch;
    this.http.post<Branch>(this.baseUrl,branchWithoutId, {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      }
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

  updateBranch(branch: Branch): void {
    const token = sessionStorage.getItem('token');
    this.http.put(`${this.baseUrl}/${branch.id}`, branch, {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    })
      .pipe(
        tap(() => console.log('Branch updated on server.')),
        finalize(() => this.getBranches())
      ).subscribe(
      () => {
        console.log('Branch updated successful and list will be refreshed.');
      },
      error => {
        console.error('Error updating branch:', error);
      }
    );
  }

  deleteBranch(id: number): void {
    const token = sessionStorage.getItem('token');
    this.http.delete(`${this.baseUrl}/${id}`, {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      }
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
}

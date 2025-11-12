import { Injectable } from '@angular/core';
import { BehaviorSubject, finalize, Observable, tap } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { CardResponse } from '../../core/models/response/card/card-response';

@Injectable({
  providedIn: 'root'
})
export class CardService {
  private readonly baseUrl = 'http://localhost:8080/api/cards';
  private readonly cardSubject = new BehaviorSubject<CardResponse[]>([]);
  card$ = this.cardSubject.asObservable();

  constructor(private http: HttpClient) {
  }

  getCards(): void {
    const token = sessionStorage.getItem('token');
    this.http.get<ApiResponse<PageResponse<CardResponse>>>(this.baseUrl, {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    }).subscribe(
      {
        next: (response) => {
          console.log('CardComponent API response:', response);

          if (response.success && response.data && response.data.content) {
            console.log('CardComponent data:', response.data.content);
            this.cardSubject.next(response.data.content);
          } else {
            console.error('Invalid response structure:', response);
            this.cardSubject.next([]);
          }
        },
        error: (error) => {
          console.error('Error fetching cards:', error);
          this.cardSubject.next([]);
        }
      }
    );
  }

  getCard(id: number): Observable<ApiResponse<CardResponse>> {
    const token = sessionStorage.getItem('token');
    return this.http.get<ApiResponse<CardResponse>>(`${this.baseUrl}/${id}`, {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    });
  }

  addCard(card: CardResponse): void {
    const token = sessionStorage.getItem('token');
    const { id, ...cardWithoutId } = card;
    this.http.post<CardResponse>(this.baseUrl, cardWithoutId, {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    })
      .subscribe(
        (newCard) => {
          console.log('CardResponse added successfully:', cardWithoutId);
          this.getCards();
        },
        (error) => {
          console.error('Error adding card:', error);
        }
      );
  }

  updateCard(card: CardResponse): void {
    const token = sessionStorage.getItem('token');
    this.http.put(`${this.baseUrl}/${card.id}`, card, {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    })
      .pipe(
        tap(() => console.log('CardResponse updated on server.')),
        finalize(() => this.getCards())
      ).subscribe(
      () => {
        console.log('CardResponse updated successful and list will be refreshed.');
      },
      error => {
        console.error('Error updating card:', error);
      }
    );
  }

  deleteCard(id: number): void {
    const token = sessionStorage.getItem('token');
    this.http.delete(`${this.baseUrl}/${id}`, {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    })
      .pipe(
        tap(() => console.log('CardResponse deleted on server.')),
        finalize(() => this.getCards())
      )
      .subscribe(
        () => {
          console.log('CardResponse deletion successful and list will be refreshed.');
        },
        error => {
          console.error('Error deleting card:', error);
        }
      );
  }
}

interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
}

interface PageResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
}

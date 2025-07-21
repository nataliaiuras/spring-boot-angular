import { Injectable } from '@angular/core';
import { BehaviorSubject, finalize, tap } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Post } from '../../models/post';

@Injectable({
  providedIn: 'root'
})
export class PostService {

  private baseUrl = 'http://localhost:8080/api/posts';
  private postsSubject = new BehaviorSubject<Post[]>([]);
  posts$ = this.postsSubject.asObservable();
  private posts: Post[] = [];

  constructor(private http: HttpClient) {
  }

  getPosts(): void {
    const token = sessionStorage.getItem('token');
    this.http.get<Post[]>(this.baseUrl, {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    }).subscribe(posts => this.postsSubject.next(posts));
  }

  addPost(post: Post): void {
    const token = sessionStorage.getItem('token');
    const { id, ...postWithoutId } = post;
    this.http.post<Post>(this.baseUrl, postWithoutId, {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    })
      .subscribe(
        (newPost) => {
          console.log('Post added successfully:', postWithoutId);
          this.getPosts();
        },
        (error) => {
          console.error('Error adding post:', error);
        }
      );
  }

  updatePost(post: Post): void {
    const token = sessionStorage.getItem('token');
    this.http.put(`${this.baseUrl}/${post.id}`, post, {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    })
      .pipe(
        tap(() => console.log('Post updated on server.')),
        finalize(() => this.getPosts())
      ).subscribe(
      () => {
        console.log('Post updated successful and list will be refreshed.');
      },
      error => {
        console.error('Error updating post:', error);
      }
    );
  }

  deletePost(id: number): void {
    const token = sessionStorage.getItem('token');
    this.http.delete(`${this.baseUrl}/${id}`, {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    })
      .pipe(
        tap(() => console.log('Post deleted on server.')),
        finalize(() => this.getPosts())
      )
      .subscribe(
        () => {
          console.log('Post deletion successful and list will be refreshed.');
        },
        error => {
          console.error('Error deleting post:', error);
        }
      );
  }
}

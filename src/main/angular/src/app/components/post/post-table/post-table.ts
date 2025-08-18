import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import {
  MatCell, MatCellDef, MatColumnDef,
  MatHeaderCell, MatHeaderCellDef,
  MatHeaderRow,
  MatHeaderRowDef,
  MatRow,
  MatRowDef, MatTable,
  MatTableDataSource
} from '@angular/material/table';
import { MatDialog } from '@angular/material/dialog';
import { MatButton, MatIconButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';
import { PostService } from '../../../services/post/post-service';
import { Post } from '../../../models/post';
import {DatePipe, SlicePipe} from '@angular/common';
import { PostForm } from '../post-form/post-form';

@Component({
  selector: 'app-post-table',
  templateUrl: './post-table.html',
  imports: [
    MatPaginator,
    MatRow,
    MatHeaderRow,
    MatHeaderRowDef,
    MatRowDef,
    MatIcon,
    MatIconButton,
    MatCell,
    MatHeaderCell,
    MatHeaderCellDef,
    MatCellDef,
    MatColumnDef,
    MatTable,
    MatButton,
    DatePipe,
    SlicePipe
  ],
  styleUrls: ['./post-table.css'],
  standalone: true,
})
export class PostTable implements OnInit, AfterViewInit {
  displayedColumns = ['id', 'title', 'content', 'author', 'createdAt', 'actions'];
  dataSource = new MatTableDataSource<Post>([]);

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(private postService: PostService, private dialog: MatDialog) {}

  ngOnInit() {
    this.postService.getPosts();
    this.postService.posts$.subscribe(posts => {
      this.dataSource.data = posts;
    });
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }

  openForm(post?: Post) {
    const dialogRef = this.dialog.open(PostForm, {
      width: '600px',
      data: post ? { ...post } : null
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        if (post) {
          this.postService.updatePost(result);
        } else {
          this.postService.addPost(result);
        }
      }
    });
  }

  deletePost(id: number) {
    this.postService.deletePost(id);
  }
}

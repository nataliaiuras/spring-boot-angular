import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell, MatHeaderCellDef,
  MatHeaderRow, MatHeaderRowDef, MatRow, MatRowDef, MatTable,
  MatTableDataSource
} from '@angular/material/table';
import {MatPaginator} from '@angular/material/paginator';
import {MatDialog} from '@angular/material/dialog';
import { BankService } from '../../services/bank/bank-service';
import {BankForm} from '../bank-form/bank-form';
import {DatePipe, SlicePipe} from '@angular/common';
import {MatButton, MatIconButton} from '@angular/material/button';
import {MatIcon} from '@angular/material/icon';
import {Bank} from '../../models/bank';
import {Router} from '@angular/router';

@Component({
  selector: 'app-bank-table',
  imports: [
    DatePipe,
    MatButton,
    MatCell,
    MatCellDef,
    MatColumnDef,
    MatHeaderCell,
    MatHeaderRow,
    MatHeaderRowDef,
    MatIcon,
    MatIconButton,
    MatPaginator,
    MatRow,
    MatRowDef,
    MatTable,
    SlicePipe,
    MatHeaderCellDef,
  ],
  templateUrl: './bank-table.html',
  styleUrl: './bank-table.css'
})
export class BankTable implements OnInit, AfterViewInit {
  displayedColumns = ['id', 'name', 'telephoneNumber', 'email', 'website', 'actions'];
  dataSource = new MatTableDataSource<Bank>([]);

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(private bankService: BankService, private dialog: MatDialog, private router: Router) {}

  ngOnInit() {
    this.bankService.getBanks();
    this.bankService.banks$.subscribe(banks => {
      this.dataSource.data = banks;
    });
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }

  openForm(bank?: BankTable) {
    const dialogRef = this.dialog.open(BankForm, {
      width: '600px',
      data: bank ? { ...bank } : null
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        if (bank) {
          this.bankService.updateBank(result);
        } else {
          this.bankService.addBank(result);
        }
      }
    });
  }

  deleteBank(id: number) {
    this.bankService.deleteBank(id);
  }

  open(id: number) {
    this.bankService.getBank(id);
  }
}

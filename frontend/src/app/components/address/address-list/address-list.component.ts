import {Component, OnInit} from '@angular/core';
import {AddressService} from '../../../services/address/address-service';
import {AddressResponse} from '../../../core/models/response/address/address-response';
import {NgForOf} from '@angular/common';
import {MatTableDataSource} from '@angular/material/table';
import {BranchResponse} from '../../../core/models/response/branch/branch-response';

@Component({
  selector: 'app-address-list',
  imports: [
    NgForOf
  ],
  templateUrl: './address-list.component.html',
  styleUrl: './address-list.component.css'
})
export class AddressListComponent implements OnInit{
  dataSource = new MatTableDataSource<AddressResponse>([]);
  addressList: AddressResponse[] = [];
  currentPage = 1;
  pageSize = 5;
  totalItems = 0;

  constructor(private readonly addressService: AddressService) {}

  ngOnInit() {
    this.loadAddresses();
  }

  loadAddresses() {
    this.addressService.addresses$.subscribe({
      next: (addresses) => {
        this.dataSource.data = addresses;
      },
      error: (error) => {
        console.error('Error loading addresses:', error);
      }
    });
    this.addressService.getAllAddresses();
  }

  onPageChange(page: number) {
    this.currentPage = page;
    this.loadAddresses();
  }


  protected readonly Math = Math;
}

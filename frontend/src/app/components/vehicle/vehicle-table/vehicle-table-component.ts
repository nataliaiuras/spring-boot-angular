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
import { VehicleFormComponent } from '../vehicle-form/vehicle-form-component';
import {MatButton, MatIconButton} from '@angular/material/button';
import {MatIcon} from '@angular/material/icon';
import {VehicleService} from '../../../services/vehicle/vehicle-service';
import {Vehicle} from '../../../core/models/other/vehicle';

@Component({
  selector: 'app-vehicle-table',
  templateUrl: './vehicle-table-component.html',
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
    MatButton
  ],
  styleUrls: ['./vehicle-table-component.css'],
  standalone: true,
})
export class VehicleTableComponent implements OnInit, AfterViewInit {
  displayedColumns = ['id', 'brand', 'model', 'yearProd', 'color', 'actions'];
  dataSource = new MatTableDataSource<Vehicle>([]);

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(private vehicleService: VehicleService, private dialog: MatDialog) {}

  ngOnInit() {
    this.vehicleService.getVehicles();
    this.vehicleService.vehicles$.subscribe(vehicles => {
      this.dataSource.data = vehicles;
    });
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }

  openForm(vehicle?: Vehicle) {
    const dialogRef = this.dialog.open(VehicleFormComponent, {
      width: '400px',
      data: vehicle ? { ...vehicle } : null
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        if (vehicle) {
          this.vehicleService.updateVehicle(result);
        } else {
          this.vehicleService.addVehicle(result);
        }
      }
    });
  }

  deleteVehicle(id: number) {
    this.vehicleService.deleteVehicle(id);
  }
}

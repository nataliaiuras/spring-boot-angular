import {Injectable} from '@angular/core';
import {BehaviorSubject, finalize, tap} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {Vehicle} from '../../models/vehicle';


@Injectable({
  providedIn: 'root'
})
export class VehicleService {

  private baseUrl = 'http://localhost:8080/api/vehicles';
  private vehiclesSubject = new BehaviorSubject<Vehicle[]>([]);
  vehicles$ = this.vehiclesSubject.asObservable();
  private vehicles: Vehicle[] = [];

  constructor(private http: HttpClient) {
  }

  getVehicles(): void {
    const token = sessionStorage.getItem('token');
    this.http.get<Vehicle[]>(this.baseUrl, {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    }).subscribe(vehicles => this.vehiclesSubject.next(vehicles));
  }


  addVehicle(vehicle: Vehicle): void {
    const token = sessionStorage.getItem('token');
    const {id, ...vehicleWithoutId} = vehicle;
    this.http.post<Vehicle>(this.baseUrl, vehicleWithoutId, {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    })
      .subscribe(
        (newVehicle) => {
          console.log('Vehicle added successfully:', vehicleWithoutId);
          this.getVehicles();
        },
        (error) => {
          console.error('Error adding vehicle:', error);
        }
      );
  }

  updateVehicle(vehicle: Vehicle): void {
    const token = sessionStorage.getItem('token');
    this.http.put(`${this.baseUrl}/${vehicle.id}`, vehicle, {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    })
      .pipe(
        tap(() => console.log('Vehicle updated on server.')),
        finalize(() => this.getVehicles())
      ).subscribe(
      () => {
        console.log('Vehicle updated successful and list will be refreshed.');
      },
      error => {
        console.error('Error updating vehicle:', error);
      }
    );
  }

  deleteVehicle(id: number): void {
    const token = sessionStorage.getItem('token');
    this.http.delete(`${this.baseUrl}/${id}`, {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    })
      .pipe(
        tap(() => console.log('Vehicle deleted on server.')),
        finalize(() => this.getVehicles())
      )
      .subscribe(
        () => {
          console.log('Vehicle deletion successful and list will be refreshed.');
        },
        error => {
          console.error('Error deleting vehicle:', error);
        }
      );
  }

}

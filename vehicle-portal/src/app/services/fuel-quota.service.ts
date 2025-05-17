import { HttpClientModule } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FuelQuotaService {

  private apiUrl = 'http://localhost:8080/api/'; // Replace with your real URL

  constructor(private http: HttpClient) { }

  registerVehicle(data: any): Observable<any> {
    return this.http.post<any>(this.apiUrl + "vehicle/register", data);
  }

  login(data: any): Observable<any> {
    data['type'] = "VEHICLE";
    return this.http.post<any>(this.apiUrl + "login", data);
  }
}

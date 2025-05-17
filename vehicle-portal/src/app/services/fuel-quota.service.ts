import { HttpClientModule, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthUserService } from './auth-user.service';

@Injectable({
  providedIn: 'root'
})
export class FuelQuotaService {

  private apiUrl = 'http://localhost:8080/api/'; // Replace with your real URL

  constructor(private http: HttpClient, private authUserService:AuthUserService) { }

  registerVehicle(data: any): Observable<any> {
    return this.http.post<any>(this.apiUrl + "vehicle/register", data);
  }

  login(data: any): Observable<any> {
    data['type'] = "VEHICLE";
    return this.http.post<any>(this.apiUrl + "login", data);
  }

  getQR():Observable<Blob>{
    const token = this.authUserService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get(this.apiUrl+"vehicle/qr",{headers,responseType:'blob'});
  }

  getQuotaDetails():Observable<any>{
    const token = this.authUserService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get(this.apiUrl+"vehicle/quota-details",{headers});
  }
}

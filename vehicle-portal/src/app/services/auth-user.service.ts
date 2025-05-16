import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthUserService {

  constructor() { }

  private readonly DATA_KEY = 'userData';

  setData(data: any): void {
    localStorage.setItem(this.DATA_KEY, JSON.stringify(data));
  }

  getData(): any {
    const data = localStorage.getItem(this.DATA_KEY);
    if (!data) {
      return null;
    }
    return JSON.parse(data);
  }

  getToken(): string | null {
    return this.getData()?.token;
  }

  getUsername(): string | null {
    return this.getData()?.username;
  }

  getRole(): string | null {
    return this.getData()?.role;
  }

  getUserData(): object | null {
    return this.getData()?.data;
  }

  hasData(): boolean {
    const data = localStorage.getItem(this.DATA_KEY);
    return data != null;
  }

  clearData(): void {
    localStorage.removeItem(this.DATA_KEY);
  }
}

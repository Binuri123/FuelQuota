import { Component, OnInit } from '@angular/core';
import { FuelQuotaService } from '../../services/fuel-quota.service';
import { AuthUserService } from '../../services/auth-user.service';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-dashboard',
  standalone: false,
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {
  qrImage: string | null = null;
  quota = 0.0;
  balance = 0.0;

  constructor(
    private fuelQuotaService: FuelQuotaService,
    private authUserService: AuthUserService,
    private router: Router,
    private snackBar: MatSnackBar,
  ) { }

  ngOnInit() {
    this.fuelQuotaService.getQR().subscribe({
      next: blob => {
        this.qrImage = URL.createObjectURL(blob);
      },
      error: err => this.handleServerErrors(err),
    });

    this.fuelQuotaService.getQuotaDetails().subscribe({
      next: quotaDetails => {
        this.quota = quotaDetails.quota;
        this.balance = quotaDetails.balance;
      },
      error: err => this.handleServerErrors(err),
    });
  }

  handleServerErrors(errorResponse: HttpErrorResponse): void {

    if (errorResponse.status == 403) {
      this.snackBar.open("Session Expired. Please login again.", "Close", { duration: 5000 });
      this.logout();
      return;
    }
    console.log(errorResponse);
    this.snackBar.open('Something went wrong', 'Close', { duration: 3000 });
  }

  logout(): void {
    this.authUserService.clearData();
    this.router.navigate(['/login']);
  }
}

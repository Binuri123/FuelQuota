import { Component } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { FuelQuotaService } from '../../services/fuel-quota.service';
import { HttpErrorResponse } from '@angular/common/http';
import { MatSnackBar } from '@angular/material/snack-bar';
import { matchPasswords } from '../../validators/match-passwords';
import { AuthUserService } from '../../services/auth-user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  loginForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private fuelQuotaService: FuelQuotaService,
    private snackBar: MatSnackBar,
    private authUserService: AuthUserService,
    private router: Router
  ) { }

  ngOnInit(): void {
    if(this.authUserService.hasData()){
      this.router.navigate(['/dashboard']);
      return;
    }
    this.loginForm = this.fb.group({
      username: [
        '',
        [
          Validators.required,
        ],
      ],
      password: [
        '',
        [
          Validators.required
        ],
      ],
    });
  }

  handleServerErrors(errorResponse: HttpErrorResponse): void {
    let knownError = false;
    if (errorResponse.status == 400) {
      const errors = errorResponse.error;
      for (let fieldName in this.loginForm.controls) {
        if (errors[fieldName]) {
          const field: FormControl = this.loginForm.controls[fieldName] as FormControl;
          field.setErrors({ servererror: errors[fieldName] });
          knownError = true;
        }
      }
    }

    if(errorResponse.status == 401){
      knownError = true;
      this.snackBar.open("Invalid username or password.","Close",{duration:5000});
      this.loginForm.get("password")?.setValue("");
    }

    if (!knownError) {
      console.log(errorResponse);
      this.snackBar.open('Something went wrong', 'Close', { duration: 3000 });
    }
  }

  handleSuccess(response: any): void {
    this.authUserService.setData(response);
    this.snackBar.open('Successfully Logged in', 'Close', { duration: 3000 });
    this.router.navigate(['/dashboard']);
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      this.fuelQuotaService.login(this.loginForm.value).subscribe({
        next: response => this.handleSuccess(response),
        error: err => this.handleServerErrors(err)
      });
    } else {
      this.loginForm.markAllAsTouched();
    }
  }
}

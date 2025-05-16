import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { FuelQuotaService } from '../../services/fuel-quota.service';
import { HttpErrorResponse } from '@angular/common/http';
import { MatSnackBar } from '@angular/material/snack-bar';
import { matchPasswords } from '../../validators/match-passwords';
import { AuthUserService } from '../../services/auth-user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-vehicle-form',
  standalone: false,
  templateUrl: './vehicle-form.component.html',
  styleUrl: './vehicle-form.component.scss'
})
export class VehicleFormComponent {
  vehicleForm!: FormGroup;
  vehicleTypes = ['Car', 'Van', 'Three Wheel', 'Bike', 'Lorry', 'Bus'];
  fuelTypes = ['Petrol', 'Diesel'];

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
    this.vehicleForm = this.fb.group({
      vehicleNo: [
        '',
        [
          Validators.required,
          Validators.pattern('^[A-Za-z0-9]{1,4}-\\d{4}$'),
        ],
      ],
      chassisNo: [
        '',
        [
          Validators.required,
          Validators.minLength(10),
          Validators.maxLength(60),
        ],
      ],
      type: ['', [Validators.required]],
      fuelType: ['', [Validators.required]],
      ownerNic: [
        '',
        [
          Validators.required,
          Validators.pattern('^((\\d{12})|(\\d{9}[vVxX]))$'),
        ],
      ],
      mobile: [
        '',
        [
          Validators.required,
          Validators.pattern('^0?[^0\\D]\\d{8}$'),
        ],
      ],
      firstName: [
        '',
        [
          Validators.required,
          Validators.minLength(3),
          Validators.maxLength(100),
          Validators.pattern("^[A-Za-z'\\s]*$"),
        ],
      ],
      lastName: [
        '',
        [
          Validators.required,
          Validators.minLength(3),
          Validators.maxLength(100),
          Validators.pattern("^[A-Za-z'\\s]*$"),
        ],
      ],
      address: [
        '',
        [
          Validators.required,
          Validators.minLength(5),
          Validators.maxLength(500),
        ],
      ],
      password: [
        '',
        [
          Validators.required,
          Validators.maxLength(20),
          Validators.pattern("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\d\\sA-Za-z]).{8,}$"),
        ],
      ],
      confirmPassword: [
        '',
        [
          Validators.required,
        ],
      ],
    });

    this.vehicleForm.addValidators(matchPasswords("password", "confirmPassword"));
  }

  handleServerErrors(errorResponse: HttpErrorResponse): void {
    let knownError = false;
    if (errorResponse.status == 400) {
      const errors = errorResponse.error;
      for (let fieldName in this.vehicleForm.controls) {
        if (errors[fieldName]) {
          const field: FormControl = this.vehicleForm.controls[fieldName] as FormControl;
          field.setErrors({ servererror: errors[fieldName] });
          knownError = true;
        }
      }
    }

    if (!knownError) {
      console.log(errorResponse);
      this.snackBar.open('Something went wrong', 'Close', { duration: 3000 });
    }
  }

  handleSuccess(response: any): void {
    this.authUserService.setData(response);
    this.snackBar.open('Successfully registered', 'Close', { duration: 3000 });
    this.router.navigate(['/dashboard']);
  }

  onSubmit(): void {
    if (this.vehicleForm.valid) {
      this.fuelQuotaService.registerVehicle(this.vehicleForm.value).subscribe({
        next: response => this.handleSuccess(response),
        error: err => this.handleServerErrors(err)
      });
    } else {
      this.vehicleForm.markAllAsTouched();
    }
  }
}

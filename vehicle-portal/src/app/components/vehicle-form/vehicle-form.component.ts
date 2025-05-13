import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { FuelQuotaService } from '../../services/fuel-quota.service';
import { HttpErrorResponse } from '@angular/common/http';
import { MatSnackBar } from '@angular/material/snack-bar';

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

  constructor(private fb: FormBuilder, private fuelQuotaService: FuelQuotaService, private snackBar: MatSnackBar) { }

  ngOnInit(): void {
    this.vehicleForm = this.fb.group({
      vehicleNo: [
        'CAK-3429',
        [
          Validators.required,
          Validators.pattern('^[A-Za-z0-9]{1,4}-\\d{4}$'),
        ],
      ],
      chassisNo: [
        'CHS1000000000001',
        [
          Validators.required,
          Validators.minLength(10),
          Validators.maxLength(60),
        ],
      ],
      type: ['Van', [Validators.required]],
      fuelType: ['Diesel', [Validators.required]],
      ownerNic: [
        '199775502761',
        [
          Validators.required,
          Validators.pattern('^((\\d{12})|(\\d{9}[vVxX]))$'),
        ],
      ],
      mobile: [
        '0702825863',
        [
          Validators.required,
          Validators.pattern('^0?[^0\\D]\\d{8}$'),
        ],
      ],
      firstName: [
        'Nimal',
        [
          Validators.required,
          Validators.minLength(3),
          Validators.maxLength(100),
          Validators.pattern("^[A-Za-z'\\s]*$"),
        ],
      ],
      lastName: [
        'Perera',
        [
          Validators.required,
          Validators.minLength(3),
          Validators.maxLength(100),
          Validators.pattern("^[A-Za-z'\\s]*$"),
        ],
      ],
      address: [
        'C11,Railway Flats,Dematagoda,Colombo 09',
        [
          Validators.required,
          Validators.minLength(5),
          Validators.maxLength(500),
        ],
      ],
    });
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

  onSubmit(): void {
    if (this.vehicleForm.valid) {
      console.log('Submitted:', this.vehicleForm.value);
      this.fuelQuotaService.registerVehicle(this.vehicleForm.value).subscribe({
        next: response => console.log('Response:', response),
        error: err => this.handleServerErrors(err)
      });
    } else {
      this.vehicleForm.markAllAsTouched();
    }
  }
}

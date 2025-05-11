import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { FuelQuotaService } from '../../services/fuel-quota.service';

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

  constructor(private fb: FormBuilder, private fuelQuotaService: FuelQuotaService) { }

  ngOnInit(): void {
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
          Validators.pattern('^0?\\d{9}$'),
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
    });
  }

  onSubmit(): void {
    if (this.vehicleForm.valid) {
      console.log('Submitted:', this.vehicleForm.value);
      this.fuelQuotaService.registerVehicle(this.vehicleForm.value).subscribe({
        next: response => console.log('Response:', response),
        error: err => console.error('Error:', err)
      });
    } else {
      this.vehicleForm.markAllAsTouched();
    }
  }
}

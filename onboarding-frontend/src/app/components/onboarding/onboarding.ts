import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { ApiService } from '../../services/api';

@Component({
  selector: 'app-onboarding',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './onboarding.html',
  styleUrls: ['./onboarding.css'],
})
export class OnboardingComponent implements OnInit {
  usuarioId = '';
  errorMessage = '';
  successMessage = '';

  datosForm = {
    telefono: '',
    fechaNacimiento: '',

    calle: '',
    ciudad: '',
    codigoPostal: '',

    tipoDocumento: 'INE',
    numeroDocumento: '',
  };

  constructor(
    private apiService: ApiService,
    private router: Router,
  ) {}

  ngOnInit() {
    this.usuarioId = localStorage.getItem('usuarioId') || '';

    if (!this.usuarioId) {
      this.router.navigate(['/login']);
      return;
    }

    this.apiService.obtenerOnboarding(this.usuarioId).subscribe({
      next: (res) => {
        if (res) {
          this.datosForm = {
            telefono: res.telefono || '',
            fechaNacimiento: res.fechaNacimiento || '',
            calle: res.calle || '',
            ciudad: res.ciudad || '',
            codigoPostal: res.codigoPostal || '',
            tipoDocumento: res.tipoDocumento || 'INE',
            numeroDocumento: res.numeroDocumento || '',
          };
        }
      },
      error: () => {},
    });
  }

  onGuardar() {
    this.errorMessage = '';
    this.successMessage = '';

    this.apiService
      .guardarOnboarding(this.usuarioId, this.datosForm)
      .subscribe({
        next: () => {
          this.successMessage = 'Información procesada';
          setTimeout(() => {
            this.router.navigate(['/home']);
          }, 2000);
        },
        error: (err) => {
          this.errorMessage = err.error || 'Error';
        },
      });
  }
}

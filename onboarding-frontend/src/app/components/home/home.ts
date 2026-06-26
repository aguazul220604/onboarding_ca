import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { ApiService } from '../../services/api';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './home.html',
  styleUrls: ['./home.css'],
})
export class HomeComponent implements OnInit {
  nombreCompleto = '';
  usuarioId = '';
  hasOnboardingData = false;

  constructor(
    private apiService: ApiService,
    private router: Router,
  ) {}

  ngOnInit() {
    this.nombreCompleto = localStorage.getItem('nombreCompleto') || 'Usuario';
    this.usuarioId = localStorage.getItem('usuarioId') || '';

    if (this.usuarioId) {
      this.apiService.obtenerOnboarding(this.usuarioId).subscribe({
        next: (res) => {
          if (res) this.hasOnboardingData = true;
        },
        error: () => {
          this.hasOnboardingData = false;
        },
      });
    }
  }

  onEliminarCuenta() {
    const confirmar = confirm('¿Está seguro de darte de baja?');

    if (confirmar && this.usuarioId) {
      this.apiService.eliminarCuenta(this.usuarioId).subscribe({
        next: () => {
          alert('Su cuenta ha sido de baja exitosamente');
          this.onLogout();
        },
        error: (err) => {
          console.error('Error:', err);

          const mensajeError =
            err.error?.message || err.error || err.message || 'Error';
          alert('Error: ' + mensajeError);
        },
      });
    }
  }

  onLogout() {
    localStorage.clear();
    this.router.navigate(['/login']);
  }
}

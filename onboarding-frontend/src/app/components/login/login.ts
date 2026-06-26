import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiService } from '../../services/api';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html',
  styleUrls: ['./login.css'],
})
export class LoginComponent {
  isLoginMode = true;

  authData = { username: '', password: '' };
  registerData = { username: '', password: '', nombreCompleto: '' };

  errorMessage = '';
  successMessage = '';

  constructor(
    private apiService: ApiService,
    private router: Router,
  ) {}

  onLogin() {
    this.errorMessage = '';
    this.apiService.login(this.authData).subscribe({
      next: (res) => {
        localStorage.setItem('token', res.token);
        localStorage.setItem('username', res.username);
        localStorage.setItem('nombreCompleto', res.nombreCompleto);
        localStorage.setItem('usuarioId', res.usuarioId);

        this.router.navigate(['/home']);
      },
      error: (err) => {
        this.errorMessage = err.error || 'Credenciales inválidas';
      },
    });
  }

  onRegister() {
    this.errorMessage = '';
    this.successMessage = '';
    this.apiService.registrar(this.registerData).subscribe({
      next: () => {
        this.successMessage = 'Cuenta creada con éxito';
        this.isLoginMode = true;
        this.authData.username = this.registerData.username;
      },
      error: (err) => {
        this.errorMessage = err.error || 'Error';
      },
    });
  }
}

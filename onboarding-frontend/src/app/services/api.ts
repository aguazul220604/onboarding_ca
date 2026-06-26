import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  private baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  registrar(usuario: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/usuarios/registro`, usuario);
  }

  login(credenciales: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/usuarios/login`, credenciales);
  }

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`,
    });
  }

  guardarOnboarding(usuarioId: string, datos: any): Observable<any> {
    return this.http.post(
      `${this.baseUrl}/onboarding/guardar/${usuarioId}`,
      datos,
      { headers: this.getHeaders() },
    );
  }

  obtenerOnboarding(usuarioId: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/onboarding/datos/${usuarioId}`, {
      headers: this.getHeaders(),
    });
  }

  eliminarCuenta(usuarioId: string): Observable<any> {
    return this.http.delete(
      `${this.baseUrl}/onboarding/eliminar/${usuarioId}`,
      {
        headers: this.getHeaders(),
        responseType: 'text',
      },
    );
  }
}

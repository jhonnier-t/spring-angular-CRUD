import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, Observable, of, throwError } from 'rxjs';
import Swal from 'sweetalert2';
import { Cliente } from './cliente';


@Injectable({providedIn: 'root'})
export class ClienteService {
  private urlEndPoint: string = "http://localhost:8081/api/clients";
  private httpHeaders = new HttpHeaders({'Content-Type':'application/json'});

  constructor(private http: HttpClient, private router: Router) { }

  getClientes(): Observable<Cliente[]> {
    //return of(CLIENTES);
    return this.http.get<Cliente[]>(this.urlEndPoint);
  }

  create(cliente: Cliente): Observable<Cliente>{
    return this.http.post<Cliente>(this.urlEndPoint, cliente, {headers: this.httpHeaders}).pipe(
      catchError(e => {
        console.error(e.error.message);
        Swal.fire(e.error.msg, '', 'error');
        return throwError(e);
      })
    );
  }

  getCliente(id): Observable<Cliente>{
    return this.http.get<Cliente>(`${this.urlEndPoint}/${id}`).pipe(
      catchError(e => {
        console.error(e.error.message);
        Swal.fire(e.error.msg, '', 'error');
        this.router.navigate(['/clients']);
        return throwError(e);
      })
    );
  }

  update(cliente: Cliente): Observable<Cliente>{
    return this.http.put<Cliente>(`${this.urlEndPoint}/${cliente.id}`, cliente, {headers: this.httpHeaders}).pipe(
      catchError(e => {
        console.error(e.error.message);
        Swal.fire(e.error.msg, '', 'error');
        return throwError(e);
      })
    );
  }

  delete(id: number): Observable<Cliente>{
    return this.http.delete<Cliente>(`${this.urlEndPoint}/${id}`, {headers: this.httpHeaders})
  }

}

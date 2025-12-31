import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ContextRegisterService {

  private apiUrl = '/aims/api/sectionData';

  constructor(private http: HttpClient) {}

  loadContextData() {
    return this.http.get(this.apiUrl);
  }
}

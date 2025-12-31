import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  standalone: true,
})
export class LoginComponent {

  companyCode = '';
  username = '';
  password = '';

  constructor(private router: Router) {

    }

  login() {
    // TODO: call backend auth API later
    if (this.companyCode && this.username && this.password) {
      localStorage.setItem('loggedIn', 'true');
      this.router.navigate(['/home']);
    }
  }
}

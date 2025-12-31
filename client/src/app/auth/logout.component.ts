import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  standalone: true,
  selector: 'app-logout',
  template: `Logging out...`
})
export class LogoutComponent implements OnInit {
  constructor(private router: Router) {}

  ngOnInit(): void {
    localStorage.removeItem('loggedIn');
    this.router.navigate(['']);
  }
}

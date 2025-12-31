import { Component } from '@angular/core';
import { RouterOutlet, Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
   imports: [CommonModule, RouterOutlet],
})
export class HomeComponent {

constructor(private router: Router) {}

  goToContextRegister() {
    this.router.navigate(['/home/context-register']);
  }
}

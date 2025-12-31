import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';

import { HostListener, ElementRef } from '@angular/core';
import { Router, NavigationStart } from '@angular/router';
import { filter } from 'rxjs/operators';


@Component({
  selector: 'app-layout',
  standalone: true,

 imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.css'],
})
export class LayoutComponent {
  collapsed = true;

  user = {
    firstName: 'Nag',
    lastName: 'Pal',
    role: 'Senior Consultant',
    location: 'Atlanta, USA'
  };




  toggle() {
    this.collapsed = !this.collapsed;
  }

 logout() {
   }

 userMenuOpen = false;

 get userDisplayName(): string {
   return `${this.user.firstName} ${this.user.lastName}`.trim();
 }

 get userInitials(): string {
   const f = (this.user.firstName || '').trim()[0] || '';
   const l = (this.user.lastName || '').trim()[0] || '';
   return (f + l).toUpperCase();
 }

 constructor(private router: Router, private elRef: ElementRef) {
   // Close popup on navigation
   this.router.events
     .pipe(filter(e => e instanceof NavigationStart))
     .subscribe(() => this.userMenuOpen = false);
 }

 toggleUserMenu() {
   this.userMenuOpen = !this.userMenuOpen;
 }

 closeUserMenu() {
   this.userMenuOpen = false;
 }

 editProfile() {
   this.userMenuOpen = false;
   // TODO later: route to profile page / modal
   alert('Edit profile (TODO)');
 }

 @HostListener('document:click', ['$event'])
 onDocClick(event: MouseEvent) {
   if (!this.userMenuOpen) return;

   const clickedInside = this.elRef.nativeElement.contains(event.target);
   if (!clickedInside) {
     this.userMenuOpen = false;
   }
 }



}

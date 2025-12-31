import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { LayoutComponent } from './layout/layout.component';
import { HomeComponent } from './home/home.component';
import { authGuard } from './login/auth.guard';
import { LogoutComponent } from './auth/logout.component';
import { StandardsComponent } from './standards/standards.component';
import { ContextRegisterComponent } from './context-register/context-register.component';



export const routes: Routes = [
  { path: '', component: LoginComponent },

    {
      path: '',
      component: LayoutComponent,
      canActivate: [authGuard],
      children: [
        { path: 'home', component: HomeComponent },
        { path: 'logout', component: LogoutComponent },
        { path: 'standards', component: StandardsComponent },
        { path: 'context-register', component: ContextRegisterComponent },

        // { path: 'standards/:id', component: StandardDetailsComponent },
        // add more pages here later:
        // { path: 'audits', component: AuditsComponent },
        // { path: 'reports', component: ReportsComponent },
        // { path: 'settings', component: SettingsComponent },
      ]
    },
  { path: '**', redirectTo: '' }


];

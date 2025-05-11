import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LandingPageComponent } from './components/landing-page/landing-page.component';
import { VehicleFormComponent } from './components/vehicle-form/vehicle-form.component';
import { LoginComponent } from './components/login/login.component';
import { NotFoundPageComponent } from './components/not-found-page/not-found-page.component';

const routes: Routes = [
  {path:'',component:LandingPageComponent},
  {path:"register",component:VehicleFormComponent},
  {path:'login',component:LoginComponent},
  {path:'**',component:NotFoundPageComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

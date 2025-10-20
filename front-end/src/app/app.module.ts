import {RouterModule, Routes} from '@angular/router';
import {AppComponent} from './app.component';
import {NgModule} from '@angular/core';
import {ProductsComponent} from './componants/products/products.component';
import {HeaderComponent} from './componants/header/header.component';
import {CategoryComponent} from './componants/category/category.component';
import {CardDetailsComponent} from './componants/card-details/card-details.component';
import {CardComponent} from './componants/card/card.component';
import {BrowserModule} from '@angular/platform-browser';
import {FooterComponent} from './componants/footer/footer.component';
import { ChefsComponent } from './componants/chefs/chefs.component';
import { ContactInfoComponent } from './componants/contact-info/contact-info.component';
import {APP_BASE_HREF} from '@angular/common';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import { LoginComponent } from './componants/login/login.component';
import { SignupComponent } from './componants/signup/signup.component';
import {AuthInterceptor} from '../interceptors/interceptors/auth.interceptor';
import {AuthGuard} from '../guard/auth.guard';
import {LoginSignUpGuard} from '../guard/login-sign-up.guard';
import {NgbPaginationModule} from '@ng-bootstrap/ng-bootstrap';
import { OrderCodeComponent } from './componants/order-code/order-code.component';
import { DashboardComponent } from './componants/dashboard/dashboard.component';
import { AdminProductsComponent } from './componants/dashboard/admin-products/admin-products.component';
import {FormsModule} from '@angular/forms';
import { OrdersHistoryComponent } from './componants/dashboard/orders-history/orders-history.component';
import { AllOrdersComponent } from './componants/dashboard/all-orders/all-orders.component';
import { ChangePasswordComponent } from './componants/dashboard/change-password/change-password.component';
import { DisableAccountComponent } from './componants/dashboard/disable-account/disable-account.component';
import { AdminProductFormComponent } from './componants/dashboard/admin-products/admin-product-form/admin-product-form.component';
import { ConfirmDialogComponent } from './componants/confirm-dialog/confirm-dialog-component';
import { AccountDetailsComponent } from './componants/dashboard/account-details/account-details.component';
import { ContactMessagesComponent } from './componants/dashboard/contact-messages/contact-messages.component';

// http://localhost:4200/
export const routes: Routes = [

  // http://localhost:4200/products
  {path: 'products', component: ProductsComponent, canActivate: [AuthGuard]},
  {path: 'products/:key', component: ProductsComponent, canActivate: [AuthGuard]},
  {path: 'category/:id', component: ProductsComponent, canActivate: [AuthGuard]},
  // http://localhost:4200/cardDetails
  {path: 'cardDetails', component: CardDetailsComponent, canActivate: [AuthGuard]},
  {path: 'contact-info', component: ContactInfoComponent, canActivate: [AuthGuard]},
  {path: 'chefs', component: ChefsComponent, canActivate: [AuthGuard]},
  {path: 'order-code/:code', component: OrderCodeComponent, canActivate: [AuthGuard]},
  {path: 'login', component:  LoginComponent, canActivate: [LoginSignUpGuard]},
  {path: 'signup', component: SignupComponent, canActivate: [LoginSignUpGuard]},
  { path: 'change-password', component: ChangePasswordComponent },
  // { path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard] },
  // http://localhost:4200/
  {path: '', redirectTo: '/products', pathMatch: 'full'},

  // if user enter thing without all routes
  // http://localhost:4200/ghy
  {
    path: 'dashboard',
    canActivate: [AuthGuard],
    component: DashboardComponent,
    children: [
      { path: 'orders-history', component: OrdersHistoryComponent },
      { path: 'all-orders', component: AllOrdersComponent },
      { path: 'products', component: AdminProductsComponent },
      { path: 'change-password', component: ChangePasswordComponent },
      { path: 'disable-account', component: DisableAccountComponent },
      { path: 'account-details', component: AccountDetailsComponent },
      { path: 'contact-messages', component: ContactMessagesComponent },
      // default child
      { path: '', redirectTo: 'orders-history', pathMatch: 'full' }
    ]
  },
  {path: '**', redirectTo: '/products', pathMatch: 'full'},



];



/*
*   // http://localhost:4200/
  {path: '', component:OrderItemsComponent}
* */
@NgModule({
  declarations: [
    AppComponent,
    ProductsComponent,
    HeaderComponent,
    CategoryComponent,
    CardDetailsComponent,
    CardComponent,
    FooterComponent,
    ChefsComponent,
    ContactInfoComponent,
    LoginComponent,
    SignupComponent,
    OrderCodeComponent,
    DashboardComponent,
    AdminProductsComponent,
    OrdersHistoryComponent,
    AllOrdersComponent,
    ChangePasswordComponent,
    DisableAccountComponent,
    AdminProductFormComponent,
    ConfirmDialogComponent,
    AccountDetailsComponent,
    ContactMessagesComponent
  ],
  imports: [
    RouterModule.forRoot(routes),
    BrowserModule,
    HttpClientModule,
    NgbPaginationModule,
    FormsModule
  ],
  providers: [{provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true},
              { provide: APP_BASE_HREF, useValue: '/' }],
  bootstrap: [
    AppComponent
  ]
})
export class AppModule { }

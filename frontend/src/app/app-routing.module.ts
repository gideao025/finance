import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { 
    path: 'dashboard', 
    loadChildren: () => import('./dashboard/dashboard.module').then(m => m.DashboardModule)
  },
  { 
    path: 'transactions', 
    loadComponent: () => import('./transactions/transactions.component').then(m => m.TransactionsComponent)
  },
  { 
    path: 'cards', 
    loadComponent: () => import('./cards/cards.component').then(m => m.CardsComponent)
  },
  { 
    path: 'vouchers', 
    loadComponent: () => import('./vouchers/vouchers.component').then(m => m.VouchersComponent)
  },
  { path: '**', redirectTo: '/dashboard' } // Wildcard route for 404 page
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {
    enableTracing: false, // Set to true for debugging
    scrollPositionRestoration: 'top'
  })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
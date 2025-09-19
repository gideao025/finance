import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

// Angular Material
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatChipsModule } from '@angular/material/chips';
import { MatTableModule } from '@angular/material/table';

// Chart.js
import { NgChartsModule } from 'ng2-charts';

// Lucide Icons
import { LucideAngularModule, TrendingUp, TrendingDown, DollarSign, CreditCard, 
         PiggyBank, Target, MoreVertical, ArrowUpRight, ArrowDownRight } from 'lucide-angular';

// Components
import { DashboardComponent } from './dashboard.component';
import { StatsCardComponent } from './components/stats-card/stats-card.component';
import { ChartCardComponent } from './components/chart-card/chart-card.component';
import { TransactionListComponent } from './components/transaction-list/transaction-list.component';
import { GoalsCardComponent } from './components/goals-card/goals-card.component';

const routes = [
  { path: '', component: DashboardComponent }
];

@NgModule({
  declarations: [
    DashboardComponent,
    StatsCardComponent,
    ChartCardComponent,
    TransactionListComponent,
    GoalsCardComponent
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    
    // Angular Material
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatMenuModule,
    MatProgressBarModule,
    MatChipsModule,
    MatTableModule,
    
    // Charts
    NgChartsModule,
    
    // Icons
    LucideAngularModule.pick({
      TrendingUp,
      TrendingDown,
      DollarSign,
      CreditCard,
      PiggyBank,
      Target,
      MoreVertical,
      ArrowUpRight,
      ArrowDownRight
    })
  ]
})
export class DashboardModule { }
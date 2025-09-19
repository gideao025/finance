import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatChipsModule } from '@angular/material/chips';
import { MatMenuModule } from '@angular/material/menu';
import { LucideAngularModule } from 'lucide-angular';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup } from '@angular/forms';

export interface Transaction {
  id: string;
  description: string;
  amount: number;
  type: 'income' | 'expense';
  category: string;
  date: Date;
  status: 'completed' | 'pending' | 'cancelled';
  account: string;
  tags?: string[];
}

@Component({
  selector: 'app-transactions',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatChipsModule,
    MatMenuModule,
    LucideAngularModule,
    FormsModule,
    ReactiveFormsModule
  ],
  templateUrl: './transactions.component.html',
  styleUrls: ['./transactions.component.scss']
})
export class TransactionsComponent implements OnInit {
  transactions: Transaction[] = [
    {
      id: '1',
      description: 'Salário',
      amount: 5000,
      type: 'income',
      category: 'Salário',
      date: new Date('2024-01-15'),
      status: 'completed',
      account: 'Conta Corrente',
      tags: ['trabalho', 'mensal']
    },
    {
      id: '2',
      description: 'Supermercado',
      amount: -250.50,
      type: 'expense',
      category: 'Alimentação',
      date: new Date('2024-01-14'),
      status: 'completed',
      account: 'Cartão de Crédito',
      tags: ['essencial']
    },
    {
      id: '3',
      description: 'Freelance',
      amount: 800,
      type: 'income',
      category: 'Freelance',
      date: new Date('2024-01-13'),
      status: 'pending',
      account: 'Conta Poupança',
      tags: ['extra']
    },
    {
      id: '4',
      description: 'Combustível',
      amount: -120,
      type: 'expense',
      category: 'Transporte',
      date: new Date('2024-01-12'),
      status: 'completed',
      account: 'Cartão de Débito'
    },
    {
      id: '5',
      description: 'Netflix',
      amount: -29.90,
      type: 'expense',
      category: 'Entretenimento',
      date: new Date('2024-01-11'),
      status: 'completed',
      account: 'Cartão de Crédito',
      tags: ['assinatura', 'mensal']
    }
  ];

  filteredTransactions: Transaction[] = [];
  filterForm: FormGroup;
  displayedColumns: string[] = ['date', 'description', 'category', 'amount', 'status', 'actions'];

  categories = [
    'Todas',
    'Alimentação',
    'Transporte',
    'Entretenimento',
    'Saúde',
    'Educação',
    'Salário',
    'Freelance',
    'Investimentos'
  ];

  accounts = [
    'Todas',
    'Conta Corrente',
    'Conta Poupança',
    'Cartão de Crédito',
    'Cartão de Débito'
  ];

  statuses = [
    'Todos',
    'completed',
    'pending',
    'cancelled'
  ];

  constructor(private fb: FormBuilder) {
    this.filterForm = this.fb.group({
      search: [''],
      category: ['Todas'],
      account: ['Todas'],
      status: ['Todos'],
      dateFrom: [null],
      dateTo: [null],
      type: ['all']
    });
  }

  ngOnInit() {
    this.filteredTransactions = [...this.transactions];
    this.filterForm.valueChanges.subscribe(() => {
      this.applyFilters();
    });
  }

  applyFilters() {
    const filters = this.filterForm.value;
    
    this.filteredTransactions = this.transactions.filter(transaction => {
      // Search filter
      if (filters.search && !transaction.description.toLowerCase().includes(filters.search.toLowerCase())) {
        return false;
      }

      // Category filter
      if (filters.category && filters.category !== 'Todas' && transaction.category !== filters.category) {
        return false;
      }

      // Account filter
      if (filters.account && filters.account !== 'Todas' && transaction.account !== filters.account) {
        return false;
      }

      // Status filter
      if (filters.status && filters.status !== 'Todos' && transaction.status !== filters.status) {
        return false;
      }

      // Type filter
      if (filters.type && filters.type !== 'all' && transaction.type !== filters.type) {
        return false;
      }

      // Date range filter
      if (filters.dateFrom && transaction.date < filters.dateFrom) {
        return false;
      }

      if (filters.dateTo && transaction.date > filters.dateTo) {
        return false;
      }

      return true;
    });
  }

  clearFilters() {
    this.filterForm.reset({
      search: '',
      category: 'Todas',
      account: 'Todas',
      status: 'Todos',
      dateFrom: null,
      dateTo: null,
      type: 'all'
    });
  }

  getTransactionIcon(category: string): string {
    const iconMap: { [key: string]: string } = {
      'Alimentação': 'utensils',
      'Transporte': 'car',
      'Entretenimento': 'gamepad-2',
      'Saúde': 'heart',
      'Educação': 'graduation-cap',
      'Salário': 'briefcase',
      'Freelance': 'laptop',
      'Investimentos': 'trending-up'
    };
    return iconMap[category] || 'circle';
  }

  getStatusColor(status: string): string {
    switch (status) {
      case 'completed': return 'success';
      case 'pending': return 'warning';
      case 'cancelled': return 'error';
      default: return 'default';
    }
  }

  getStatusText(status: string): string {
    switch (status) {
      case 'completed': return 'Concluída';
      case 'pending': return 'Pendente';
      case 'cancelled': return 'Cancelada';
      default: return status;
    }
  }

  formatCurrency(amount: number): string {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL'
    }).format(Math.abs(amount));
  }

  getAmountClass(amount: number): string {
    return amount >= 0 ? 'amount-positive' : 'amount-negative';
  }

  editTransaction(transaction: Transaction) {
    // Implementar edição de transação
    console.log('Editar transação:', transaction);
  }

  deleteTransaction(transaction: Transaction) {
    // Implementar exclusão de transação
    console.log('Excluir transação:', transaction);
  }

  exportTransactions() {
    // Implementar exportação de transações
    console.log('Exportar transações');
  }

  addTransaction() {
    // Implementar adição de nova transação
    console.log('Adicionar nova transação');
  }

  trackByTransactionId(index: number, transaction: Transaction): string {
    return transaction.id;
  }
}
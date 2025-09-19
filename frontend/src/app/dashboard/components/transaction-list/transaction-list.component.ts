import { Component, Input } from '@angular/core';

interface Transaction {
  id: string;
  description: string;
  amount: number;
  type: 'income' | 'expense';
  category: string;
  date: Date;
  status: 'completed' | 'pending';
}

@Component({
  selector: 'app-transaction-list',
  templateUrl: './transaction-list.component.html',
  styleUrls: ['./transaction-list.component.scss']
})
export class TransactionListComponent {
  @Input() transactions: Transaction[] = [];

  constructor() { }

  getTransactionIcon(category: string): string {
    const icons: { [key: string]: string } = {
      'Salário': 'DollarSign',
      'Freelance': 'TrendingUp',
      'Alimentação': 'ShoppingCart',
      'Transporte': 'Car',
      'Utilidades': 'Zap',
      'Lazer': 'Coffee',
      'Saúde': 'Heart',
      'Educação': 'BookOpen',
      'Casa': 'Home'
    };
    return icons[category] || 'Circle';
  }

  getStatusColor(status: string): string {
    return status === 'completed' ? 'success' : 'warning';
  }

  getStatusText(status: string): string {
    return status === 'completed' ? 'Concluída' : 'Pendente';
  }

  formatCurrency(value: number): string {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL'
    }).format(Math.abs(value));
  }

  formatDate(date: Date): string {
    return new Intl.DateTimeFormat('pt-BR', {
      day: '2-digit',
      month: 'short'
    }).format(date);
  }

  getAmountClass(type: string): string {
    return type === 'income' ? 'amount-positive' : 'amount-negative';
  }

  trackByTransactionId(index: number, transaction: Transaction): string {
    return transaction.id;
  }
}
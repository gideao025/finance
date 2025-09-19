import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTabsModule } from '@angular/material/tabs';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatMenuModule } from '@angular/material/menu';
import { MatChipsModule } from '@angular/material/chips';
import { MatDividerModule } from '@angular/material/divider';
import { LucideAngularModule } from 'lucide-angular';

export interface CreditCard {
  id: string;
  name: string;
  type: 'credit' | 'debit';
  brand: 'visa' | 'mastercard' | 'elo' | 'american-express';
  lastFourDigits: string;
  limit: number;
  availableLimit: number;
  currentBalance: number;
  dueDate: Date;
  closingDate: Date;
  status: 'active' | 'blocked' | 'cancelled';
  color: string;
  isVirtual: boolean;
}

export interface CardTransaction {
  id: string;
  cardId: string;
  description: string;
  amount: number;
  category: string;
  date: Date;
  status: 'pending' | 'processed';
  installments?: number;
  currentInstallment?: number;
}

@Component({
  selector: 'app-cards',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatTabsModule,
    MatProgressBarModule,
    MatMenuModule,
    MatChipsModule,
    MatDividerModule,
    LucideAngularModule
  ],
  templateUrl: './cards.component.html',
  styleUrls: ['./cards.component.scss']
})
export class CardsComponent implements OnInit {
  cards: CreditCard[] = [
    {
      id: '1',
      name: 'Cartão Principal',
      type: 'credit',
      brand: 'visa',
      lastFourDigits: '1234',
      limit: 5000,
      availableLimit: 3200,
      currentBalance: 1800,
      dueDate: new Date('2024-02-15'),
      closingDate: new Date('2024-01-20'),
      status: 'active',
      color: '#1976d2',
      isVirtual: false
    },
    {
      id: '2',
      name: 'Cartão Gold',
      type: 'credit',
      brand: 'mastercard',
      lastFourDigits: '5678',
      limit: 10000,
      availableLimit: 8500,
      currentBalance: 1500,
      dueDate: new Date('2024-02-10'),
      closingDate: new Date('2024-01-15'),
      status: 'active',
      color: '#ff9800',
      isVirtual: false
    },
    {
      id: '3',
      name: 'Cartão Virtual',
      type: 'credit',
      brand: 'elo',
      lastFourDigits: '9012',
      limit: 2000,
      availableLimit: 1800,
      currentBalance: 200,
      dueDate: new Date('2024-02-20'),
      closingDate: new Date('2024-01-25'),
      status: 'active',
      color: '#4caf50',
      isVirtual: true
    },
    {
      id: '4',
      name: 'Cartão Débito',
      type: 'debit',
      brand: 'visa',
      lastFourDigits: '3456',
      limit: 0,
      availableLimit: 2500,
      currentBalance: 0,
      dueDate: new Date(),
      closingDate: new Date(),
      status: 'active',
      color: '#9c27b0',
      isVirtual: false
    }
  ];

  recentTransactions: CardTransaction[] = [
    {
      id: '1',
      cardId: '1',
      description: 'Supermercado Extra',
      amount: 250.50,
      category: 'Alimentação',
      date: new Date('2024-01-14'),
      status: 'processed'
    },
    {
      id: '2',
      cardId: '2',
      description: 'Netflix',
      amount: 29.90,
      category: 'Entretenimento',
      date: new Date('2024-01-13'),
      status: 'processed'
    },
    {
      id: '3',
      cardId: '1',
      description: 'Posto Shell',
      amount: 120.00,
      category: 'Transporte',
      date: new Date('2024-01-12'),
      status: 'pending'
    },
    {
      id: '4',
      cardId: '3',
      description: 'Amazon',
      amount: 89.99,
      category: 'Compras',
      date: new Date('2024-01-11'),
      status: 'processed',
      installments: 3,
      currentInstallment: 1
    }
  ];

  selectedTabIndex = 0;

  constructor() { }

  ngOnInit(): void {
  }

  getBrandIcon(brand: string): string {
    const brandIcons: { [key: string]: string } = {
      'visa': 'credit-card',
      'mastercard': 'credit-card',
      'elo': 'credit-card',
      'american-express': 'credit-card'
    };
    return brandIcons[brand] || 'credit-card';
  }

  getCardTypeIcon(type: string): string {
    return type === 'credit' ? 'credit-card' : 'banknote';
  }

  getStatusColor(status: string): string {
    switch (status) {
      case 'active': return 'success';
      case 'blocked': return 'warning';
      case 'cancelled': return 'error';
      default: return 'default';
    }
  }

  getStatusText(status: string): string {
    switch (status) {
      case 'active': return 'Ativo';
      case 'blocked': return 'Bloqueado';
      case 'cancelled': return 'Cancelado';
      default: return status;
    }
  }

  getLimitUsagePercentage(card: CreditCard): number {
    if (card.type === 'debit') return 0;
    return ((card.limit - card.availableLimit) / card.limit) * 100;
  }

  formatCurrency(amount: number): string {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL'
    }).format(amount);
  }

  formatCardNumber(lastFourDigits: string): string {
    return `**** **** **** ${lastFourDigits}`;
  }

  getDaysUntilDue(dueDate: Date): number {
    const today = new Date();
    const diffTime = dueDate.getTime() - today.getTime();
    return Math.ceil(diffTime / (1000 * 60 * 60 * 24));
  }

  getCardForTransaction(cardId: string): CreditCard | undefined {
    return this.cards.find(card => card.id === cardId);
  }

  blockCard(card: CreditCard): void {
    console.log('Bloquear cartão:', card);
    // Implementar bloqueio do cartão
  }

  unblockCard(card: CreditCard): void {
    console.log('Desbloquear cartão:', card);
    // Implementar desbloqueio do cartão
  }

  cancelCard(card: CreditCard): void {
    console.log('Cancelar cartão:', card);
    // Implementar cancelamento do cartão
  }

  viewCardDetails(card: CreditCard): void {
    console.log('Ver detalhes do cartão:', card);
    // Implementar visualização de detalhes
  }

  viewStatement(card: CreditCard): void {
    console.log('Ver fatura do cartão:', card);
    // Implementar visualização da fatura
  }

  payBill(card: CreditCard): void {
    console.log('Pagar fatura do cartão:', card);
    // Implementar pagamento da fatura
  }

  requestCard(): void {
    console.log('Solicitar novo cartão');
    // Implementar solicitação de novo cartão
  }

  generateVirtualCard(): void {
    console.log('Gerar cartão virtual');
    // Implementar geração de cartão virtual
  }

  trackByCardId(index: number, card: CreditCard): string {
    return card.id;
  }

  trackByTransactionId(index: number, transaction: CardTransaction): string {
    return transaction.id;
  }
}
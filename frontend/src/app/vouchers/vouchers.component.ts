import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTabsModule } from '@angular/material/tabs';
import { MatChipsModule } from '@angular/material/chips';
import { MatMenuModule } from '@angular/material/menu';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { LucideAngularModule } from 'lucide-angular';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup } from '@angular/forms';

export interface Voucher {
  id: string;
  title: string;
  name: string;
  description: string;
  type: 'food' | 'transport' | 'meal' | 'gift' | 'fuel' | 'pharmacy';
  category: 'food' | 'transport' | 'meal' | 'gift' | 'fuel' | 'pharmacy';
  value: number;
  balance: number;
  expiryDate: Date;
  status: 'active' | 'expired' | 'used' | 'blocked';
  provider: string;
  company: string;
  cardNumber?: string;
  qrCode?: string;
  barcode?: string;
  usageHistory: VoucherUsage[];
  usage: VoucherUsage[]; // Alias for usageHistory to match template
}

export interface VoucherUsage {
  id: string;
  date: Date;
  amount: number;
  merchant: string;
  location: string;
  description: string;
}

@Component({
  selector: 'app-vouchers',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatTabsModule,
    MatChipsModule,
    MatMenuModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatProgressSpinnerModule,
    LucideAngularModule,
    FormsModule,
    ReactiveFormsModule
  ],
  templateUrl: './vouchers.component.html',
  styleUrls: ['./vouchers.component.scss']
})
export class VouchersComponent implements OnInit {
  vouchers: Voucher[] = [
    {
      id: '1',
      title: 'Vale Alimentação',
      name: 'Vale Alimentação',
      description: 'Vale para compras em supermercados e estabelecimentos alimentícios',
      type: 'food',
      category: 'food',
      value: 500,
      balance: 320.50,
      expiryDate: new Date('2024-12-31'),
      status: 'active',
      provider: 'Alelo',
      company: 'Alelo',
      cardNumber: '1234 5678 9012 3456',
      usageHistory: [
        {
          id: '1',
          date: new Date('2024-01-14'),
          amount: 45.30,
          merchant: 'Supermercado Pão de Açúcar',
          location: 'São Paulo, SP',
          description: 'Compras do mês'
        },
        {
          id: '2',
          date: new Date('2024-01-12'),
          amount: 23.80,
          merchant: 'Padaria Central',
          location: 'São Paulo, SP',
          description: 'Café da manhã'
        }
      ],
      usage: [
        {
          id: '1',
          date: new Date('2024-01-14'),
          amount: 45.30,
          merchant: 'Supermercado Pão de Açúcar',
          location: 'São Paulo, SP',
          description: 'Compras do mês'
        },
        {
          id: '2',
          date: new Date('2024-01-12'),
          amount: 23.80,
          merchant: 'Padaria Central',
          location: 'São Paulo, SP',
          description: 'Café da manhã'
        }
      ]
    },
    {
      id: '2',
      title: 'Vale Refeição',
      name: 'Vale Refeição',
      description: 'Vale para refeições em restaurantes e lanchonetes',
      type: 'meal',
      category: 'meal',
      value: 300,
      balance: 180.75,
      expiryDate: new Date('2024-12-31'),
      status: 'active',
      provider: 'Sodexo',
      company: 'Sodexo',
      cardNumber: '9876 5432 1098 7654',
      usageHistory: [
        {
          id: '3',
          date: new Date('2024-01-15'),
          amount: 25.50,
          merchant: 'Restaurante Sabor & Arte',
          location: 'São Paulo, SP',
          description: 'Almoço executivo'
        }
      ],
      usage: [
        {
          id: '3',
          date: new Date('2024-01-15'),
          amount: 25.50,
          merchant: 'Restaurante Sabor & Arte',
          location: 'São Paulo, SP',
          description: 'Almoço executivo'
        }
      ]
    },
    {
      id: '3',
      title: 'Vale Transporte',
      name: 'Vale Transporte',
      description: 'Vale para transporte público e aplicativos de mobilidade',
      type: 'transport',
      category: 'transport',
      value: 200,
      balance: 150.00,
      expiryDate: new Date('2024-06-30'),
      status: 'active',
      provider: 'VR Mobilidade',
      company: 'VR Mobilidade',
      usageHistory: [
        {
          id: '4',
          date: new Date('2024-01-15'),
          amount: 12.50,
          merchant: 'Uber',
          location: 'São Paulo, SP',
          description: 'Corrida para o trabalho'
        }
      ],
      usage: [
        {
          id: '4',
          date: new Date('2024-01-15'),
          amount: 12.50,
          merchant: 'Uber',
          location: 'São Paulo, SP',
          description: 'Corrida para o trabalho'
        }
      ]
    },
    {
      id: '4',
      title: 'Vale Combustível',
      name: 'Vale Combustível',
      description: 'Vale para abastecimento em postos de combustível',
      type: 'fuel',
      category: 'fuel',
      value: 400,
      balance: 280.00,
      expiryDate: new Date('2024-12-31'),
      status: 'active',
      provider: 'Ticket Car',
      company: 'Ticket Car',
      cardNumber: '5555 4444 3333 2222',
      usageHistory: [],
      usage: []
    },
    {
      id: '5',
      title: 'Vale Presente',
      name: 'Vale Presente',
      description: 'Vale presente para compras em lojas parceiras',
      type: 'gift',
      category: 'gift',
      value: 100,
      balance: 0,
      expiryDate: new Date('2024-03-31'),
      status: 'used',
      provider: 'Gift Card',
      company: 'Gift Card',
      usageHistory: [
        {
          id: '5',
          date: new Date('2024-01-10'),
          amount: 100,
          merchant: 'Magazine Luiza',
          location: 'São Paulo, SP',
          description: 'Compra de eletrônicos'
        }
      ],
      usage: [
        {
          id: '5',
          date: new Date('2024-01-10'),
          amount: 100,
          merchant: 'Magazine Luiza',
          location: 'São Paulo, SP',
          description: 'Compra de eletrônicos'
        }
      ]
    }
  ];

  filteredVouchers: Voucher[] = [];
  filterForm: FormGroup;
  selectedTabIndex = 0;
  loading = false;

  voucherTypes = [
    { value: 'all', label: 'Todos' },
    { value: 'food', label: 'Alimentação' },
    { value: 'meal', label: 'Refeição' },
    { value: 'transport', label: 'Transporte' },
    { value: 'fuel', label: 'Combustível' },
    { value: 'gift', label: 'Presente' },
    { value: 'pharmacy', label: 'Farmácia' }
  ];

  statusOptions = [
    { value: 'all', label: 'Todos' },
    { value: 'active', label: 'Ativo' },
    { value: 'expired', label: 'Expirado' },
    { value: 'used', label: 'Usado' },
    { value: 'blocked', label: 'Bloqueado' }
  ];

  constructor(private fb: FormBuilder) {
    this.filterForm = this.fb.group({
      search: [''],
      type: ['all'],
      status: ['all'],
      provider: ['']
    });
  }

  ngOnInit(): void {
    this.filteredVouchers = [...this.vouchers];
    this.filterForm.valueChanges.subscribe(() => {
      this.applyFilters();
    });
  }

  applyFilters(): void {
    const filters = this.filterForm.value;
    
    this.filteredVouchers = this.vouchers.filter(voucher => {
      // Search filter
      if (filters.search && !voucher.title.toLowerCase().includes(filters.search.toLowerCase()) &&
          !voucher.description.toLowerCase().includes(filters.search.toLowerCase())) {
        return false;
      }

      // Type filter
      if (filters.type && filters.type !== 'all' && voucher.type !== filters.type) {
        return false;
      }

      // Status filter
      if (filters.status && filters.status !== 'all' && voucher.status !== filters.status) {
        return false;
      }

      // Provider filter
      if (filters.provider && !voucher.provider.toLowerCase().includes(filters.provider.toLowerCase())) {
        return false;
      }

      return true;
    });
  }

  clearFilters(): void {
    this.filterForm.reset({
      search: '',
      type: 'all',
      status: 'all',
      provider: ''
    });
  }

  getCategoryIcon(category: string): string {
    const iconMap: { [key: string]: string } = {
      'food': 'ShoppingCart',
      'meal': 'Utensils',
      'transport': 'Car',
      'fuel': 'Fuel',
      'gift': 'Gift',
      'pharmacy': 'Pill'
    };
    return iconMap[category] || 'Ticket';
  }

  getVoucherIcon(type: string): string {
    const iconMap: { [key: string]: string } = {
      'food': 'shopping-cart',
      'meal': 'utensils',
      'transport': 'car',
      'fuel': 'fuel',
      'gift': 'gift',
      'pharmacy': 'pill'
    };
    return iconMap[type] || 'ticket';
  }

  getVoucherColor(type: string): string {
    const colorMap: { [key: string]: string } = {
      'food': 'success',
      'meal': 'primary',
      'transport': 'info',
      'fuel': 'warning',
      'gift': 'secondary',
      'pharmacy': 'error'
    };
    return colorMap[type] || 'default';
  }

  getStatusColor(status: string): string {
    switch (status) {
      case 'active': return 'success';
      case 'expired': return 'error';
      case 'used': return 'secondary';
      case 'blocked': return 'warning';
      default: return 'default';
    }
  }

  getStatusText(status: string): string {
    switch (status) {
      case 'active': return 'Ativo';
      case 'expired': return 'Expirado';
      case 'used': return 'Usado';
      case 'blocked': return 'Bloqueado';
      default: return status;
    }
  }

  getTypeLabel(type: string): string {
    const typeMap: { [key: string]: string } = {
      'food': 'Alimentação',
      'meal': 'Refeição',
      'transport': 'Transporte',
      'fuel': 'Combustível',
      'gift': 'Presente',
      'pharmacy': 'Farmácia'
    };
    return typeMap[type] || type;
  }

  formatCurrency(amount: number): string {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL'
    }).format(amount);
  }

  getBalancePercentage(voucher: Voucher): number {
    return (voucher.balance / voucher.value) * 100;
  }

  getDaysUntilExpiry(expiryDate: Date): number {
    const today = new Date();
    const diffTime = expiryDate.getTime() - today.getTime();
    return Math.ceil(diffTime / (1000 * 60 * 60 * 24));
  }

  isExpiringSoon(voucher: Voucher): boolean {
    return this.getDaysUntilExpiry(voucher.expiryDate) <= 30 && voucher.status === 'active';
  }

  viewVoucherDetails(voucher: Voucher): void {
    console.log('Ver detalhes do vale:', voucher);
    // Implementar visualização de detalhes
  }

  blockVoucher(voucher: Voucher): void {
    console.log('Bloquear vale:', voucher);
    // Implementar bloqueio do vale
  }

  unblockVoucher(voucher: Voucher): void {
    console.log('Desbloquear vale:', voucher);
    // Implementar desbloqueio do vale
  }

  requestVoucher(): void {
    console.log('Solicitar novo vale');
    // Implementar solicitação de novo vale
  }

  rechargeVoucher(voucher: Voucher): void {
    console.log('Recarregar vale:', voucher);
    // Implementar recarga do vale
  }

  exportStatement(voucher: Voucher): void {
    console.log('Exportar extrato do vale:', voucher);
    // Implementar exportação do extrato
  }

  trackByVoucherId(index: number, voucher: Voucher): string {
    return voucher.id;
  }

  trackByUsageId(index: number, usage: VoucherUsage): string {
    return usage.id;
  }

  viewVoucher(voucher: Voucher): void {
    console.log('Visualizar vale:', voucher);
    // Implementar visualização do vale
  }

  useVoucher(voucher: Voucher): void {
    console.log('Usar vale:', voucher);
    // Implementar uso do vale
  }

  editVoucher(voucher: Voucher): void {
    console.log('Editar vale:', voucher);
    // Implementar edição do vale
  }

  deleteVoucher(voucher: Voucher): void {
    console.log('Excluir vale:', voucher);
    // Implementar exclusão do vale
  }

  getExpiryStatus(expiryDate: Date): string {
    const today = new Date();
    const expiry = new Date(expiryDate);
    const daysUntilExpiry = Math.ceil((expiry.getTime() - today.getTime()) / (1000 * 60 * 60 * 24));
    
    if (daysUntilExpiry < 0) {
      return 'expired';
    } else if (daysUntilExpiry <= 30) {
      return 'expiring-soon';
    } else {
      return 'valid';
    }
  }

  formatDate(date: Date): string {
    return new Date(date).toLocaleDateString('pt-BR', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric'
    });
  }

  getStatusLabel(status: string): string {
    const statusLabels: { [key: string]: string } = {
      'active': 'Ativo',
      'expired': 'Expirado',
      'used': 'Usado',
      'blocked': 'Bloqueado'
    };
    return statusLabels[status] || status;
  }
}
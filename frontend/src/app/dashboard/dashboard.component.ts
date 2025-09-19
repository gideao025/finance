import { Component, OnInit } from '@angular/core';

interface StatsData {
  title: string;
  value: string;
  change: string;
  changeType: 'positive' | 'negative';
  icon: string;
  color: string;
}

interface Transaction {
  id: string;
  description: string;
  amount: number;
  type: 'income' | 'expense';
  category: string;
  date: Date;
  status: 'completed' | 'pending';
}

interface Goal {
  id: string;
  title: string;
  target: number;
  current: number;
  deadline: Date;
  category: string;
}

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  
  statsData: StatsData[] = [
    {
      title: 'Saldo Total',
      value: 'R$ 12.450,00',
      change: '+12.5%',
      changeType: 'positive',
      icon: 'DollarSign',
      color: 'primary'
    },
    {
      title: 'Receitas',
      value: 'R$ 8.200,00',
      change: '+8.2%',
      changeType: 'positive',
      icon: 'TrendingUp',
      color: 'success'
    },
    {
      title: 'Despesas',
      value: 'R$ 3.750,00',
      change: '-5.1%',
      changeType: 'negative',
      icon: 'TrendingDown',
      color: 'error'
    },
    {
      title: 'Cartões',
      value: 'R$ 1.250,00',
      change: '+2.3%',
      changeType: 'positive',
      icon: 'CreditCard',
      color: 'warning'
    }
  ];

  recentTransactions: Transaction[] = [
    {
      id: '1',
      description: 'Salário - Empresa XYZ',
      amount: 5500.00,
      type: 'income',
      category: 'Salário',
      date: new Date('2024-01-15'),
      status: 'completed'
    },
    {
      id: '2',
      description: 'Supermercado ABC',
      amount: -320.50,
      type: 'expense',
      category: 'Alimentação',
      date: new Date('2024-01-14'),
      status: 'completed'
    },
    {
      id: '3',
      description: 'Freelance - Projeto Web',
      amount: 1200.00,
      type: 'income',
      category: 'Freelance',
      date: new Date('2024-01-13'),
      status: 'pending'
    },
    {
      id: '4',
      description: 'Conta de Luz',
      amount: -180.75,
      type: 'expense',
      category: 'Utilidades',
      date: new Date('2024-01-12'),
      status: 'completed'
    },
    {
      id: '5',
      description: 'Gasolina',
      amount: -95.00,
      type: 'expense',
      category: 'Transporte',
      date: new Date('2024-01-11'),
      status: 'completed'
    }
  ];

  goals: Goal[] = [
    {
      id: '1',
      title: 'Reserva de Emergência',
      target: 15000,
      current: 8500,
      deadline: new Date('2024-12-31'),
      category: 'Poupança'
    },
    {
      id: '2',
      title: 'Viagem para Europa',
      target: 8000,
      current: 3200,
      deadline: new Date('2024-07-15'),
      category: 'Lazer'
    },
    {
      id: '3',
      title: 'Novo Notebook',
      target: 4500,
      current: 2800,
      deadline: new Date('2024-03-30'),
      category: 'Tecnologia'
    }
  ];

  // Chart data
  expenseChartData = {
    labels: ['Alimentação', 'Transporte', 'Lazer', 'Utilidades', 'Saúde', 'Outros'],
    datasets: [{
      data: [1200, 800, 600, 400, 300, 450],
      backgroundColor: [
        'var(--primary-500)',
        'var(--secondary-500)',
        'var(--success-500)',
        'var(--warning-500)',
        'var(--error-500)',
        'var(--info-500)'
      ],
      borderWidth: 0
    }]
  };

  incomeVsExpenseData = {
    labels: ['Jan', 'Fev', 'Mar', 'Abr', 'Mai', 'Jun'],
    datasets: [
      {
        label: 'Receitas',
        data: [5500, 6200, 5800, 6500, 7200, 8200],
        borderColor: 'var(--success-500)',
        backgroundColor: 'var(--success-100)',
        tension: 0.4,
        fill: true
      },
      {
        label: 'Despesas',
        data: [3200, 3800, 3500, 4100, 3900, 3750],
        borderColor: 'var(--error-500)',
        backgroundColor: 'var(--error-100)',
        tension: 0.4,
        fill: true
      }
    ]
  };

  chartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: 'bottom' as const,
        labels: {
          usePointStyle: true,
          padding: 20
        }
      }
    },
    scales: {
      y: {
        beginAtZero: true,
        grid: {
          color: 'var(--border-light)'
        }
      },
      x: {
        grid: {
          color: 'var(--border-light)'
        }
      }
    }
  };

  doughnutOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: 'bottom' as const,
        labels: {
          usePointStyle: true,
          padding: 15
        }
      }
    }
  };

  constructor() { }

  ngOnInit(): void {
    // Inicialização do componente
  }

  getTransactionIcon(category: string): string {
    const icons: { [key: string]: string } = {
      'Salário': 'DollarSign',
      'Freelance': 'TrendingUp',
      'Alimentação': 'ShoppingCart',
      'Transporte': 'Car',
      'Utilidades': 'Zap',
      'Lazer': 'Coffee'
    };
    return icons[category] || 'Circle';
  }

  getStatusColor(status: string): string {
    return status === 'completed' ? 'success' : 'warning';
  }

  formatCurrency(value: number): string {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL'
    }).format(Math.abs(value));
  }

  getGoalProgress(goal: Goal): number {
    return Math.round((goal.current / goal.target) * 100);
  }

  getDaysUntilDeadline(deadline: Date): number {
    const today = new Date();
    const diffTime = deadline.getTime() - today.getTime();
    return Math.ceil(diffTime / (1000 * 60 * 60 * 24));
  }
}
import { Component, Input } from '@angular/core';

interface Goal {
  id: string;
  title: string;
  target: number;
  current: number;
  deadline: Date;
  category: string;
}

@Component({
  selector: 'app-goals-card',
  templateUrl: './goals-card.component.html',
  styleUrls: ['./goals-card.component.scss']
})
export class GoalsCardComponent {
  @Input() title: string = '';
  @Input() subtitle: string = '';
  @Input() goals: Goal[] = [];

  constructor() { }

  getGoalProgress(goal: Goal): number {
    return Math.round((goal.current / goal.target) * 100);
  }

  getDaysUntilDeadline(deadline: Date): number {
    const today = new Date();
    const diffTime = deadline.getTime() - today.getTime();
    return Math.ceil(diffTime / (1000 * 60 * 60 * 24));
  }

  formatCurrency(value: number): string {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL'
    }).format(value);
  }

  getProgressColor(progress: number): string {
    if (progress >= 80) return 'success';
    if (progress >= 50) return 'warning';
    return 'primary';
  }

  getCategoryIcon(category: string): string {
    const icons: { [key: string]: string } = {
      'Poupança': 'PiggyBank',
      'Lazer': 'Coffee',
      'Tecnologia': 'Laptop',
      'Casa': 'Home',
      'Educação': 'BookOpen',
      'Saúde': 'Heart',
      'Viagem': 'Plane'
    };
    return icons[category] || 'Target';
  }

  getDeadlineStatus(days: number): 'urgent' | 'warning' | 'normal' {
    if (days <= 30) return 'urgent';
    if (days <= 90) return 'warning';
    return 'normal';
  }

  trackByGoalId(index: number, goal: Goal): string {
    return goal.id;
  }
}
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-stats-card',
  templateUrl: './stats-card.component.html',
  styleUrls: ['./stats-card.component.scss']
})
export class StatsCardComponent {
  @Input() title: string = '';
  @Input() value: string = '';
  @Input() change: string = '';
  @Input() changeType: 'positive' | 'negative' = 'positive';
  @Input() icon: string = '';
  @Input() color: string = 'primary';

  constructor() { }

  get changeIcon(): string {
    return this.changeType === 'positive' ? 'TrendingUp' : 'TrendingDown';
  }

  get changeClass(): string {
    return `change-${this.changeType}`;
  }

  get cardClass(): string {
    return `stats-card stats-card--${this.color}`;
  }
}
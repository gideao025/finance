import { Component, Input } from '@angular/core';
import { ChartConfiguration, ChartType } from 'chart.js';

@Component({
  selector: 'app-chart-card',
  templateUrl: './chart-card.component.html',
  styleUrls: ['./chart-card.component.scss']
})
export class ChartCardComponent {
  @Input() title: string = '';
  @Input() subtitle: string = '';
  @Input() chartData: any;
  @Input() chartOptions: any;
  @Input() chartType: ChartType = 'line';

  constructor() { }
}
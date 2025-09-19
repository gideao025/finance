import { Component, OnInit, OnDestroy } from '@angular/core';
import { ResponsiveService } from './shared/services/responsive.service';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit, OnDestroy {
  title = 'Finance Dashboard';
  
  sidenavOpened = true;
  isMobile = false;
  private destroy$ = new Subject<void>();
  
  menuItems = [
    { icon: 'Home', label: 'Dashboard', route: '/dashboard' },
    { icon: 'ArrowUpDown', label: 'Transações', route: '/transactions' },
    { icon: 'CreditCard', label: 'Cartões', route: '/cards' },
    { icon: 'Gift', label: 'Vales', route: '/vouchers' }
  ];

  constructor(private responsiveService: ResponsiveService) {}

  ngOnInit() {
    // Subscribe to mobile breakpoint changes
    this.responsiveService.isMobile$
      .pipe(takeUntil(this.destroy$))
      .subscribe(isMobile => {
        this.isMobile = isMobile;
        // Auto-close sidebar on mobile
        if (isMobile) {
          this.sidenavOpened = false;
        } else {
          this.sidenavOpened = true;
        }
      });
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  toggleSidenav() {
    this.sidenavOpened = !this.sidenavOpened;
  }
}
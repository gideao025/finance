import { Injectable } from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Observable } from 'rxjs';
import { map, shareReplay } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ResponsiveService {
  
  // Breakpoint observables
  isHandset$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
    .pipe(
      map(result => result.matches),
      shareReplay()
    );

  isTablet$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Tablet)
    .pipe(
      map(result => result.matches),
      shareReplay()
    );

  isWeb$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Web)
    .pipe(
      map(result => result.matches),
      shareReplay()
    );

  // Custom breakpoints
  isMobile$: Observable<boolean> = this.breakpointObserver.observe('(max-width: 767px)')
    .pipe(
      map(result => result.matches),
      shareReplay()
    );

  isDesktop$: Observable<boolean> = this.breakpointObserver.observe('(min-width: 1024px)')
    .pipe(
      map(result => result.matches),
      shareReplay()
    );

  constructor(private breakpointObserver: BreakpointObserver) {}

  // Utility methods
  isHandset(): boolean {
    return this.breakpointObserver.isMatched(Breakpoints.Handset);
  }

  isTablet(): boolean {
    return this.breakpointObserver.isMatched(Breakpoints.Tablet);
  }

  isWeb(): boolean {
    return this.breakpointObserver.isMatched(Breakpoints.Web);
  }

  isMobile(): boolean {
    return this.breakpointObserver.isMatched('(max-width: 767px)');
  }

  isDesktop(): boolean {
    return this.breakpointObserver.isMatched('(min-width: 1024px)');
  }
}
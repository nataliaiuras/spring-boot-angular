import {Component, inject, OnDestroy, OnInit, signal} from '@angular/core';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MediaMatcher} from '@angular/cdk/layout';
import {MatListModule} from '@angular/material/list';
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MegaMenuModule} from 'primeng/megamenu';
import {MegaMenuItem} from 'primeng/api';
import {MatMenu, MatMenuItem, MatMenuTrigger} from '@angular/material/menu';
import {MatDivider} from '@angular/material/divider';
import {AuthService, UserProfile} from '../../services/auth/auth';
import {Router, RouterLink, RouterLinkActive, RouterOutlet} from '@angular/router';
import {NgOptimizedImage} from '@angular/common';

@Component({
  selector: 'app-sidenav',
  templateUrl: 'sidenav.html',
  styleUrl: 'sidenav.css',
  imports: [MatToolbarModule, MatButtonModule, MatIconModule, MatSidenavModule, MatListModule, MegaMenuModule, RouterLink, RouterLinkActive, RouterOutlet, MatMenuTrigger, MatMenu, NgOptimizedImage, MatDivider, MatMenuItem],
  standalone: true,
})
export class Sidenav implements OnDestroy, OnInit {

  protected readonly fillerNav = ['Vehicles', 'Posts', 'Calendar', 'Documents', 'Reports', 'Tasks', 'Settings', 'Help', 'Logout'];

  protected readonly isMobile = signal(true);

  private readonly _mobileQuery: MediaQueryList;
  private readonly _mobileQueryListener: () => void;

  imagePath: string = 'assets/logo.png';
  logoAlt = 'logo';

  // User profile information
  userProfile: UserProfile | null = null;
  username: string = '';
  userRole: string = '';

  constructor(private authService: AuthService, private router: Router) {
    const media = inject(MediaMatcher);

    this._mobileQuery = media.matchMedia('(max-width: 600px)');
    this.isMobile.set(this._mobileQuery.matches);
    this._mobileQueryListener = () => this.isMobile.set(this._mobileQuery.matches);
    this._mobileQuery.addEventListener('change', this._mobileQueryListener);

    // Subscribe to user profile changes
    this.authService.userProfile$.subscribe(profile => {
      this.userProfile = profile;
      this.username = profile?.username || '';
      this.userRole = profile?.role || '';
    });
  }

  ngOnDestroy(): void {
    this._mobileQuery.removeEventListener('change', this._mobileQueryListener);
  }

  items: MegaMenuItem[] | undefined;

  ngOnInit() {
    this.items = [
      {
        label: 'Home',
        icon: 'pi pi-home'
      },
      {
        label: 'Features',
        icon: 'pi pi-star'
      },
      {
        label: 'Projects',
        icon: 'pi pi-search',
      },
      {
        label: 'Contact',
        icon: 'pi pi-envelope'
      }
    ]
  }

  private readonly STORAGE_KEY = 'username';
  private readonly TOKEN_KEY = 'token';

  logout(): void {
    sessionStorage.removeItem(this.STORAGE_KEY);
    sessionStorage.removeItem(this.TOKEN_KEY);

    this.authService.isAuthenticatedSubject.next(false);
    this.authService.logout();

    this.router.navigate(['/auth/login'], {
      replaceUrl: true,
    }).then(() => {
    });
  }


}

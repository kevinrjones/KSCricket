import {Component} from '@angular/core';
import {AuthenticationService} from '../../services/authentication.service';
import {
  faCaretDown,
  faAngleDown,
  faRightFromBracket,
  faRightToBracket,
  faUser
} from '@fortawesome/free-solid-svg-icons';
import {environment} from '../../../environments/environment';
import {MenuItem} from "primeng/api";
import {ConfigService} from "../../services/config.service";

@Component({
  selector: 'app-nav-menu',
  templateUrl: './nav-menu.component.html',
  styleUrls: ['./nav-menu.component.css'],
  standalone: false
})
export class NavMenuComponent {
  isExpanded = false;
  public authenticated$ = this.auth.getIsAuthenticated();
  public anonymous$ = this.auth.getIsAnonymous();
  public logoutUrl$ = this.auth.getLogoutUrl();
  public name$ = this.auth.getUsername();

  faUser = faUser
  faRightToBracket = faRightToBracket
  faRightFromBracket = faRightFromBracket
  faCaretDown = faCaretDown
  faAngleDown = faAngleDown

  protected readonly environment = environment;
  private authenticated: boolean = false;
  private logoutUrl: string = "";
  private authorityUrl: string | undefined;
  notLoggedInMenu: Array<MenuItem> = []


  ngOnInit() {
    this.authenticated$.subscribe(value => {
      this.authenticated = value
    })

    this.logoutUrl$.subscribe(value => {
      this.logoutUrl = value ?? ""
      this.loggedInMenu[0].url = this.logoutUrl;
    })

    const config = this.configService.getConfig();
    this.authorityUrl = config?.authorityUrl
    this.buildNotLoggedInMenu()
  }

  constructor(private auth: AuthenticationService, private configService: ConfigService) {
  }

  private readonly REGISTRATION_PATH = 'identity/account/register';

  private buildRegistrationUrl(): string {
    if (!this.authorityUrl) return `${this.REGISTRATION_PATH}`;

    return `${this.authorityUrl}/${this.REGISTRATION_PATH}`;
  }

  collapse() {
    this.isExpanded = false;
  }

  toggle() {
    this.isExpanded = !this.isExpanded;
  }

  topLevelMenu: Array<MenuItem> = [
    {
      label: 'Home',
      icon: 'pi pi-fw pi-home',
      routerLink: ['/']
    },
    {
      label: 'News',
      icon: 'pi pi-fw pi-home',
      items: [
        {
          label: 'News',
          icon: 'pi pi-fw pi-home',
          url: 'https://acscricket.com/?page_id=4280'
        },
        {
          label: 'Recent Web Site Updates',
          icon: 'pi pi-fw pi-home',
          url: 'https://acscricket.com/?page_id=2302'
        },
      ]
    },
    {
      label: 'About',
      icon: 'pi pi-fw pi-home',
      items: [
        {
          label: 'About the ACS',
          icon: 'pi pi-fw pi-home',
          url: 'https://acscricket.com/?page_id=267'
        },
        {
          label: 'Join the ACS',
          icon: 'pi pi-fw pi-home',
          url: 'https://acscricket.com/?page_id=294'
        },
        {
          label: 'Contact Us',
          icon: 'pi pi-fw pi-home',
          url: 'https://acscricket.com/?page_id=37'
        },
        {
          label: 'FAQ',
          icon: 'pi pi-fw pi-home',
          url: 'https://acscricket.com/?page_id=3897'
        },
        {
          label: 'Ask the ACS',
          icon: 'pi pi-fw pi-home',
          url: 'https://acscricket.com/?page_id=2025'
        },
        {
          label: 'Awards',
          icon: 'pi pi-fw pi-home',
          url: 'https://acscricket.com/?page_id=165'
        },
        {
          label: 'Advertising',
          icon: 'pi pi-fw pi-home',
          url: 'https://acscricket.com/?page_id=966'
        },
      ]
    },
    {
      label: 'Records',
      icon: 'pi pi-fw pi-home',
      url: 'https://acscricket.com/?page_id=5580'
    },
    {
      label: 'Resources',
      icon: 'pi pi-fw pi-home',
      items: [
        {
          label: 'Publications',
          icon: 'pi pi-fw pi-home',
          url: 'https://acscricket.com/?page_id=55'
        },
        {
          label: 'Library of ACS Books',
          icon: 'pi pi-fw pi-home',
          url: 'https://acscricket.com/?page_id=4277'
        },
        {
          label: 'Cricket 1882-1914',
          icon: 'pi pi-fw pi-home',
          url: 'https://acscricket.com/?page_id=1974'
        },
        {
          label: 'Research',
          icon: 'pi pi-fw pi-home',
          url: 'https://acscricket.com/?page_id=376'
        },
        {
          label: 'New Biographical Information',
          icon: 'pi pi-fw pi-home',
          url: 'https://acscricket.com/?page_id=1006'
        },
        {
          label: 'Links',
          icon: 'pi pi-fw pi-home',
          url: 'https://acscricket.com/?page_id=30'
        },
        {
          label: 'ACS List A',
          icon: 'pi pi-fw pi-home',
          url: 'https://stats.acscricket.com/ListA/Description.html'
        },
      ]
    },
    {
      label: `Shop`,
      icon: 'pi pi-fw pi-home',
      items: [
        {
          label: 'Shop',
          icon: 'pi pi-fw pi-home',
          url: 'https://shop.acscricket.com/'
        },
        {
          label: "Member's Marketplace",
          icon: 'pi pi-fw pi-home',
          url: 'https://archive.acscricket.com/cms/marketplace/show_items.pl'
        }
      ]
    },
    {
      label: 'Members',
      icon: 'pi pi-fw pi-home',
      url: 'https://acscricket.com/?page_id=144',
    }
  ]

  buildNotLoggedInMenu() {
    this.notLoggedInMenu = [
      {
        label: 'Login',
        // icon: 'pi pi-fw pi-home',
        url: '/bff/login',
        target: '_self'
      },
      {
        url: this.buildRegistrationUrl(),
        label: 'Register',
        target: '_self',
      }
    ]
  }

  loggedInMenu: Array<MenuItem> = [
    {
      label: 'Logout',
      icon: 'pi pi-fw pi-home',
      url: this.logoutUrl
    },
    {
      label: 'Some other stuff here',
    }]
}

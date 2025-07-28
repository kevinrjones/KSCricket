import {Component, OnInit} from '@angular/core';
import {faFacebook, faInstagram, faTwitter} from '@fortawesome/free-brands-svg-icons';

@Component({
    selector: 'app-header',
    templateUrl: './header.component.html',
    styleUrls: ['./header.component.css'],
    standalone: false
})
export class HeaderComponent implements OnInit {

  faTwitter = faTwitter;
  faFacebook = faFacebook;
  faInstagram = faInstagram;

  constructor() {
  }

  ngOnInit(): void {
  }

}

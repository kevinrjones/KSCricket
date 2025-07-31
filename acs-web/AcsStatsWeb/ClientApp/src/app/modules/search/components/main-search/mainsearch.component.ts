import {Component} from '@angular/core';
import {Router} from '@angular/router';

@Component({
    selector: 'app-main-search',
    templateUrl: './mainsearch.component.html',
    styleUrls: ['./mainsearch.component.css'],
    standalone: false
})
export class MainSearchComponent {

  links = [
    {path: '/search/card', label: 'Scorecard', isActive: true},
    {path: '/search/player', label: 'Player', isActive: false}
  ];
  activeLink: string = "card";

  constructor(private route: Router) {
  }

  ngOnInit() {
    this.activeLink = this.route.url
  }

}

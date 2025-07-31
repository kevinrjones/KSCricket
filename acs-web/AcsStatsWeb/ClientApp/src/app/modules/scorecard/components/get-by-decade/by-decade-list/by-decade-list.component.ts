import {Component, Input} from '@angular/core';
import {Observable} from 'rxjs';
import {ActivatedRoute} from '@angular/router';

@Component({
    selector: 'app-by-decade-list',
    templateUrl: './by-decade-list.component.html',
    styleUrls: ['./by-decade-list.component.css'],
    standalone: false
})
export class ScorecardByDecadeListComponent {
  @Input() byYearList$!: Observable<{ [decade: number]: string[] }>;
  name!: string | null;

  constructor(private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      this.name = params.get('name');
    })
  }

  encodeDate(year: string) {
    return encodeURIComponent(year);
  }

}

import {Component, Input, OnInit} from '@angular/core';
import {FormGroup} from '@angular/forms';
import {Observable} from 'rxjs';
import {MatchSubTypeModel} from '../../../../models/match-sub-type.model';

@Component({
  selector: 'app-search-header',
  templateUrl: './records-search-select.component.html',
  styleUrls: ['./records-search-select.component.css'],
  standalone: false
})
export class RecordsSearchSelectComponent implements OnInit {

  @Input() parentFormGroup!: FormGroup
  @Input() matchSubTypes$!: Observable<MatchSubTypeModel[]>

  ngOnInit(): void {
  }

  matchTypes = [
    {
      name: "Test Matches",
      key: "t"
    },
    {
      name: "ODIs",
      key: "o"
    },
    {
      name: "International T20s",
      key: "itt"
    },
    {
      name: "First Class",
      key: "f"
    },
    {
      name: "List A",
      key: "a"
    },
    {
      name: "T20",
      key: "tt"
    },
    {
      name: "Women's Test Matches",
      key: "wt"
    },
    {
      name: "Women's ODIs",
      key: "wo"
    },
    {
      name: "Women's International T20s",
      key: "witt"
    },
    {
      name: "Women's First CLass",
      key: "wf"
    },
    {
      name: "Women's List A",
      key: "wa"
    },
    {
      name: "Women's T20",
      key: "wtt"
    },
    {
      name: "Second XI Championship",
      key: "sec"
    },
    {
      name: "Second XI Trophy",
      key: "set"
    },
    {
      name: "Second XI T20",
      key: "sett"
    },
    {
      name: "Minor Counties Championship",
      key: "minc"
    },
    {
      name: "Minor Counties Trophy",
      key: "mint"
    },
    {
      name: "Minor Counties T20",
      key: "mintt"
    },
    {
      name: "Under 19s Test",
      key: "ut"
    },
    {
      name: "Under 19s ODI",
      key: "uo"
    },
    {
      name: "Under 19s T20",
      key: "ut"
    },
  ]

  matchTypesEx = [
    {
      name: "50",
      key: "tt"
    },
    {
      name: "ODIs",
      key: "o"
    },
    {
      name: "International T20s",
      key: "itt"
    },
    {
      name: "First Class",
      key: "f"
    },
    {
      name: "50",
      key: "50"
    },
    {
      name: "100",
      key: "100"
    },
    {
      name: "200",
      key: "200"
    },
    {
      name: "500",
      key: "500"
    },
  ]

  pageSizes = [
    {
      name: "50",
      key: 50
    },
    {
      name: "100",
      key: 100
    },
    {
      name: 200,
      key: "200"
    },
    {
      name: 500,
      key: "500"
    },
  ]

}

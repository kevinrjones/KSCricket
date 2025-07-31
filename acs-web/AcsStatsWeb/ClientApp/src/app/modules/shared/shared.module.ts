import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule} from '@angular/router';
import {PagingComponent} from './components/paging/paging.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {RecordSummaryComponent} from './components/record-summary/batting-summary.component';
import {RecordsSearchNavComponent} from './components/records-search-nav/records-search-nav.component';
import {RecordsSearchSelectComponent} from './components/search-select/records-search-select.component';
import {RecordsMainNavComponent} from './components/records-main-nav/records-main-nav.component';
import {RecordsMainFooterComponent} from './components/records-main-footer/records-main-footer.component';
import {RecordsUIComponent} from './components/records-ui/records-ui.component';
import {CollapseComponent} from './components/collapse/collapse.component';
import {Button} from "primeng/button";
import {RadioButtonModule} from "primeng/radiobutton";
import {CheckboxModule} from 'primeng/checkbox';
import {SelectModule} from 'primeng/select';
import {IftaLabelModule} from 'primeng/iftalabel';
import {AutoCompleteModule} from 'primeng/autocomplete';
import {InputNumberModule} from 'primeng/inputnumber';
import {DatePickerModule} from 'primeng/datepicker';
import {MenubarModule} from 'primeng/menubar';
import {TieredMenuModule} from 'primeng/tieredmenu';
import {MessageModule} from 'primeng/message';
import {TooltipModule} from "primeng/tooltip";
import {ToastModule} from 'primeng/toast';
import {TabsModule} from 'primeng/tabs';
import {Menu} from "primeng/menu";

@NgModule({
  declarations: [RecordSummaryComponent,
    RecordsSearchNavComponent,
    PagingComponent,
    RecordsSearchSelectComponent,
    RecordsMainNavComponent,
    RecordsMainFooterComponent,
    RecordsUIComponent,
    CollapseComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    RouterModule,
    FontAwesomeModule,
    Button,
    RadioButtonModule,
    CheckboxModule,
    SelectModule,
    IftaLabelModule,
    AutoCompleteModule,
    InputNumberModule,
    DatePickerModule,
    MenubarModule,
    TieredMenuModule,
    MessageModule,
    ToastModule,
    TabsModule,
    TooltipModule,
    Menu,
  ],
  exports: [
    RecordsMainNavComponent,
    RecordsMainFooterComponent,
    RecordSummaryComponent,
    RecordsSearchNavComponent,
    PagingComponent,
    RecordsSearchSelectComponent,
    RecordsUIComponent,
    CollapseComponent,
    RadioButtonModule,
    CheckboxModule,
    SelectModule,
    IftaLabelModule,
    AutoCompleteModule,
    InputNumberModule,
    DatePickerModule,
    MenubarModule,
    TieredMenuModule,
    MessageModule,
    ToastModule,
    TabsModule,
    TooltipModule,
  ]
})
export class SharedModule {
}

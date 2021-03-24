import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { JhipsterSampleApplicationNewSharedModule } from 'app/shared/shared.module';
import { JhipsterSampleApplicationNewCoreModule } from 'app/core/core.module';
import { JhipsterSampleApplicationNewAppRoutingModule } from './app-routing.module';
import { JhipsterSampleApplicationNewHomeModule } from './home/home.module';
import { JhipsterSampleApplicationNewEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    JhipsterSampleApplicationNewSharedModule,
    JhipsterSampleApplicationNewCoreModule,
    JhipsterSampleApplicationNewHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    JhipsterSampleApplicationNewEntityModule,
    JhipsterSampleApplicationNewAppRoutingModule,
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent],
})
export class JhipsterSampleApplicationNewAppModule {}

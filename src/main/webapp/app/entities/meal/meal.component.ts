import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMeal } from 'app/shared/model/meal.model';
import { MealService } from './meal.service';
import { MealDeleteDialogComponent } from './meal-delete-dialog.component';

@Component({
  selector: 'jhi-meal',
  templateUrl: './meal.component.html',
})
export class MealComponent implements OnInit, OnDestroy {
  meals?: IMeal[];
  eventSubscriber?: Subscription;

  constructor(protected mealService: MealService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.mealService.query().subscribe((res: HttpResponse<IMeal[]>) => (this.meals = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInMeals();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IMeal): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInMeals(): void {
    this.eventSubscriber = this.eventManager.subscribe('mealListModification', () => this.loadAll());
  }

  delete(meal: IMeal): void {
    const modalRef = this.modalService.open(MealDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.meal = meal;
  }
}

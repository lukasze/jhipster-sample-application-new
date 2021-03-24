import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFood } from 'app/shared/model/food.model';
import { FoodService } from './food.service';
import { FoodDeleteDialogComponent } from './food-delete-dialog.component';

@Component({
  selector: 'jhi-food',
  templateUrl: './food.component.html',
})
export class FoodComponent implements OnInit, OnDestroy {
  foods?: IFood[];
  eventSubscriber?: Subscription;

  constructor(protected foodService: FoodService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.foodService.query().subscribe((res: HttpResponse<IFood[]>) => (this.foods = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInFoods();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IFood): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInFoods(): void {
    this.eventSubscriber = this.eventManager.subscribe('foodListModification', () => this.loadAll());
  }

  delete(food: IFood): void {
    const modalRef = this.modalService.open(FoodDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.food = food;
  }
}

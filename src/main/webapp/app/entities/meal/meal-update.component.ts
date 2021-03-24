import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IMeal, Meal } from 'app/shared/model/meal.model';
import { MealService } from './meal.service';
import { IFood } from 'app/shared/model/food.model';
import { FoodService } from 'app/entities/food/food.service';

@Component({
  selector: 'jhi-meal-update',
  templateUrl: './meal-update.component.html',
})
export class MealUpdateComponent implements OnInit {
  isSaving = false;
  foods: IFood[] = [];
  dateDp: any;

  editForm = this.fb.group({
    id: [],
    date: [],
    quantity: [],
    food: [],
  });

  constructor(
    protected mealService: MealService,
    protected foodService: FoodService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ meal }) => {
      this.updateForm(meal);

      this.foodService.query().subscribe((res: HttpResponse<IFood[]>) => (this.foods = res.body || []));
    });
  }

  updateForm(meal: IMeal): void {
    this.editForm.patchValue({
      id: meal.id,
      date: meal.date,
      quantity: meal.quantity,
      food: meal.food,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const meal = this.createFromForm();
    if (meal.id !== undefined) {
      this.subscribeToSaveResponse(this.mealService.update(meal));
    } else {
      this.subscribeToSaveResponse(this.mealService.create(meal));
    }
  }

  private createFromForm(): IMeal {
    return {
      ...new Meal(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value,
      quantity: this.editForm.get(['quantity'])!.value,
      food: this.editForm.get(['food'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMeal>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IFood): any {
    return item.id;
  }
}

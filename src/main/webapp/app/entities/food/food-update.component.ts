import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IFood, Food } from 'app/shared/model/food.model';
import { FoodService } from './food.service';

@Component({
  selector: 'jhi-food-update',
  templateUrl: './food-update.component.html',
})
export class FoodUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
    calories: [],
    proteins: [],
    carbs: [],
    fats: [],
  });

  constructor(protected foodService: FoodService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ food }) => {
      this.updateForm(food);
    });
  }

  updateForm(food: IFood): void {
    this.editForm.patchValue({
      id: food.id,
      name: food.name,
      calories: food.calories,
      proteins: food.proteins,
      carbs: food.carbs,
      fats: food.fats,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const food = this.createFromForm();
    if (food.id !== undefined) {
      this.subscribeToSaveResponse(this.foodService.update(food));
    } else {
      this.subscribeToSaveResponse(this.foodService.create(food));
    }
  }

  private createFromForm(): IFood {
    return {
      ...new Food(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      calories: this.editForm.get(['calories'])!.value,
      proteins: this.editForm.get(['proteins'])!.value,
      carbs: this.editForm.get(['carbs'])!.value,
      fats: this.editForm.get(['fats'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFood>>): void {
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
}

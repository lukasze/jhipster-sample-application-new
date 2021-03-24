import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'food',
        loadChildren: () => import('./food/food.module').then(m => m.JhipsterSampleApplicationNewFoodModule),
      },
      {
        path: 'meal',
        loadChildren: () => import('./meal/meal.module').then(m => m.JhipsterSampleApplicationNewMealModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class JhipsterSampleApplicationNewEntityModule {}

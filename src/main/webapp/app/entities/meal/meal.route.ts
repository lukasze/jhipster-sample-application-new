import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IMeal, Meal } from 'app/shared/model/meal.model';
import { MealService } from './meal.service';
import { MealComponent } from './meal.component';
import { MealDetailComponent } from './meal-detail.component';
import { MealUpdateComponent } from './meal-update.component';

@Injectable({ providedIn: 'root' })
export class MealResolve implements Resolve<IMeal> {
  constructor(private service: MealService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMeal> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((meal: HttpResponse<Meal>) => {
          if (meal.body) {
            return of(meal.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Meal());
  }
}

export const mealRoute: Routes = [
  {
    path: '',
    component: MealComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Meals',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MealDetailComponent,
    resolve: {
      meal: MealResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Meals',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MealUpdateComponent,
    resolve: {
      meal: MealResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Meals',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MealUpdateComponent,
    resolve: {
      meal: MealResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Meals',
    },
    canActivate: [UserRouteAccessService],
  },
];

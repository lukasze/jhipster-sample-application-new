import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IFood, Food } from 'app/shared/model/food.model';
import { FoodService } from './food.service';
import { FoodComponent } from './food.component';
import { FoodDetailComponent } from './food-detail.component';
import { FoodUpdateComponent } from './food-update.component';

@Injectable({ providedIn: 'root' })
export class FoodResolve implements Resolve<IFood> {
  constructor(private service: FoodService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFood> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((food: HttpResponse<Food>) => {
          if (food.body) {
            return of(food.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Food());
  }
}

export const foodRoute: Routes = [
  {
    path: '',
    component: FoodComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Foods',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FoodDetailComponent,
    resolve: {
      food: FoodResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Foods',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FoodUpdateComponent,
    resolve: {
      food: FoodResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Foods',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FoodUpdateComponent,
    resolve: {
      food: FoodResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Foods',
    },
    canActivate: [UserRouteAccessService],
  },
];

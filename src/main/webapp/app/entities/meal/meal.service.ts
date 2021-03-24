import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IMeal } from 'app/shared/model/meal.model';

type EntityResponseType = HttpResponse<IMeal>;
type EntityArrayResponseType = HttpResponse<IMeal[]>;

@Injectable({ providedIn: 'root' })
export class MealService {
  public resourceUrl = SERVER_API_URL + 'api/meals';

  constructor(protected http: HttpClient) {}

  create(meal: IMeal): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(meal);
    return this.http
      .post<IMeal>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(meal: IMeal): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(meal);
    return this.http
      .put<IMeal>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IMeal>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMeal[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(meal: IMeal): IMeal {
    const copy: IMeal = Object.assign({}, meal, {
      date: meal.date && meal.date.isValid() ? meal.date.format(DATE_FORMAT) : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? moment(res.body.date) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((meal: IMeal) => {
        meal.date = meal.date ? moment(meal.date) : undefined;
      });
    }
    return res;
  }
}

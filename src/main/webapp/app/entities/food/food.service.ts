import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IFood } from 'app/shared/model/food.model';

type EntityResponseType = HttpResponse<IFood>;
type EntityArrayResponseType = HttpResponse<IFood[]>;

@Injectable({ providedIn: 'root' })
export class FoodService {
  public resourceUrl = SERVER_API_URL + 'api/foods';

  constructor(protected http: HttpClient) {}

  create(food: IFood): Observable<EntityResponseType> {
    return this.http.post<IFood>(this.resourceUrl, food, { observe: 'response' });
  }

  update(food: IFood): Observable<EntityResponseType> {
    return this.http.put<IFood>(this.resourceUrl, food, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFood>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFood[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}

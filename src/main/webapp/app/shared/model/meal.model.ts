import { Moment } from 'moment';
import { IFood } from 'app/shared/model/food.model';

export interface IMeal {
  id?: number;
  date?: Moment;
  quantity?: number;
  food?: IFood;
}

export class Meal implements IMeal {
  constructor(public id?: number, public date?: Moment, public quantity?: number, public food?: IFood) {}
}

import { IMeal } from 'app/shared/model/meal.model';

export interface IFood {
  id?: number;
  name?: string;
  calories?: number;
  proteins?: number;
  carbs?: number;
  fats?: number;
  meals?: IMeal[];
}

export class Food implements IFood {
  constructor(
    public id?: number,
    public name?: string,
    public calories?: number,
    public proteins?: number,
    public carbs?: number,
    public fats?: number,
    public meals?: IMeal[]
  ) {}
}

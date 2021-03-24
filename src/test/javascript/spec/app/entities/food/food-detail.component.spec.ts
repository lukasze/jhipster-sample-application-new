import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JhipsterSampleApplicationNewTestModule } from '../../../test.module';
import { FoodDetailComponent } from 'app/entities/food/food-detail.component';
import { Food } from 'app/shared/model/food.model';

describe('Component Tests', () => {
  describe('Food Management Detail Component', () => {
    let comp: FoodDetailComponent;
    let fixture: ComponentFixture<FoodDetailComponent>;
    const route = ({ data: of({ food: new Food(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplicationNewTestModule],
        declarations: [FoodDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(FoodDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FoodDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load food on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.food).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});

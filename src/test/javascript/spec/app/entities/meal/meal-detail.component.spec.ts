import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JhipsterSampleApplicationNewTestModule } from '../../../test.module';
import { MealDetailComponent } from 'app/entities/meal/meal-detail.component';
import { Meal } from 'app/shared/model/meal.model';

describe('Component Tests', () => {
  describe('Meal Management Detail Component', () => {
    let comp: MealDetailComponent;
    let fixture: ComponentFixture<MealDetailComponent>;
    const route = ({ data: of({ meal: new Meal(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplicationNewTestModule],
        declarations: [MealDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(MealDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MealDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load meal on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.meal).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});

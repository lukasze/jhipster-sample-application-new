import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { JhipsterSampleApplicationNewTestModule } from '../../../test.module';
import { FoodComponent } from 'app/entities/food/food.component';
import { FoodService } from 'app/entities/food/food.service';
import { Food } from 'app/shared/model/food.model';

describe('Component Tests', () => {
  describe('Food Management Component', () => {
    let comp: FoodComponent;
    let fixture: ComponentFixture<FoodComponent>;
    let service: FoodService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplicationNewTestModule],
        declarations: [FoodComponent],
      })
        .overrideTemplate(FoodComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FoodComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FoodService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Food(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.foods && comp.foods[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});

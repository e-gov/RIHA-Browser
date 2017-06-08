import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { PipeTransform, Pipe } from '@angular/core';
import { RihaNavbarComponent } from './riha-navbar.component';
import {TranslateService} from '@ngx-translate/core';
import {JsonDataService} from '../../json-data.service';
import { Subject } from 'rxjs/Subject';
import 'rxjs/add/operator/startWith';

@Pipe({name: 'translate'})
export class TestTranslatePipe implements PipeTransform {
  transform(value: string, args: any[]): any {
    if (!value) {
      return;
    }
    return `translate: ${value}`;
  }
}

describe('When initializing RihaNavbarComponent', () => {
  let component: RihaNavbarComponent;
  let fixture: ComponentFixture<RihaNavbarComponent>;
  let resetTestBed;
  class ThreeRoutes {
    public routes = new Subject().startWith(this.getRoutes());

    getRoutes() {
      return ['first', 'second', 'third'].map(this.getRoute);
    }

    getRoute(i18n) {
      return {
        i18n,
        hash: `#${i18n}`
      };
    }
  }

  beforeAll((done) => new Promise((resolve) => {
    resetTestBed = TestBed.resetTestingModule;
    TestBed.resetTestingModule = () => {
      return TestBed;
    };
    TestBed.configureTestingModule({
        declarations: [RihaNavbarComponent, TestTranslatePipe],
        providers: [
          {provide: JsonDataService, useClass: ThreeRoutes}
        ]
      })
      .compileComponents()
      .then(() => {
        fixture = TestBed.createComponent(RihaNavbarComponent);
        component = fixture.componentInstance;
        fixture.autoDetectChanges(true);
        resolve();
      });
  }).then(done));

  afterAll(() => {
    resetTestBed();
    TestBed.resetTestingModule = resetTestBed;
  });

  it('creates RihaNavbarComponent', () => {
    expect(component).toBeTruthy();
  });

  it('shows three navigation links', () => {
    expect(fixture.nativeElement.querySelectorAll('.nav-link').length).toEqual(3);
  });

  it('shows three translated label for navigation links', () => {
    const list = [].slice.call(fixture.nativeElement.querySelectorAll('.nav-link'));
    expect(list.map((el) => el.textContent)).toEqual([
      'translate: navbar.first(current)',
      'translate: navbar.second',
      'translate: navbar.third'
    ]);
  });

  it('renders hash attributes for each navigation links', () => {
    const list = [].slice.call(fixture.nativeElement.querySelectorAll('.nav-link'));
    expect(list.map((el) => el.getAttribute('href'))).toEqual(['#first', '#second', '#third']);
  });

  it('marks first navigation link as active', () => {
    const firstLink = fixture.nativeElement.querySelector('.nav-item');
    expect(firstLink.classList.contains('active')).toBe(true);
  });
});

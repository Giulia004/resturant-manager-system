import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ContabilitaDashboardComponent} from './contabilita-dashboard.component';

describe('ContabilitaDashboardComponent', () => {
  let component: ContabilitaDashboardComponent;
  let fixture: ComponentFixture<ContabilitaDashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ContabilitaDashboardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ContabilitaDashboardComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

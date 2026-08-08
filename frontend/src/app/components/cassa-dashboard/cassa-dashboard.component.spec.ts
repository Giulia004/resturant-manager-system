import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CassaDashboard } from './cassa-dashboard';

describe('CassaDashboard', () => {
  let component: CassaDashboard;
  let fixture: ComponentFixture<CassaDashboard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CassaDashboard]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CassaDashboard);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

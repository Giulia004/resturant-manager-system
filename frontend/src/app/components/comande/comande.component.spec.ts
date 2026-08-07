import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Comande } from './comande';

describe('Comande', () => {
  let component: Comande;
  let fixture: ComponentFixture<Comande>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Comande],
    }).compileComponents();

    fixture = TestBed.createComponent(Comande);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

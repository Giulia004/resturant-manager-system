import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Tavoli } from './tavoli';

describe('Tavoli', () => {
  let component: Tavoli;
  let fixture: ComponentFixture<Tavoli>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Tavoli],
    }).compileComponents();

    fixture = TestBed.createComponent(Tavoli);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

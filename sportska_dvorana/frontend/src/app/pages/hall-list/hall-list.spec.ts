import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HallList } from './hall-list';

describe('HallList', () => {
  let component: HallList;
  let fixture: ComponentFixture<HallList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HallList]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HallList);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HallDetails } from './hall-details';

describe('HallDetails', () => {
  let component: HallDetails;
  let fixture: ComponentFixture<HallDetails>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HallDetails]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HallDetails);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

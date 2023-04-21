import {HttpClientModule} from '@angular/common/http';
import {ComponentFixture, TestBed} from '@angular/core/testing';
import {ReactiveFormsModule} from '@angular/forms';
import {MatSnackBarModule} from '@angular/material/snack-bar';
import {RouterTestingModule,} from '@angular/router/testing';
import {expect} from '@jest/globals';
import {SessionService} from '../../../../services/session.service';

import {DetailComponent} from './detail.component';


describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let service: SessionService;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule
      ],
      declarations: [DetailComponent],
      providers: [{provide: SessionService, useValue: mockSessionService}],
    })
      .compileComponents();
    service = TestBed.inject(SessionService);
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('Should return underfined whe delete is called', () => {
    expect(component.delete()).toBeUndefined();
  });

  it('Should return underfined whe participate is called', () => {
    expect(component.participate()).toBeUndefined();
  });

  it('Should return underfined whe unParticipate is called', () => {
    expect(component.unParticipate()).toBeUndefined();
  });

  it('Should return underfined whe back is called', () => {
    expect(component.back()).toBeUndefined();
  });

  // TODO : Replace with a mock and IT
  it('Should return underfined whe ngOnInit is called', () => {
    expect(component.ngOnInit()).toBeUndefined();
  });
});


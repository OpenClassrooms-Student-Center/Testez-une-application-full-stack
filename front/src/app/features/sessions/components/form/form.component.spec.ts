import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormComponent } from './form.component';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;

  let sessionServiceMock: SessionService;
  let sessionApiServiceMock: SessionApiService;

  let fakeActivatedRoute: ActivatedRoute;
  let fakeRouter: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientTestingModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule,
      ],
      providers: [
        SessionService,
        SessionApiService,
        { provide: ActivatedRoute, useValue: fakeActivatedRoute },
        { provide: Router, useValue: fakeRouter },
      ],
      declarations: [FormComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(FormComponent);

    sessionServiceMock = TestBed.inject(SessionService);
    sessionApiServiceMock = TestBed.inject(SessionApiService);

    component = fixture.componentInstance;

    fixture.detectChanges();
  });

  // Add tests for the first part of the code block here

  it('should create', async () => {
    sessionServiceMock.sessionInformation = {
      id: 1,
      firstName: 'John',
      lastName: 'Doe',
      username: 'user@user.com',
      token: 'jwt',
      type: 'session',
      admin: true,
    };
    fixture.detectChanges();
    component.ngOnInit();

    expect(component).toBeTruthy();
  });

  describe('Session', () => {
    it('should create session', () => {
      const sessionData = {
        id: 1,
        name: 'Test',
        description: 'Test description',
        date: new Date(),
        teacher_id: 1,
        users: [],
        createdAt: new Date(),
        updatedAt: new Date(),
      };

      const sessionForm = component.sessionForm;
      component.onUpdate = false;

      Object.defineProperty(sessionForm, 'value', {
        writable: true,
        value: sessionData,
      });

      const createSpy = jest
        .spyOn(sessionApiServiceMock, 'create')
        .mockReturnValue(of(sessionData));

      const snackBarOpenSpy = jest.spyOn(component['matSnackBar'], 'open');
      const routerNavigateSpy = jest.spyOn(component['router'], 'navigate');

      component.submit();

      expect(createSpy).toHaveBeenCalledWith(sessionData);
      expect(snackBarOpenSpy).toHaveBeenCalledWith(
        'Session created !',
        'Close',
        {
          duration: 3_000,
        }
      );
      expect(routerNavigateSpy).toHaveBeenCalledWith('/sessions');
    });
  });
});

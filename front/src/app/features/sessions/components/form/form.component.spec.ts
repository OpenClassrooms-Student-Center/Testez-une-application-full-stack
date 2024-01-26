import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import {
  BrowserAnimationsModule,
  NoopAnimationsModule,
} from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { ActivatedRoute, Router } from '@angular/router';
import { BehaviorSubject, of } from 'rxjs';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormComponent } from './form.component';
import { Session } from '../../interfaces/session.interface';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;

  let sessionServiceMock: SessionService;
  let sessionApiServiceMock: SessionApiService;

  let fakeActivatedRoute: ActivatedRoute;

  const fakeRouter = {
    navigate: (commands: any[], extras?, options?: any) => {},
    url: '/sessions',
  } as Router;

  const session: Session = {
    users: [],
    name: '',
    description: '',
    date: new Date(),
    createdAt: new Date(),
    updatedAt: new Date(),
    teacher_id: 0,
  };

  const sessionInformation: SessionInformation = {
    admin: true,
    id: 69,
    token: '',
    type: '',
    username: '',
    firstName: '',
    lastName: '',
  };

  sessionApiServiceMock = {
    pathService: '/api/session',
    httpClient: {
      // @ts-ignore
      get: jest.fn(() => of(session)),
      // @ts-ignore
      post: jest.fn(() => of(session)),
      // @ts-ignore
      put: jest.fn(() => of(session)),
      // @ts-ignore
      delete: jest.fn(() => of(null)),
    },
    all: jest.fn(() => of([])),
    detail: jest.fn((sessionId: string) => of(session)),
    create: jest.fn(() => of(session)),
    update: jest.fn(() => of(session)),
    delete: jest.fn(() => of(null)),
    participate: jest.fn((_sessionId: string, _userId: string) => of()),
    unParticipate: jest.fn((_sessionId: string, _userId: string) => of()),
  };

  //@ts-ignore
  sessionServiceMock = {
    sessionInformation,
    isLogged: true,
    isLoggedSubject: new BehaviorSubject<boolean>(true),
    $isLogged: () => of(true),
    logIn: (_user: SessionInformation): void => {},
    logOut: (): void => {},
    next: (): void => {},
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        NoopAnimationsModule,
        RouterTestingModule,
        HttpClientTestingModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
      ],
      providers: [
        { provide: SessionService, useValue: sessionServiceMock },
        { provide: SessionApiService, useValue: sessionApiServiceMock },
        { provide: ActivatedRoute, useValue: fakeActivatedRoute },
        { provide: Router, useValue: fakeRouter },
      ],
      declarations: [FormComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(FormComponent);

    component = fixture.componentInstance;

    fixture.detectChanges();
  });

  // Add tests for the first part of the code block here

  it('should render the component without any issues', () => {
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
      expect(routerNavigateSpy).toHaveBeenCalledWith(['sessions']);
    });
  });
});

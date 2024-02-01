import { HttpClientModule } from '@angular/common/http';
import {
  ComponentFixture,
  TestBed,
  fakeAsync,
  flushMicrotasks,
  tick,
  waitForAsync,
} from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from '../../../../services/session.service';

import { DetailComponent } from './detail.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TeacherService } from 'src/app/services/teacher.service';
import { SessionApiService } from '../../services/session-api.service';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { Session } from '../../interfaces/session.interface';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';

describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let sessionApiServiceMock: SessionApiService;

  let teacherServiceMock: TeacherService;

  const sessionInformation: SessionInformation = {
    admin: true,
    id: 69,
    token: '',
    type: '',
    username: '',
    firstName: '',
    lastName: '',
  };

  const session: Session = {
    users: [],
    name: '',
    description: '',
    date: new Date(),
    createdAt: new Date(),
    updatedAt: new Date(),
    teacher_id: 0,
  };

  let sessionServiceMock: Partial<SessionService>;

  beforeEach(async () => {
    sessionServiceMock = {
      sessionInformation,
      isLogged: true,
      //@ts-ignore
      isLoggedSubject: new BehaviorSubject<boolean>(true),
      $isLogged: () => of(true),
      logIn: (_user: SessionInformation): void => {},
      logOut: (): void => {},
      next: (): void => {},
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

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientTestingModule,
        MatSnackBarModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        ReactiveFormsModule,
      ],
      declarations: [DetailComponent],
      providers: [
        { provide: SessionApiService, useValue: sessionApiServiceMock },
        { provide: TeacherService, useValue: teacherServiceMock },
        { provide: SessionService, useValue: sessionServiceMock },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;

    sessionApiServiceMock = TestBed.inject(SessionApiService);
    teacherServiceMock = TestBed.inject(TeacherService);

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should navigate back when clicking the back button', () => {
    const windowHistorySpy = jest.spyOn(window.history, 'back');

    component.back();

    expect(windowHistorySpy).toHaveBeenCalled();
  });

  it('should participate in the session and fetch the session again', () => {
    const sessionId = 'session-id';
    const userId = 'user-id';

    const sessionApiServiceParticipateSpy = jest
      .spyOn(sessionApiServiceMock, 'participate')
      .mockReturnValue(of());

    component.sessionId = sessionId;
    component.userId = userId;

    component.participate();

    expect(sessionApiServiceParticipateSpy).toHaveBeenCalledWith(
      sessionId,
      userId
    );
  });

  it('should toggle participation and fetch the session again when un-participating', () => {
    const sessionId = 'session-id';
    const userId = 'user-id';

    // Prepare the mocks and spies
    const sessionApiServiceUnParticipateSpy = jest
      .spyOn(sessionApiServiceMock, 'unParticipate')
      .mockReturnValue(of());

    // Trigger the unParticipate method
    component.sessionId = sessionId;
    component.userId = userId;
    component.unParticipate();

    // Assert that the unParticipate method was called with the correct arguments
    expect(sessionApiServiceUnParticipateSpy).toHaveBeenCalledWith(
      sessionId,
      userId
    );
  });

  it('should delete the session and navigate back when deleting a session', () => {
    const sessionId = 'session-id';
    const snackBarOpenSpy = jest.spyOn(component['matSnackBar'], 'open');

    const sessionApiServiceDeleteSpy = jest
      .spyOn(sessionApiServiceMock, 'delete')
      .mockReturnValue(of(null));

    component.sessionId = sessionId;

    component.delete();

    expect(sessionApiServiceDeleteSpy).toHaveBeenCalledWith(sessionId);

    expect(snackBarOpenSpy).toHaveBeenCalledWith('Session deleted !', 'Close', {
      duration: 3_000,
    });
  });
});

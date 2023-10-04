import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DetailComponent } from './detail.component';
import { SessionService } from 'src/app/services/session.service';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientModule } from '@angular/common/http';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { ReactiveFormsModule } from '@angular/forms';
import { of } from 'rxjs';
import { expect } from '@jest/globals';
import { SessionApiService } from '../../services/session-api.service';
import { TeacherService } from 'src/app/services/teacher.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Session } from '../../interfaces/session.interface';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { Teacher } from 'src/app/interfaces/teacher.interface';

describe('DetailComponent Unit tests suites', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let sessionServiceMock: jest.Mocked<SessionService>;
  let sessionApiServiceMock: jest.Mocked<SessionApiService>;
  let matSnackBarMock: jest.Mocked<MatSnackBar>;
  let routerMock: jest.Mocked<Router>;
  let activatedRouteMock: jest.Mocked<ActivatedRoute>;
  let teacherServiceMock: jest.Mocked<TeacherService>;
  let sessionMock: jest.Mocked<Session>;

  const SessionInformationMock: SessionInformation = {
    token: 'token',
    type: 'user',
    id: 1,
    username: 'Jo',
    firstName: 'John',
    lastName: 'Doe',
    admin: true
  };
  const teacherMock: Teacher = {
    id: 1, lastName: 'Doe',
    firstName: 'John',
    createdAt: new Date(),
    updatedAt: new Date()
  };

  beforeEach(async () => {
    sessionServiceMock = {
      sessionInformation: SessionInformationMock,
      logOut: jest.fn()
    } as unknown as jest.Mocked<SessionService>;

    sessionApiServiceMock = {
      detail: jest.fn().mockReturnValue(of(sessionMock)),
      participate: jest.fn().mockReturnValue(of(sessionMock)),
      unParticipate: jest.fn().mockReturnValue(of(sessionMock)),
      delete: jest.fn().mockReturnValue(of(sessionMock))
    } as unknown as jest.Mocked<SessionApiService>;

    matSnackBarMock = {
      open: jest.fn()
    } as unknown as jest.Mocked<MatSnackBar>;

    routerMock = {
      navigate: jest.fn()
    } as unknown as jest.Mocked<Router>;

    activatedRouteMock = {
      snapshot: {
        paramMap: {
          get: jest.fn().mockReturnValue('1')
        }
      }
    } as unknown as jest.Mocked<ActivatedRoute>;

    teacherServiceMock = {
      detail: jest.fn().mockReturnValue(of(teacherMock))
    } as unknown as jest.Mocked<TeacherService>;

    sessionMock = {
      id: 1,
      title: 'title',
      description: 'description',
      teacher_id: 1,
      start: new Date(),
      end: new Date(),
      createdAt: new Date(),
      updatedAt: new Date(),
      users: [1]
    } as unknown as jest.Mocked<Session>;

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule
      ],
      declarations: [DetailComponent],
      providers: [
        { provide: SessionService, useValue: sessionServiceMock },
        { provide: SessionApiService, useValue: sessionApiServiceMock },
        { provide: TeacherService, useValue: teacherServiceMock },
        { provide: MatSnackBar, useValue: matSnackBarMock },
        { provide: Router, useValue: routerMock },
        { provide: ActivatedRoute, useValue: activatedRouteMock }
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch session details on init', () => {
    expect(sessionApiServiceMock.detail).toHaveBeenCalledWith('1');
    expect(teacherServiceMock.detail).toHaveBeenCalledWith('1');
    expect(component.session).toBeTruthy();
    expect(component.teacher).toBeTruthy();
  });
  
  it('should delete the session', () => {
    component.delete();
    expect(sessionApiServiceMock.delete).toHaveBeenCalledWith('1');
    expect(matSnackBarMock.open).toHaveBeenCalledWith('Session deleted !', 'Close', { duration: 3000 });
    expect(routerMock.navigate).toHaveBeenCalledWith(['sessions']);
  });

  it('should navigate back', () => {
    const backSpy = jest.spyOn(window.history, 'back');
    component.back();
    expect(backSpy).toHaveBeenCalled();
    backSpy.mockRestore();
  });
});

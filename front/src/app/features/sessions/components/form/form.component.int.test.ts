import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { ActivatedRoute, Router } from '@angular/router';
import { TeacherService } from 'src/app/services/teacher.service';
import { Session } from '../../interfaces/session.interface';
import { of } from 'rxjs';
import { Teacher } from 'src/app/interfaces/teacher.interface';

describe('FormComponent', () => {
  const mockSessionInformation: SessionInformation = {
    token: '',
    type: '',
    id: 1,
    username: '',
    firstName: '',
    lastName: '',
    admin: true,
  };

  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;

  let mockRouter: jest.Mocked<Router>;
  let mockMatSnackBar: jest.Mocked<MatSnackBar>;
  let mockActivatedRoute: jest.Mocked<ActivatedRoute>;
  let sessionApiService: SessionApiService;
  let sessionService: SessionService;
  let teacherService: TeacherService;
  let mockSession: jest.Mocked<Session>;

  beforeEach(async () => {
    mockSession = {
      id: 1,
      name: 'Session',
      date: '',
      description: 'no desc',
      users: [0],
      teacher_id: 999,
    } as unknown as jest.Mocked<Session>;

    mockRouter = {
      url: '/update/1',
      navigate: jest.fn(),
    } as unknown as jest.Mocked<any>;

    mockMatSnackBar = {
      open: jest.fn(),
    } as unknown as jest.Mocked<MatSnackBar>;

    mockActivatedRoute = {
      snapshot: {
        paramMap: {
          get: jest.fn().mockReturnValue('1'), // Mocking the 'id' parameter value
        },
      },
    } as unknown as jest.Mocked<ActivatedRoute>;

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
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
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        { provide: Router, useValue: mockRouter },
        SessionApiService,
        TeacherService,
        SessionService,
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
      ],
      declarations: [FormComponent],
    }).compileComponents();

    sessionService = TestBed.inject(SessionService);
    sessionApiService = TestBed.inject(SessionApiService);
    teacherService = TestBed.inject(TeacherService);
    sessionService.sessionInformation = mockSessionInformation;
    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should set onUpdate to true, call sessionApiService.detail if url contains update', () => {
    component.onUpdate = false;
    // @ts-ignore
    mockRouter.url = '/update/1';
    const detailSpy = jest.spyOn(sessionApiService, 'detail');
    const getSpy = jest.spyOn(mockActivatedRoute.snapshot.paramMap, 'get');
    // @ts-ignore
    const initFormSpy = jest.spyOn(component, 'initForm');
    component.ngOnInit();
    expect(component.onUpdate).toBe(true);
    expect(getSpy).toHaveBeenCalled;
    // @ts-ignore
    expect(component.id).toBe('1');
    expect(detailSpy).toHaveBeenCalledWith('1'); //componentid
    expect(initFormSpy).toHaveBeenCalled;
  });

  it('should update a session', () => {
    const updateSpy = jest.spyOn(sessionApiService, 'update');
    // @ts-ignore
    const exitSpy = jest.spyOn(component, 'exitPage');
    // @ts-ignore
    component.id = '2';
    component.onUpdate = true;
    component.submit();
    fixture.whenStable().then(() => {
      expect(updateSpy).toHaveBeenCalled;
      expect(exitSpy).toHaveBeenCalledWith('Session updated !');
    });
  });

  it('should create a session', () => {
    const createSpy = jest.spyOn(sessionApiService, 'create');
    // @ts-ignore
    const exitSpy = jest.spyOn(component, 'exitPage');
    component.onUpdate = false;
    component.submit();
    fixture.whenStable().then(() => {
      expect(createSpy).toHaveBeenCalled;
      expect(exitSpy).toHaveBeenCalledWith('Session created !');
    });
  });
});

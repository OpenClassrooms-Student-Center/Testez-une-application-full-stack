import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import {  FormBuilder, ReactiveFormsModule } from '@angular/forms';
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

  const mockSessionInformation: SessionInformation = { token: '', type: '', id: 1, username: '', firstName: '', lastName: '', admin: true };

  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;

  let mockRouter: jest.Mocked<Router>;
  let mockMatSnackBar: jest.Mocked<MatSnackBar>;
  let mockActivatedRoute: jest.Mocked<ActivatedRoute>;
  let mockSessionApiService: jest.Mocked<SessionApiService>;
  let mockSessionService: jest.Mocked<SessionService>;
  let mockTeacherService: jest.Mocked<TeacherService>;
  let mockSession: jest.Mocked<Session>;

  beforeEach(async () => {

    mockSessionService = {
      sessionInformation: mockSessionInformation
    } as unknown as jest.Mocked<SessionService>;

    mockSession = {
      id: 1,
      name: 'Session',
      date: '',
      description: 'no desc',
      users: [0],
      teacher_id: 999
    } as unknown as jest.Mocked<Session>;

    mockRouter = {
      url: '/update/1',
      navigate: jest.fn()
    } as unknown as jest.Mocked<any>;

    mockMatSnackBar = {
      open: jest.fn()
    } as unknown as jest.Mocked<MatSnackBar>;

    mockSessionService = {
      sessionInformation: mockSessionInformation
    } as unknown as jest.Mocked<SessionService>;

    mockSessionApiService = {
      detail: jest.fn().mockReturnValue(of({...mockSession, name: 'Session detail'})),
      create: jest.fn().mockReturnValue(of({...mockSession, name: 'Session create'})),
      update: jest.fn().mockReturnValue(of({...mockSession, name: 'Session update'})),
    } as unknown as jest.Mocked<SessionApiService>;

    mockTeacherService = {
      all: jest.fn().mockReturnValue(of({} as Teacher)),
    } as unknown as jest.Mocked<TeacherService>;

    mockActivatedRoute = {
      snapshot: {
        paramMap: {
          get: jest.fn().mockReturnValue('1'),// Mocking the 'id' parameter value
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
        BrowserAnimationsModule
      ],
      providers: [
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        { provide: Router, useValue: mockRouter },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: SessionService, useValue: mockSessionService },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
      ],
      declarations: [FormComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('ngOnInit', () => {

    it('should navigate to /sessions if sessionInformation is not admin', () => {
      mockSessionService.sessionInformation = {...mockSessionInformation, admin: false};
      component.ngOnInit();
      expect(mockRouter.navigate).toHaveBeenCalledWith(['/sessions']);
    });

    it('should not call navigate if sessionInformation is admin', () => {
      mockSessionService.sessionInformation = {...mockSessionInformation, admin: true};
      component.ngOnInit();
      expect(mockRouter.navigate).not.toHaveBeenCalled
    });

    it('should set onUpdate to true, call sessionApiService.detail if url contains update', () => {
      component.onUpdate = false;
      // @ts-ignore
      mockRouter.url = '/update/1';
      const detailSpy = jest.spyOn(mockSessionApiService, 'detail');
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

    it('should call initForm if url does not contain update', () => {
      component.onUpdate = false;
      // @ts-ignore
      mockRouter.url = '/delete/1';
      const detailSpy = jest.spyOn(mockSessionApiService, 'detail');
      // @ts-ignore
      const initFormSpy = jest.spyOn(component, 'initForm');
      component.ngOnInit();
      expect(component.onUpdate).toBe(false);
      expect(detailSpy).not.toHaveBeenCalled;
      expect(initFormSpy).toHaveBeenCalled;
      // initForm : initialize form with empty value
      expect(component.sessionForm?.get('name')?.value).toBe('');
      expect(component.sessionForm?.get('date')?.value).toBe('');
      expect(component.sessionForm?.get('teacher_id')?.value).toBe('');
      expect(component.sessionForm?.get('description')?.value).toBe('');
    });

  });

  describe('submit', () => {

    it('should call sessionApiService.create and exitPage("Session created !") if not onUpdate', () => {
      component.onUpdate = false;
      const createSpy = jest.spyOn(mockSessionApiService, 'create');
      // @ts-ignore
      const exitSpy = jest.spyOn(component, 'exitPage');
      component.submit();
      expect(createSpy).toHaveBeenCalled;
      expect(exitSpy).toHaveBeenCalledWith('Session created !');
    });

    it('should call sessionApiService.update and exitPage("Session updated !") if onUpdate', () => {
      component.onUpdate = true;
      const updateSpy = jest.spyOn(mockSessionApiService, 'update');
      // @ts-ignore
      const exitSpy = jest.spyOn(component, 'exitPage');
      component.submit();
      expect(updateSpy).toHaveBeenCalled;
      expect(exitSpy).toHaveBeenCalledWith('Session updated !');
    });
  });

  describe('exitPage', () => {
    it('should call matSnackBar.open and router.navigate', () => {
      const openSpy = jest.spyOn(mockMatSnackBar, 'open');
      const navigateSpy = jest.spyOn(mockRouter, 'navigate');
      // @ts-ignore
      component.exitPage('test exitPage');
      expect(openSpy).toHaveBeenCalledWith('test exitPage', 'Close', { duration: 3000 });
      expect(navigateSpy).toHaveBeenCalledWith(['sessions']);
    });
  });

});

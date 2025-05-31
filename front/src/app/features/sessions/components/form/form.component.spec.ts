import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import {  FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { TeacherService } from 'src/app/services/teacher.service';
import { SessionApiService } from '../../services/session-api.service';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';
import { FormComponent } from './form.component';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let routerMock: { url: string; navigate: jest.Mock };
  let sessionApiServiceMock: {
  detail: jest.Mock;
  create: jest.Mock;
  update: jest.Mock;
  };
  let sessionServiceMock: { sessionInformation: { admin: boolean } };
  let teacherServiceMock: { all: jest.Mock };

  /*const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  } */

  beforeEach(async () => {
    routerMock = {
      url: '/sessions/update/1',
      navigate: jest.fn()
    };
    sessionApiServiceMock = {
      detail: jest.fn(),
      create: jest.fn(),
      update: jest.fn()
    };
    sessionServiceMock = {
      sessionInformation: { admin: true }
    };
    teacherServiceMock = {
      all: jest.fn().mockReturnValue(of([]))
    };



    await TestBed.configureTestingModule({

      imports: [
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule, 
        MatSnackBarModule,
        MatSelectModule,
        NoopAnimationsModule,
        MatSnackBarModule
      ],
      providers: [
        FormBuilder,
        { provide: Router, useValue: routerMock },
        { provide: SessionApiService, useValue: sessionApiServiceMock },
        { provide: SessionService, useValue: sessionServiceMock },
        { provide: TeacherService, useValue: teacherServiceMock },
        { provide: ActivatedRoute, useValue: { snapshot: { paramMap: { get: () => '1' } } } }
        
      ],
      declarations: [FormComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
   // fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should set onUpdate, id, and call sessionApiService.detail/initForm if url contains update', () => {
    // Arrange
    const sessionMock = { id: 1, name: 'Test', date: '2024-01-01', teacher_id: 2, description: 'desc' };
    sessionApiServiceMock.detail.mockReturnValue(of(sessionMock));
    const initFormSpy = jest.spyOn(component as any, 'initForm');

    // Act
    component.ngOnInit();

    // Assert
    expect(component.onUpdate).toBe(true);
    expect((component as unknown as { id: string }).id).toBe('1');
    expect(sessionApiServiceMock.detail).toHaveBeenCalledWith('1');
    expect(initFormSpy).toHaveBeenCalledWith(sessionMock);
  });

  it('should call initForm with no argument if url does not include "update"', () => {
  // Arrange
  routerMock.url = '/sessions/create'; // URL ne contenant pas "update"
  const initFormSpy = jest.spyOn(component as unknown as { initForm: () => void }, 'initForm');
  // On s'assure que detail n'est pas appelé
  const detailSpy = sessionApiServiceMock.detail;

  // Act
  component.ngOnInit();

  // Assert
  expect(initFormSpy).toHaveBeenCalledWith();
  expect(detailSpy).not.toHaveBeenCalled();
  expect(component.onUpdate).toBe(false);
});

  it('should call create and exitPage with "Session created !" if not onUpdate', () => {
  // Arrange
  component.onUpdate = false;
  component.sessionForm = new FormBuilder().group({
    name: ['Test'],
    date: ['2024-01-01'],
    teacher_id: [2],
    description: ['desc']
  });
  const sessionValue = component.sessionForm.value;
  sessionApiServiceMock.create = jest.fn().mockReturnValue(of(sessionValue));
  const exitPageSpy = jest.spyOn(component as unknown as { exitPage: (msg: string) => void }, 'exitPage');

  // Act
  component.submit();

  // Assert
  expect(sessionApiServiceMock.create).toHaveBeenCalledWith(sessionValue);
  expect(exitPageSpy).toHaveBeenCalledWith('Session created !');
});

it('should call update and exitPage with "Session updated !" if onUpdate', () => {
  // Arrange
  (component as unknown as { id: string }).id = '123';
  component.onUpdate = true;
  component.sessionForm = new FormBuilder().group({
    name: ['Test'],
    date: ['2024-01-01'],
    teacher_id: [2],
    description: ['desc']
  });
  const sessionValue = component.sessionForm.value;
  sessionApiServiceMock.update = jest.fn().mockReturnValue(of(sessionValue));
  const exitPageSpy = jest.spyOn(component as unknown as { exitPage: (msg: string) => void }, 'exitPage');

  // Act
  component.submit();

  // Assert
  expect(sessionApiServiceMock.update).toHaveBeenCalledWith('123', sessionValue);
  expect(exitPageSpy).toHaveBeenCalledWith('Session updated !');
});

it('should initialize the form with session values', () => {
  const sessionMock = {
    name: 'Test',
    date: '2024-01-01',
    teacher_id: 2,
    description: 'desc'
  };

  // Act
  (component as unknown as { initForm: (session?: typeof sessionMock) => void }).initForm(sessionMock);

  // Assert
  expect(component.sessionForm?.value).toEqual({
    name: 'Test',
    date: '2024-01-01',
    teacher_id: 2,
    description: 'desc'
  });
});

it('should initialize the form with empty values if no session', () => {
  (component as unknown as { initForm: () => void }).initForm();

  expect(component.sessionForm?.value).toEqual({
    name: '',
    date: '',
    teacher_id: '',
    description: ''
  });
});

it('should open snackbar and navigate to sessions when exitPage is called', () => {
  // Arrange
  const snackBar = TestBed.inject(MatSnackBar);
  const snackBarSpy = jest.spyOn(snackBar, 'open');
  const routerSpy = routerMock.navigate;

  // Act
  (component as unknown as { exitPage: (msg: string) => void }).exitPage('Test message');

  // Assert
  expect(snackBarSpy).toHaveBeenCalledWith('Test message', 'Close', { duration: 3000 });
  expect(routerSpy).toHaveBeenCalledWith(['sessions']);
});

it('should redirect to /sessions if user is not admin', () => {
  // Arrange
  sessionServiceMock.sessionInformation.admin = false;
  const routerSpy = routerMock.navigate;

  // Mock les méthodes utilisées après le if pour éviter les erreurs
  routerMock.url = '/sessions/create';
  sessionApiServiceMock.detail.mockReturnValue(of({}));
  jest.spyOn(component as unknown as { initForm: () => void }, 'initForm').mockImplementation(() => {});

  // Act
  component.ngOnInit();

  // Assert
  expect(routerSpy).toHaveBeenCalledWith(['/sessions']);
});

});


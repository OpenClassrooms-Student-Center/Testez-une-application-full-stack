import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { By } from '@angular/platform-browser';
import { expect } from '@jest/globals';
import { of } from 'rxjs';

import { FormComponent } from './form.component';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { TeacherService } from 'src/app/services/teacher.service';
import { ActivatedRoute } from '@angular/router';

describe('FormComponent (create mode)', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let sessionApiService: any;
  let snackBar: MatSnackBar;
  let router: Router;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1,
    },
  };

  const teacherServiceMock = {
    all: jest
      .fn()
      .mockReturnValue(of([{ id: 1, firstName: 'John', lastName: 'Doe' }])),
  };

  beforeEach(async () => {
    sessionApiService = {
      create: jest.fn().mockReturnValue(of({})),
      update: jest.fn().mockReturnValue(of({})),
      detail: jest.fn().mockReturnValue(of({})),
    };

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
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
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: sessionApiService },
        { provide: TeacherService, useValue: teacherServiceMock },
      ],
      declarations: [FormComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    snackBar = TestBed.inject(MatSnackBar);
    router = TestBed.inject(Router);

    jest.spyOn(snackBar, 'open').mockImplementation();
    jest.spyOn(router, 'navigate').mockResolvedValue(true as any);

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have invalid form initially and disable submit', () => {
    expect(component.sessionForm?.invalid).toBe(true);
    const button = fixture.debugElement.query(By.css('button[type="submit"]'))
      .nativeElement as HTMLButtonElement;
    expect(button.disabled).toBe(true);
  });

  it('should enable submit when form is valid', () => {
    component.sessionForm?.setValue({
      name: 'Yoga Flow',
      date: '2025-09-01',
      teacher_id: 1,
      description: 'Nice yoga session',
    });
    fixture.detectChanges();
    expect(component.sessionForm?.valid).toBe(true);
    const button = fixture.debugElement.query(By.css('button[type="submit"]'))
      .nativeElement as HTMLButtonElement;
    expect(button.disabled).toBe(false);
  });

  it('should call create service and navigate on submit', () => {
    component.sessionForm?.setValue({
      name: 'Yoga Flow',
      date: '2025-09-01',
      teacher_id: 1,
      description: 'Nice yoga session',
    });
    component.submit();
    expect(sessionApiService.create).toHaveBeenCalledWith(
      component.sessionForm?.value
    );
    expect(snackBar.open).toHaveBeenCalledWith('Session created !', 'Close', {
      duration: 3000,
    });
    expect(router.navigate).toHaveBeenCalledWith(['sessions']);
  });

  it('should not submit when form is invalid (missing required fields)', () => {
    component.sessionForm?.setValue({
      name: '',
      date: '',
      teacher_id: '',
      description: '',
    });
    expect(component.sessionForm?.invalid).toBe(true);
    component.submit();
    expect(sessionApiService.create).not.toHaveBeenCalled();
  });
});

describe('FormComponent (update mode)', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let sessionApiService: any;
  let snackBar: MatSnackBar;
  let router: Router;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1,
    },
  };

  const teacherServiceMock = {
    all: jest
      .fn()
      .mockReturnValue(of([{ id: 1, firstName: 'John', lastName: 'Doe' }])),
  };

  const existingSession = {
    id: '42',
    name: 'Morning Flow',
    date: '2025-09-01T10:00:00.000Z',
    teacher_id: 1,
    description: 'Existing session',
  };

  beforeEach(async () => {
    sessionApiService = {
      create: jest.fn().mockReturnValue(of({})),
      update: jest.fn().mockReturnValue(of(existingSession)),
      detail: jest.fn().mockReturnValue(of(existingSession)),
    };

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
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
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: sessionApiService },
        { provide: TeacherService, useValue: teacherServiceMock },
        {
          provide: ActivatedRoute,
          useValue: { snapshot: { paramMap: { get: () => '42' } } },
        },
      ],
      declarations: [FormComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    snackBar = TestBed.inject(MatSnackBar);
    router = TestBed.inject(Router);

    jest.spyOn(snackBar, 'open').mockImplementation();
    jest.spyOn(router, 'navigate').mockResolvedValue(true as any);
    jest.spyOn(router, 'url', 'get').mockReturnValue('/sessions/update/42');

    fixture.detectChanges();
  });

  it('should set onUpdate to true and pre-fill form', () => {
    expect(component.onUpdate).toBe(true);
    expect(component.sessionForm?.value.name).toBe('Morning Flow');
    expect(component.sessionForm?.value.description).toBe('Existing session');
  });

  it('should call update service and navigate on submit', () => {
    component.sessionForm?.setValue({
      name: 'Morning Flow Updated',
      date: '2025-09-02',
      teacher_id: 1,
      description: 'Updated session',
    });
    component.submit();
    expect(sessionApiService.update).toHaveBeenCalledWith(
      '42',
      component.sessionForm?.value
    );
    expect(snackBar.open).toHaveBeenCalledWith('Session updated !', 'Close', {
      duration: 3000,
    });
    expect(router.navigate).toHaveBeenCalledWith(['sessions']);
  });

  it('should not call update when form is invalid', () => {
    component.sessionForm?.setValue({
      name: '',
      date: '',
      teacher_id: '',
      description: '',
    });
    expect(component.sessionForm?.invalid).toBe(true);
    component.submit();
    expect(sessionApiService.update).not.toHaveBeenCalled();
  });
});

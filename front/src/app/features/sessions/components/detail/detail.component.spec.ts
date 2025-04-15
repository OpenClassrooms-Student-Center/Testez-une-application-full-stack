import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';
import { RouterTestingModule } from '@angular/router/testing';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { expect } from '@jest/globals';

import { DetailComponent } from './detail.component';
import { SessionService } from '../../../../services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { TeacherService } from '../../../../services/teacher.service';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { By } from '@angular/platform-browser';

describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let mockSessionService: any;
  let mockSessionApiService: any;
  let mockTeacherService: any;
  let mockSnackBar: any;
  let mockRouter: any;

  beforeEach(async () => {
    mockSnackBar = { open: jest.fn() };
    mockRouter = { navigate: jest.fn() };

    mockSessionService = {
      sessionInformation: {
        admin: true,
        id: 1
      }
    };

    mockSessionApiService = {
      detail: jest.fn().mockReturnValue(of({
        id: '1',
        name: 'Test Session',
        users: [1, 2, 3],
        teacher_id: '101',
        date: new Date('2025-02-25'),
        description: 'Session description',
        createdAt: new Date('2025-02-25'),
        updatedAt: new Date('2025-02-25')
      })),
      delete: jest.fn().mockReturnValue(of(null)),
      participate: jest.fn().mockReturnValue(of(null)),
      unParticipate: jest.fn().mockReturnValue(of(null))
    };

    mockTeacherService = {
      detail: jest.fn().mockReturnValue(of({
        id: '101',
        firstName: 'John',
        lastName: 'Doe'
      }))
    };

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        MatSnackBarModule,
        ReactiveFormsModule,
        MatIconModule,
        MatButtonModule,
        MatCardModule,
        BrowserAnimationsModule
      ],
      declarations: [DetailComponent],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: MatSnackBar, useValue: mockSnackBar },
        { provide: Router, useValue: mockRouter },
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: {
                get: (key: string) => '1'
              }
            }
          }
        }
      ]
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch session on initialization', () => {
    expect(mockSessionApiService.detail).toHaveBeenCalledWith('1');
    expect(mockTeacherService.detail).toHaveBeenCalledWith('101');
    expect(component.session).toEqual(expect.objectContaining({
      id: '1',
      name: 'Test Session',
      users: [1, 2, 3]
    }));
    expect(component.isParticipate).toBe(true);
    expect(component.teacher).toEqual(expect.objectContaining({
      firstName: 'John',
      lastName: 'Doe'
    }));
  });

  it('should navigate back on back()', () => {
    const spy = jest.spyOn(window.history, 'back');
    component.back();
    expect(spy).toHaveBeenCalled();
  });

  it('should delete session and show a snackbar', () => {
    const snackBarSpy = jest.spyOn(mockSnackBar, 'open');
    const navigateSpy = jest.spyOn(mockRouter, 'navigate');

    component.delete();

    expect(mockSessionApiService.delete).toHaveBeenCalledWith('1');
    expect(snackBarSpy).toHaveBeenCalledWith('Session deleted !', 'Close', { duration: 3000 });
    expect(navigateSpy).toHaveBeenCalledWith(['sessions']);
  });

  it('should participate in session and fetch session again', () => {
    component.participate();
    expect(mockSessionApiService.participate).toHaveBeenCalledWith('1', '1');
    expect(mockSessionApiService.detail).toHaveBeenCalledTimes(2);
  });

  it('should unparticipate in session and fetch session again', () => {
    component.unParticipate();
    expect(mockSessionApiService.unParticipate).toHaveBeenCalledWith('1', '1');
    expect(mockSessionApiService.detail).toHaveBeenCalledTimes(2);
  });

  it('should display the delete button if the user is admin', () => {
    const deleteButton = fixture.debugElement.query(By.css('[data-testid="deleteButton"]'));
    expect(deleteButton).toBeDefined();
  });

  it('should not display the delete button if the user is not admin', () => {
    mockSessionService.sessionInformation = { admin: false, id: 1 };

    fixture.destroy();
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;

    component.ngOnInit();
    fixture.detectChanges();

    const deleteButton = fixture.debugElement.query(By.css('[data-testid="deleteButton"]'));
    expect(deleteButton).toBeNull();
  });
});

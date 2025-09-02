import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { By } from '@angular/platform-browser';
import { of } from 'rxjs';
import { Router, ActivatedRoute } from '@angular/router';
import { expect } from '@jest/globals';

import { DetailComponent } from './detail.component';
import { SessionService } from '../../../../services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { TeacherService } from '../../../../services/teacher.service';

const sessionId = 's1';
const sessionMock = {
  id: sessionId,
  name: 'evening flow',
  description: 'Nice class',
  date: '2025-09-02T18:30:00.000Z',
  createdAt: '2025-08-29T10:00:00.000Z',
  updatedAt: '2025-08-31T10:00:00.000Z',
  teacher_id: 7,
  users: [1, 42],
} as any;
const teacherMock = {
  id: 7,
  firstName: 'Simon',
  lastName: 'Borg-Olivier',
} as any;

const makeActivatedRoute = (id: string) =>
  ({ snapshot: { paramMap: { get: () => id } } } as any);
const makeSessionService = (admin: boolean, id: number) =>
  ({ sessionInformation: { admin, id } } as Partial<SessionService>);

describe('DetailComponent', () => {
  let fixture: ComponentFixture<DetailComponent>;
  let component: DetailComponent;

  describe('admin user', () => {
    beforeEach(async () => {
      await TestBed.configureTestingModule({
        declarations: [DetailComponent],
        imports: [
          RouterTestingModule,
          MatSnackBarModule,
          ReactiveFormsModule,
          MatCardModule,
          MatIconModule,
          MatButtonModule,
        ],
        providers: [
          { provide: ActivatedRoute, useValue: makeActivatedRoute(sessionId) },
          { provide: SessionService, useValue: makeSessionService(true, 1) },
          {
            provide: SessionApiService,
            useValue: { detail: jest.fn().mockReturnValue(of(sessionMock)) },
          },
          {
            provide: TeacherService,
            useValue: { detail: jest.fn().mockReturnValue(of(teacherMock)) },
          },
        ],
      }).compileComponents();

      fixture = TestBed.createComponent(DetailComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });

    it('should create', () => {
      expect(component).toBeTruthy();
    });

    it('should display session and teacher info', () => {
      const h1 = fixture.debugElement.query(By.css('h1'))
        .nativeElement as HTMLElement;
      expect(h1.textContent?.trim()).toBe('Evening Flow');
      const teacherSpan = fixture.debugElement.query(
        By.css('mat-card-subtitle span')
      ).nativeElement as HTMLElement;
      expect(teacherSpan.textContent?.replace(/\s+/g, ' ').trim()).toBe(
        'Simon BORG-OLIVIER'
      );
    });

    it('should show Delete button', () => {
      const delBtn = fixture.debugElement.query(
        By.css('button[color="warn"] span.ml1')
      );
      expect(delBtn).toBeTruthy();
      expect((delBtn.nativeElement as HTMLElement).textContent?.trim()).toBe(
        'Delete'
      );
    });
  });

  describe('non-admin user not participating', () => {
    beforeEach(async () => {
      const sessionMockNotParticipating = { ...sessionMock, users: [42] };

      await TestBed.configureTestingModule({
        declarations: [DetailComponent],
        imports: [
          RouterTestingModule,
          MatSnackBarModule,
          ReactiveFormsModule,
          MatCardModule,
          MatIconModule,
          MatButtonModule,
        ],
        providers: [
          { provide: ActivatedRoute, useValue: makeActivatedRoute(sessionId) },
          { provide: SessionService, useValue: makeSessionService(false, 1) },
          {
            provide: SessionApiService,
            useValue: {
              detail: jest
                .fn()
                .mockReturnValue(of(sessionMockNotParticipating)),
            },
          },
          {
            provide: TeacherService,
            useValue: { detail: jest.fn().mockReturnValue(of(teacherMock)) },
          },
        ],
      }).compileComponents();

      fixture = TestBed.createComponent(DetailComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });

    it('should NOT show Delete button', () => {
      const delBtn = fixture.debugElement.query(
        By.css('button[color="warn"] span.ml1')
      );
      expect(delBtn).toBeNull();
    });

    it('should show Participate button', () => {
      const participateBtn = fixture.debugElement.query(
        By.css('button[color="primary"] span.ml1')
      );
      expect(participateBtn).toBeTruthy();
      expect(
        (participateBtn.nativeElement as HTMLElement).textContent?.trim()
      ).toBe('Participate');
    });
  });

  describe('non-admin user already participating', () => {
    beforeEach(async () => {
      const sessionMockParticipating = { ...sessionMock, users: [1, 42] };

      await TestBed.configureTestingModule({
        declarations: [DetailComponent],
        imports: [
          RouterTestingModule,
          MatSnackBarModule,
          ReactiveFormsModule,
          MatCardModule,
          MatIconModule,
          MatButtonModule,
        ],
        providers: [
          { provide: ActivatedRoute, useValue: makeActivatedRoute(sessionId) },
          { provide: SessionService, useValue: makeSessionService(false, 1) },
          {
            provide: SessionApiService,
            useValue: {
              detail: jest.fn().mockReturnValue(of(sessionMockParticipating)),
            },
          },
          {
            provide: TeacherService,
            useValue: { detail: jest.fn().mockReturnValue(of(teacherMock)) },
          },
        ],
      }).compileComponents();

      fixture = TestBed.createComponent(DetailComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });

    it('should show "Do not participate" button', () => {
      const unPartBtn = fixture.debugElement.query(
        By.css('button[color="warn"] span.ml1')
      );
      expect(unPartBtn).toBeTruthy();
      expect((unPartBtn.nativeElement as HTMLElement).textContent?.trim()).toBe(
        'Do not participate'
      );
    });
  });

  describe('delete()', () => {
    let sessionApiService: any;
    let snackBar: MatSnackBar;
    let router: Router;

    beforeEach(async () => {
      const sessionApiServiceMock = {
        detail: jest.fn().mockReturnValue(of(sessionMock)),
        delete: jest.fn().mockReturnValue(of(void 0)),
      };

      await TestBed.configureTestingModule({
        declarations: [DetailComponent],
        imports: [
          RouterTestingModule,
          MatSnackBarModule,
          ReactiveFormsModule,
          MatCardModule,
          MatIconModule,
          MatButtonModule,
        ],
        providers: [
          { provide: ActivatedRoute, useValue: makeActivatedRoute(sessionId) },
          { provide: SessionService, useValue: makeSessionService(true, 1) },
          { provide: SessionApiService, useValue: sessionApiServiceMock },
          {
            provide: TeacherService,
            useValue: { detail: jest.fn().mockReturnValue(of(teacherMock)) },
          },
        ],
      }).compileComponents();

      fixture = TestBed.createComponent(DetailComponent);
      component = fixture.componentInstance;

      sessionApiService = TestBed.inject(SessionApiService);
      snackBar = TestBed.inject(MatSnackBar);
      router = TestBed.inject(Router);

      jest.spyOn(snackBar, 'open').mockImplementation();
      jest.spyOn(router, 'navigate').mockResolvedValue(true as any);

      fixture.detectChanges();
    });

    it('should call delete API, open snackbar and navigate', () => {
      component.delete();
      expect(sessionApiService.delete).toHaveBeenCalledWith(sessionId);
      expect(snackBar.open).toHaveBeenCalledWith('Session deleted !', 'Close', {
        duration: 3000,
      });
      expect(router.navigate).toHaveBeenCalledWith(['sessions']);
    });
  });
});

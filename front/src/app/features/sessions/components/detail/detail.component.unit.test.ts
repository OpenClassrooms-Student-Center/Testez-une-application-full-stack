import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { of } from 'rxjs';
import { SessionService } from '../../../../services/session.service';
import { TeacherService } from '../../../../services/teacher.service';
import { Session } from '../../interfaces/session.interface';
import { SessionApiService } from '../../services/session-api.service';

import { DetailComponent } from './detail.component';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { Teacher } from 'src/app/interfaces/teacher.interface';


describe('DetailComponent', () => {

  const mockSessionInformation: SessionInformation = { token: '', type: '', id: 1, username: '', firstName: '', lastName: '', admin: true };
  const mockTeacher: Teacher = { id: 1, lastName: 'Doe', firstName: 'John', createdAt: new Date(), updatedAt: new Date()};

  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;

  let mockRouter: jest.Mocked<Router>;
  let mockMatSnackBar: jest.Mocked<MatSnackBar>;
  let mockActivatedRoute: jest.Mocked<ActivatedRoute>;
  let mockSessionApiService: jest.Mocked<SessionApiService>;
  let mockSessionService: jest.Mocked<SessionService>;
  let mockTeacherService: jest.Mocked<TeacherService>;
  let mockSession: jest.Mocked<Session>;

  beforeEach(async () => {

    mockSession = {
      id: 1,
      users: [0],
      teacher_id: 999
    } as unknown as jest.Mocked<Session>;

    mockRouter = {
      navigate: jest.fn()
    } as unknown as jest.Mocked<Router>;

    mockMatSnackBar = {
      open: jest.fn()
    } as unknown as jest.Mocked<MatSnackBar>;

    mockSessionService = {
      sessionInformation: mockSessionInformation
    } as unknown as jest.Mocked<SessionService>;

    mockSessionApiService = {
      delete: jest.fn(),
      participate: jest.fn(),
      unParticipate: jest.fn(),
      detail: jest.fn().mockReturnValue(of(mockSession))
    } as unknown as jest.Mocked<SessionApiService>;

    mockTeacherService = {
      detail: jest.fn(),
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
        MatSnackBarModule,
        ReactiveFormsModule,
        MatCardModule,
        MatIconModule
      ],
      declarations: [DetailComponent],
      providers: [
        FormBuilder,
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        { provide: Router, useValue: mockRouter },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: SessionService, useValue: mockSessionService },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(DetailComponent);

    component = fixture.componentInstance;

    fixture.detectChanges();

  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('back', () => {
    it('should call windows.history.back()', () => {
      const backSpy = jest.spyOn(window.history, 'back');
      component.back();
      expect(backSpy).toHaveBeenCalledTimes(1);
    });
  });

  describe('ngOnInit', () => {
    it('should call sessionApiService.detail and teacherService.detail on ngOnInit', () => {
      const SessionApiServiceSpy = jest.spyOn(mockSessionApiService, 'detail').mockReturnValue(of(mockSession));
      const TeacherServiceSpy = jest.spyOn(mockTeacherService, 'detail').mockReturnValue(of(mockTeacher));

      component.ngOnInit();

      expect(SessionApiServiceSpy).toHaveBeenCalledWith('1');
      expect(TeacherServiceSpy).toHaveBeenCalledWith('999');
      expect(component.session).toEqual(mockSession);
      expect(component.teacher).toEqual(mockTeacher);
      expect(component.isParticipate).toBeFalsy();
    });
  });

  describe('delete', () => {
    it('should call sessionApiService.delete and router.navigate on delete', () => {
      const sessionApiServiceSpy = jest.spyOn(mockSessionApiService, 'delete').mockReturnValue(of(null));

      component.delete();

      expect(sessionApiServiceSpy).toHaveBeenCalledWith('1');
      expect(mockMatSnackBar.open).toHaveBeenCalledWith('Session deleted !', 'Close', { duration: 3000 });
      expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
    });
  });

  describe('participate', () => {
    it('should call sessionApiService.participate on participate', () => {
      const participeSpy =  jest.spyOn(mockSessionApiService, 'participate').mockReturnValue(of(void 0));

      component.participate();

      expect(participeSpy).toHaveBeenCalledWith(mockSession.id?.toString(), mockSessionInformation.id.toString());
    });
  });

  describe('unParticipate', () => {
    it('should call sessionApiService.unParticipate on unParticipate', () => {
      const unParticipeSpy =  jest.spyOn(mockSessionApiService, 'unParticipate').mockReturnValue(of(void 0));

      component.unParticipate();

      expect(unParticipeSpy).toHaveBeenCalledWith(mockSession.id?.toString(), mockSessionInformation.id.toString());
    });
  });



});

import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { of } from 'rxjs';
import { SessionService } from '../../../../services/session.service';
import { TeacherService } from '../../../../services/teacher.service';
import { Session } from '../../interfaces/session.interface';
import { SessionApiService } from '../../services/session-api.service';

import { DetailComponent } from './detail.component';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { Teacher } from 'src/app/interfaces/teacher.interface';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';

describe('DetailComponent', () => {
  const mockSessionInformation: SessionInformation = {
    token: '',
    type: '',
    id: 1,
    username: '',
    firstName: '',
    lastName: '',
    admin: true,
  };
  const mockTeacher: Teacher = {
    id: 1,
    lastName: 'Doe',
    firstName: 'John',
    createdAt: new Date(),
    updatedAt: new Date(),
  };

  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;

  let mockRouter: jest.Mocked<Router>;
  let matSnackBar: MatSnackBar;
  let mockActivatedRoute: jest.Mocked<ActivatedRoute>;
  let sessionApiService: SessionApiService;
  let sessionService: SessionService;
  let teacherService: TeacherService;
  let mockSession: jest.Mocked<Session>;
  let controller: HttpTestingController;
  const pathSessionService: string = 'api/session';
  const pathTeacherService: string = 'api/teacher';

  beforeEach(async () => {
    mockRouter = {
      navigate: jest.fn(),
    } as unknown as jest.Mocked<Router>;

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
        HttpClientTestingModule,
        MatSnackBarModule,
        ReactiveFormsModule,
        MatCardModule,
        MatIconModule,
      ],
      declarations: [DetailComponent],
      providers: [
        FormBuilder,
        SessionApiService,
        TeacherService,
        SessionService,
        MatSnackBar,
        { provide: Router, useValue: mockRouter },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
      ],
    }).compileComponents();

    sessionService = TestBed.inject(SessionService);
    sessionApiService = TestBed.inject(SessionApiService);
    teacherService = TestBed.inject(TeacherService);
    matSnackBar = TestBed.inject(MatSnackBar);
    controller = TestBed.inject(HttpTestingController);

    sessionService.sessionInformation = mockSessionInformation;

    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('delete', () => {
    it('should call sessionApiService.delete and router.navigate on delete', () => {
      controller.expectOne(pathSessionService + '/1');

      const sessionApiServiceSpy = jest.spyOn(sessionApiService, 'delete');
      const matSnackBarSpy = jest.spyOn(matSnackBar, 'open');

      component.delete();

      fixture.whenStable().then(() => {
        expect(sessionApiServiceSpy).toHaveBeenCalledWith('1');
        expect(matSnackBarSpy).toHaveBeenCalledWith(
          'Session deleted !',
          'Close',
          { duration: 3000 }
        );
      });
    });
  });

  describe('participate', () => {
    it('should call sessionApiService.participate on participate', () => {
      const participeSpy = jest.spyOn(sessionApiService, 'participate');
      const detailSpy = jest.spyOn(sessionApiService, 'detail');
      const teacherDetailSpy = jest.spyOn(teacherService, 'detail');

      component.participate();

      fixture.whenStable().then(() => {
        expect(participeSpy).toHaveBeenCalledWith(
          mockSession.id?.toString(),
          mockSessionInformation.id.toString()
        );
        expect(detailSpy).toHaveBeenCalledWith(mockSession.id?.toString());
        expect(teacherDetailSpy).toHaveBeenCalledWith(mockTeacher.id);
        expect(component.isParticipate).toBeTruthy();
      });
    });
  });

  describe('unParticipate', () => {
    it('should call sessionApiService.unParticipate on unParticipate', () => {
      const unParticipeSpy = jest.spyOn(sessionApiService, 'unParticipate');
      const detailSpy = jest.spyOn(sessionApiService, 'detail');
      const teacherDetailSpy = jest.spyOn(teacherService, 'detail');

      component.participate();

      fixture.whenStable().then(() => {
        expect(unParticipeSpy).toHaveBeenCalledWith(
          mockSession.id?.toString(),
          mockSessionInformation.id.toString()
        );
        expect(detailSpy).toHaveBeenCalledWith(mockSession.id?.toString());
        expect(teacherDetailSpy).toHaveBeenCalledWith(mockTeacher.id);
        expect(component.isParticipate).toBeFalsy();
      });
    });
  });
});

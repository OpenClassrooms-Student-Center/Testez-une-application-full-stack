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
import { of } from 'rxjs';

describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let sessionServiceMock: SessionService;

  let sessionApiServiceMock: SessionApiService;

  let teacherServiceMock: TeacherService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientTestingModule,
        MatSnackBarModule,
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

    sessionServiceMock = TestBed.inject(SessionService);
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

  it('should participate in the session and fetch the session again', fakeAsync(() => {
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
  }));

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

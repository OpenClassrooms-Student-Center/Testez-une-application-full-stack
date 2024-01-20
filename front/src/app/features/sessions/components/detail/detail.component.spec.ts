import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from '../../../../services/session.service';

import { DetailComponent } from './detail.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TeacherService } from 'src/app/services/teacher.service';
import { SessionApiService } from '../../services/session-api.service';

describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let sessionServiceMock: SessionService;

  let sessionApiServiceMock: SessionApiService;

  let teacherServiceMock: TeacherService;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1,
    },
  };

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
        { provide: SessionService, useValue: mockSessionService },
        SessionApiService,
        TeacherService,
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

  // it('should toggle participation and fetch the session again when un-participating', async () => {
  //   const sessionId = 'session-id';
  //   const userId = 'user-id';

  //   const session = { users: [] };
  //   session.users.push(userId);

  //   sessionApiServiceMock.unParticipate.mockResolvedValue(session);
  //   sessionApiServiceMock.detail.mockResolvedValue(session);

  //   component.sessionId = sessionId;
  //   component.isParticipate = true;

  //   component.unParticipate();

  //   expect(sessionApiServiceMock.unParticipate).toHaveBeenCalledWith(
  //     sessionId,
  //     userId
  //   );
  //   expect(sessionApiServiceMock.detail).toHaveBeenCalledWith(sessionId);
  //   expect(component.isParticipate).toBeFalsy();
  // });

  it('should delete the session and navigate back when deleting a session', async () => {
    const sessionId = 'session-id';
    const snackBarOpenSpy = jest.spyOn(component['matSnackBar'], 'open');

    component.sessionId = sessionId;

    component.delete();

    expect(sessionApiServiceMock.delete).toHaveBeenCalledWith(sessionId);
    expect(snackBarOpenSpy).toHaveBeenCalledWith('Session deleted !', 'Close', {
      duration: 3000,
    });
  });
});

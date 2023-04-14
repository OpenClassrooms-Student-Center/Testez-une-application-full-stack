import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';
import { Session } from '../interfaces/session.interface';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';

describe('SessionsService', () => {
  let sessionApiService: SessionApiService;
  let mockHttpTestingController: HttpTestingController;
  let pathService = 'api/session';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    sessionApiService = TestBed.inject(SessionApiService);
    mockHttpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    mockHttpTestingController.verify();
  });

  it('should be created', () => {
    expect(sessionApiService).toBeTruthy();
  });

  describe('all', () => {
    it('should fetch all sessions', () => {
      const mockSessions: Session[] = [
        {
          name: 'hello',
          description: 'world',
          date: new Date(),
          teacher_id: 1,
          users: [1],
        },
      ];
      sessionApiService.all().subscribe((sessions) => {
        expect(sessions).toEqual(mockSessions);
      });
      const req = mockHttpTestingController.expectOne(pathService);
      req.flush(mockSessions);
    });
  });

  describe('detail', () => {
    it('should get session detail by id', () => {
      const sessionId = '1';
      const mockSession: Session = {
        name: 'hello',
        description: 'world',
        date: new Date(),
        teacher_id: 1,
        users: [1],
      };
      sessionApiService.detail(sessionId).subscribe((session) => {
        expect(session).toEqual(mockSession);
      });
      const req = mockHttpTestingController.expectOne(
        `${pathService}/${sessionId}`
      );
      expect(req.request.method).toBe('GET');
      req.flush(mockSession);
    });
  });

  describe('delete', () => {
    it('should delete session by id', () => {
      const sessionId = '1';
      sessionApiService.delete(sessionId).subscribe();
      const req = mockHttpTestingController.expectOne(
        `${pathService}/${sessionId}`
      );
      expect(req.request.method).toBe('DELETE');
      req.flush('');
    });
  });

  describe('create', () => {
    it('should create session', () => {
      const mockSession: Session = {
        name: 'hello',
        description: 'world',
        date: new Date(),
        teacher_id: 1,
        users: [1],
      };
      sessionApiService.create(mockSession).subscribe((session) => {
        expect(session).toEqual(mockSession);
      });
      const req = mockHttpTestingController.expectOne(pathService);
      expect(req.request.method).toBe('POST');
      req.flush(mockSession);
    });
  });

  describe('update', () => {
    it('should update session', () => {
      const sessionId = '1';
      const mockSession: Session = {
        name: 'hello',
        description: 'world',
        date: new Date(),
        teacher_id: 1,
        users: [1],
      };
      sessionApiService.update(sessionId, mockSession).subscribe((session) => {
        expect(session).toEqual(mockSession);
      });
      const req = mockHttpTestingController.expectOne(
        `${pathService}/${sessionId}`
      );
      expect(req.request.method).toBe('PUT');
      req.flush(mockSession);
    });
  });

  describe('participate', () => {
    it('should participate', () => {
      const sessionId = '1';
      const userId = '1';
      sessionApiService.participate(sessionId, userId).subscribe();
      const req = mockHttpTestingController.expectOne(
        `${pathService}/${sessionId}/participate/${userId}`
      );
      expect(req.request.method).toBe('POST');
      req.flush('');
    });

    it('should unparticipate', () => {
      const sessionId = '1';
      const userId = '1';
      sessionApiService.unParticipate(sessionId, userId).subscribe();
      const req = mockHttpTestingController.expectOne(
        `${pathService}/${sessionId}/participate/${userId}`
      );
      expect(req.request.method).toBe('DELETE');
      req.flush('');
    });
  });
});

import { HttpClientModule } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';
import { Session } from '../interfaces/session.interface';

describe('SessionsService', () => {
  let sessionApiService: SessionApiService;
  let mockHttpTestingController: HttpTestingController;
  let pathService = 'api/session';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientTestingModule
      ]
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
    it('should get all sessions', () => {
      const mockSessions: Session[] = [
        { id: 1, name: 'Session 1', description: '', date: new Date(), teacher_id: 1, users: [] },
        { id: 2, name: 'Session 2', description: '', date: new Date(), teacher_id: 2, users: [] }
      ];
      sessionApiService.all().subscribe(sessions => {
        expect(sessions).toEqual(mockSessions);
      });
      const req = mockHttpTestingController.expectOne(pathService);
      expect(req.request.method).toBe('GET');
      req.flush(mockSessions);
    });
  });

  describe('detail', () => {
    it('should get session detail by id', () => {
      const sessionId = '1';
      const mockSession: Session = { id: 1, name: 'Session 1', description: '', date: new Date(), teacher_id: 1, users: [] };
      sessionApiService.detail(sessionId).subscribe(session => {
        expect(session).toEqual(mockSession);
      });
      const req = mockHttpTestingController.expectOne(`${pathService}/${sessionId}`);
      expect(req.request.method).toBe('GET');
      req.flush(mockSession);
    });
  });

  describe('delete', () => {
    it('should delete session by id', () => {
      const sessionId = '1';
      sessionApiService.delete(sessionId).subscribe();
      const req = mockHttpTestingController.expectOne(`${pathService}/${sessionId}`);
      expect(req.request.method).toBe('DELETE');
      req.flush({});
    });
  });

  describe('create', () => {
    it('should create a session', () => {
      const mockSession: Session = { id: 1, name: 'Session 1', description: '', date: new Date(), teacher_id: 1, users: [] };
      sessionApiService.create(mockSession).subscribe(session => {
        expect(session).toEqual(mockSession);
      });
      const req = mockHttpTestingController.expectOne(pathService);
      expect(req.request.method).toBe('POST');
      req.flush(mockSession);
    });
  });

  describe('update', () => {
    it('should update a session by id', () => {
      const sessionId = '1';
      const mockSession: Session = { id: 1, name: 'Session 1', description: '', date: new Date(), teacher_id: 1, users: [] };
      sessionApiService.update(sessionId, mockSession).subscribe(session => {
        expect(session).toEqual(mockSession);
      });
      const req = mockHttpTestingController.expectOne(`api/session/${sessionId}`);
      expect(req.request.method).toBe('PUT');
      req.flush(mockSession);
    });
  });

  describe('participate', () => {
    it('should let an user to participate in a session', () => {
      const sessionId = '1';
      const userId = '1';
      sessionApiService.participate(sessionId, userId).subscribe();
      const req = mockHttpTestingController.expectOne(`api/session/${sessionId}/participate/${userId}`);
      expect(req.request.method).toBe('POST');
      req.flush({});
    });
  });

  describe('unParticipate', () => {
    it('should let an user to unparticipate in a session', () => {
      const sessionId = '1';
      const userId = '1';
      sessionApiService.unParticipate(sessionId, userId).subscribe();
      const req = mockHttpTestingController.expectOne(`api/session/${sessionId}/participate/${userId}`);
      expect(req.request.method).toBe('DELETE');
      req.flush({});
    });
  });
});

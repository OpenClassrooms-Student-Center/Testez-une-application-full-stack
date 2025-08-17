import { HttpClientModule } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { Session } from '../interfaces/session.interface';
import { SessionApiService } from './session-api.service';

// Define a minimal User interface for testing purposes

const mockSessions: Session[] = [
      { id: 1, name: 'Session 1', date: new Date(), teacher_id: 2, description: 'desc' , users: [1,2], createdAt: new Date(), updatedAt: new Date() },
    ];

      
describe('SessionsService', () => {
  let service: SessionApiService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[ HttpClientTestingModule],
      providers: [SessionApiService]
    });
    service = TestBed.inject(SessionApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

    

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch all sessions', () => {
    
    service.all().subscribe(sessions => {
      expect(sessions).toEqual(mockSessions);
    });

    const req = httpMock.expectOne('api/session');
    expect(req.request.method).toBe('GET');
    req.flush(mockSessions);
  });

  it('should fetch session detail by id', () => {
    service.detail('1').subscribe(session => {
      expect(session).toEqual(mockSessions[0]);
    });

    const req = httpMock.expectOne('api/session/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockSessions[0]);
  });

  it('should call DELETE on the correct URL', () => {
    service.delete('1').subscribe(response => {
      expect(response).toBeNull(); 
    });

    const req = httpMock.expectOne('api/session/1');
    expect(req.request.method).toBe('DELETE');
    req.flush(null); // ou {} selon la réponse attendue
  });

  it('should create a session', () => {
    const sessionToCreate: Session = {
      id: 2,
      name: 'Session test',
      date: new Date(),
      teacher_id: 2,
      description: 'desc',
      users: [1, 2]
    };

    service.create(sessionToCreate).subscribe(session => {
      expect(session).toEqual(sessionToCreate);
    });

    const req = httpMock.expectOne('api/session');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(sessionToCreate);
    req.flush(sessionToCreate);
  });

  it('should update a session', () => {
    const sessionToUpdate: Session = {
      id: 1,
      name: 'Session 1',
      date: new Date(),
      teacher_id: 3,
      description: 'desc',
      users: [1, 2]
    };

    service.update('1', sessionToUpdate).subscribe(session => {
      expect(session).toEqual(sessionToUpdate);
    });

    const req = httpMock.expectOne('api/session/1');
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(sessionToUpdate);
    req.flush(sessionToUpdate);
  });


  it('should call POST to participate endpoint', () => {
    service.participate('1', '42').subscribe(response => {
      expect(response).toBeUndefined();
    });

    const req = httpMock.expectOne('api/session/1/participate/42');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toBeNull();
    req.flush(null);
  });

  it('should call DELETE to unParticipate endpoint', () => {
    service.unParticipate('1', '42').subscribe(response => {
      expect(response).toBeUndefined();
    });

    const req = httpMock.expectOne('api/session/1/participate/42');
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });
});

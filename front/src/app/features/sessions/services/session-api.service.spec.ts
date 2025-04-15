import { HttpClient, HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';
import { Session } from '../interfaces/session.interface';
import { of, throwError } from 'rxjs';

describe('SessionApiService', () => {
  let service: SessionApiService;
  let httpClientSpy: any;

  beforeEach(() => {
    httpClientSpy = {
      get: jest.fn(),
      post: jest.fn(),
      put: jest.fn(),
      delete: jest.fn(),
    };

    TestBed.configureTestingModule({
      imports: [HttpClientModule],
      providers: [
        SessionApiService,
        { provide: HttpClient, useValue: httpClientSpy }
      ]
    });

    service = TestBed.inject(SessionApiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return an array of sessions', () => {
    const mockSessions: Session[] = [
      { id: 1, name: 'Session 1', description: 'Description 1', date: new Date(), teacher_id: 1, users: [] },
      { id: 2, name: 'Session 2', description: 'Description 2', date: new Date(), teacher_id: 2, users: [] }
    ];

    httpClientSpy.get.mockReturnValue(of(mockSessions));

    service.all().subscribe(sessions => {
      expect(sessions).toEqual(mockSessions);
    });
  });

  it('should handle error and return empty array on failure', () => {
    httpClientSpy.get.mockReturnValue(throwError(() => new Error('Error occurred')));

    service.all().subscribe(sessions => {
      expect(sessions).toEqual([]);
    });
  });

  it('should return a session by id', () => {
    const mockSession: Session = { id: 1, name: 'Session 1', description: 'Description 1', date: new Date(), teacher_id: 1, users: [] };

    httpClientSpy.get.mockReturnValue(of(mockSession));

    service.detail('1').subscribe(session => {
      expect(session).toEqual(mockSession);
    });
  });

  it('should handle error on failure', () => {
    httpClientSpy.get.mockReturnValue(throwError(() => new Error('Error occurred')));

    service.detail('1').subscribe({
      next: () => fail('expected an error, not a session'),
      error: error => expect(error).toBeTruthy()
    });
  });

  it('should delete a session and return void', () => {
    httpClientSpy.delete.mockReturnValue(of(undefined));

    service.delete('1').subscribe(response => {
      expect(response).toBeUndefined();
    });
  });

  it('should handle error on failure', () => {
    httpClientSpy.delete.mockReturnValue(throwError(() => new Error('Error occurred')));

    service.delete('1').subscribe({
      next: () => fail('expected an error, not a response'),
      error: error => expect(error).toBeTruthy()
    });
  });

  it('should create a new session', () => {
    const newSession: Session = { name: 'New Session', description: 'New session description', date: new Date(), teacher_id: 1, users: [] };
    const createdSession: Session = { ...newSession, id: 1 };

    httpClientSpy.post.mockReturnValue(of(createdSession));

    service.create(newSession).subscribe(session => {
      expect(session).toEqual(createdSession);
    });
  });

  it('should handle error on failure', () => {
    const newSession: Session = { name: 'New Session', description: 'New session description', date: new Date(), teacher_id: 1, users: [] };
    httpClientSpy.post.mockReturnValue(throwError(() => new Error('Error occurred')));

    service.create(newSession).subscribe({
      next: () => fail('expected an error, not a session'),
      error: error => expect(error).toBeTruthy()
    });
  });

  it('should update an existing session', () => {
    const updatedSession: Session = { id: 1, name: 'Updated Session', description: 'Updated description', date: new Date(), teacher_id: 1, users: [] };

    httpClientSpy.put.mockReturnValue(of(updatedSession));

    service.update('1', updatedSession).subscribe(session => {
      expect(session).toEqual(updatedSession);
    });
  });

  it('should handle error on failure', () => {
    const updatedSession: Session = { id: 1, name: 'Updated Session', description: 'Updated description', date: new Date(), teacher_id: 1, users: [] };
    httpClientSpy.put.mockReturnValue(throwError(() => new Error('Error occurred')));

    service.update('1', updatedSession).subscribe({
      next: () => fail('expected an error, not a session'),
      error: error => expect(error).toBeTruthy()
    });
  });

  it('should participate in a session', () => {
    httpClientSpy.post.mockReturnValue(of(undefined));

    service.participate('1', '1').subscribe(response => {
      expect(response).toBeUndefined();
    });
  });

  it('should handle error on failure', () => {
    httpClientSpy.post.mockReturnValue(throwError(() => new Error('Error occurred')));

    service.participate('1', '1').subscribe({
      next: () => fail('expected an error, not a response'),
      error: error => expect(error).toBeTruthy()
    });
  });

  it('should unparticipate from a session', () => {
    httpClientSpy.delete.mockReturnValue(of(undefined));

    service.unParticipate('1', '1').subscribe(response => {
      expect(response).toBeUndefined();
    });
  });

  it('should handle error on failure', () => {
    httpClientSpy.delete.mockReturnValue(throwError(() => new Error('Error occurred')));

    service.unParticipate('1', '1').subscribe({
      next: () => fail('expected an error, not a response'),
      error: error => expect(error).toBeTruthy()
    });
  });
});

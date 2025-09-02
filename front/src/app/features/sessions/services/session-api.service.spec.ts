import { TestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';
import { Session } from '../interfaces/session.interface';

describe('SessionApiService', () => {
  let service: SessionApiService;
  let httpMock: HttpTestingController;

  const baseUrl = 'api/session';

  const mockSession: Session = {
    id: 's1',
    name: 'Morning Flow',
    date: '2025-09-01T10:00:00.000Z',
    teacher_id: 7,
    description: 'Nice session',
    users: [1, 2],
    createdAt: '2025-08-20T09:00:00.000Z',
    updatedAt: '2025-08-21T09:00:00.000Z',
  } as any;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SessionApiService],
    });

    service = TestBed.inject(SessionApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('all() should GET list of sessions', () => {
    const mockList: Session[] = [mockSession];

    service.all().subscribe((res) => {
      expect(res).toEqual(mockList);
    });

    const req = httpMock.expectOne(baseUrl);
    expect(req.request.method).toBe('GET');
    req.flush(mockList);
  });

  it('detail() should GET one session by id', () => {
    service.detail('s1').subscribe((res) => {
      expect(res).toEqual(mockSession);
    });

    const req = httpMock.expectOne(`${baseUrl}/s1`);
    expect(req.request.method).toBe('GET');
    req.flush(mockSession);
  });

  it('delete() should DELETE by id', () => {
    service.delete('s1').subscribe((res) => {
      expect(res).toBeTruthy();
    });

    const req = httpMock.expectOne(`${baseUrl}/s1`);
    expect(req.request.method).toBe('DELETE');
    req.flush({ ok: true });
  });

  it('create() should POST a session', () => {
    service.create(mockSession).subscribe((res) => {
      expect(res).toEqual(mockSession);
    });

    const req = httpMock.expectOne(baseUrl);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(mockSession);
    req.flush(mockSession);
  });

  it('update() should PUT a session by id', () => {
    const updated: Session = { ...mockSession, name: 'Updated' };

    service.update('s1', updated).subscribe((res) => {
      expect(res).toEqual(updated);
    });

    const req = httpMock.expectOne(`${baseUrl}/s1`);
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(updated);
    req.flush(updated);
  });

  it('participate() should POST void with null body', () => {
    service.participate('s1', 'u1').subscribe((res) => {
      expect(res).toBeUndefined();
    });

    const req = httpMock.expectOne(`${baseUrl}/s1/participate/u1`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toBeNull();
    req.flush(null);
  });

  it('unParticipate() should DELETE void', () => {
    service.unParticipate('s1', 'u1').subscribe((res) => {
      expect(res).toBeUndefined();
    });

    const req = httpMock.expectOne(`${baseUrl}/s1/participate/u1`);
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });
});

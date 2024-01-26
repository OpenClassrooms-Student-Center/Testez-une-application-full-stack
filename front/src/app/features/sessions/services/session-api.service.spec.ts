import { TestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import { SessionApiService } from './session-api.service';
import { Session } from '../interfaces/session.interface';
import { expect } from '@jest/globals';

describe('SessionApiService', () => {
  let service: SessionApiService;
  let httpMock: HttpTestingController;

  const session: Session = {
    name: '',
    description: '',
    date: new Date(),
    teacher_id: 0,
    users: [],
  };
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

  it('retrieves all Sessions', () => {
    const returnedFromServer: Session[] = [];

    service.all().subscribe((data) => {
      expect(data).toEqual(returnedFromServer);
    });

    const req = httpMock.expectOne('api/session');
    expect(req.request.method).toBe('GET');
    req.flush(returnedFromServer);
  });

  it('retrieves a single Session details', () => {
    const sessionId = '1';
    const returnedFromServer: Session = {
      name: '',
      description: '',
      date: new Date(),
      teacher_id: 0,
      users: [],
    };

    service.detail(sessionId).subscribe((data) => {
      expect(data).toEqual(returnedFromServer);
    });

    const req = httpMock.expectOne(`api/session/${sessionId}`);
    expect(req.request.method).toBe('GET');
    req.flush(returnedFromServer);
  });

  it('deletes a Session', () => {
    const sessionId = '1';

    service.delete(sessionId).subscribe(() => {});

    const req = httpMock.expectOne(`api/session/${sessionId}`);
    expect(req.request.method).toBe('DELETE');
  });

  it('creates a Session', () => {
    service.create(session).subscribe((data) => {
      expect(data).toEqual(session);
    });

    const req = httpMock.expectOne('api/session');
    expect(req.request.method).toBe('POST');
    req.flush(session);
  });

  it('updates a Session', () => {
    const sessionId = '1';

    service.update(sessionId, session).subscribe((data) => {
      expect(data).toEqual(session);
    });

    const req = httpMock.expectOne(`api/session/${sessionId}`);
    expect(req.request.method).toBe('PUT');
    req.flush(session);
  });

  it('lets participant join a Session', () => {
    const sessionId = '1';
    const userId = '2';

    service.participate(sessionId, userId).subscribe(() => {});

    const req = httpMock.expectOne(
      `api/session/${sessionId}/participate/${userId}`
    );
    expect(req.request.method).toBe('POST');
  });

  it('lets participant leave a Session', () => {
    const sessionId = '1';
    const userId = '2';

    service.unParticipate(sessionId, userId).subscribe(() => {});

    const req = httpMock.expectOne(
      `api/session/${sessionId}/participate/${userId}`
    );
    expect(req.request.method).toBe('DELETE');
  });
});

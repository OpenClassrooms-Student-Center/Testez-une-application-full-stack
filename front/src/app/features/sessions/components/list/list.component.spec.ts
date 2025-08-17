import { HttpClientModule} from '@angular/common/http';
import {HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { Session } from '../../interfaces/session.interface';
import { ListComponent } from './list.component';

describe('ListComponent', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;
  let httpMock: HttpTestingController;

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      imports: [HttpClientTestingModule, MatCardModule, MatIconModule],
      providers: [{ provide: SessionService, useValue: mockSessionService }]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should return the current user from sessionService', () => {
  expect(component.user).toEqual(mockSessionService.sessionInformation);
});

  // Test d'intégration 
  it('should fetch and expose sessions from the API', () => {
    const mockSessions: Session[] = [
      { id: 1, name: 'Session 1', date: new Date(), teacher_id: 2, description: 'desc', users: [1, 2], createdAt: new Date(), updatedAt: new Date() },
      { id: 2, name: 'Session 2', date: new Date(), teacher_id: 3, description: 'desc2', users: [3], createdAt: new Date(), updatedAt: new Date() }
    ];

    let received: Session[] | undefined;
    component.sessions$.subscribe(sessions => received = sessions);

    const req = httpMock.expectOne('api/session');
    expect(req.request.method).toBe('GET');
    req.flush(mockSessions);

    expect(received).toEqual(mockSessions);
  });
});

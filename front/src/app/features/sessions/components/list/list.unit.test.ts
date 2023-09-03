import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { Observable, of } from 'rxjs';
import { expect } from '@jest/globals';
import { ListComponent } from './list.component';
import { Session } from '../../interfaces/session.interface';
import { SessionService } from 'src/app/services/session.service';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { AuthService } from 'src/app/features/auth/services/auth.service';
import { Router } from '@angular/router';
import { SessionApiService } from '../../services/session-api.service';

describe('ListComponent', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;
  let authServiceMock: Partial<AuthService>;
  let sessionServiceMock: Partial<SessionService>;
  let routerMock: Partial<Router>;
  let sessionApiServiceMock: Partial<SessionApiService>;

  const mockSessionInformation: SessionInformation = {
    token: 'your-token-here',
    type: 'user',
    id: 1,
    username: 'your-username',
    firstName: 'your-first-name',
    lastName: 'your-last-name',
    admin: true
  };

  beforeEach(async () => {
    authServiceMock = {
      login: jest.fn()
    };

    sessionServiceMock = {
      sessionInformation: mockSessionInformation,
      $isLogged: jest.fn(() => of(true)),
      logIn: jest.fn(),
      logOut: jest.fn()
    };

    routerMock = {
      navigate: jest.fn()
    };

    sessionApiServiceMock = {
      all: jest.fn(() => of([
        {
          id: 1,
          name: 'Yoga Session 1',
          description: 'Description 1',
          date: new Date(),
          teacher_id: 1,
          users: [],
          createdAt: new Date(),
          updatedAt: new Date(),
        },
        {
          id: 2,
          name: 'Yoga Session 2',
          description: 'Description 2',
          date: new Date(),
          teacher_id: 2,
          users: [],
          createdAt: new Date(),
          updatedAt: new Date(),
        },
      ] as Session[])),
    };

    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      imports:  [HttpClientModule, MatCardModule, MatIconModule],
      providers: [
        { provide: SessionService, useValue: sessionServiceMock as SessionService },
        { provide: AuthService, useValue: authServiceMock },
        { provide: Router, useValue: routerMock },
        { provide: SessionApiService, useValue: sessionApiServiceMock as SessionApiService } 
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display the list of sessions', () => {
    fixture.detectChanges();
    const sessionElements = fixture.nativeElement.querySelectorAll('.item');
    expect(sessionElements.length).toBe(2);
  });

  it('should display Create and Detail buttons for admin user', () => {
    fixture.detectChanges();
    const createButton = fixture.nativeElement.querySelectorAll('.create-button');
    const detailButtons = fixture.nativeElement.querySelectorAll('.detail-button');

    expect(createButton.length).toBe(1);
    expect(detailButtons.length).toBe(2);
  });
});
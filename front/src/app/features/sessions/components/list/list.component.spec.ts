import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { RouterTestingModule } from '@angular/router/testing';

import { ListComponent } from './list.component';
import { By } from '@angular/platform-browser';
import { Session } from '../../interfaces/session.interface';
import { of } from 'rxjs';
import { SessionApiService } from '../../services/session-api.service';

describe('ListComponent', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  };

  const mockSessionApiService = {
    all: jest.fn()
  };

  const mockSessions: Session[] = [
    {
      id: 1,
      name: 'Yoga Morning',
      description: 'A refreshing yoga session to start the day.',
      date: new Date(),
      teacher_id: 1,
      users: [1, 2, 3],
      createdAt: new Date()
    },
    {
      id: 2,
      name: 'Yoga Afternoon',
      description: 'A yoga session to end the day.',
      date: new Date(),
      teacher_id: 1,
      users: [4, 5, 6],
      createdAt: new Date()
    }
  ];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      imports: [
        HttpClientModule, 
        MatCardModule, 
        MatIconModule,
        RouterTestingModule
      ],
      providers: [
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: SessionService, useValue: mockSessionService }
      ]
    }).compileComponents();

    mockSessionApiService.all.mockReturnValue(of(mockSessions));

    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display the correct number of sessions', () => {
    const sessionCards = fixture.debugElement.queryAll(By.css('.item'));
    expect(sessionCards.length).toBe(mockSessions.length);
  });

  it('should render the correct title for the first session', () => {
    const sessionCards = fixture.debugElement.queryAll(By.css('.item'));
    const firstSessionTitle = sessionCards[0].query(By.css('mat-card-title'));
    expect(firstSessionTitle.nativeElement.textContent).toContain('Yoga Morning');
  });

  it('should render the correct description for the first session', () => {
    const sessionCards = fixture.debugElement.queryAll(By.css('.item'));
    const firstSessionDescription = sessionCards[0].query(By.css('mat-card-content'));
    expect(firstSessionDescription.nativeElement.textContent).toContain('A refreshing yoga session to start the day.');
  });

  it('should display the create button if user is admin', () => {
    const createButton = fixture.debugElement.query(By.css('button[routerLink="create"]'));
    expect(createButton).toBeTruthy();
    expect(createButton.nativeElement.textContent).toContain('Create');
  });

  it('should not display the create button if user is not admin', () => {
    mockSessionService.sessionInformation = { admin: false };
    fixture.detectChanges();
    const createButton = fixture.debugElement.query(By.css('[data-testid="create"]'));
    expect(createButton).toBeFalsy();
  });

  it('should not display the edit button if user is not admin', () => {
    mockSessionService.sessionInformation = { admin: false };
    fixture.detectChanges();
    const editButton = fixture.debugElement.query(By.css('[data-testid="edit"]'));
    expect(editButton).toBeFalsy();
  });
});

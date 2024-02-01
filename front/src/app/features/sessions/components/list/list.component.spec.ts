import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { ListComponent } from './list.component';
import { of } from 'rxjs';
import { SessionApiService } from '../../services/session-api.service';

describe('ListComponent', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
    },
  };

  const mockSessionApiService = {
    all: jest.fn(() => of<any>([])),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      imports: [HttpClientModule, MatCardModule, MatIconModule],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should retrieve the session list', () => {
    const sessionsObservable = of([
      { id: 1, name: 'Test Session', date: new Date(), location: 'Location X' },
    ]);

    mockSessionApiService.all.mockReturnValue(sessionsObservable);

    component.sessions$.subscribe((sessions) => {
      expect(sessions).toEqual(sessionsObservable);
    });

    expect(mockSessionApiService.all).toHaveBeenCalled();
  });
});

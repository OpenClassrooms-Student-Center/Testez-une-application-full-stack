import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { of } from 'rxjs';
import { RouterTestingModule } from '@angular/router/testing';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { expect } from '@jest/globals';

import { ListComponent } from './list.component';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { Session } from '../../interfaces/session.interface';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';

describe('ListComponent', () => {
  let fixture: ComponentFixture<ListComponent>;
  let component: ListComponent;

  // --- test data ---
  const sessionsMock: Session[] = [
    {
      id: 's1',
      name: 'Morning Flow',
      date: '2025-09-01T09:00:00.000Z',
      description: 'Gentle session',
    } as any,
    {
      id: 's2',
      name: 'Power Yoga',
      date: '2025-09-02T18:30:00.000Z',
      description: 'Strong practice',
    } as any,
  ];

  // --- Services mocks ---
  const sessionApiServiceMock = {
    all: jest.fn().mockReturnValue(of(sessionsMock)),
  };

  const makeSessionService = (admin: boolean) =>
    ({
      sessionInformation: { admin } as SessionInformation,
    } as Partial<SessionService>);

  describe('when user is admin', () => {
    beforeEach(async () => {
      await TestBed.configureTestingModule({
        declarations: [ListComponent],
        imports: [RouterTestingModule, MatCardModule, MatIconModule],
        providers: [
          { provide: SessionApiService, useValue: sessionApiServiceMock },
          { provide: SessionService, useValue: makeSessionService(true) },
        ],
      }).compileComponents();

      fixture = TestBed.createComponent(ListComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });

    it('should create', () => {
      expect(component).toBeTruthy();
    });

    it('should display the list title', () => {
      const titleDe = fixture.debugElement.query(By.css('mat-card-title'));
      expect(titleDe).toBeTruthy();
      expect(titleDe.nativeElement.textContent.trim()).toBe(
        'Session(s) available'
      );
    });

    it('should render one card per session', () => {
      // matcard in the ngFor
      const itemCards = fixture.debugElement.queryAll(By.css('.items .item'));
      expect(itemCards.length).toBe(sessionsMock.length);

      // title of the first session
      const firstTitle = itemCards[0].query(By.css('mat-card-title'))
        .nativeElement.textContent;
      expect(firstTitle).toContain(sessionsMock[0].name);
    });

    it('should show the "Create" button for admin', () => {
      const createBtn = fixture.debugElement.query(
        By.css('mat-card-header button[color="primary"]')
      );
      expect(createBtn).toBeTruthy();

      const text = createBtn.nativeElement.textContent
        .replace(/\s+/g, ' ')
        .trim();
      expect(text).toContain('Create');
    });

    it('should show "Detail" and "Edit" buttons on each session card for admin', () => {
      const cards = fixture.debugElement.queryAll(By.css('.items .item'));
      expect(cards.length).toBe(sessionsMock.length);

      for (const card of cards) {
        const detailBtn = card.query(
          By.css('button[ng-reflect-router-link^="detail"]')
        );
        const editBtn = card.query(
          By.css('button[ng-reflect-router-link^="update"]')
        );
        expect(detailBtn).toBeTruthy();
        expect(editBtn).toBeTruthy();
      }
    });
  });

  describe('when user is NOT admin', () => {
    beforeEach(async () => {
      await TestBed.configureTestingModule({
        declarations: [ListComponent],
        imports: [RouterTestingModule, MatCardModule, MatIconModule],
        providers: [
          { provide: SessionApiService, useValue: sessionApiServiceMock },
          { provide: SessionService, useValue: makeSessionService(false) },
        ],
      }).compileComponents();

      fixture = TestBed.createComponent(ListComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });

    it('should NOT show the "Create" button', () => {
      const createBtn = fixture.debugElement.query(
        By.css('mat-card-header button[color="primary"]')
      );
      // if dont exist query returns null
      expect(createBtn).toBeNull();
    });

    it('should show "Detail" but NOT "Edit" on each session card', () => {
      const cards = fixture.debugElement.queryAll(By.css('.items .item'));
      expect(cards.length).toBe(sessionsMock.length);

      for (const card of cards) {
        const detailBtn = card.query(
          By.css('button[ng-reflect-router-link^="detail"]')
        );
        const editBtn = card.query(
          By.css('button[ng-reflect-router-link^="update"]')
        );
        expect(detailBtn).toBeTruthy();
        expect(editBtn).toBeNull();
      }
    });
  });
});

import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';

import { AppComponent } from './app.component';
import { of } from 'rxjs';
import { Router } from '@angular/router';
import { SessionService } from './services/session.service';
import { AuthService } from './features/auth/services/auth.service';

describe('AppComponent', () => {
  let app: AppComponent;
  let fixture: ComponentFixture<AppComponent>;
  let mockRouter: any;
  let mockSessionService: any;
  let mockAuthService: any;

  beforeEach(async () => {
    mockRouter = {
      navigate: jest.fn()
    };

    mockSessionService = {
      logOut: jest.fn(),
      $isLogged: jest.fn().mockReturnValue(of(true))
    };

    mockAuthService = {};

    await TestBed.configureTestingModule({
      providers: [
        { provide: Router, useValue: mockRouter },
        { provide: SessionService, useValue: mockSessionService },
        { provide: AuthService, useValue: mockAuthService }
      ],
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatToolbarModule
      ],
      declarations: [AppComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    app = fixture.componentInstance;
  });

  it('should create the app', () => {
    expect(app).toBeTruthy();
  });

  it('should call $isLogged() from SessionService and return an observable', (done) => {
    app.$isLogged().subscribe((isLogged) => {
      expect(mockSessionService.$isLogged).toHaveBeenCalled();
      expect(isLogged).toBe(true);
      done();
    });
  });

  it('should log out and navigate to home', () => {
    app.logout();

    expect(mockSessionService.logOut).toHaveBeenCalled();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['']);
  });
});

import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';

import { AppComponent } from './app.component';
import { AuthService } from './features/auth/services/auth.service';
import { Router } from '@angular/router';
import { SessionService } from './services/session.service';
import { SessionInformation } from './interfaces/sessionInformation.interface';
import { BehaviorSubject, of } from 'rxjs';


describe('AppComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatToolbarModule
      ],
      declarations: [
        AppComponent
      ],
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });
describe('logout', ()=>{
  let mockComponent: AppComponent;
  let mockSessionService: any;
  let mockRouter: any;
  let mockAuthService: any;

  mockSessionService= {
      logOut: jest.fn().mockResolvedValue({})
  };

  mockRouter = {navigate: jest.fn().mockReturnValue([])};

  mockAuthService = jest.fn();

  mockComponent = new AppComponent(
    mockAuthService as AuthService,
    mockRouter as Router,
    mockSessionService as SessionService
  );

  it('should logout successfully ', ()=>{
    let sessionInformation!: SessionInformation | undefined;
    let isLogged!: Boolean;
    let isLoggedSubject = new BehaviorSubject<Boolean>(isLogged);

    let isLoggedSub = isLoggedSubject.next(isLogged);

    mockComponent.logout();

    expect(mockSessionService.logOut).toHaveBeenCalledTimes(1);

    mockSessionService.logOut.mockImplementation(
      {sessionInformation: undefined,
      isLogged: false,
      isLoggedSub});

    expect(mockRouter.navigate).toHaveBeenCalledTimes(1);
    expect(mockRouter.navigate).toHaveBeenCalledWith(['']);
  });
});
});

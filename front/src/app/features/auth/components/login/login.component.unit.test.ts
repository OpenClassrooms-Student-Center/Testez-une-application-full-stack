import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { of, throwError } from 'rxjs';

describe('LoginComponent', () => {

  const mockSessionInformation: SessionInformation = { token: '', type: '', id: 1, username: '', firstName: '', lastName: '', admin: true };

  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let mockRouter: Router;
  let mockSessionService: SessionService;
  let mockAuthService: AuthService;

  mockRouter = {
    navigate: jest.fn()
 } as unknown as jest.Mocked<Router>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        SessionService,
        AuthService,
        { provide: Router, useValue: mockRouter }

      ],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule]
    })
      .compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    mockAuthService = TestBed.inject(AuthService);
    mockRouter = TestBed.inject(Router);
    mockSessionService = TestBed.inject(SessionService);

  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call AuthService.login()', () => {
    const loginSpy = jest.spyOn(mockAuthService, 'login').mockReturnValue( of({} as SessionInformation));
    component.submit();
    expect(loginSpy).toHaveBeenCalled;
  });

  it('should call sessionService.logIn()', () => {
    const logInSpy = jest.spyOn(mockSessionService, 'logIn').mockImplementation( () => {} );
    component.submit();
    expect(logInSpy).toHaveBeenCalled;
  });

  it('should navigate to /sessions', () => {
    const navigateSpy = jest.spyOn(mockRouter, 'navigate');
    component.submit();
    expect(navigateSpy).toHaveBeenCalledWith(['/sessions']);
  });

  it('should set onError to true on error during login', () => {
    const error = new Error();
    const errorSpy = jest.spyOn(mockAuthService, 'login').mockReturnValueOnce(throwError(error));
    component.submit();
    expect(component.onError).toBeTruthy();
  });

});

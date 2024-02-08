import { HttpClientModule, HttpErrorResponse } from '@angular/common/http';
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
import { LoginRequest } from '../../interfaces/loginRequest.interface';

describe('LoginComponent integration tests', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let sessionService: SessionService;
  let authService: AuthService;
  let router = {
    navigate: jest.fn()
  } as unknown as jest.Mocked<Router>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        SessionService,
        AuthService,
        { provide: Router, useValue: router }
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
    authService = TestBed.inject(AuthService);
    sessionService = TestBed.inject(SessionService);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should login', () => {
    let loginRequest: LoginRequest = { email: 'yoga@studio.com', password: 'test!1234' };
    let sessionInformation: SessionInformation = { token: 'token', type: 'Bearer', id: 1, username: 'yoga@studio.com', firstName: 'Admin', lastName: 'Admin', admin: true };
    let authServiceSpy = jest.spyOn(authService, 'login').mockReturnValue(of(sessionInformation));
    let sessionServiceSpy = jest.spyOn(sessionService, 'logIn');
    let routerSpy = jest.spyOn(router, 'navigate');
    component.form.controls.email.setValue(loginRequest.email);
    component.form.controls.password.setValue(loginRequest.password);
    let submitButton = fixture.nativeElement.querySelector('form > button[type="submit"]');

    fixture.detectChanges();
    submitButton.click();

    expect(authServiceSpy).toHaveBeenCalledWith(component.form.value);
    expect(sessionServiceSpy).toHaveBeenCalled();
    expect(sessionService.isLogged).toBeTruthy();
    expect(sessionService.sessionInformation).toBe(sessionInformation);
    expect(routerSpy).toHaveBeenCalledWith(['/sessions']);
    expect(component.onError).toBeFalsy();
  });

  it('should not login', () => {
    let loginRequest: LoginRequest = { email: 'yoga@studio.com', password: 'badtest!1234' };
    let errorResponse: {
      path: "/api/auth/login",
      error: "Unauthorized",
      message: "Bad credentials",
      status: 401
    }
    let authServiceSpy = jest.spyOn(authService, 'login').mockReturnValue(throwError(() => errorResponse));
    component.form.controls.email.setValue(loginRequest.email);
    component.form.controls.password.setValue(loginRequest.password);
    let submitButton = fixture.nativeElement.querySelector('form > button[type="submit"]');

    fixture.detectChanges();
    submitButton.click();

    fixture.detectChanges();
    let errorMessage = fixture.nativeElement.querySelector('form > p').textContent;

    expect(authServiceSpy).toHaveBeenCalledWith(component.form.value);
    expect(component.onError).toBeTruthy();
    expect(sessionService.isLogged).toBeFalsy();
    expect(sessionService.sessionInformation).toBe(undefined);
    expect(errorMessage).toContain('error');
  });
});

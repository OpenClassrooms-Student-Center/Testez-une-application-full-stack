import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { By } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';

import { AuthService } from '../../services/auth.service';
import { SessionService } from 'src/app/services/session.service';
import { LoginComponent } from './login.component';

// -- Mocks simples --
const authServiceMock = {
  login: jest.fn(),
};
const sessionServiceMock = {
  logIn: jest.fn(),
};

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: AuthService;
  let sessionService: SessionService;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
      ],
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: SessionService, useValue: sessionServiceMock },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    sessionService = TestBed.inject(SessionService);
    router = TestBed.inject(Router);

    // reset des spies/mocks entre les tests
    jest.clearAllMocks();

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render the form title and inputs', () => {
    const titleDe = fixture.debugElement.query(By.css('mat-card-title'));
    expect(titleDe).toBeTruthy();
    expect(titleDe.nativeElement.textContent.trim()).toBe('Login');

    const emailDebug = fixture.debugElement.query(
      By.css('input[formControlName="email"]')
    );
    const passwordDebug = fixture.debugElement.query(
      By.css('input[formControlName="password"]')
    );
    expect(emailDebug).toBeTruthy();
    expect(passwordDebug).toBeTruthy();
  });

  it('should have the submit button disabled initially', () => {
    const buttonDe = fixture.debugElement.query(
      By.css('button[type="submit"]')
    );
    expect(buttonDe).toBeTruthy();
    expect(buttonDe.nativeElement.disabled).toBe(true);
  });

  it('should enable the submit button when the form is valid', () => {
    component.form.setValue({
      email: 'johndoe@example.com',
      password: 'password',
    });
    fixture.detectChanges();
    const buttonDe = fixture.debugElement.query(
      By.css('button[type="submit"]')
    );
    expect(buttonDe.nativeElement.disabled).toBe(false);
  });

  // --- submit success ---
  it('should submit successfully: call AuthService.login, store session, and navigate', () => {
    const loginRequest = { email: 'johndoe@example.com', password: 'password' };
    const sessionInfo = { token: 't', userId: 1 } as any;
    component.form.setValue(loginRequest);

    (authService.login as jest.Mock).mockReturnValue(of(sessionInfo));
    const logInSpy = jest.spyOn(sessionService, 'logIn');
    const navMock = jest
      .spyOn(router, 'navigate')
      .mockResolvedValue(true as any);

    component.submit();
    fixture.detectChanges();

    expect(authService.login).toHaveBeenCalledWith(loginRequest);
    expect(logInSpy).toHaveBeenCalledWith(sessionInfo);
    expect(navMock).toHaveBeenCalledWith(['/sessions']);
    expect(component.onError).toBe(false);
  });

  // --- submit error ---
  it('should set onError on login failure and show the error message', () => {
    component.form.setValue({ email: 'johndoe@example.com', password: 'bad' });

    (authService.login as jest.Mock).mockReturnValue(
      throwError(() => new Error('Bad credentials'))
    );

    const navSpy = jest.spyOn(router, 'navigate');

    component.submit();
    fixture.detectChanges();

    expect(component.onError).toBe(true);
    expect(navSpy).not.toHaveBeenCalled();

    const compiled: HTMLElement = fixture.nativeElement;
    const errorEl = compiled.querySelector('p.error');
    expect(errorEl?.textContent?.trim()).toBe('An error occurred');
  });

  it('should keep the form invalid and show error when email is missing', () => {
    component.form.setValue({ email: '', password: 'validpass' });
    fixture.detectChanges();
    expect(component.form.invalid).toBe(true);

    const buttonDe = fixture.debugElement.query(
      By.css('button[type="submit"]')
    );
    expect(buttonDe.nativeElement.disabled).toBe(true);
  });

  it('should keep the form invalid and disable submit when password is missing', () => {
    component.form.setValue({ email: 'john.doe@example.com', password: '' });
    fixture.detectChanges();

    expect(component.form.invalid).toBe(true);

    const buttonDe = fixture.debugElement.query(
      By.css('button[type="submit"]')
    );
    expect(buttonDe.nativeElement.disabled).toBe(true);
  });
});

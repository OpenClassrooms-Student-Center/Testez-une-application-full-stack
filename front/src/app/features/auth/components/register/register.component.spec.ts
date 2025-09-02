import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { By } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { expect } from '@jest/globals';

import { RegisterComponent } from './register.component';
import { AuthService } from '../../services/auth.service';

// -- Mocks --
const authServiceMock = {
  register: jest.fn(),
};

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let router: Router;
  let authService: AuthService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
      ],
      providers: [{ provide: AuthService, useValue: authServiceMock }],
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    authService = TestBed.inject(AuthService);

    jest.clearAllMocks();
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render title and inputs', () => {
    const titleDe = fixture.debugElement.query(By.css('mat-card-title'));
    expect(titleDe).toBeTruthy();
    expect(titleDe.nativeElement.textContent.trim()).toBe('Register');

    // Inputs
    expect(
      fixture.debugElement.query(By.css('input[formControlName="firstName"]'))
    ).toBeTruthy();
    expect(
      fixture.debugElement.query(By.css('input[formControlName="lastName"]'))
    ).toBeTruthy();
    expect(
      fixture.debugElement.query(By.css('input[formControlName="email"]'))
    ).toBeTruthy();
    expect(
      fixture.debugElement.query(By.css('input[formControlName="password"]'))
    ).toBeTruthy();
  });

  it('should have submit button disabled initially (invalid form)', () => {
    const btnDe = fixture.debugElement.query(By.css('button[type="submit"]'));
    expect(btnDe).toBeTruthy();
    expect(btnDe.nativeElement.disabled).toBe(true);
  });

  it('should enable submit when form is valid', () => {
    component.form.setValue({
      firstName: 'John',
      lastName: 'Doe',
      email: 'john.doe@example.com',
      password: 'strong-pass',
    });
    fixture.detectChanges();

    const btnDe = fixture.debugElement.query(By.css('button[type="submit"]'));
    expect(btnDe.nativeElement.disabled).toBe(false);
  });

  // --- Register success ---
  it('should submit successfully: call register and navigate to /login', async () => {
    component.form.setValue({
      firstName: 'John',
      lastName: 'Doe',
      email: 'john.doe@example.com',
      password: 'strong-pass',
    });
    fixture.detectChanges();

    (authService.register as jest.Mock).mockReturnValue(of(void 0));
    const navSpy = jest
      .spyOn(router, 'navigate')
      .mockResolvedValue(true as any);

    component.submit();
    fixture.detectChanges();

    expect(authService.register).toHaveBeenCalledWith(component.form.value);
    expect(navSpy).toHaveBeenCalledWith(['/login']);
    expect(component.onError).toBe(false);
  });

  // --- API Error message ---
  it('should set onError when register fails and show error message', () => {
    component.form.setValue({
      firstName: 'John',
      lastName: 'Doe',
      email: 'john.doe@example.com',
      password: 'strong-pass',
    });
    fixture.detectChanges();

    (authService.register as jest.Mock).mockReturnValue(
      throwError(() => new Error('Server error'))
    );

    const navSpy = jest.spyOn(router, 'navigate');

    component.submit();
    fixture.detectChanges();

    expect(component.onError).toBe(true);
    expect(navSpy).not.toHaveBeenCalled();

    const errorEl = (fixture.nativeElement as HTMLElement).querySelector(
      'span.error'
    );
    expect(errorEl?.textContent?.trim()).toBe('An error occurred');
  });

  // --- Form missing ---
  it('should keep form invalid and disable submit when a required field is missing (firstName)', () => {
    component.form.setValue({
      firstName: '',
      lastName: 'Doe',
      email: 'john.doe@example.com',
      password: 'strong-pass',
    });
    fixture.detectChanges();

    expect(component.form.invalid).toBe(true);

    const btnDe = fixture.debugElement.query(By.css('button[type="submit"]'));
    expect(btnDe.nativeElement.disabled).toBe(true);
  });
});

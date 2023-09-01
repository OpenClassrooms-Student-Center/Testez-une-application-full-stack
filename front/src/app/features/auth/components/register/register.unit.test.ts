import { RegisterComponent } from './register.component';
import { FormBuilder, FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { expect } from '@jest/globals';
import { AuthService } from '../../services/auth.service';
import { RegisterRequest } from '../../interfaces/registerRequest.interface';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authService: jest.Mocked<AuthService>;
  let router: jest.Mocked<Router>;

  beforeEach(() => {
    authService = {
      register: jest.fn(),
      login: jest.fn(), // Simulez également la méthode login
    } as unknown as jest.Mocked<AuthService>;

    router = {
      navigate: jest.fn(),
    } as unknown as jest.Mocked<Router>;

    TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      providers: [
        { provide: AuthService, useValue: authService },
        { provide: Router, useValue: router },
      ],
      imports: [
        BrowserAnimationsModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
      ],
    });

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should initialize the form correctly', () => {
    expect(component.form.get('email')).toBeInstanceOf(FormControl);
    expect(component.form.get('firstName')).toBeInstanceOf(FormControl);
    expect(component.form.get('lastName')).toBeInstanceOf(FormControl);
    expect(component.form.get('password')).toBeInstanceOf(FormControl);
  });

  it('should handle an error when registering', () => {
    const registerRequest: RegisterRequest = {
      email: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: 'password123',
    };

    
    authService.register.mockReturnValue(of(void 0));

    component.form.setValue(registerRequest);
    component.submit();

    expect(authService.register).toHaveBeenCalledWith(registerRequest);
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
    expect(component.onError).toBeFalsy();
  });

  it('should set onError to true on error during register', () => {
    const registerRequest: RegisterRequest = {
      email: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: 'password123',
    };

    const errorMessage = 'Registration failed';

    authService.register.mockReturnValueOnce(throwError(errorMessage));

    component.form.setValue(registerRequest);
    component.submit();

    expect(authService.register).toHaveBeenCalledWith(registerRequest);
    expect(router.navigate).not.toHaveBeenCalled();
    expect(component.onError).toBeTruthy();
  });
});


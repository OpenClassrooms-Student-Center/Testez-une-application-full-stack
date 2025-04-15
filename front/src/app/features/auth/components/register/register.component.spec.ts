import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';

import { RegisterComponent } from './register.component';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { of, throwError, lastValueFrom } from 'rxjs';
import { By } from '@angular/platform-browser';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let mockAuthService: any;
  let mockRouter: any;

  beforeEach(async () => {
    mockAuthService = {
      register: jest.fn()
    };

    mockRouter = {
      navigate: jest.fn()
    };

    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      providers: [
        { provide: AuthService, useValue: mockAuthService },
        { provide: Router, useValue: mockRouter }
      ],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should call AuthService.register and navigate to /login on successful submit', async () => {
    const mockFormValues = {
      email: "test@mail.com",
      firstName: "Nata",
      lastName: "Atana",
      password: 'passwd'
    };

    mockAuthService.register.mockReturnValue(of(void 0));

    component.form.setValue(mockFormValues);
    component.submit();
    await fixture.whenStable(); 

    expect(mockAuthService.register).toHaveBeenCalledWith(mockFormValues);
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/login']);
    expect(component.onError).toBe(false);
  });

  it('should set onError to true on failed submit', async () => {
    const mockRegisterRequest = {
      email: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: 'password123',
    };
    
    mockAuthService.register.mockReturnValue(throwError(() => new Error('Registration failed')));

    component.form.setValue(mockRegisterRequest);
    component.submit();
    await fixture.whenStable();

    expect(mockAuthService.register).toHaveBeenCalledWith(mockRegisterRequest);
    expect(component.onError).toBe(true);
    expect(mockRouter.navigate).not.toHaveBeenCalled();
  });


  it('should set onError to true on failed submit', () => {
    const mockRegisterRequest = {
      email: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: 'password123',
    };
    mockAuthService.register.mockReturnValue(throwError(() => new Error('Registration failed')));

    component.form.setValue(mockRegisterRequest);

    component.submit();

    expect(mockAuthService.register).toHaveBeenCalledWith(mockRegisterRequest);
    expect(component.onError).toBe(true);
    expect(mockRouter.navigate).not.toHaveBeenCalled();
  });


  it('should display an error message on registration failure', async () => {
    mockAuthService.register.mockReturnValue(throwError(() => new Error('Register failed')));

    component.form.setValue({
      email: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: 'password123'
    });

    component.submit();
    await fixture.whenStable();
    fixture.detectChanges();

    const errorElement = fixture.debugElement.query(By.css('.error'));
    expect(errorElement).toBeTruthy();
    expect(errorElement.nativeElement.textContent).toContain('An error occurred');
  });

  it('should not display error message on successful register', () => {
      mockAuthService.register.mockReturnValue(of(void 0));

    component.form.setValue({ 
      email: 'test@example.com', 
      lastName: 'Nata', 
      firstName: 'Atana', 
      password: 'password' 
    });

    component.submit();
    fixture.detectChanges();

    expect(component.onError).toBeFalsy();
  
    const errorElement = fixture.debugElement.query(By.css('.error'));
    expect(errorElement).toBeFalsy();
});

  it('should disable submit button if form is invalid', async () => {
    const submitButton = fixture.debugElement.query(By.css('button[type="submit"]'));

    expect(submitButton.nativeElement.disabled).toBeTruthy();

    component.form.get('email')?.setValue('test@example.com');
    fixture.detectChanges();
    expect(submitButton.nativeElement.disabled).toBeTruthy();

    component.form.get('firstName')?.setValue('John');
    fixture.detectChanges();
    expect(submitButton.nativeElement.disabled).toBeTruthy();

    component.form.get('lastName')?.setValue('Doe');
    fixture.detectChanges();
    expect(submitButton.nativeElement.disabled).toBeTruthy();

    component.form.get('password')?.setValue('password123');
    fixture.detectChanges();
    expect(submitButton.nativeElement.disabled).toBeFalsy();
  });
});

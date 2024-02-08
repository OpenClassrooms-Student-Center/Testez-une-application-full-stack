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
import { RouterTestingModule } from '@angular/router/testing';
import { RegisterRequest } from '../../interfaces/registerRequest.interface';
import { of, throwError } from 'rxjs';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authService: AuthService;
  let router = {
    navigate: jest.fn()
  } as unknown as jest.Mocked<Router>

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      providers: [
        AuthService,
        { provide: Router, useValue: router }
      ],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should register', () => {
    let registerRequest: RegisterRequest = {email: "testaccount@mail.com", firstName:"account", lastName:"test", password: "passtest"};
    let authServiceSpy = jest.spyOn(authService, 'register').mockReturnValue(of(void 0));
    let routerSpy = jest.spyOn(router, 'navigate');
    component.form.controls.firstName.setValue(registerRequest.firstName);
    component.form.controls.lastName.setValue(registerRequest.lastName);
    component.form.controls.email.setValue(registerRequest.email);
    component.form.controls.password.setValue(registerRequest.password);
    let submitButton = fixture.nativeElement.querySelector('form > button[type="submit"]');

    fixture.detectChanges();
    submitButton.click();

    expect(authServiceSpy).toHaveBeenCalledWith(component.form.value);
    expect(routerSpy).toHaveBeenCalledWith(['/login']);
    expect(component.onError).toBeFalsy();
  });

  it('should not register', () => {
    let registerRequest: RegisterRequest = {email: "testaccount@mail.com", firstName:"account", lastName:"test", password: "pa"};
    let errorResponse =  'Bad request';
    let authServiceSpy = jest.spyOn(authService, 'register').mockReturnValue(throwError(() => errorResponse));
    component.form.controls.firstName.setValue(registerRequest.firstName);
    component.form.controls.lastName.setValue(registerRequest.lastName);
    component.form.controls.email.setValue(registerRequest.email);
    component.form.controls.password.setValue(registerRequest.password);
    let submitButton = fixture.nativeElement.querySelector('form > button[type="submit"]');

    fixture.detectChanges();
    submitButton.click();

    fixture.detectChanges();
    let errorMessage = fixture.nativeElement.querySelector('form > span').textContent;

    expect(authServiceSpy).toHaveBeenCalledWith(component.form.value);
    expect(component.onError).toBeTruthy();
    expect(errorMessage).toContain('error');
  });
});

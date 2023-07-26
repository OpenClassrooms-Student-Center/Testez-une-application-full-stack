import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { of, throwError } from 'rxjs';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [SessionService],
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
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  }),

  describe('LoginComponent', ()=>{
    let component: LoginComponent;
    let mockFB: FormBuilder;
    let mockAuthService: any;
    let mockSessionService: any;
    let mockRouter: any;

    mockAuthService = {login: jest.fn().mockReturnValue
    ({subscribe: jest.fn()})};

    mockSessionService = {
      logIn: jest.fn()};

    mockRouter = {navigate: jest.fn()};
    
    mockFB = new FormBuilder();

    const validRequest = {
      email:'yoga@studio.com',
      password:'test!1234'
    };

    component = new LoginComponent(
      mockAuthService as AuthService,
      mockFB,
      mockRouter as Router,
      mockSessionService as SessionService
    );   

    component.form = mockFB.group(validRequest);

    it('should have a sucessful login', ()=>{
      const login = { email:'yoga@studio.com', password: 'test!1234'};

      const responseSessionInformation = {
        token: "testToken",
        type: "",
        id: 1,
        username: "Admin",
        firstName: "Admin",
        lastName: "Admin",
        admin: true
      };

      component.form.setValue(login);
      component.submit();

      expect(mockAuthService.login).toHaveBeenCalledTimes(1);
      expect(mockAuthService.login).toHaveBeenCalledWith(login);

      mockAuthService.login.mockReturnValueOnce(of(responseSessionInformation));
      
      component.submit();
      expect(mockSessionService.logIn).toHaveBeenCalledTimes(1);
      expect(mockSessionService.logIn).toHaveBeenCalledWith(responseSessionInformation);
      
      expect(mockRouter.navigate).toHaveBeenCalledTimes(1);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['/sessions']);
    });
    it('show error to bad logins', ()=>{
      // When AuthService return a error
    
     
      mockAuthService.login.mockReturnValue(throwError('Invalid Credentials'));
      
      component.submit();
      expect(component.onError).toBe(true);
      expect(mockSessionService.logIn).not.toHaveBeenCalled;
      expect(mockRouter.navigate).not.toHaveBeenCalled;

      // To bad required fields
      const invalidLogin = { email: 'invalid@user.com', password: 'invalidpassword' };

        component.form.setValue(invalidLogin);

        component.submit();

        expect(component.onError).toBe(true);

        // To empty required field

        const emptyLogin = {email: 'yoga@studio.com', password: ''};

        component.form.setValue(emptyLogin);

        mockAuthService.login.mockReturnValue(throwError('Empty Required Field'));

        expect(component.onError).toBe(true);
        
    });
    
  });
});

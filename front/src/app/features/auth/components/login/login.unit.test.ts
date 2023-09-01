import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginComponent } from './login.component';
import { AuthService } from '../../services/auth.service';
import { SessionService } from 'src/app/services/session.service';
import { Observable, of, throwError } from 'rxjs';
import { MatCardModule } from '@angular/material/card';
import { RouterTestingModule } from '@angular/router/testing';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule } from '@angular/common/http';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { expect } from '@jest/globals';

describe('LoginComponent', () => {
    let component: LoginComponent;
    let fixture: ComponentFixture<LoginComponent>;
    let authServiceMock: Partial<AuthService>;
    let sessionServiceMock: Partial<SessionService>;
    let routerMock: Partial<Router>;

    beforeEach(() => {
        authServiceMock = {
            login: jest.fn()
        };
        sessionServiceMock = {
            logIn: jest.fn()
        };
        routerMock = {
            navigate: jest.fn()
        };

        TestBed.configureTestingModule({
            declarations: [LoginComponent],
            providers: [
                FormBuilder,
                { provide: AuthService, useValue: authServiceMock },
                { provide: SessionService, useValue: sessionServiceMock },
                { provide: Router, useValue: routerMock }
            ],
            imports: [
                RouterTestingModule,
                BrowserAnimationsModule,
                HttpClientModule,
                MatCardModule,
                MatIconModule,
                MatFormFieldModule,
                MatInputModule,
                ReactiveFormsModule
            ]
        }).compileComponents();

        fixture = TestBed.createComponent(LoginComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });

    it('should handle successful login', () => {
        const loginRequest = { email: 'test@example.com', password: 'password' };
        const sessionInfo = {
            token: 'token',
            type: 'type',
            id: 1,
            username: 'user',
            firstName: 'John',
            lastName: 'Doe',
            admin: false
        };

        authServiceMock.login = jest.fn(() => of(sessionInfo));

        component.form.setValue(loginRequest);
        component.submit();

        expect(authServiceMock.login).toHaveBeenCalledWith(loginRequest);
        expect(sessionServiceMock.logIn).toHaveBeenCalledWith(sessionInfo);
        expect(routerMock.navigate).toHaveBeenCalledWith(['/sessions']);
        expect(component.onError).toBeFalsy(); 
    });

    it('should handle login error', () => {
        const loginRequest = { email: 'test@example.com', password: 'password' };

        authServiceMock.login = jest.fn(() => throwError('Login error'));

        component.form.setValue(loginRequest);
        component.submit();

        expect(authServiceMock.login).toHaveBeenCalledWith(loginRequest);
        expect(sessionServiceMock.logIn).not.toHaveBeenCalled();
        expect(routerMock.navigate).not.toHaveBeenCalled();
        expect(component.onError).toBeTruthy();
    });
});

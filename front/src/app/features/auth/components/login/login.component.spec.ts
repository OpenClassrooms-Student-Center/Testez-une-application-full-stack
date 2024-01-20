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
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { fillInputs } from 'src/utils/test.utils';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { of } from 'rxjs';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  const fakeRouter = {
    navigate: (commands: any[], extras?, options?: any) => {},
  } as Router;

  let authServiceMock: AuthService;
  let sessionServiceMock: SessionService;
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientTestingModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
      ],
      providers: [
        AuthService,
        SessionService,
        { provide: Router, useValue: fakeRouter },
      ],
    }).compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;

    authServiceMock = TestBed.inject(AuthService);
    sessionServiceMock = TestBed.inject(SessionService);

    fixture.detectChanges();
  });

  it('should render the component with 2 form fields from Angular Material', () => {
    expect(component).toBeTruthy();
  });

  it('should login without any errors', () => {
    const mockSessionInformation: SessionInformation = {
      token: 'jwt',
      type: 'session',
      id: 1,
      username: 'user@user.com',
      firstName: 'User',
      lastName: 'User',
      admin: false,
    };
    // create a spy on the login method of the auth service
    const authServiceSpy = jest
      .spyOn(authServiceMock, 'login')
      .mockReturnValueOnce(of(mockSessionInformation));
    // create a spy on the logIn method of the session service
    const sessionServiceSpy = jest.spyOn(sessionServiceMock, 'logIn');

    const navigateSpy = jest.spyOn(fakeRouter, 'navigate');

    const nativeEl = fixture.nativeElement;

    const form = nativeEl.querySelector('.login-form')! as HTMLFormElement;

    const expectedData = {
      email: 'user@user.com',
      password: 'test!1234',
    };

    // Enter form data
    fillInputs(form, expectedData);

    form.submit();

    expect(authServiceSpy).toHaveBeenCalledWith(expectedData);

    expect(sessionServiceSpy).toHaveBeenCalled;
    expect(sessionServiceMock.sessionInformation).toBe(mockSessionInformation);

    expect(navigateSpy).toHaveBeenCalledWith(['/sessions']);
  });
});

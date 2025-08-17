import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule,FormBuilder } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { LoginComponent } from './login.component';

class MockAuthService {
  login = jest.fn();
}
class MockRouter {
  navigate = jest.fn();
}
class MockSessionService {
  logIn = jest.fn();
}

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let mockAuthService: MockAuthService;
  let mockRouter: MockRouter; 
  let mockSessionService: MockSessionService;

  beforeEach(async () => {
    mockAuthService = new MockAuthService();
    mockRouter = new MockRouter();
    mockSessionService = new MockSessionService();


    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        { provide: AuthService, useValue: mockAuthService },
        { provide: Router, useValue: mockRouter },
        { provide: SessionService, useValue: mockSessionService },
        FormBuilder
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
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should log in and navigate on success', () => {
    const sessionInfo = { id: 1, token: 'abc' };
    mockAuthService.login.mockReturnValue(of(sessionInfo));
    component.form.setValue({ email: 'test@mail.com', password: '1234' });

    component.submit();

    expect(mockAuthService.login).toHaveBeenCalledWith({ email: 'test@mail.com', password: '1234' });
    expect(mockSessionService.logIn).toHaveBeenCalledWith(sessionInfo);
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/sessions']);
    expect(component.onError).toBe(false);
  });
  it('should set onError to true on error', () => {
    mockAuthService.login.mockReturnValue(throwError(() => new Error('fail')));
    component.form.setValue({ email: 'test@mail.com', password: '1234' });

    component.submit();

    expect(mockAuthService.login).toHaveBeenCalled();
    expect(component.onError).toBe(true);
  });
});

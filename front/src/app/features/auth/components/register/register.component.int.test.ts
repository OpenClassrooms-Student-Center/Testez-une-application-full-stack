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
import { of, throwError } from 'rxjs';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let mockAuthService: AuthService;
  let mockRouter: Router;

  mockRouter = {
    navigate: jest.fn(),
  } as unknown as jest.Mocked<Router>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      providers: [AuthService, { provide: Router, useValue: mockRouter }],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    mockAuthService = TestBed.inject(AuthService);
    mockRouter = TestBed.inject(Router);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should navigate to login page when register request succeeds', () => {
    const registerSpy = jest
      .spyOn(mockAuthService, 'register')
      .mockReturnValue(of(void 0));
    const navigateSpy = jest.spyOn(mockRouter, 'navigate');
    component.submit();

    expect(navigateSpy).toHaveBeenCalledWith(['/login']);
    expect(registerSpy).toHaveBeenCalled;
  });

  it('should set onError to true on error during register', () => {
    const errorSpy = jest
      .spyOn(mockAuthService, 'register')
      .mockReturnValueOnce(throwError(new Error()));
    component.submit();
    expect(component.onError).toBeTruthy();
  });
});

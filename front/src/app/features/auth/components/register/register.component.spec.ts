import { of, throwError } from 'rxjs';
import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { RegisterComponent } from './register.component';



// Mocks typés
class MockAuthService {
  register = jest.fn();
}
class MockRouter {
  navigate = jest.fn();
}

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let mockAuthService: MockAuthService;
  let mockRouter: MockRouter

  beforeEach(async () => {
    mockAuthService = new MockAuthService();
    mockRouter = new MockRouter();

    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,  
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [
        { provide: FormBuilder, useClass: FormBuilder },
        { provide: Router, useValue: mockRouter },
        { provide: AuthService, useValue: mockAuthService }
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should navigate to /login on successful register', () => {
  mockAuthService.register.mockReturnValue(of(undefined));
  component.form.setValue({
    email: 'test@mail.com',
    firstName: 'TestFirstName',
    lastName: 'TestLastName',
    password: '123456'
  });

  component.submit();

  expect(mockAuthService.register).toHaveBeenCalled();
  expect(mockRouter.navigate).toHaveBeenCalledWith(['/login']);
});

  it('should set onError to true on register error', () => {
    mockAuthService.register.mockReturnValue(throwError(() => new Error('fail')));
    component.form.setValue({ email: 'test@mail.com', password: '123456', lastName: 'TestLastName', firstName: 'TestFirstName' });

    component.submit();

    expect(mockAuthService.register).toHaveBeenCalled();
    expect(component.onError).toBe(true);
  });
});

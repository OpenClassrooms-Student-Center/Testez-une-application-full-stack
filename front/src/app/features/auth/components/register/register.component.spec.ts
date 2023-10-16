import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Form, FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';

import { RegisterComponent } from './register.component';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { of, throwError } from 'rxjs';
import { RegisterRequest } from '../../interfaces/registerRequest.interface';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;

  beforeEach(async () => {
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
describe('Submit', ()=>{
  let mockComponent: RegisterComponent;
  let mockFB: FormBuilder;
  let mockRouter: any;
  let mockAuthService: any;

  mockFB = new FormBuilder();

  mockAuthService = {
    register: jest.fn().mockReturnValue({subscribe: jest.fn()})
  };

  mockRouter = {
    navigate: jest.fn()
  }

  const registerFields = {
    email:'',
    firstName: '',
    lastName:'',
    password:''
  }

  mockComponent = new RegisterComponent(
    mockAuthService as AuthService,
    mockFB,
    mockRouter as Router
  );

  mockComponent.form = mockFB.group(registerFields);
  
  it('should create an account with success', ()=>{
    
    const requiredFields = {
      email:'test@test.com',
      firstName:'Admin',
      lastName:'test',
      password:'Test!1234'
    };

    const registerRequest = mockComponent.form.setValue(requiredFields);
    mockAuthService.register.mockReturnValue(of(registerRequest));

    mockComponent.submit();

    expect(mockAuthService.register).toHaveBeenCalledTimes(1);
    expect(mockAuthService.register).toHaveBeenCalledWith(requiredFields);

    expect(mockRouter.navigate).toHaveBeenCalledWith(['/login']);
  });
  it('show error to empty required field', ()=>{

    const invalidRegister: RegisterRequest = {
      email:'',
      firstName:'',
      lastName:'',
      password:'Test!1234'
    };

    mockComponent.form.setValue(invalidRegister);

    mockAuthService.register.mockReturnValue(throwError('Empty required fields'));
    mockComponent.submit();

    expect(mockComponent.onError).toBe(true);

    expect(mockRouter.navigate).not.toHaveBeenCalledWith();

    });
  });
});

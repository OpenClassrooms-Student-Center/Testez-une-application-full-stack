import { TestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import { expect } from '@jest/globals';

import { AuthService } from './auth.service';
import { LoginRequest } from '../interfaces/loginRequest.interface';
import { RegisterRequest } from '../interfaces/registerRequest.interface';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;

  const baseUrl = 'api/auth';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthService],
    });

    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('register() should POST a RegisterRequest and return void', () => {
    const registerData: RegisterRequest = {
      email: 'john@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: 'secret',
    };

    service.register(registerData).subscribe((res) => {
      expect(res).toBeUndefined();
    });

    const req = httpMock.expectOne(`${baseUrl}/register`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(registerData);

    req.flush(null); // simulate void response
  });

  it('login() should POST a LoginRequest and return SessionInformation', () => {
    const loginData: LoginRequest = {
      email: 'john@example.com',
      password: 'secret',
    };

    const mockResponse: SessionInformation = {
      id: 1,
      token: 'jwt-token',
      admin: true,
      type: '',
      username: '',
      firstName: '',
      lastName: '',
    };

    service.login(loginData).subscribe((res) => {
      expect(res).toEqual(mockResponse);
    });

    const req = httpMock.expectOne(`${baseUrl}/login`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(loginData);

    req.flush(mockResponse);
  });
});

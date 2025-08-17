import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AuthService } from './auth.service';
import { RegisterRequest } from '../interfaces/registerRequest.interface';
import { LoginRequest } from '../interfaces/loginRequest.interface';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { expect } from '@jest/globals';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthService]
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

  it('should call POST /register with RegisterRequest', () => {
    const registerRequest: RegisterRequest = {
      email: 'test@test.com',
      password: 'password',
      firstName: 'John',
      lastName: 'Doe'
    };

    service.register(registerRequest).subscribe(response => {
      expect(response).toBeUndefined();
    });

    const req = httpMock.expectOne('api/auth/register');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(registerRequest);
    req.flush(null);
  });

  it('should call POST /login with LoginRequest and return SessionInformation', () => {
    const loginRequest: LoginRequest = {
      email: 'test@test.com',
      password: 'password'
    };

    const sessionInfo: SessionInformation = {
      id: 1,
      admin: true,
      token: 'token',
      type: 'user',
      username: 'johndoe',
      firstName: 'John',
      lastName: 'Doe'
    };

    service.login(loginRequest).subscribe(response => {
      expect(response).toEqual(sessionInfo);
    });

    const req = httpMock.expectOne('api/auth/login');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(loginRequest);
    req.flush(sessionInfo);
  });
});
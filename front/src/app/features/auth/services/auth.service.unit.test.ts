import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { LoginRequest } from '../interfaces/loginRequest.interface';
import { RegisterRequest } from '../interfaces/registerRequest.interface';
import { AuthService } from './auth.service';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';

describe('AuthService', () => {
  let authService: AuthService;
  let mockHttpTestingController: HttpTestingController;
  let pathService = 'api/auth';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ]
    });
    authService = TestBed.inject(AuthService);
    mockHttpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    mockHttpTestingController.verify();
  });

  it('should be created', () => {
    expect(authService).toBeTruthy();
  });

  describe('register', () => {
    it('makes expected calls', () => {
      const mockRegisterRequest: RegisterRequest = {email: '', firstName: '', lastName: '', password: ''};
      authService.register(mockRegisterRequest).subscribe(res => {
        expect(res).toEqual(mockRegisterRequest);
      });
      const req = mockHttpTestingController.expectOne(`${pathService}/register`);
      expect(req.request.method).toBe('POST');
      req.flush(mockRegisterRequest);
    });
  });

  describe('login', () => {
    it('makes expected calls', () => {
      const mockLoginRequest: LoginRequest = {email: 'test@mail.com', password: ''};
      const mockSessionInformation: SessionInformation = { token: '', type: '', id: 1, username: '', firstName: '', lastName: '', admin: true };

      authService.login(mockLoginRequest).subscribe(sessionInfo => {
        expect(sessionInfo).toEqual(mockSessionInformation);
      });
      const req = mockHttpTestingController.expectOne(`${pathService}/login`);
      expect(req.request.method).toEqual('POST');
      req.flush(mockSessionInformation);
    });
  });
});

import { TestBed } from '@angular/core/testing';
import { HttpClient } from '@angular/common/http';
import { of, throwError } from 'rxjs';
import { AuthService } from './auth.service';
import { LoginRequest } from '../interfaces/loginRequest.interface';
import { RegisterRequest } from '../interfaces/registerRequest.interface';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { expect } from '@jest/globals';

describe('AuthService', () => {
    let service: AuthService;
    let httpClientSpy: any;

    const registerRequest: RegisterRequest = {
        email: 'nata@mail.com',
        firstName: 'Natao',
        lastName: 'Atana',
        password: 'password123'
    };

    const loginRequest: LoginRequest = {
        email: 'nata@mail.com',
        password: 'password123'
    };

    const sessionInfo: SessionInformation = {
        token: 'fakeToken',
        type: 'yoga',
        id: 1,
        username: 'user',
        firstName: 'Nata',
        lastName: 'Atana',
        admin: true
    };

    beforeEach(() => {
        httpClientSpy = {
            post: jest.fn()
        };

        TestBed.configureTestingModule({
            providers: [
                AuthService,
                { provide: HttpClient, useValue: httpClientSpy }
            ]
        });

        service = TestBed.inject(AuthService);
    });

    it('should be created', () => {
        expect(service).toBeTruthy();
    });

    describe('register', () => {
        it('should call register API and return void on success', () => {
            httpClientSpy.post.mockReturnValue(of(undefined));

            service.register(registerRequest).subscribe(response => {
                expect(response).toBeUndefined();
                expect(httpClientSpy.post).toHaveBeenCalledWith('api/auth/register', registerRequest);
            });
        });

        it('should handle error and return empty response on failure', () => {
            httpClientSpy.post.mockReturnValue(throwError(() => new Error('Error occurred')));

            service.register(registerRequest).subscribe({
                next: () => fail('expected an error, not a response'),
                error: error => expect(error).toBeTruthy()
            });
        });
    });

    describe('login', () => {
        it('should call login API and return session information on success', () => {
            httpClientSpy.post.mockReturnValue(of(sessionInfo));

            service.login(loginRequest).subscribe(session => {
                expect(session).toEqual(sessionInfo);
                expect(httpClientSpy.post).toHaveBeenCalledWith('api/auth/login', loginRequest);
            });
        });

        it('should handle error and return an empty session on failure', () => {
            httpClientSpy.post.mockReturnValue(throwError(() => new Error('Error occurred')));

            service.login(loginRequest).subscribe({
                next: () => fail('expected an error, not session info'),
                error: error => expect(error).toBeTruthy()
            });
        });
    });
});

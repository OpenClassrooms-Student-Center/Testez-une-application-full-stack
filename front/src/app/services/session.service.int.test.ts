import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should set isLogged to true after logIn', () => {
    const user: SessionInformation = {
      token: 'testToken',
      type: 'user',
      id: 1,
      username: 'testUser',
      firstName: 'John',
      lastName: 'Doe',
      admin: false,
    };

    service.logIn(user);

    expect(service.isLogged).toBeTruthy();
  });

  it('should set isLogged to false after logOut', () => {
    service.logOut();

    expect(service.isLogged).toBeFalsy();
  });

  it('should return observable for isLogged', () => {
    service.$isLogged().subscribe((isLogged) => {
      expect(isLogged).toBeFalsy();
    });

    service.logOut();
  });
  it('should create a session when logging in', () => {
    const user: SessionInformation = {
      token: 'testToken',
      type: 'user',
      id: 1,
      username: 'testUser',
      firstName: 'John',
      lastName: 'Doe',
      admin: false,
    };
  
    service.logIn(user);
  
    expect(service.isLogged).toBeTruthy();
    expect(service.sessionInformation).toEqual(user);
  });
  
  it('should remove the session when logging out', () => {
    const user: SessionInformation = {
      token: 'testToken',
      type: 'user',
      id: 1,
      username: 'testUser',
      firstName: 'John',
      lastName: 'Doe',
      admin: false,
    };
  
    service.logIn(user);
    service.logOut();
  
    expect(service.isLogged).toBeFalsy();
    expect(service.sessionInformation).toBeUndefined();
  });
  
});

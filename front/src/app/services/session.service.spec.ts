import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';

describe('SessionService', () => {
  let service: SessionService;

  const user = {
    token: 'string',
    type: 'string',
    id: 1,
    username: 'toto',
    firstName: 'toto',
    lastName: 'qwincy',
    admin: false
  };

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return true if the user is logged', () => {
    service.isLogged = true;
    expect(service.$isLogged()).toBeTruthy();
  });

  it('should return false if the user is not logged', () => {
    service.isLogged = false;
    expect(service.$isLogged()).toBeTruthy();
  });

  it('Should log in the user', () => {
    service.logIn(user);
    expect(service.isLogged).toBeTruthy();
    expect(service.sessionInformation).toEqual(user);
  });

  it('Should log out the user', () => {
    service.logOut();
    expect(service.isLogged).toBeFalsy();
    expect(service.sessionInformation).toBeUndefined();
  });


});


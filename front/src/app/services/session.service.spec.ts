import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService unit tests', () => {
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should log in the user', () => {
    let sessionInformation: SessionInformation = { token: 'token', type: 'Bearer', id: 1, username: 'yoga@studio.com', firstName: 'Admin', lastName: 'Admin', admin: true };
    service.logIn(sessionInformation);
    expect(service.sessionInformation).toEqual(sessionInformation);
    expect(service.isLogged).toBeTruthy();
  });

  it('should log out the user', () => {
    service.logOut();
    expect(service.sessionInformation).toEqual(undefined);
    expect(service.isLogged).toBeFalsy();
  });
});

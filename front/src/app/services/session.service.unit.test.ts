import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';
import {  Observable } from 'rxjs';

describe('SessionService', () => {

  const mockSessionInformation: SessionInformation = { token: '', type: '', id: 1, username: '', firstName: '', lastName: '', admin: true };

  let sessionService: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    sessionService = TestBed.inject(SessionService);
  });

  describe('initialization', () => {
    it('should be created', () => {
      expect(sessionService).toBeTruthy();
    });

    it('should init  set isLogged to false', () => {
      expect(sessionService.isLogged).toBeFalsy();
    });

    it('should init set sessionInformation to undefined', () => {
      expect(sessionService.sessionInformation).toBeUndefined();
    });
  });

  describe('$isLogged', () => {
    it('should return an Observable for $isLogged()', () => {
      expect(sessionService.$isLogged()).toBeInstanceOf(Observable);
    });
  });

  describe('logIn', () => {
    it('should set sessionInformation and set isLogger to true on logIn', () => {
      sessionService.logIn(mockSessionInformation);
      expect(sessionService.isLogged).toBeTruthy();
      expect(sessionService.sessionInformation).toEqual(mockSessionInformation);
    });
  });

  describe('logOut', () => {
    it('should set sessionInformation to undefined and set isLogger to false on logOut', () => {
      sessionService.logOut();
      expect(sessionService.isLogged).toBeFalsy();
      expect(sessionService.sessionInformation).toBeUndefined();
    });
  });

});

import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SessionService]
    });
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should emit the current isLogged value', (done) => {
    service.$isLogged().subscribe(value => {
      expect(value).toBe(false); // valeur initiale
      done();
    });
  });

  it('should emit true after logIn and false after logOut', (done) => {
    const user = {
    id: 1,
    admin: true,
    token: 'fake-token',
    type: 'user',
    username: 'user1',
    firstName: 'User',
    lastName: 'One'
  };

    const emitted: boolean[] = [];
    const sub = service.$isLogged().subscribe(val => {
      emitted.push(val);
      if (emitted.length === 3) {
        expect(emitted).toEqual([false, true, false]);
        sub.unsubscribe();
        done();
      }
    });

    service.logIn(user);
    service.logOut();
  });
});

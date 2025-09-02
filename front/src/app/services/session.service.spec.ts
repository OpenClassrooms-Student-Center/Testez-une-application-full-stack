import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';
import { expect } from '@jest/globals';

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    service = new SessionService();
  });

  it('should start with isLogged = false and no sessionInformation', () => {
    expect(service.isLogged).toBe(false);
    expect(service.sessionInformation).toBeUndefined();
  });

  it('should log in and update sessionInformation + isLogged', () => {
    const sessionInfo: SessionInformation = { id: 1, admin: true } as any;

    service.logIn(sessionInfo);

    expect(service.isLogged).toBe(true);
    expect(service.sessionInformation).toEqual(sessionInfo);
  });

  it('should log out and clear sessionInformation + set isLogged = false', () => {
    const sessionInfo: SessionInformation = { id: 1, admin: true } as any;
    service.logIn(sessionInfo);

    service.logOut();

    expect(service.isLogged).toBe(false);
    expect(service.sessionInformation).toBeUndefined();
  });

  it('should emit values on $isLogged observable', (done) => {
    const values: boolean[] = [];
    const sub = service.$isLogged().subscribe((val) => {
      values.push(val);
      if (values.length === 2) {
        expect(values).toEqual([false, true]); // starts false → then logIn sets true
        sub.unsubscribe();
        done();
      }
    });

    service.logIn({ id: 2, admin: false } as any);
  });
});

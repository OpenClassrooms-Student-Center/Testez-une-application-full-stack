import { UnauthGuard } from './unauth.guard';
import { SessionService } from '../services/session.service';
import { Router } from '@angular/router';
import { expect } from '@jest/globals';

describe('UnauthGuard', () => {
  let unauthGuard: UnauthGuard;
  let sessionServiceMock: Partial<SessionService>;
  let routerMock: Partial<Router>;

  beforeEach(() => {
    sessionServiceMock = {
      isLogged: true,
    };

    routerMock = {
      navigate: jest.fn(),
    };

    unauthGuard = new UnauthGuard(
      routerMock as Router,
      sessionServiceMock as SessionService
    );
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  it('can activate when user is not logged in', () => {
    sessionServiceMock.isLogged = false;

    const result = unauthGuard.canActivate();

    expect(result).toBe(true);
    expect(routerMock.navigate).not.toHaveBeenCalled();
  });

  it('cannot activate when user is logged in', () => {
    sessionServiceMock.isLogged = true;

    const result = unauthGuard.canActivate();

    expect(result).toBe(false);
    expect(routerMock.navigate).toHaveBeenCalledWith(['rentals']);
  });
});

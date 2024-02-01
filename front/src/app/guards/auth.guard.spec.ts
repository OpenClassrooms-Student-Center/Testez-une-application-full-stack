import { AuthGuard } from './auth.guard';
import { SessionService } from '../services/session.service';
import { Router } from '@angular/router';
import { expect } from '@jest/globals';

describe('AuthGuard', () => {
  let authGuard: AuthGuard;
  let sessionServiceMock: Partial<SessionService>;
  let routerMock: Partial<Router>;

  beforeEach(() => {
    sessionServiceMock = {
      isLogged: false,
    };

    routerMock = {
      navigate: jest.fn(),
    };

    authGuard = new AuthGuard(
      routerMock as Router,
      sessionServiceMock as SessionService
    );
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  it('can activate when user is logged in', () => {
    sessionServiceMock.isLogged = true;

    const result = authGuard.canActivate();

    expect(result).toBe(true);
    expect(routerMock.navigate).not.toHaveBeenCalled();
  });

  it('cannot activate when user is not logged in', () => {
    sessionServiceMock.isLogged = false;

    const result = authGuard.canActivate();

    expect(result).toBe(false);
    expect(routerMock.navigate).toHaveBeenCalledWith(['login']);
  });
});

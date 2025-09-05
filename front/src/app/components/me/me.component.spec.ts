// me.component.spec.ts
import { TestBed, ComponentFixture } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { of } from 'rxjs';
import { MeComponent } from './me.component';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SessionService } from '../../services/session.service';
import { UserService } from '../../services/user.service';
import { expect } from '@jest/globals'; // utile si ton setup ne rend pas expect global

describe('MeComponent (Jest)', () => {
  let fixture: ComponentFixture<MeComponent>;
  let component: MeComponent;

  const routerMock = { navigate: jest.fn() } as unknown as Router;
  const snackMock = { open: jest.fn() } as unknown as MatSnackBar;
  const sessionMock = {
    sessionInformation: { id: 1, admin: true },
    logOut: jest.fn(),
  } as unknown as SessionService;

  const mockUser = { id: '1', name: 'John Doe' } as any;

  const userServiceMock = {
    getById: jest.fn().mockReturnValue(of(mockUser)),
    delete: jest.fn().mockReturnValue(of(void 0)),
  } as unknown as UserService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      providers: [
        { provide: Router, useValue: routerMock },
        { provide: MatSnackBar, useValue: snackMock },
        { provide: SessionService, useValue: sessionMock },
        { provide: UserService, useValue: userServiceMock },
      ],
      schemas: [NO_ERRORS_SCHEMA],
    })
      .overrideComponent(MeComponent, { set: { template: '' } })
      .compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  it('ngOnInit charge le user via getById avec l’id de session', () => {
    fixture.detectChanges(); // déclenche ngOnInit
    expect(userServiceMock.getById).toHaveBeenCalledWith('1');
    expect(component.user).toBe(mockUser);
  });

  it('delete() supprime, affiche le snackbar, logout et navigue /', () => {
    component.delete();

    expect(userServiceMock.delete).toHaveBeenCalledWith('1');
    expect(snackMock.open).toHaveBeenCalledWith(
      'Your account has been deleted !',
      'Close',
      { duration: 3000 }
    );
    expect(sessionMock.logOut).toHaveBeenCalled();
    expect(routerMock.navigate).toHaveBeenCalledWith(['/']);
  });

  it('back() appelle window.history.back()', () => {
    const backSpy = jest
      .spyOn(window.history, 'back')
      .mockImplementation(() => undefined as any);
    component.back();
    expect(backSpy).toHaveBeenCalled();
  });
});

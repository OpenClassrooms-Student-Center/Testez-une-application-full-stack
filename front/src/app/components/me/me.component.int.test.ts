import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';

import { MeComponent } from './me.component';
import { expect } from '@jest/globals';
import { RouterTestingModule } from '@angular/router/testing';
import { UserService } from 'src/app/services/user.service';
import { Router } from '@angular/router';
import { User } from 'src/app/interfaces/user.interface';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';

describe('MeComponent', () => {
  const mockUser: User = {
    id: 1,
    email: '',
    lastName: '',
    firstName: '',
    admin: true,
    password: '',
    createdAt: new Date(),
    updatedAt: new Date(),
  };

  const mockSessionInformation: SessionInformation = {
    token: '',
    type: '',
    id: mockUser.id,
    username: mockUser.email,
    firstName: mockUser.firstName,
    lastName: mockUser.lastName,
    admin: mockUser.admin,
  };

  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let sessionService: SessionService;
  let userService: UserService;
  let mockRouter: jest.Mocked<any>;
  let mockMatSnackBar: MatSnackBar;
  let controller: HttpTestingController;

  const pathService: string = 'api/user';

  mockRouter = {
    navigate: jest.fn(),
  } as unknown as jest.Mocked<Router>;

  mockMatSnackBar = {
    open: jest.fn(),
  } as unknown as jest.Mocked<MatSnackBar>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        RouterTestingModule,
        MatSnackBarModule,
        HttpClientTestingModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
      ],
      providers: [
        SessionService,
        UserService,
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        { provide: Router, useValue: mockRouter },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    sessionService = TestBed.inject(SessionService);
    sessionService.logIn(mockSessionInformation);
    userService = TestBed.inject(UserService);
    mockRouter = TestBed.inject(Router);
    mockMatSnackBar = TestBed.inject(MatSnackBar);
    controller = TestBed.inject(HttpTestingController);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should delete user', () => {
    expect(sessionService.isLogged).toBeTruthy();

    const deleteSpy = jest.spyOn(userService, 'delete');
    const logoutSpy = jest.spyOn(sessionService, 'logOut');

    component.delete();

    fixture.whenStable().then(() => {
      const requests = controller.match(pathService + '/1');
      expect(requests.length).toBe(2);

      requests[0].flush(mockUser);
      requests[1].flush('user deleted');

      expect(deleteSpy).toHaveBeenCalledTimes(1);
      expect(logoutSpy).toHaveBeenCalledTimes(1);

      expect(component.user).toBe(mockUser);
      expect(sessionService.isLogged).toBeFalsy();
    });
  });
});

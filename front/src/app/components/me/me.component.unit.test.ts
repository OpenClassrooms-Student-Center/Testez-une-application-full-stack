import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { of } from 'rxjs';
import { SessionService } from 'src/app/services/session.service';

import { MeComponent } from './me.component';
import { User } from 'src/app/interfaces/user.interface';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';
import { SessionInformation } from '../../interfaces/sessionInformation.interface';


describe('MeComponent', () => {

  const mockUser: User = { id: 1, email: '', lastName: '', firstName: '', admin: true, password: '', createdAt: new Date(), updatedAt: new Date() };
  const mockSessionInformation: SessionInformation = { token: '', type: '', id: 1, username: '', firstName: '', lastName: '', admin: true };

  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let mockRouter: jest.Mocked<any>;
  let mockSessionService: jest.Mocked<SessionService>;
  let mockUserService: jest.Mocked<UserService>;
  let mockMatSnackBar: jest.Mocked<MatSnackBar>;

  mockRouter = {
     navigate: jest.fn()
  } as unknown as jest.Mocked<Router>

  mockUserService = {
    getById: jest.fn().mockReturnValue(of(mockUser)),
    delete: jest.fn()
  } as unknown as jest.Mocked<UserService>;

  mockSessionService = {
    sessionInformation: mockSessionInformation,
    logOut: jest.fn()
  } as unknown as jest.Mocked<SessionService>;

  mockMatSnackBar = {
    open: jest.fn()
  } as unknown as jest.Mocked<MatSnackBar>;

  beforeEach(async () => {

    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: UserService, useValue: mockUserService },
        { provide: Router, useValue: mockRouter },
        { provide: MatSnackBar, useValue: mockMatSnackBar }
      ],
    })
      .compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();

  });

  describe('ngOnInit', () => {
    it('should call UserService.getById and set user', () => {
      component.ngOnInit();
      expect(mockUserService.getById).toHaveBeenCalledWith(mockSessionInformation.id.toString());
      expect(component.user).toEqual(mockUser);
    });
  });

  describe('back', () => {
    it('should call windows.history.back()', () => {
      const backSpy = jest.spyOn(window.history, 'back');
      component.back();
      expect(backSpy).toHaveBeenCalledTimes(1);
    });
  });

  describe('delete', () => {
    it('should call userService.delete()', () => {
      const deleteSpy = jest.spyOn(mockUserService, 'delete').mockImplementation((userId: string) => { return of({})});
      component.delete();
      expect(deleteSpy).toHaveBeenCalledWith(mockSessionInformation.id.toString());
    });

    it('should call logOut()', () => {
      component.delete();
      expect(mockSessionService.logOut).toHaveBeenCalled;
    });

    it('should navigate to the home page', () => {
      const navigateSpy = jest.spyOn(mockRouter, 'navigate');
      component.delete();
      expect(navigateSpy).toHaveBeenCalledWith(['/']);
    });

    it('should call matSnackBar.open() with correct parameter', () => {
      component.delete();
      expect(mockMatSnackBar.open).toHaveBeenCalledWith("Your account has been deleted !", 'Close', { duration: 3000 });
    });
  });
});

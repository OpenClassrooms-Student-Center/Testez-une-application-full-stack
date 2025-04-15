import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';
import { expect } from '@jest/globals';

import { MeComponent } from './me.component';

import { UserService } from 'src/app/services/user.service';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { User } from 'src/app/interfaces/user.interface';
import { By } from '@angular/platform-browser';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let mockSessionService: any;
  let mockUserService: any;
  let mockRouter: any;
  let mockSnackBar: any;
  let mockUser: User;

  beforeEach(async () => {
    mockSessionService = {
      sessionInformation: {
        admin: true,
        id: 1
      },
      logOut: jest.fn()
    };
    mockUserService = {
      getById: jest.fn(),
      delete: jest.fn()
    };
    mockRouter = {
      navigate: jest.fn()
    };
    mockSnackBar = {
      open: jest.fn()
    };

    mockUser = {
      id: 1,
      firstName: 'John',
      lastName: 'Doe',
      email: 'john.doe@example.com',
      admin: true,
      password: 'pass',
      createdAt: new Date('2025-02-23'),
      updatedAt: new Date('2025-02-23'),
    };

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
        { provide: MatSnackBar, useValue: mockSnackBar }
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch user data on init', () => {
    mockUserService.getById.mockReturnValue(of(mockUser));
    fixture.detectChanges();
    expect(mockUserService.getById).toHaveBeenCalledWith('1');
    expect(component.user).toEqual(mockUser);
  });

  it('should navigate back when back() is called', () => {
    jest.spyOn(window.history, 'back');
    component.back();
    expect(window.history.back).toHaveBeenCalled();
  });

  it('should delete the user and log out', () => {
    mockUserService.delete.mockReturnValue(of(null));
    component.delete();
    expect(mockUserService.delete).toHaveBeenCalledWith('1');
    expect(mockSnackBar.open).toHaveBeenCalledWith(
      'Your account has been deleted !',
      'Close',
      { duration: 3000 }
    );
    expect(mockSessionService.logOut).toHaveBeenCalled();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/']);
  });

  it('should display the correct user data', () => {
    mockUserService.getById.mockReturnValue(of(mockUser));
    fixture.detectChanges();
    const displayNames = fixture.debugElement.queryAll(By.css('p')); 
    expect(displayNames[0].nativeElement.textContent).toContain('Name:');
    expect(displayNames[0].nativeElement.textContent).toContain(mockUser.firstName);
    expect(displayNames[0].nativeElement.textContent).toContain(mockUser.lastName.toUpperCase());
    expect(displayNames[1].nativeElement.textContent).toContain('Email:');
    expect(displayNames[1].nativeElement.textContent).toContain(mockUser.email);
  });

  it('should handle error if fetching user data fails', () => {
    mockUserService.getById.mockReturnValue(throwError(() => new Error('User not found')));
    fixture.detectChanges();
    expect(component.user).toBeUndefined();
  });
});

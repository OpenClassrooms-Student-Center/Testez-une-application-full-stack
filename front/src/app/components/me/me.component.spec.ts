import { MeComponent } from './me.component';
import { Observable, of } from 'rxjs';
import { SessionService } from '../../services/session.service';
import { UserService } from '../../services/user.service';
import { User } from '../../interfaces/user.interface';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';

import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';

import { expect } from '@jest/globals';

describe('MeComponent', () => {
  const fakeUser: Partial<User> = {
    id: 1,
    email: 'test@gmail.com',
    password: '',
    firstName: 'John',
    lastName: 'Doe',
    admin: false,
    createdAt: new Date(),
    updatedAt: new Date(),
  };

  const fakeSessionService = {
    sessionInformation: {
      admin: true,
      id: 1,
    },
    logOut: jest.fn(() => of({})),
    isLogged: jest.fn(() => of(true)),
  };

  const fakeUserService = {
    getById: jest.fn(() => of(fakeUser)),
    delete: jest.fn(() => Promise.resolve()),
  };

  const fakeRouter = {
    navigate: (commands: any[], extras?, options?: any) => {},
  } as Router;
  const fakeMatSnackBar = {
    open: (message: string, action?: string | undefined, opts?: any) => {},
  } as MatSnackBar;

  let fixture: ComponentFixture<MeComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        HttpClientTestingModule,
        MatButtonModule,
        MatIconModule,
        MatCardModule,
      ],
      providers: [
        { provide: SessionService, useValue: fakeSessionService },
        { provide: UserService, useValue: fakeUserService },
        { provide: Router, useValue: fakeRouter },
        { provide: MatSnackBar, useValue: fakeMatSnackBar },
        { provide: Observable, useValue: Observable },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(MeComponent);
  });

  it('renders the title and navigation button', () => {
    fixture.detectChanges();

    const h1 = fixture.nativeElement.querySelector('h1');
    const navButton = fixture.nativeElement.querySelector(
      'button[mat-icon-button]'
    );

    expect(h1.textContent).toEqual('User information');
    expect(navButton).toBeTruthy();
  });

  describe('when user data is retrieved successfully', () => {
    beforeEach(async () => {
      (fakeUserService.getById as jest.Mock).mockResolvedValue(of(fakeUser));
      await fixture.whenStable();
      fixture.detectChanges();
    });

    it('displays user name, email and creation & update dates', () => {
      const nameElement = fixture.nativeElement.querySelector('p:nth-child(1)');
      const emailElement =
        fixture.nativeElement.querySelector('p:nth-child(2)');
      const createAtElement =
        fixture.nativeElement.querySelector('p:nth-child(3)');
      const updatedAtElement =
        fixture.nativeElement.querySelector('p:nth-child(4)');

      expect(nameElement.textContent).toEqual(`Name: John DOE`);
      expect(emailElement.textContent).toEqual(`Email: ${fakeUser.email}`);
      expect(createAtElement.textContent).toEqual(
        `Create at: ${fakeUser?.createdAt?.toLocaleDateString()}`
      );
      expect(updatedAtElement.textContent).toEqual(
        `Last update: ${fakeUser?.updatedAt?.toLocaleDateString()}`
      );
    });

    it('does NOT display delete button if user is an admin', () => {
      fakeUser.admin = true;
      fixture.detectChanges();
      const deleteBtn = fixture.nativeElement.querySelector(
        'button[mat-raised-button][color=warn]'
      );
      expect(deleteBtn).toBeFalsy();
    });

    it('display delete button if user is NOT an admin', () => {
      fixture.detectChanges();
      const deleteBtn = fixture.nativeElement.querySelector(
        'button[mat-raised-button][color=warn]'
      );
      expect(deleteBtn).toBeTruthy();
    });

    it('deletes the user when clicking the delete button', async () => {
      jest.spyOn(fakeMatSnackBar, 'open');
      jest.spyOn(fakeRouter, 'navigate');
      jest.spyOn(fakeSessionService, 'logOut');

      fixture.detectChanges();
      const deleteBtn = fixture.nativeElement.querySelector(
        'button[mat-raised-button][color=warn]'
      );

      await deleteBtn.dispatchEvent(new MouseEvent('click'));

      expect(fakeUserService.delete).toHaveBeenCalledTimes(1);
      expect(fakeMatSnackBar.open).toHaveBeenCalledWith(
        'Your account has been deleted !',
        'Close',
        { duration: 3000 }
      );

      expect(fakeRouter.navigate).toHaveBeenCalledWith(['/']);

      expect(fakeSessionService.logOut).toHaveBeenCalled();
    });
  });
});

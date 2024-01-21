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
import { formatDateWithOptions } from 'src/utils/date.utils';

describe('MeComponent', () => {
  const fakeUser: User = {
    id: 1,
    email: 'user@user.com',
    password: '',
    firstName: 'John',
    lastName: 'Doe',
    admin: false,
    createdAt: new Date('06/13/2002'),
    updatedAt: new Date('06/13/2002'),
  };
  const fakeAdmin: User = { ...fakeUser, admin: true };

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
  let component: MeComponent;

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
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;

    fixture.detectChanges();
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
    it('displays user name, email and creation & update dates without the delete button as an admin', () => {
      component.user = fakeAdmin;

      fixture.detectChanges();

      const [
        nameElement,
        emailElement,
        adminStatusParagraph,
        createAtElement,
        updatedAtElement,
      ] = fixture.nativeElement.querySelectorAll('p');

      expect(nameElement.textContent).toEqual(
        `Name: ${fakeAdmin.firstName} ${fakeAdmin.lastName.toUpperCase()}`
      );

      expect(emailElement.textContent).toEqual(`Email: ${fakeAdmin.email}`);

      expect(adminStatusParagraph.textContent).toEqual('You are admin');

      expect(createAtElement.textContent).toEqual(
        `Create at:  ${formatDateWithOptions(fakeAdmin.createdAt, 'en-US', {
          year: 'numeric',
          month: 'long',
          day: 'numeric',
        })}`
      );

      expect(updatedAtElement.textContent).toEqual(
        `Last update:  ${formatDateWithOptions(
          fakeAdmin.updatedAt as Date,
          'en-US',
          {
            year: 'numeric',
            month: 'long',
            day: 'numeric',
          }
        )}`
      );
    });

    it('displays user name, email and creation & update dates with the delete button as user', () => {
      component.user = fakeUser;

      fixture.detectChanges();

      const [
        nameElement,
        emailElement,
        deleteAccountParagraph,
        createAtElement,
        updatedAtElement,
      ] = fixture.nativeElement.querySelectorAll('p') as HTMLParagraphElement[];

      const deleteAccountButton = fixture.nativeElement.querySelector(
        'button'
      ) as HTMLButtonElement;

      expect(nameElement.textContent).toEqual(
        `Name: ${fakeUser.firstName} ${fakeUser.lastName.toUpperCase()}`
      );

      expect(emailElement.textContent).toEqual(`Email: ${fakeUser.email}`);

      expect(deleteAccountParagraph.textContent).toEqual('Delete my account:');
      expect(deleteAccountButton).toBeTruthy();

      expect(createAtElement.textContent).toEqual(
        `Create at:  ${formatDateWithOptions(fakeUser.createdAt, 'en-US', {
          year: 'numeric',
          month: 'long',
          day: 'numeric',
        })}`
      );

      expect(updatedAtElement.textContent).toEqual(
        `Last update:  ${formatDateWithOptions(
          fakeUser.updatedAt as Date,
          'en-US',
          {
            year: 'numeric',
            month: 'long',
            day: 'numeric',
          }
        )}`
      );
    });

    it('deletes the user when clicking the delete button', async () => {
      component.user = fakeUser;

      fixture.detectChanges();

      jest
        .spyOn(fakeUserService, 'delete')
        .mockImplementation(() => Promise.resolve());
      jest.spyOn(fakeMatSnackBar, 'open');
      jest.spyOn(fakeRouter, 'navigate');
      jest.spyOn(fakeSessionService, 'logOut');

      fixture.detectChanges();
      const deleteAccountButton = fixture.nativeElement.querySelector(
        'button[mat-raised-button][color=warn]'
      );

      expect(deleteAccountButton).toBeTruthy();

      await deleteAccountButton.dispatchEvent(new MouseEvent('click'));

      expect(fakeUserService.delete).toHaveBeenCalledTimes(1);

      expect(fakeMatSnackBar.open).toHaveBeenCalledWith(
        'Your account has been deleted !',
        'Close',
        { duration: 3_000 }
      );

      expect(fakeRouter.navigate).toHaveBeenCalledWith(['/']);

      expect(fakeSessionService.logOut).toHaveBeenCalled();
    });
  });
});

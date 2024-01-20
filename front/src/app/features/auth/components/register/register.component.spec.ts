import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed, tick } from '@angular/core/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';

import { RegisterComponent } from './register.component';
import { AuthService } from '../../services/auth.service';
import { ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import { SessionService } from 'src/app/services/session.service';

describe('RegisterComponent', () => {
  let fixture: ComponentFixture<RegisterComponent>;
  let component: RegisterComponent;

  let httpMock: HttpTestingController;
  let authServiceMock: AuthService;

  const fakeRouter = {
    navigate: (commands: any[], extras?, options?: any) => {},
  } as Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        HttpClientTestingModule,
        BrowserAnimationsModule,
        ReactiveFormsModule,
        FormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
      ],
      providers: [
        AuthService,
        SessionService,
        { provide: Router, useValue: fakeRouter },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;

    httpMock = TestBed.inject(HttpTestingController);

    authServiceMock = TestBed.inject(AuthService);

    fixture.detectChanges();
  });

  it('should have all form fields necessary to register', async () => {
    expect(component).toBeTruthy();

    const nativeEl = fixture.nativeElement;

    const form = nativeEl.querySelector('.register-form');
    expect(form).toBeTruthy();

    const firstNameInput = form.querySelector(
      'input[formcontrolname=firstName]'
    );
    expect(firstNameInput).toBeTruthy();

    const lastNameInput = form.querySelector('input[formcontrolname=lastName]');
    expect(lastNameInput).toBeTruthy();

    const emailInput = form.querySelector('input[formcontrolname=email]');
    expect(emailInput).toBeTruthy();

    const passwordInput = form.querySelector('input[formcontrolname=password]');
    expect(passwordInput).toBeTruthy();
  });

  it('should submit registration details and navigate to the login page', async () => {
    const nativeEl = fixture.nativeElement;

    const authServiceMockSpy = jest.spyOn(authServiceMock, 'register');
    const navigateSpy = jest.spyOn(fakeRouter, 'navigate');

    const expectedData = {
      email: 'test@test.com',
      firstName: 'Test',
      lastName: 'User',
      password: 'test!1234',
    };

    const form = nativeEl.querySelector('.register-form')! as HTMLFormElement;

    // Enter form data
    fillInputs(form, expectedData);
    // Submit the form
    form.submit();

    const req = httpMock.expectOne({
      url: 'api/auth/register',
      method: 'POST',
    });

    req.flush(null); // Respond with null or whatever the server returns upon successful registration

    // Perform checks
    expect(authServiceMockSpy).toHaveBeenCalledWith(expectedData);
    expect(navigateSpy).toHaveBeenCalledWith(['/login']);
  });

  it('should mark the form as invalid when a digit is included in the first name field', () => {
    const nativeEl = fixture.nativeElement;

    const firstNameInput = nativeEl.querySelector(
      'input[formControlName="firstName"]'
    ) as HTMLInputElement;

    const submitButton = nativeEl.querySelector(
      'button[type="submit"]'
    ) as HTMLButtonElement;

    firstNameInput.value = 'Te1st';
    firstNameInput.dispatchEvent(new Event('input'));

    expect(submitButton.hasAttribute('disabled')).toBe(true);
  });
});

/**
 * Fills the input fields of a form with the provided data.
 *
 * @param {HTMLFormElement} form - The HTMLFormElement representing the form.
 * @param {Record<string, string>} data - An object containing key-value pairs where the key is the name of the form control and the value is the data to be filled.
 * @returns void
 */
function fillInputs(form: HTMLFormElement, data: Record<string, string>) {
  for (const key in data) {
    const input = form.querySelector(
      `input[formcontrolname=${key}]`
    ) as HTMLInputElement;

    input.value = data[key];

    // * Notifies Angular that a change to one of the form fields was made
    input.dispatchEvent(new Event('input'));
  }
}

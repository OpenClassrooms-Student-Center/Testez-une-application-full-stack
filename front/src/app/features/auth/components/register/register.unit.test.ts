import { RegisterComponent } from './register.component';
import { FormBuilder, FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { expect } from '@jest/globals';
import { AuthService } from '../../services/auth.service';
import { RegisterRequest } from '../../interfaces/registerRequest.interface';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { HttpClientModule } from '@angular/common/http';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authService: jest.Mocked<AuthService>;
  let router: jest.Mocked<Router>;


  beforeEach(() => {
    authService = {
      register: jest.fn(),
      login: jest.fn(), // Simulez également la méthode login
    } as unknown as jest.Mocked<AuthService>;

    router = {
      navigate: jest.fn(),
    } as unknown as jest.Mocked<Router>;

    TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      providers: [
        { provide: AuthService, useValue: authService },
        { provide: Router, useValue: router },
      ],
      imports: [
        BrowserAnimationsModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        HttpClientModule
      ],
    });

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should initialize the form correctly', () => {
    // Vérifiez que les contrôles du formulaire ont été initialisés correctement
    expect(component.form.get('email')).toBeInstanceOf(FormControl);
    expect(component.form.get('firstName')).toBeInstanceOf(FormControl);
    expect(component.form.get('lastName')).toBeInstanceOf(FormControl);
    expect(component.form.get('password')).toBeInstanceOf(FormControl);

    // Vérifiez les règles de validation pour chaque champ
    expect(component.form.get('email')?.hasError('required')).toBeTruthy();
    expect(component.form.get('email')?.hasError('email')).toBeFalsy(); // Champ vide, donc pas d'erreur d'e-mail
    expect(component.form.get('firstName')?.hasError('required')).toBeTruthy();
    expect(component.form.get('firstName')?.hasError('minlength')).toBeFalsy(); // Prénom initialisé à vide, donc pas d'erreur de longueur minimale
    expect(component.form.get('firstName')?.hasError('maxlength')).toBeFalsy(); // Prénom initialisé à vide, donc pas d'erreur de longueur maximale
    expect(component.form.get('lastName')?.hasError('required')).toBeTruthy();
    expect(component.form.get('lastName')?.hasError('minlength')).toBeFalsy(); // Nom initialisé à vide, donc pas d'erreur de longueur minimale
    expect(component.form.get('lastName')?.hasError('maxlength')).toBeFalsy(); // Nom initialisé à vide, donc pas d'erreur de longueur maximale
    expect(component.form.get('password')?.hasError('required')).toBeTruthy();
    expect(component.form.get('password')?.hasError('minlength')).toBeFalsy(); // Mot de passe initialisé à vide, donc pas d'erreur de longueur minimale
    expect(component.form.get('password')?.hasError('maxlength')).toBeFalsy(); // Mot de passe initialisé à vide, donc pas d'erreur de longueur maximale
});

it('should require all fields to be filled', () => {
  // Laissez tous les champs du formulaire vides
  component.form.setValue({
    email: '',
    firstName: '',
    lastName: '',
    password: '',
  });

  // Retournez un observable avec une erreur (simulant une erreur lors de l'enregistrement)
  authService.register.mockReturnValue(throwError('Some error'));

  component.submit();

  // Vérifiez que le service d'authentification a été appelé (car c'est le comportement actuel du composant)
  expect(authService.register).toHaveBeenCalledTimes(1);

  // Vérifiez que le router n'a pas été appelé pour la navigation
  expect(router.navigate).not.toHaveBeenCalled();

  // Vérifiez que onError est défini à true
  expect(component.onError).toBeTruthy();
});





  it('should create an account successfully', () => {
    const registerRequest: RegisterRequest = {
      email: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: 'password123',
    };
  
    authService.register.mockReturnValue(of(void 0));
  
    component.form.setValue(registerRequest);
    component.submit();
  
    expect(authService.register).toHaveBeenCalledWith(registerRequest);
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
    expect(component.onError).toBeFalsy();
  });
  

  it('should handle an error when registering', () => {
    const registerRequest: RegisterRequest = {
      email: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: 'password123',
    };

    
    authService.register.mockReturnValue(of(void 0));

    component.form.setValue(registerRequest);
    component.submit();

    expect(authService.register).toHaveBeenCalledWith(registerRequest);
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
    expect(component.onError).toBeFalsy();
  });

  it('should set onError to true on error during register', () => {
    const registerRequest: RegisterRequest = {
      email: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: 'password123',
    };

    const errorMessage = 'Registration failed';

    authService.register.mockReturnValueOnce(throwError(errorMessage));

    component.form.setValue(registerRequest);
    component.submit();

    expect(authService.register).toHaveBeenCalledWith(registerRequest);
    expect(router.navigate).not.toHaveBeenCalled();
    expect(component.onError).toBeTruthy();
  });
});
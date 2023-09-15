import { TestBed, ComponentFixture } from '@angular/core/testing';
import { ReactiveFormsModule, FormBuilder } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SessionService } from '../../../../services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { FormComponent } from './form.component';
import { RouterTestingModule } from '@angular/router/testing';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';
import { Session } from '../../interfaces/session.interface';
import { expect } from '@jest/globals';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let sessionApiService: SessionApiService;
  let matSnackBar: MatSnackBar;
  let router: Router;

  // Mock FormBuilder
  const formBuilder: FormBuilder = new FormBuilder();

  // Mock ActivatedRoute
  const mockActivatedRoute = {
    snapshot: {
      paramMap: {
        get: (param: string) => {
          if (param === 'id') {
            return '1'; // Mocking an ID value
          }
        },
      },
    },
    url: of([{ path: 'update' }]), // Mocking the URL with 'update'
  };

  // Mock SessionService
  const mockSessionService = {
    sessionInformation: {
      admin: true,
    },
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ReactiveFormsModule, RouterTestingModule],
      declarations: [FormComponent],
      providers: [
        { provide: FormBuilder, useValue: formBuilder },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
        { provide: SessionService, useValue: mockSessionService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    sessionApiService = TestBed.inject(SessionApiService);
    matSnackBar = TestBed.inject(MatSnackBar);
    router = TestBed.inject(Router);

    // Initialize the form
    component.ngOnInit();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should submit a new session', () => {
    // Mock a session object
    const mockSession: Session = {
      name: 'Test Session',
      date: new Date('2023-09-10'),
      teacher_id: 1,
      description: 'Test description',
      users: [],
    };

    // Set onUpdate to false (indicating a new session)
    component.onUpdate = false;

    // Set the sessionForm with mockSession values
    component.sessionForm = formBuilder.group({
      name: [mockSession.name],
      date: [mockSession.date],
      teacher_id: [mockSession.teacher_id],
      description: [mockSession.description],
    });

    // Mock the response from the sessionApiService.create() method
    spyOn(sessionApiService, 'create').and.returnValue(of(mockSession));

    // Spy on MatSnackBar
    spyOn(matSnackBar, 'open');

    // Call the submit method
    component.submit();

    // Expect that the sessionApiService.create() method was called with the mockSession
    expect(sessionApiService.create).toHaveBeenCalledWith(mockSession);

    // Expect that the MatSnackBar open method was called with 'Session created !'
    expect(matSnackBar.open).toHaveBeenCalledWith('Session created !', 'Close', {
      duration: 3000,
    });

    // Expect that router.navigate was called with ['sessions']
    expect(router.navigate).toHaveBeenCalledWith(['sessions']);
  });

  it('should submit an updated session', () => {
    // Mock a session object
    const mockSession: Session = {
      name: 'Updated Test Session',
      date: new Date('2023-09-10'),
      teacher_id: 1,
      description: 'Updated test description',
        users: [],
    };

    // Set onUpdate to true (indicating an update)
    component.onUpdate = true;

    // Set the sessionForm with mockSession values
    component.sessionForm = formBuilder.group({
      name: [mockSession.name],
      date: [mockSession.date],
      teacher_id: [mockSession.teacher_id],
      description: [mockSession.description],
    });

    // Mock the response from the sessionApiService.update() method
    spyOn(sessionApiService, 'update').and.returnValue(of(mockSession));

    // Spy on MatSnackBar
    spyOn(matSnackBar, 'open');

    // Call the submit method
    component.submit();

    // Expect that the sessionApiService.update() method was called with the mockSession and component.id
    expect(sessionApiService.update).toHaveBeenCalledWith('1', mockSession);

    // Expect that the MatSnackBar open method was called with 'Session updated !'
    expect(matSnackBar.open).toHaveBeenCalledWith('Session updated !', 'Close', {
      duration: 3000,
    });

    // Expect that router.navigate was called with ['sessions']
    expect(router.navigate).toHaveBeenCalledWith(['sessions']);
  });
});

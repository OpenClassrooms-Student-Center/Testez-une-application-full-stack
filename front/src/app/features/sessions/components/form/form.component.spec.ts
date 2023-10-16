import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import {  FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';
import { Session } from '../../interfaces/session.interface';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  } 

  beforeEach(async () => {
    await TestBed.configureTestingModule({

      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule, 
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        SessionApiService
      ],
      declarations: [FormComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('Session', ()=>{
    let mockComponent: FormComponent;
    let mockFB: FormBuilder;
    let mockSessionApiService: any;
    let mockMatSnackBar: any;
    let mockRouter: any;

    //No users for creation session
    let mockRoute: any;
    let mockSessionService: any;
    let mockTeacherService: any;

    mockFB = new FormBuilder();

    mockSessionApiService = {
      create: jest.fn().mockReturnValue({subscribe: jest.fn()}),
      update: jest.fn().mockReturnValue(of({}))
    };

    mockMatSnackBar = { 
      open: jest.fn() };

    mockRouter = {
      navigate: jest.fn()
    }
    mockRoute = {
          snapshot: {
            paramMap: {
              get: jest.fn()
            }
          }
    };
    mockTeacherService = {all: jest.fn()};
beforeEach(()=>{
    mockComponent = new FormComponent(
      mockRoute as ActivatedRoute,
      mockFB,
      mockMatSnackBar as MatSnackBar,
      mockSessionApiService as SessionApiService,
      mockSessionService,
      mockTeacherService,
      mockRouter as Router
    );
  });
    it('should create session', ()=>{
      //session
      const session = {
        id: 1,
        name: 'Test',
        description: 'Test description',
        date: new Date(),
        teacher_id: 1,
        users: [],
        createdAt: new Date(),
        updatedAt: new Date(),
      };
    
      //sessionForm
      const sessionForm = mockComponent.sessionForm?.setValue(session);
      mockComponent.onUpdate = false;
      //call function
      mockComponent.submit();

      //mockSessionApiService.create - session
      expect(mockSessionApiService.create).toHaveBeenCalledTimes(1);
      expect(mockSessionApiService.create).toHaveBeenCalledWith(sessionForm);

      let message = 'Session created !';
      let router = ['sessions'];

      mockSessionApiService.create.mockReturnValue(of(
        mockMatSnackBar.open(message),
        mockRouter.navigate(router)
        ));
      // mockSessionApiService.create.subscribe - exitPage
      expect(mockMatSnackBar.open).toHaveBeenCalledTimes(1);
      expect(mockMatSnackBar.open).toHaveBeenCalledWith('Session created !');
      expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
      
    });
    it('should update session', ()=>{

      mockComponent.onUpdate = true;

      mockComponent.teachers$ = mockTeacherService.all();
      const id: string | undefined = '1';

      const identity = mockRoute.snapshot.paramMap.get.mockReturnValue(id);
      const session = {
        id: 1,
        name: 'Test',
        description: 'Test description',
        date: new Date(),
        teacher_id: 1,
        users: [],
        createdAt: new Date(),
        updatedAt: new Date(),
      };
      
      mockComponent.submit();

      mockSessionApiService.update.mockReturnValueOnce(of(session).subscribe((sessions)=>{
        expect(mockSessionApiService.update).toHaveBeenCalledTimes(1);
        expect(mockSessionApiService.update).toHaveBeenCalledWith(identity, sessions);

        expect(mockMatSnackBar.open).toHaveBeenCalledWith('Session updated !');
        expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
      }));
    });
    it('show error for empty required field', ()=>{
      //Error for create session
      const asSession: Session = {
        id: 1,
        name: '',
        description: '',
        date: new Date(),
        teacher_id: 1,
        users: [],
        createdAt: new Date(),
        updatedAt: new Date(),
      };
      const session = mockComponent.sessionForm?.setValue(asSession);
      mockComponent.onUpdate = false;

      mockSessionApiService.create.mockReturnValue(of(session).subscribe((error: any) =>{
        error = 'Empty required Field !!!';
        expect(mockSessionApiService.create).toBe(error);
      }));

      //Error for update session
      mockComponent.onUpdate = true;

      mockSessionApiService.update.mockReturnValue(of(session).subscribe((error:any)=>{
        error = 'Empty required Field !!!';
        expect(mockSessionApiService.update).toBe(error);
      }));
    });
  });
});

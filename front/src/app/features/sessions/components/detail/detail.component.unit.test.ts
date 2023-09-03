// import { ComponentFixture, TestBed } from '@angular/core/testing';
// import { DetailComponent } from './detail.component';
// import { SessionService } from 'src/app/services/session.service';
// import { RouterTestingModule } from '@angular/router/testing';
// import { HttpClientModule } from '@angular/common/http';
// import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
// import { ReactiveFormsModule } from '@angular/forms';
// import { of } from 'rxjs';
// import { expect } from '@jest/globals';
// import { SessionApiService } from '../../services/session-api.service';
// import { TeacherService } from 'src/app/services/teacher.service';
// import { Router } from '@angular/router';

// describe('DetailComponent', () => {
//     let component: DetailComponent;
//     let fixture: ComponentFixture<DetailComponent>;
//     let sessionServiceMock: Partial<SessionService>;
//     let sessionApiServiceMock: any; // Vous devrez définir vos mocks pour sessionApiService et teacherService
//     let teacherServiceMock: any;
//     let matSnackBarMock: any;
//     let routerMock: any;
  
//     const mockSessionService = {
//         sessionInformation: {
//           token: 'your-token-here',
//           type: 'user',
//           id: 1,
//           username: 'your-username',
//           firstName: 'your-first-name',
//           lastName: 'your-last-name',
//           admin: true
//         }
//       };
      
  
//     beforeEach(async () => {
//       sessionServiceMock = {
//         sessionInformation: mockSessionService.sessionInformation
//       };
  
//       sessionApiServiceMock = {
//         detail: jest.fn(() => of({ /* détails de session simulés */ })),
//         participate: jest.fn(() => of(undefined)),
//         unParticipate: jest.fn(() => of(undefined)),
//         delete: jest.fn(() => of(undefined))
//         // Vous devrez définir d'autres méthodes simulées au besoin
//       };
  
//       teacherServiceMock = {
//         detail: jest.fn(() => of({ /* détails de l'enseignant simulés */ }))
//         // Vous devrez définir d'autres méthodes simulées au besoin
//       };
  
//       matSnackBarMock = {
//         open: jest.fn()
//       };
  
//       routerMock = {
//         navigate: jest.fn()
//       };
  
//       await TestBed.configureTestingModule({
//         imports: [
//           RouterTestingModule,
//           HttpClientModule,
//           MatSnackBarModule,
//           ReactiveFormsModule
//         ],
//         declarations: [DetailComponent],
//         providers: [
//           { provide: SessionService, useValue: sessionServiceMock },
//           { provide: SessionApiService, useValue: sessionApiServiceMock },
//           { provide: TeacherService, useValue: teacherServiceMock },
//           { provide: MatSnackBar, useValue: matSnackBarMock },
//           { provide: Router, useValue: routerMock }
//         ],
//       }).compileComponents();
  
//       fixture = TestBed.createComponent(DetailComponent);
//       component = fixture.componentInstance;
//       fixture.detectChanges();
//     });
  
//     it('should create', () => {
//       expect(component).toBeTruthy();
//     });
  
//     it('should fetch session details on init', () => {
//       expect(sessionApiServiceMock.detail).toHaveBeenCalledWith('1');
//       expect(teacherServiceMock.detail).toHaveBeenCalledWith('1');
//       expect(component.session).toBeTruthy();
//       expect(component.teacher).toBeTruthy();
//     });
  
//     it('should participate in the session', () => {
//       component.participate();
//       expect(sessionApiServiceMock.participate).toHaveBeenCalledWith('1', '1');
//       expect(component.isParticipate).toBe(true);
//     });
  
//     it('should un-participate in the session', () => {
//       component.unParticipate();
//       expect(sessionApiServiceMock.unParticipate).toHaveBeenCalledWith('1', '1');
//       expect(component.isParticipate).toBe(false);
//     });
  
//     it('should delete the session', () => {
//       component.delete();
//       expect(sessionApiServiceMock.delete).toHaveBeenCalledWith('1');
//       expect(matSnackBarMock.open).toHaveBeenCalledWith('Session deleted !', 'Close', { duration: 3000 });
//       expect(routerMock.navigate).toHaveBeenCalledWith(['sessions']);
//     });
  
//     it('should navigate back', () => {
//       component.back();
//       expect(window.history.back).toHaveBeenCalled();
//     });
//   });
import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals'; 
import { SessionService } from '../../../../services/session.service';

import { DetailComponent } from './detail.component';


describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>; 
  let service: SessionService;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule
      ],
      declarations: [DetailComponent], 
      providers: [{ provide: SessionService, useValue: mockSessionService }],
    })
      .compileComponents();
      service = TestBed.inject(SessionService);
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
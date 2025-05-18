import { of } from 'rxjs';
import { RouterTestingModule } from '@angular/router/testing'
import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import {Router, ActivatedRoute } from '@angular/router';
import { expect } from '@jest/globals'; 
import { SessionService } from '../../../../services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { TeacherService } from '../../../../services/teacher.service';
import { DetailComponent } from './detail.component';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';

class MockSessionService {
  delete = jest.fn();
  getById = jest.fn();
  sessionInformation = { id: 1, admin: true }; 
  
}
class MockSessionApiService {
  detail = jest.fn();
  delete = jest.fn();
  participate = jest.fn();
  unParticipate = jest.fn();
}
class MockTeacherService {
  detail = jest.fn();
}

 



describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>; 
  let mockSessionService: MockSessionService;
  let mockSessionApiService: MockSessionApiService;
  let mockTeacherService: MockTeacherService;
  let routerMock : {navigate : jest.Mock};;
  
  

  /*const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }*/

  beforeEach(async () => {
    mockSessionService = new MockSessionService();
    mockSessionApiService = new MockSessionApiService();
    mockTeacherService = new MockTeacherService();
    

    mockSessionApiService.delete.mockReturnValue(of(undefined));
    mockSessionApiService.detail.mockReturnValue(of({ users: [], teacher_id: 1 })); // <-- AJOUT ICI
    mockTeacherService.detail.mockReturnValue(of({ id: 1, name: 'Prof' })); // Pour éviter d'autres erreurs
    
    routerMock = {
      navigate: jest.fn().mockResolvedValue(true) };

   

    await TestBed.configureTestingModule({
      imports: [
        MatIconModule,
        MatCardModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule
        
      ],
      declarations: [DetailComponent], 
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService }, 
        { provide: ActivatedRoute, useValue: { snapshot: { paramMap: { get: () => '1' } } } }, 
        { provide: Router, useValue: routerMock }
        ],
    })
      .compileComponents();

    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call delete and navigate on success',async () => {
    mockSessionApiService.delete.mockReturnValue(of(undefined));
    const mockSnackBar = TestBed.inject(MatSnackBar);
    jest.spyOn(mockSnackBar, 'open');
    
    
    component.sessionId = '1';
    component.delete();

     await fixture.whenStable();

    expect(mockSessionApiService.delete).toHaveBeenCalled();
    expect(mockSnackBar.open).toHaveBeenCalledWith(
    'Session deleted !',
    'Close',
    { duration: 3000 }
  );
    /*console.log('Router mock in test:', routerMock);
    expect(routerMock.navigate).toHaveBeenCalledWith(['sessions']);*/
  });

   it('should fetch session and teacher, and set isParticipate', () => {
    // Arrange
    const sessionMock = {
      users: [1, 2, 3],
      teacher_id: 42
    };
    const teacherMock = { id: 42, name: 'Prof' }

    mockSessionApiService.detail.mockReturnValue(of(sessionMock));
    mockTeacherService.detail.mockReturnValue(of(teacherMock));

    component['fetchSession']();

    expect(mockSessionApiService.detail).toHaveBeenCalledWith('1');
    expect(component.session).toEqual(sessionMock);
    expect(component.isParticipate).toBe(true);
    expect(mockTeacherService.detail).toHaveBeenCalledWith('42');
    expect(component.teacher).toEqual(teacherMock);
  });

  it('should call participate and then fetchSession', () => {
 
  const participateSpy = mockSessionApiService.participate.mockReturnValue(of(undefined));
  const fetchSessionSpy = jest.spyOn(component as any, 'fetchSession');

  // Act
  component.participate();

  // Assert
  expect(participateSpy).toHaveBeenCalledWith(component.sessionId, component.userId);
  expect(fetchSessionSpy).toHaveBeenCalled();
});

it('should call unParticipate and then fetchSession', () => {
  // Arrange
  const unParticipateSpy = mockSessionApiService.unParticipate.mockReturnValue(of(undefined));
  const fetchSessionSpy = jest.spyOn(component as any, 'fetchSession');

  // Act
  component.unParticipate();

  // Assert
  expect(unParticipateSpy).toHaveBeenCalledWith(component.sessionId, component.userId);
  expect(fetchSessionSpy).toHaveBeenCalled();
});
});


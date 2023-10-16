import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals'; 
import { SessionService } from '../../../../services/session.service';

import { DetailComponent } from './detail.component';
import { ActivatedRoute, Router } from '@angular/router';
import { SessionApiService } from '../../services/session-api.service';
import { TeacherService } from 'src/app/services/teacher.service';
import { of } from 'rxjs';


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

  describe('delete session', ()=>{
    let mockComponent: DetailComponent;
    let mockRoute: any;
    let mockSessionApiService: any;
    let mockMatSnackBar: any;
    let mockRouter: any;
    let mockFB: FormBuilder;

    //Just for constructor
    let mockSessionService:any;
    let mockTeacherService:any;

    mockFB = new FormBuilder();

      mockSessionService = {
      sessionInformation: { 
        admin: jest.fn().mockReturnValue,
        id: jest.fn().mockReturnValue
      }
    };

      mockSessionApiService = {
        delete: jest.fn().mockReturnValue({subscribe: jest.fn()})
      };

      mockTeacherService = jest.fn();

      mockMatSnackBar = {open: jest.fn()};

      mockRouter = {navigate: jest.fn()};

      mockRoute = {
        snapshot:{
          paramMap:{
            get:jest.fn().mockReturnValue('')
          }
      }};

      mockComponent = new DetailComponent(
        mockRoute as ActivatedRoute,
        mockFB,
        mockSessionService as SessionService,
        mockSessionApiService as SessionApiService,
        mockTeacherService as TeacherService,
        mockMatSnackBar as MatSnackBar,
        mockRouter as Router
      );

      let sessionId = mockRoute.snapshot.paramMap.get('id');
    
      it('should delete session correctly', ()=>{
    
      mockComponent.delete();

      expect(mockSessionApiService.delete).toHaveBeenCalledTimes(1);

      mockSessionApiService.delete.mockReturnValue(of(sessionId).subscribe((idSession)=>{
        expect(mockSessionApiService.delete).toHaveBeenCalledWith(idSession);

        expect(mockMatSnackBar.open).toHaveBeenCalledWith('Session deleted !');
        expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
      }));

    });
  });
});


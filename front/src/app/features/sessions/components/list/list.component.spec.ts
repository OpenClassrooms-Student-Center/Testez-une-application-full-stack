import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { ListComponent } from './list.component';
import { SessionApiService } from '../../services/session-api.service';
import { Session } from '../../interfaces/session.interface';
import { of } from 'rxjs';

describe('ListComponent', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      imports: [HttpClientModule, MatCardModule, MatIconModule],
      providers: [{ provide: SessionService, useValue: mockSessionService }]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('session', ()=>{
    
    let mockComponent: ListComponent;
    let mockSessionService: any;
    let mockSessionApiService: any;

    mockSessionApiService = {
      all: jest.fn().mockReturnValue([])
    };
    //session

    mockComponent = new ListComponent(
      mockSessionService as SessionService,
      mockSessionApiService as SessionApiService
    );

  it('Session List', ()=>{

    let expectedSession: Session[]=[{
      id: 1,
      name: 'Admin',
      description: 'Test Description',
      date: new Date(),
      teacher_id: 1,
      users: [],
      createdAt: new Date(),
      updatedAt: new Date(),
    }];

    let response: Session[] = [{
      id: 1,
      name: 'Admin',
      description: 'Test Description',
      date: new Date(),
      teacher_id: 1,
      users: [],
      createdAt: new Date(),
      updatedAt: new Date(),
    }];

    component.sessions$.subscribe(session => 
      response = session
      );
    expect(expectedSession).toEqual(response);
  });
  });
});

import { HttpClientModule } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed, ComponentFixture } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { User } from '../interfaces/user.interface';
import { UserService } from './user.service';
import { Component, Input } from '@angular/core';

// Composant d'intégration pour le test
@Component({
  selector: 'app-user-profile',
  template: `<div *ngIf="user"><span class="user-name">{{user.firstName}} {{user.lastName}}</span></div>`
})
class UserProfileComponent {
  user?: User;
  constructor(private userService: UserService) {}

  loadUser(id: string) {
    this.userService.getById(id).subscribe(u => this.user = u);
  }
}

describe('UserService', () => {
  let service: UserService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientTestingModule
      ],
      providers: [UserService]
    });
    service = TestBed.inject(UserService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch user by id', () => {
    const mockUser: User = { 
    id: 1,
    email: 'User1@Test.com',
    lastName: 'Last1',
    firstName: 'User1',
    admin: false,
    password: '123456',
    createdAt: new Date(),
    updatedAt: new Date()};

    service.getById('1').subscribe(user => {
      expect(user).toEqual(mockUser);
    });

    const req = httpMock.expectOne('api/user/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockUser);
  });

   it('should call DELETE on the correct URL', () => {
    service.delete('1').subscribe(response => {
      expect(response).toBeNull(); // ou à adapter selon ton backend
    });

    const req = httpMock.expectOne('api/user/1');
    expect(req.request.method).toBe('DELETE');
    req.flush(null); // ou {} selon la réponse attendue
  });

});

  // ----------- TEST D'INTEGRATION -----------
  describe('Integration: UserService with UserProfileComponent', () => {
    let fixture: ComponentFixture<UserProfileComponent>;
    let httpMock: HttpTestingController;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [UserProfileComponent],
        imports: [HttpClientTestingModule],
        providers: [UserService]
      });
      fixture = TestBed.createComponent(UserProfileComponent);
      httpMock = TestBed.inject(HttpTestingController);
    });

    it('should display user name after loading user', () => {
      const mockUser: User = {
        id: 2,
        email: 'User2@Test.com',
        lastName: 'Last2',
        firstName: 'User2',
        admin: false,
        password: 'abcdef',
        createdAt: new Date(),
        updatedAt: new Date()
      };

      fixture.componentInstance.loadUser('2');
      const req = httpMock.expectOne('api/user/2');
      req.flush(mockUser);

      fixture.detectChanges();

      const name = fixture.nativeElement.querySelector('.user-name').textContent;
      expect(name).toContain('User2 Last2');
    });
  });
  

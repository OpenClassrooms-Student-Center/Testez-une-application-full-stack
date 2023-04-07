import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { UserService } from './user.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { User } from '../interfaces/user.interface';

describe('UserService', () => {
  let userService: UserService;
  let mockHttpTestingController: HttpTestingController;
  let pathService = 'api/user';


  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientTestingModule
      ]
    });
    userService = TestBed.inject(UserService);
    mockHttpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    mockHttpTestingController.verify();
  });

  it('should be created', () => {
    expect(userService).toBeTruthy();
  });

  describe('getById', () => {
    it('should get user by id', () => {
      const userId = '1';
      const mockUser: User = { id: 1, email: '', lastName: '', firstName: '', admin: false, password: '', createdAt: new Date(), updatedAt: new Date()};
      userService.getById(userId).subscribe(user => {
        expect(user).toEqual(mockUser);
      });
      const req = mockHttpTestingController.expectOne(`${pathService}/${userId}`);
      expect(req.request.method).toBe('GET');
      req.flush(mockUser);
    });
  });

  describe('delete', () => {
    it('should delete user by id', () => {
      const userId = '1';
      userService.delete(userId).subscribe();
      const req = mockHttpTestingController.expectOne(`${pathService}/${userId}`);
      expect(req.request.method).toBe('DELETE');
      req.flush('');
    });
  });



});

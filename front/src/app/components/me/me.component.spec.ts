import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';
import { expect } from '@jest/globals';
import { UserService } from 'src/app/services/user.service';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { of } from 'rxjs';
import { User } from '../../interfaces/user.interface';
import { MeComponent } from './me.component';

// Mocks typés
class MockRouter {
  navigate = jest.fn();
}
class MockSessionService {
  sessionInformation = { id: 1 };
  logOut = jest.fn();
}
class MockMatSnackBar {
  open = jest.fn();
}
class MockUserService {
  getById = jest.fn().mockReturnValue(of({ id: 1} as User));
  delete = jest.fn().mockReturnValue(of({}));
}



describe('MeComponent', () => {
let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let mockRouter: MockRouter;
  let mockSessionService: MockSessionService;
  let mockMatSnackBar: MockMatSnackBar;
  let mockUserService: MockUserService;

  beforeEach(async () => {
    mockRouter = new MockRouter();
    mockSessionService = new MockSessionService();
    mockMatSnackBar = new MockMatSnackBar();
    mockUserService = new MockUserService();
    

    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: UserService, useValue: mockUserService },
        { provide: Router, useValue: mockRouter },
        { provide: MatSnackBar, useValue: mockMatSnackBar }
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load user on init', () => {
    component.ngOnInit();
    expect(mockUserService.getById).toHaveBeenCalledWith('1');
    expect(component.user).toEqual({ id: 1 });
  });

  it('should call window.history.back on back()', () => {
    const spy = jest.spyOn(window.history, 'back').mockImplementation(() => {});
    component.back();
    expect(spy).toHaveBeenCalled();
    spy.mockRestore();
  });

  it('should delete user and navigate home', () => {
    component.delete();
    expect(mockUserService.delete).toHaveBeenCalledWith('1');
    expect(mockMatSnackBar.open).toHaveBeenCalledWith(
      "Your account has been deleted !",
      'Close',
      { duration: 3000 }
    );
    expect(mockSessionService.logOut).toHaveBeenCalled();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/']);
  });
});







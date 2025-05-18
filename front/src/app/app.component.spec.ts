import { HttpClientModule } from '@angular/common/http';
import { TestBed , ComponentFixture} from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterTestingModule } from '@angular/router/testing';
import { SessionService } from './services/session.service';
import { Router } from '@angular/router';
import { expect } from '@jest/globals';
import { of } from 'rxjs';
import { AppComponent } from './app.component';


describe('AppComponent', () => {
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;
  let sessionServiceMock: { $isLogged: jest.Mock; logOut: jest.Mock  };


  beforeEach(async () => {
    sessionServiceMock = {
      $isLogged: jest.fn(),
      logOut: jest.fn()
    };

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatToolbarModule
      ],
      declarations: [
        AppComponent
      ],
      providers: [
        { provide: SessionService, useValue: sessionServiceMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
  });

  it('should create the app', () => {
    
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it('should return the observable from sessionService.$isLogged', (done) => {
    // Arrange
    sessionServiceMock.$isLogged.mockReturnValue(of(true));

    // Act
    component.$isLogged().subscribe(value => {
      // Assert
      expect(sessionServiceMock.$isLogged).toHaveBeenCalled();
      expect(value).toBe(true);
      done();
    });
  });

it('should call logOut and navigate to root on logout', () => {
    const router = TestBed.inject(Router);
    const navigateSpy = jest.spyOn(router, 'navigate');

    component.logout();

    expect(sessionServiceMock.logOut).toHaveBeenCalled();
    expect(navigateSpy).toHaveBeenCalledWith(['']);
  });

});

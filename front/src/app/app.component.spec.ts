import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { of } from 'rxjs';
import { expect } from '@jest/globals';

import { AppComponent } from './app.component';
import { SessionService } from './services/session.service';
import { AuthService } from './features/auth/services/auth.service';
import { MaterialModule } from './material.module';

describe('AppComponent', () => {
  let fixture: ComponentFixture<AppComponent>;
  let component: AppComponent;
  let sessionService: any;
  let router: Router;

  beforeEach(async () => {
    sessionService = {
      $isLogged: jest.fn().mockReturnValue(of(true)),
      logOut: jest.fn(),
    };

    await TestBed.configureTestingModule({
      declarations: [AppComponent],
      imports: [
        RouterTestingModule,
        HttpClientTestingModule,
        BrowserAnimationsModule,
        MaterialModule,
      ],
      providers: [
        { provide: SessionService, useValue: sessionService },
        { provide: AuthService, useValue: {} },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);

    jest.spyOn(router, 'navigate').mockResolvedValue(true as any);
    fixture.detectChanges();
  });

  it('should create the app', () => {
    expect(component).toBeTruthy();
  });

  it('$isLogged should return an observable<boolean>', (done) => {
    component.$isLogged().subscribe((val) => {
      expect(val).toBe(true);
      done();
    });
  });

  it('should log out and navigate to home', () => {
    component.logout();
    expect(sessionService.logOut).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(['']);
  });
});

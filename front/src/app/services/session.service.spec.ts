import { Component } from '@angular/core';
import { ComponentFixture,TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';


// Composant d'intégration pour le test
@Component({
  selector: 'app-session-status',
  template: `<span class="status">{{ isLogged ? 'Connecté' : 'Déconnecté' }}</span>`
})
class SessionStatusComponent {
  isLogged = false;
  constructor(private sessionService: SessionService) {
    this.sessionService.$isLogged().subscribe(val => this.isLogged = val);
  }
}

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SessionService]
    });
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should emit the current isLogged value', (done) => {
    service.$isLogged().subscribe(value => {
      expect(value).toBe(false); // valeur initiale
      done();
    });
  });

  it('should emit true after logIn and false after logOut', (done) => {
    const user = {
    id: 1,
    admin: true,
    token: 'fake-token',
    type: 'user',
    username: 'user1',
    firstName: 'User',
    lastName: 'One'
  };

    const emitted: boolean[] = [];
    const sub = service.$isLogged().subscribe(val => {
      emitted.push(val);
      if (emitted.length === 3) {
        expect(emitted).toEqual([false, true, false]);
        sub.unsubscribe();
        done();
      }
    });

    service.logIn(user);
    service.logOut();
  });
});

// ----------- TEST D'INTEGRATION -----------
describe('Integration: SessionService with SessionStatusComponent', () => {
  let fixture: ComponentFixture<SessionStatusComponent>;
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SessionStatusComponent],
      providers: [SessionService]
    });
    fixture = TestBed.createComponent(SessionStatusComponent);
    service = TestBed.inject(SessionService);
  });

  it('should display "Connecté" after login and "Déconnecté" after logout', () => {
    fixture.detectChanges();
    // Initial state: déconnecté
    expect(fixture.nativeElement.querySelector('.status').textContent).toContain('Déconnecté');

    // Log in
    service.logIn({
      id: 1,
      admin: true,
      token: 'fake-token',
      type: 'user',
      username: 'user1',
      firstName: 'User',
      lastName: 'One'
    });
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('.status').textContent).toContain('Connecté');
// Log out
    service.logOut();
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('.status').textContent).toContain('Déconnecté');
  });
});
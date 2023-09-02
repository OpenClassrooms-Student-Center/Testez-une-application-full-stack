import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { Observable, of } from 'rxjs';
import { expect } from '@jest/globals';
import { ListComponent } from './list.component';
import { Session } from '../../interfaces/session.interface';
import { SessionService } from 'src/app/services/session.service';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';

describe('ListComponent', () => {
    let component: ListComponent;
    let fixture: ComponentFixture<ListComponent>;
  
    const mockSessionInformation: SessionInformation = {
        token: 'your-token-here',
        type: 'user',
        id: 1,
        username: 'your-username',
        firstName: 'your-first-name',
        lastName: 'your-last-name',
        admin: true
      };
      
      const mockSessionService: Partial<SessionService> = {
        sessionInformation: mockSessionInformation,
      };
      
      
  
    beforeEach(async () => {
      await TestBed.configureTestingModule({
        declarations: [ListComponent],
        imports: [HttpClientModule, MatCardModule, MatIconModule],
        providers: [{ provide: SessionService, useValue: mockSessionService }],
      }).compileComponents();
  
      fixture = TestBed.createComponent(ListComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });
  
    it('should create', () => {
      expect(component).toBeTruthy();
    });
  
    it('should display the list of sessions', () => {
        fixture.detectChanges(); // Mettre à jour la vue après la création du composant
        const sessionElements = fixture.nativeElement.querySelectorAll('item');
        expect(sessionElements.length).toBe(2);
      });
      
      it('should display Create and Detail buttons for admin user', () => {
        fixture.detectChanges(); // Mettre à jour la vue après la création du composant
        const createButton = fixture.nativeElement.querySelectorAll('create-button');
        const detailButtons = fixture.nativeElement.querySelectorAll('detail-button');
        
        expect(createButton.length).toBe(2);
        expect(detailButtons.length).toBe(2);
      });
});
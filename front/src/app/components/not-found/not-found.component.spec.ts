import { ComponentFixture, TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { NotFoundComponent } from './not-found.component';

describe('NotFoundComponent', () => {
  let component: NotFoundComponent;
  let fixture: ComponentFixture<NotFoundComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [NotFoundComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(NotFoundComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should render the component without any issues', () => {
    expect(component).toBeTruthy();

    const nativeElement = fixture.nativeElement;
    const heading = nativeElement.querySelector('h1');
    expect(heading).not.toBeNull();
    expect(heading.textContent).toBe('Page not found !');

    const flexContainer = nativeElement.querySelector('.flex');
    expect(flexContainer).not.toBeNull();
    expect(flexContainer.classList).toContain('justify-center');
    expect(flexContainer.classList).toContain('mt3');
  });
});

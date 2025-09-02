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

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render the not-found message in the template', () => {
    const compiled: HTMLElement = fixture.nativeElement;
    const value = compiled.querySelector('h1');
    expect(value).toBeTruthy();
    expect(value?.tagName).toBe('H1');
    expect(value!.textContent?.trim()).toMatch(/^page not found!?$/i);
  });
});

import { HttpClientModule } from '@angular/common/http';
import { Component } from '@angular/core';
import { ComponentFixture,TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Teacher } from '../interfaces/teacher.interface';
import { TeacherService } from './teacher.service';

// Composant d'intégration pour le test
@Component({
  selector: 'app-teacher-list',
  template: `
    <ul>
      <li *ngFor="let teacher of teachers" class="teacher-item">
        {{teacher.firstName}} {{teacher.lastName}}
      </li>
    </ul>
  `
})
class TeacherListComponent {
  teachers: Teacher[] = [];
  constructor(private teacherService: TeacherService) {}

  loadTeachers() {
    this.teacherService.all().subscribe(ts => this.teachers = ts);
  }
}




describe('TeacherService', () => {
  let service: TeacherService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientTestingModule
      ],
      providers: [TeacherService]
    });
    service = TestBed.inject(TeacherService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch all teachers', () => {
    const mockTeachers: Teacher[] = [
      { id: 1, firstName: 'Prof A', lastName: 'Last A', createdAt: new Date(), updatedAt: new Date() },
      { id: 2, firstName: 'Prof B', lastName: 'Last B', createdAt: new Date(), updatedAt: new Date() }
    ];

    service.all().subscribe(teachers => {
      expect(teachers).toEqual(mockTeachers);
    });

    const req = httpMock.expectOne('api/teacher');
    expect(req.request.method).toBe('GET');
    req.flush(mockTeachers);
  });

  it('should fetch teacher detail by id', () => {
    const mockTeacher: Teacher =   { id: 1, firstName: 'Prof A', lastName: 'Last A', createdAt: new Date(), updatedAt: new Date() };

    service.detail('1').subscribe(teacher => {
      expect(teacher).toEqual(mockTeacher);
    });

    const req = httpMock.expectOne('api/teacher/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockTeacher);
  });
});

// ----------- TEST D'INTEGRATION -----------
describe('Integration: TeacherService with TeacherListComponent', () => {
  let fixture: ComponentFixture<TeacherListComponent>;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TeacherListComponent],
      imports: [HttpClientTestingModule],
      providers: [TeacherService]
    });
    fixture = TestBed.createComponent(TeacherListComponent);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });
it('should display teachers from the service', () => {
    const mockTeachers: Teacher[] = [
      { id: 1, firstName: 'Prof A', lastName: 'Last A', createdAt: new Date(), updatedAt: new Date() },
      { id: 2, firstName: 'Prof B', lastName: 'Last B', createdAt: new Date(), updatedAt: new Date() }
    ];

    fixture.componentInstance.loadTeachers();
    const req = httpMock.expectOne('api/teacher');
    req.flush(mockTeachers);

    fixture.detectChanges();

    const teacherElements = fixture.nativeElement.querySelectorAll('.teacher-item');
    expect(teacherElements.length).toBe(2);
    expect(teacherElements[0].textContent).toContain('Prof A');
    expect(teacherElements[1].textContent).toContain('Prof B');
  });
});

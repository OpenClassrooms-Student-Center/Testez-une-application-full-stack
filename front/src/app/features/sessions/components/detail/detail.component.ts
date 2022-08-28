import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { Teacher } from '../../../../interfaces/teacher.interface';
import { SessionService } from '../../../../services/session.service';
import { TeacherService } from '../../../../services/teacher.service';
import { Session } from '../../interfaces/session.interface';
import { SessionApiService } from '../../services/session-api.service';

@Component({
  selector: 'app-detail',
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.scss']
})
export class DetailComponent implements OnInit {
  public session: Session | undefined;
  public teacher: Teacher | undefined;
  public isAdmin = false;

  constructor(
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private sessionService: SessionService,
    private sessionApiService: SessionApiService,
    private teacherService: TeacherService,
    private matSnackBar: MatSnackBar,
    private router: Router) {
  }

  public ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id')!;
    this.isAdmin = this.sessionService.sessionInformation!.admin;
    this.sessionApiService
      .detail(id)
      .subscribe((session: Session) => {
        this.session = session;
        this.teacherService
          .detail(session.teacher_id.toString())
          .subscribe((teacher: Teacher) => this.teacher = teacher);
      });
  }

  public back() {
    window.history.back();
  }

  public delete(): void {
    const id = this.route.snapshot.paramMap.get('id')!;

    this.sessionApiService
      .delete(id)
      .subscribe((response: any) => {
          console.log(response);
          this.matSnackBar.open('Session deleted !', 'Close', { duration: 3000 });
          this.router.navigate(['sessions']);
        }
      );
  }

}

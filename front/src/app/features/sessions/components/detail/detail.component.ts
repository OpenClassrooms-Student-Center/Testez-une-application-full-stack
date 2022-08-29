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

  public isParticipate = false;
  public isAdmin = false;

  public sessionId: string;
  public userId: string;

  constructor(
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private sessionService: SessionService,
    private sessionApiService: SessionApiService,
    private teacherService: TeacherService,
    private matSnackBar: MatSnackBar,
    private router: Router) {
    this.sessionId = this.route.snapshot.paramMap.get('id')!;
    this.isAdmin = this.sessionService.sessionInformation!.admin;
    this.userId = this.sessionService.sessionInformation!.id.toString();
  }

  public ngOnInit(): void {
    this.fetchSession();
  }

  public back() {
    window.history.back();
  }

  public delete(): void {
    this.sessionApiService
      .delete(this.sessionId)
      .subscribe((_: any) => {
          this.matSnackBar.open('Session deleted !', 'Close', { duration: 3000 });
          this.router.navigate(['sessions']);
        }
      );
  }

  public participate(): void {
    this.sessionApiService.participate(this.sessionId, this.userId).subscribe(_ => this.fetchSession());
  }

  public unParticipate(): void {
    this.sessionApiService.unParticipate(this.sessionId, this.userId).subscribe(_ => this.fetchSession());
  }

  private fetchSession(): void {
    this.sessionApiService
      .detail(this.sessionId)
      .subscribe((session: Session) => {
        this.session = session;
        this.isParticipate = session.users.some(u => u === this.sessionService.sessionInformation!.id);
        this.teacherService
          .detail(session.teacher_id.toString())
          .subscribe((teacher: Teacher) => this.teacher = teacher);
      });
  }

}

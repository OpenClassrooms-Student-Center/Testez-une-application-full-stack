import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { SessionInformation } from '../../../../interfaces/sessionInformation.interface';
import { SessionService } from '../../../../services/session.service';
import { Session } from '../../interfaces/session.interface';
import { SessionApiService } from '../../services/session-api.service';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss']
})
export class ListComponent {

  public sessions$: Observable<Session[]> = this.sessionApiService.all();

  constructor(
    private sessionService: SessionService,
    private sessionApiService: SessionApiService
  ) { }

  get user(): SessionInformation | undefined {
    return this.sessionService.sessionInformation;
  }
}

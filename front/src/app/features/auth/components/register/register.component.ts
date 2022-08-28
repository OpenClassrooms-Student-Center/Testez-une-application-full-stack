import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { SessionService } from 'src/app/services/session.service';
import { AuthService } from '../../services/auth.service';
import { RegisterRequest } from '../../interfaces/registerRequest.interface';
import { AuthSuccess } from '../../interfaces/authSuccess.interface';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {

  public onError = false;

  public form = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    firstName: ['', [Validators.required, Validators.min(3), Validators.max(20)]],
    lastName: ['', [Validators.required, Validators.min(3), Validators.max(20)]],
    password: ['', [Validators.required, Validators.min(3), Validators.max(40)]]
  });

  constructor(private authService: AuthService,
    private fb: FormBuilder,
    private router: Router) { }

  public submit(): void {
    const registerRequest = this.form.value as RegisterRequest;
    this.authService.register(registerRequest).subscribe({
        next: (_: AuthSuccess) => this.router.navigate(['/login']),
        error: _ => this.onError = true,
      }
    );
  }

}

import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DetailComponent } from './components/detail/detail.component';
import { FormComponent } from './components/form/form.component';
import { ListComponent } from './components/list/list.component';

const routes: Routes = [
  { path: '', title: 'Sessions', component: ListComponent },
  { path: 'detail/:id', title: 'Sessions - detail', component: DetailComponent },
  { path: 'create', title: 'Sessions - create', component: FormComponent },
  { path: 'update/:id', title: 'Sessions - update', component: FormComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SessionsRoutingModule {
}

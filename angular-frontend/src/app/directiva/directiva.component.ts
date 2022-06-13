import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-directiva',
  templateUrl: './directiva.component.html',
})
export class DirectivaComponent {

  listCourses: string[] = ['TypeScript', 'Javascript', 'Java SE', 'C#', 'PHP'];
  enableButton: boolean = true;
  
  constructor() { }
  
  setEnableButton(): void {
    this.enableButton = this.enableButton==true? false: true;
  }
}

import { FormBuilder, FormGroup } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  constructor(private formBuilder: FormBuilder, private httpClient: HttpClient) { }

  title = 'networks-against-humanity';
  username = ""
  SERVER_URL = "http://localhost:8080/player";
  uploadForm: FormGroup;

  ngOnInit() {
      this.uploadForm = this.formBuilder.group({
        profile: ['']
      });
    }

  onSubmit() {
    console.log(this.username)
  }

}

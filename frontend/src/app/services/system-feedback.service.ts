import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {environment} from "../../environments/environment";
import {SystemFeedback} from "../models/system-feedback";

@Injectable()
export class SystemFeedbackService {

  private systemFeedbackUrl = environment.api.systemFeedbackUrl;

  constructor(private http: HttpClient) {
  }

  public leaveFeedback(feedback: SystemFeedback): Observable<any> {
    return this.http.post<SystemFeedback>(this.systemFeedbackUrl, feedback);
  }

}

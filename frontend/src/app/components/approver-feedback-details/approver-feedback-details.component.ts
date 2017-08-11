import { Component, OnInit, Input } from '@angular/core';
import { SystemsService } from '../../services/systems.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-approver-feedback-details',
  templateUrl: './approver-feedback-details.component.html',
  styleUrls: ['./approver-feedback-details.component.scss']
})
export class ApproverFeedbackDetailsComponent implements OnInit {

  @Input() feedback: any;
  replies: any[] = [];

  refreshReplies(){
    this.systemService.getSystemCommentReplies(this.feedback.infoSystemUuid, this.feedback.id).then(
      res => {
        this.replies = res.json().content;
      });
  }

  postReply(f){
    if (f.valid){
      this.systemService.postReplyToComment(this.feedback.infoSystemUuid, this.feedback.id, f.value).then(
        res => {
          f.reset();
          this.refreshReplies();
          this.toastrService.success('Kommentaar edukalt lisatud.');
        },
        err => {
          this.toastrService.error('Kommentaari lisamine ebaõnnestus. Palun proovi uuesti.');
        }
      )
    }
  }

  constructor(private systemService: SystemsService,
              private toastrService: ToastrService) {

  }

  ngOnInit() {
    this.refreshReplies();
  }

}

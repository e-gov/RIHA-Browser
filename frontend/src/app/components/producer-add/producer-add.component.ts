import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SystemsService } from '../../services/systems.service';

@Component({
  selector: 'app-producer-add',
  templateUrl: './producer-add.component.html',
  styleUrls: ['./producer-add.component.scss']
})
export class ProducerAddComponent implements OnInit {

  onSubmit(f) :void {
    if (f.valid){
      this.systemsService.addSystem(f.value).then(
        res => {
          this.router.navigate(['/Kirjelda']);
        })
    }
  }

  constructor(private systemsService: SystemsService,
              private router: Router) {
  }

  ngOnInit() {
  }

}

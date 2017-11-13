import { Component, OnInit, Input } from '@angular/core';
import { SystemsService } from '../../../services/systems.service';
import { G } from '../../../globals/globals';
import { System } from '../../../models/system';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { GeneralHelperService } from '../../../services/general-helper.service';


//
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/debounceTime';
import 'rxjs/add/operator/distinctUntilChanged';

const states = ['Alabama', 'Alaska', 'American Samoa', 'Arizona', 'Arkansas', 'California', 'Colorado',
  'Connecticut', 'Delaware', 'District Of Columbia', 'Federated States Of Micronesia', 'Florida', 'Georgia',
  'Guam', 'Hawaii', 'Idaho', 'Illinois', 'Indiana', 'Iowa', 'Kansas', 'Kentucky', 'Louisiana', 'Maine',
  'Marshall Islands', 'Maryland', 'Massachusetts', 'Michigan', 'Minnesota', 'Mississippi', 'Missouri', 'Montana',
  'Nebraska', 'Nevada', 'New Hampshire', 'New Jersey', 'New Mexico', 'New York', 'North Carolina', 'North Dakota',
  'Northern Mariana Islands', 'Ohio', 'Oklahoma', 'Oregon', 'Palau', 'Pennsylvania', 'Puerto Rico', 'Rhode Island',
  'South Carolina', 'South Dakota', 'Tennessee', 'Texas', 'Utah', 'Vermont', 'Virgin Islands', 'Virginia',
  'Washington', 'West Virginia', 'Wisconsin', 'Wyoming'];

@Component({
  selector: 'app-producer-edit-relations',
  templateUrl: './producer-edit-relations.component.html',
  styleUrls: ['./producer-edit-relations.component.scss']
})
export class ProducerEditRelationsComponent implements OnInit {

  @Input() system: System;
  @Input() relations: any[];

  globals: any = G;
  relation: {
    infoSystem: any,
    type: string
  };


  dropDownFormatter = (v)=> {
    return `${v.details.short_name} - ${this.generalHelper.truncateString(v.details.name, 90)}`;
  };

  inputFormatter = (v)=> v.details.short_name;


  search = (text$: Observable<string>) =>
    text$
      .debounceTime(200)
      .distinctUntilChanged()
      .switchMap(term => term.length < 2 ? []
        : this.systemsService.getSystemsForAutocomplete(term, this.system.details.short_name));


  addRelation(addForm){
    if (addForm.valid){
      let infoSystemShortName = typeof this.relation.infoSystem === 'string' ? this.relation.infoSystem : this.relation.infoSystem.details.short_name;
      this.systemsService.addSystemRelation(this.system.details.short_name, {infoSystemShortName: infoSystemShortName,
                                                                                     type: this.relation.type}).then(res => {
        this.refreshRelations();
        addForm.reset();
        addForm.controls.type.setValue(this.globals.relation_type.SUB_SYSTEM);
      }, err => {
        this.toastrService.error('Serveri viga');
      });
    }
  };

  deleteRelation(id){
    this.systemsService.deleteSystemRelation(this.system.details.short_name, id).then(res => {
      this.refreshRelations();
    }, err => {
      this.toastrService.error('Serveri viga');
    });
  };

  refreshRelations(){
    this.systemsService.getSystemRelations(this.system.details.short_name).then(
      res => {
        this.relations = res.json();
      }
    )
  }

  closeModal(){
    this.activeModal.close();
  };

  constructor(private systemsService: SystemsService,
              private generalHelper: GeneralHelperService,
              private toastrService: ToastrService,
              private activeModal: NgbActiveModal) { }

  ngOnInit() {
    this.relation = {
      infoSystem: null,
      type: this.globals.relation_type.SUB_SYSTEM
    };
  }

}

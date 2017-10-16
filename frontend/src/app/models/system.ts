import { G } from '../globals/globals';

export class System {
  id: number;
  details: any;

  getOwnerCode(): any {
    if (this.details && this.details.owner){
      return this.details.owner.code;
    } else {
      return null;
    }
  }

  setData(system): void {
    this.id = system.id;
    this.details = Object.assign(this.details, system.details);
    if (this.details.meta != null){
      if (!this.details.meta.system_status){
        this.details.meta.system_status = {
          status: null,
          timestamp: null
        }
      }
      if (!this.details.meta.x_road_status){
        this.details.meta.x_road_status = {
          status: null,
          timestamp: null
        }
      }
    }
  }

  getStatus(){
    return this.details.meta.system_status.status;
  }

  setStatus(status): void {
    this.details.meta.system_status.status = status;
  }

  getStatusDescription(): string {
    let description = 'tundmatu staatuses';
    switch(this.details.meta.system_status.status){
      case G.system_status.ESTABLISHING: description = 'asutamisel';
        break;
      case G.system_status.IN_USE: description = 'kasutusel';
        break;
      case G.system_status.FINISHED: description = 'lõpetatud';
        break;
    }
    return description;
  }

  isUsed(): boolean{
    return this.details.meta.system_status.status === G.system_status.IN_USE;
  }

  setInDevelopment(inDevelopment): void {
    if (inDevelopment === true){
      this.details.meta.development_status = G.development_status.IN_DEVELOPMENT;
    } else if (inDevelopment === false){
      this.details.meta.development_status = G.development_status.NOT_IN_DEVELOPMENT;
    }
  }

  getDevelopmentStatus(){
    return this.details.meta.development_status;
  }

  isInDevelopment(): boolean{
    return this.details.meta.development_status === G.development_status.IN_DEVELOPMENT;
  }

  getXRoadStatus() {
    return this.details.meta.x_road_status.status;
  }

  setXRoadStatus(xRoadStatus): void {
    this.details.meta.x_road_status.status = xRoadStatus;
  }

  getTopics() {
    return this.details.topics;
  }

  hasDocuments(): boolean{
    return this.details.documents && this.details.documents.length > 0;
  }

  hasLegislations(): boolean{
    return this.details.legislations && this.details.legislations.length > 0;
  }

  hasDataObjects(): boolean{
    return this.details.stored_data && this.details.stored_data.length > 0;
  }

  hasDataFiles(): boolean{
    return this.details.data_files && this.details.data_files.length > 0;
  }

  hasContacts(): boolean{
    return this.details.contacts && this.details.contacts.length > 0;
  }

  constructor(){
    this.id = null;
    this.details = {
      meta: {
        development_status: null,
        system_status: {
          status: null,
          timestamp: null
        },
        x_road_status: {
          status: null,
          timestamp: null
        }
      },
      topics: [],
      stored_data: [],
      data_files: [],
      legislations: [],
      documents: [],
      contacts: [],
      homepage: null,
      purpose: null,
      short_name: null
    };
  }
}

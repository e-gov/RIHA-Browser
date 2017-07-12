import { G } from '../globals/globals';

export class System {
  id: number;
  details: any;

  setData(system): void {
    this.id = system.id;
    this.details = Object.assign(this.details, system.details);
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

  isInDevelopment(): boolean{
    return this.details.meta.development_status === G.development_status.IN_DEVELOPMENT;
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
      legislations: [],
      documents: [],
      homepage: null,
      purpose: null,
      short_name: null
    };
  }
}

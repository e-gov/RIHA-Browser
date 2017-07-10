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
      case 'ESTABLISHING': description = 'asutamisel';
        break;
      case 'IN_USE': description = 'kasutusel';
        break;
      case 'FINISHED': description = 'lõpetatud';
        break;
    }
    return description;
  }

  setInDevelopment(inDevelopment): void {
    if (inDevelopment === true){
      this.details.meta.development_status = 'IN_DEVELOPMENT';
    } else if (inDevelopment === false){
      this.details.meta.development_status = 'NOT_IN_DEVELOPMENT';
    }
  }

  isUsed(): boolean{
    return this.details.meta.system_status.status === 'IN_USE';
  }

  isInDevelopment(): boolean{
    return this.details.meta.development_status === 'IN_DEVELOPMENT';
  }

  constructor(){
    this.id = null;
    this.details = {
      meta: {
        description_timestamp: null,
        approval_status: {
          status: null,
          timestamp: null
        },
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

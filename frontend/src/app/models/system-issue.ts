export class SystemIssue {

  public id: number;
  public infoSystemUuid: string;
  public dateCreated: string;
  public title: string;
  public organizationName: string;
  public organizationCode: string;
  public status: string;
  public type: string;
  public resolutionType: string;

  constructor(props?) {
    this.id = props.id || null;
    this.infoSystemUuid = props.infoSystemUuid || null;
    this.dateCreated = props.dateCreated || null;
    this.title = props.title || null;
    this.organizationName = props.organizationName || null;
    this.organizationCode = props.organizationCode || null;
    this.status = props.status || null;
    this.type = props.type || null;
    this.resolutionType = props.resolutionType || null;
  }
}

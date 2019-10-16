export class SystemRelation {

  public id: number;
  public infoSystemUuid: string;
  public infoSystemName: string;
  public infoSystemShortName: string;
  public type: string;

  constructor(props?) {
    this.id = props.id || null;
    this.infoSystemUuid = props.infoSystemUuid || null;
    this.infoSystemName = props.infoSystemName || null;
    this.infoSystemShortName = props.infoSystemShortName || null;
    this.type = props.type || null;
  }
}

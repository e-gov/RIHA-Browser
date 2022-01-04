import {classifiers} from "../services/environment.service";

export class System {
  id: number = null;
  lastPositiveApprovalRequestType: any = null;
  lastPositiveApprovalRequestDate: any = null;
  hasUsedSystemTypeRelations: boolean = false;

  details: any = {};

  getOwnerCode(): any {
    if (this.details && this.details.owner){
      return this.details.owner.code;
    } else {
      return null;
    }
  }

  getStatus(){
    return this.details.meta.system_status.status;
  }

  getStandardInformationSystem() : boolean{
    return this.details.standard_system;
  }

  getSecurityStandard() {
    return this.details.security.standard ? this.details.security.standard : null;
  }

  getSecurityLevel() {
    return this.details.security.level ? this.details.security.level : null;
  }

  getSecurityClass() {
    return this.details.security.class ? this.details.security.class : null;
  }

  getLatestAuditDate(){
    return this.details.security.latest_audit_date ? this.details.security.latest_audit_date : null;
  }

  getLatestAuditResolution(){
    return this.details.security.latest_audit_resolution ? this.details.security.latest_audit_resolution : null;
  }

  getLastPositiveApprovalRequestType(){
      return this.lastPositiveApprovalRequestType;
  }

  getLastPositiveApprovalRequestDate(){
      return this.lastPositiveApprovalRequestDate;
  }

  setStatus(status): void {
    this.details.meta.system_status.status = status;
  }

  setStandardInformationSystem(standard): void {
    this.details.standard_system = standard;
  }

  isUsed(): boolean{
    return this.details.meta.system_status.status === classifiers.system_status.IN_USE.code;
  }

  setInDevelopment(inDevelopment): void {
    if (inDevelopment === true){
      this.details.meta.development_status = classifiers.development_status.IN_DEVELOPMENT.code;
    } else if (inDevelopment === false){
      this.details.meta.development_status = classifiers.development_status.NOT_IN_DEVELOPMENT.code;
    }
  }

  getDevelopmentStatus(){
    return this.details.meta.development_status;
  }

  isInDevelopment(): boolean{
    return this.details.meta.development_status === classifiers.development_status.IN_DEVELOPMENT.code;
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

  hasSecurityInfo(): boolean{
    return this.details.security.standard != null;
  }

  hasAuditInfo(): boolean{
    return this.details.security.latest_audit_resolution != null;
  }

  // hasStandardRealisationTopic() : boolean {
  //   if (!this.getTopics()) {
  //     return false;
  //   }
  //
  //   return this.getTopics().find(value => value == 'standardlahendus');
  // }

  constructor(system?){
    system = system || {};
    this.id = system.id || null;
    this.lastPositiveApprovalRequestType = system.lastPositiveApprovalRequestType || null;
    this.lastPositiveApprovalRequestDate = system.lastPositiveApprovalRequestDate || null;
    this.hasUsedSystemTypeRelations = system.hasUsedSystemTypeRelations;

    this.details = system.details || {};
    if (this.details.meta != null){
      if (!this.details.meta.development_status){
        this.details.meta.development_status = null;
      }
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
      if (!this.details.standard_system){
        this.details.standard_system = false;
      } else if (this.details.standard_system == true && this.lastPositiveApprovalRequestType == classifiers.issue_type.AUTOMATICALLY_REGISTERED.code){
        this.details.standard_system = false;
      }
    }
    else {
      this.details.meta = {
        development_status: null,
        system_status: {
          status: null,
          timestamp: null
        },
        x_road_status: {
          status: null,
          timestamp: null
        }
      }
    }
    this.details.topics = this.details.topics || [];
    this.details.stored_data = this.details.stored_data || [];
    this.details.data_files = this.details.data_files || [];
    this.details.legislations = this.details.legislations || [];
    this.details.documents = this.details.documents || [];
    this.details.contacts = this.details.contacts || [];
    this.details.homepage = this.details.homepage || null;
    this.details.purpose = this.details.purpose || null;
    this.details.showDifferences = this.details.differences || this.hasUsedSystemTypeRelations;
    this.details.differences = this.details.differences || null;
    this.details.short_name = this.details.short_name || null;
    this.details.security = this.details.security || {
      class: null,
      level: null,
      standard: null,
      latest_audit_date: null,
      latest_audit_resolution: null
    };
  }
}

export const G = Object.freeze({
  system_status: {
    IN_USE: 'IN_USE',
    ESTABLISHING: 'ESTABLISHING',
    FINISHED: 'FINISHED'
  },
  development_status: {
    IN_DEVELOPMENT: 'IN_DEVELOPMENT',
    NOT_IN_DEVELOPMENT: 'NOT_IN_DEVELOPMENT'
  },
  x_road_status: {
    JOINED: 'JOINED',
    NOT_JOINED: 'NOT_JOINED'
  },
  relation_type: {
    SUB_SYSTEM: 'SUB_SYSTEM',
    SUPER_SYSTEM: 'SUPER_SYSTEM',
    USED_SYSTEM: 'USED_SYSTEM',
    USER_SYSTEM: 'USER_SYSTEM'
  },
  issue_type: {
    ESTABLISHMENT_REQUEST: 'ESTABLISHMENT_REQUEST',
    TAKE_INTO_USE_REQUEST: 'TAKE_INTO_USE_REQUEST',
    MODIFICATION_REQUEST: 'MODIFICATION_REQUEST',
    FINALIZATION_REQUEST: 'FINALIZATION_REQUEST'
  },
  issue_resolution_type: {
    POSITIVE: 'POSITIVE',
    NEGATIVE: 'NEGATIVE',
    DISMISSED: 'DISMISSED'
  },
  audit_resolution_type: {
    PASSED_WITHOUT_REMARKS: 'PASSED_WITHOUT_REMARKS',
    PASSED_WITH_REMARKS: 'PASSED_WITH_REMARKS',
    DID_NOT_PASS: 'DID_NOT_PASS'
  },
  security_standard: {
   iske: 'ISKE'
  },
  security_level: {
    high: 'H',
    medium: 'M',
    low: 'L'
  },
  access_restriction_reasons: [{
    code: 18,
    legislation: 'AvTS § 35 lg 1 p 9',
    description: 'Teave turvasüsteemide, turvaorganisatsiooni või turvameetmete kirjelduse kohta'
  }, {
    code: 19,
    legislation: 'AvTS § 35 lg 1 p 10',
    description: 'Tehnoloogilisi lahendusi sisaldav teave'
  }, {
    code: 36,
    legislation: 'AvTS § 35 lg 1 p 17',
    description: 'Ärisaladus'
  }, {
    code: 38,
    legislation: 'AvTS § 35 lg 1 p 18 (1)',
    description: 'Elutähtsa teenuse riskianalüüsi ja toimepidevuse plaani puudutav teave'
  }, {
    code: 39,
    legislation: 'AvTS § 35 lg 2 p',
    description: 'Õigusaktide eelnõud enne nende kooskõlastamiseks saatmist või vastuvõtmiseks esitamist'
  }, {
    code: 40,
    legislation: 'AvTS § 35 lg 2 p 2',
    description: 'Dokumendi kavand ja selle juurde kuuluvad dokumendid'
  }]
});

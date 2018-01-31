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
  issue_resolution_type:{
    POSITIVE: 'POSITIVE',
    NEGATIVE: 'NEGATIVE',
    DISMISSED: 'DISMISSED'
  }
});

import {MissingTranslationHandler, MissingTranslationHandlerParams} from '@ngx-translate/core';

class RihaMissingTranslationHandler implements MissingTranslationHandler {
  handle(params: MissingTranslationHandlerParams) {
    return 'Translation not found';
  }
}

const missingTranslationHandler = {
  provide: MissingTranslationHandler,
  useClass: RihaMissingTranslationHandler
};

export default missingTranslationHandler;

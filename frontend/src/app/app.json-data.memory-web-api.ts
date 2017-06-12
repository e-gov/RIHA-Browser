import { InMemoryDbService } from 'angular-in-memory-web-api';
export class JsonData implements InMemoryDbService {
  createDb() {
    //ATTENTION There is data key is data.json, but InMemoryDbService remove any last suffix after point!
    //Also you need pass into forRoot the following params: {apiBase: '', rootPath: '/assets/'}
    return {
      data: {
        routes: [
          {
            i18n: 'i18n',
            hash: '#hash'
          }
        ],
        cards: [{}, {}, {}]
      }
    };
  };
}

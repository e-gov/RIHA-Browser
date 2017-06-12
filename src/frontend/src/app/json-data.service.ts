import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';

import { Subject } from 'rxjs/Subject';
import 'rxjs/add/operator/distinctUntilChanged';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/startWith';
import 'rxjs/add/operator/do';

@Injectable()
export class JsonDataService {
  private jsonDataUrl = 'assets/data.json';  // URL to web API
  private lastData = new Subject();
  public routes = this.lastData.startWith({}).map(this.getRoutes).distinctUntilChanged();
  public cards = this.lastData.map(this.getCards).distinctUntilChanged();

  constructor(private http: Http) {
    this.http
      .get(this.jsonDataUrl)
      .map((response: Response) => response.json())
      .subscribe(this.updateLastData.bind(this));
  }

  updateLastData(response) {
    this.lastData.next(response['data'] || response);
  }

  getRoutes(lastData) {
    return Array.isArray(lastData.routes)
      ? lastData.routes
      : [];
  }

  getCards(lastData) {
    return Array.isArray(lastData.cards)
      ? lastData.cards
      : [];
  }
}

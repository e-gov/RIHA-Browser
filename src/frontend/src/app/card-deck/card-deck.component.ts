import { Component, OnInit } from '@angular/core';
import { JsonDataService } from '../json-data.service';

@Component({
  selector: 'app-card-deck',
  templateUrl: './card-deck.component.html',
  styleUrls: ['./card-deck.component.scss']
})
export class CardDeckComponent implements OnInit {
  private cards = [];

  constructor(private jsonDataService: JsonDataService) {
    jsonDataService.cards.subscribe(this.updateCards.bind(this));
  }

  updateCards(cards) {
    this.cards = cards.map((card) => {
      return Object.assign(card, {
        iconType: ['fa', card.iconType || 'unknown'].join('-'),
        content: (card.content || []).map((item) => {
          if (Array.isArray(item)) {
            return {
              type: 'list',
              title: item.shift(),
              items: item.length ? item : null
            };
          }
          return {
            type: 'p',
            value: item
          };
        })
      });
    });
  }

  ngOnInit() {
  }

}

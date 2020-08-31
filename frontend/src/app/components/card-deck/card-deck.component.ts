import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-card-deck',
  templateUrl: './card-deck.component.html',
  styleUrls: ['./card-deck.component.scss']
})
export class CardDeckComponent implements OnInit {
  public cards = [];

  constructor() {
    this.cards = [
      {
        "iconType": "search",
        "title": "RIHA kataloogi sirvimine",
        "content": [
          "Kõik RIHAsse kirja pandud infosüsteemid on leitavad avalikust RIHA kataloogist. Kataloog annab infot, kui palju erinevaid süsteeme Eesti riigi infosüsteemi üleüldse moodustavad.",
          [
            "Infosüsteemi sirvimine RIHA-s:",
            "annab teadmise erinevatest infosüsteemidest riigis, nende eesmärgist ja andmetest",
            "hõlbustab infosüsteemi eest vastutavate inimeste leidmist",
            "annab infosüsteemi omanikule teadmise, kust leida talle vajalikku andmekomplekti"
          ]
        ],
        "actions": {
          "links": [{
            "label": "Loe lähemalt",
            "hash":"https://abi.ria.ee/riha/infootsingud-riha-st/riha-kataloogi-kasutamine",
            "type": "external"
          }],
          "buttons": [{
            "label": "Alustan",
            "link":"/Infosüsteemid"
          }]
        }
      },
      {
        "iconType": "pencil",
        "title": "Infosüsteemi haldamine",
        "content": [
          "Selleks, et teised leiaksid sinu infosüsteemi, tuleb see esmalt kirja panna. Olgu tegemist alles loodava või juba aastaid kasutuselolevaga, siis kirjelda see RIHAs.",
          [
            "Infosüsteemi kirjeldamine RIHA-s annab:",
            "teistele asutustele ja arendajatele teada, mis andmed sul on ja kuidas neid kasutada saab",
            "teadmise, kas infosüsteem vastab riigi IT nõuetele",
            "sulle endale kindluse, et oled täitnud tublisti seadust"
          ]
        ],
        "actions": {
          "links": [{
            "label": "Loe lähemalt",
            "hash": "https://abi.ria.ee/riha/kirjeldamine",
            "type": "external"
          }],
          "buttons": [{
            "label": "Alustan",
            "link":"/Kirjelda"
          }]
        }
      },
      {
        "iconType": "times",
        "title": "X-teega liitumine",
        "content": [
          "Riigi infosüsteemidega turvaliseks ja koosvõimeliseks andmevahetuseks on võimalik kasutada X-tee andmevahetuskihti.",
          [
            "X-teega liitumine võimaldab:",
            "keskenduda infosüsteemi funktsionaalsusele ning jätta andmevahetuse turvalisus X-tee hooleks",
            "kasutada riigi infosüsteemide teenuseid oma äriprotsesside optimeerimiseks",
            "pakkuda teistele turvalist ligipääsu enda andmetele"
          ]
        ],
        "actions": {
          "links": [{
            "label": "Loe lähemalt",
            "hash": "https://abi.ria.ee/xtee/et/x-tee-juhend/x-teega-liitumine",
            "type": "external"
          }],
          "buttons": [{
            "label": "Alustan",
            "link": "https://www.x-tee.ee",
            "type": "external"
          }]
        }
      }
    ];
    this.updateCards(this.cards);
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

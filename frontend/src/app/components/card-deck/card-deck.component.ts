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
            "hash":"https://abi.riha.ee/kataloogi-kasutamine",
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
            "hash": "https://abi.riha.ee/RIHAs-kirjeldamine",
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
          "Teistest infosüsteemidest andmete saamiseks või sinna sisestamiseks tuleb kasutada X-teed. See tagab koosvõimelise andmevahetuse kõikide riigiasutustega.",
          [
            "X-teega liitumine:",
            "avab sulle päringud teistesse infosüsteemidesse",
            "annab teistele turvalise ligipääsu sinu andmetele",
            "võimaldab sul keskenduda infosüsteemi funktsionaalsuse ja andmekvaliteedile ning jätta andmevahetus X-tee hooleks"
          ]
        ],
        "actions": {
          "links": [{
            "label": "Loe lähemalt",
            "hash": "https://abi.riha.ee/X-tee-alamsysteem",
            "type": "external"
          }],
          "buttons": [{
            "label": "Alustan",
            "hash": "https://x-tee.ee",
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

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
    this.cards = [
      {
        "iconType": "pencil",
        "title": "Infosüsteemi kirjeldamine",
        "content": [
          "Selleks, et teised leiaksid sinu infosüsteemi, tuleb see esmalt kirja panna. Olgu tegemist alles loodava või juba aastaid kasutuselolevaga, siis kirjelda see RIHAs.",
          [
            "Infosüsteemi kirjeldamine RIHA-s annab:",
            "teistele asutustele ja arendajatele teada, mis andmed sul on ja kuidas neid kasutada saab",
            "teadmise, kas infosüsteem vastab riigi IT nõuetele",
            "sulle endale kindluse, et oled täitnud tublisti seadust"
          ],
          "example additional paragraph",
          ["example other list:", "point one", "point two"]
        ],
        "actions": {
          "links": [{
            "label": "Loe lähemalt",
            "hash":"Abi/Kirjeldamine"
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
          "Teistest infosüsteemidest andmete saamiskes või sinna sisestamiseks tuleb kasutada X-teed. See tagab koosvõimelise andmevahetuse kõikide riigiasutustega.",
          [
            "X-teega liitumine:",
            "avab sulle päringud teistesse infosüsteemidesse",
            "annab teistele turvalise ligipääsu sinu andmetele",
            "võimaldab sul keskenduda infosüsteemi funktsionaalsuse ja andmekvaliteedile ning jätta andmevahetus X-tee hooleks"
          ]
        ]
      },
      {
        "iconType": "magic",
        "title": "Infosüsteemi hindamine",
        "content": [
          "Riigis on mitmeid IT nõuete seadjaid, kelle ülesanne on kontrollida, kas infosüsteemid neile nõuetele vastavad. See tagab riigi infosüsteemi läbipaistvuse, koosvõimelisuse ja turvalisuse.",
          [
            "Infosüsteemi hindamine RIHA-s:",
            "lihtsustab infosüsteemi omaniku arendusi, kuna kõik riigi IT nõuded on teada ja arusaadavad"
          ]
        ],
        "actions": {
          "links": [{
            "label": "Loe lähemalt",
            "hash":"Abi/X-teega-liitumine"
          }],
          "buttons": [{
            "label": "Alustan",
            "link":"/Hinda"
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

describe('Browser', function() {

  var data = {
      "content": [
          {
              "description": {
                  "name": "Eesti kirikute, koguduste ja koguduste liitude register",
                  "shortname": "Eesti kirikuregister",
                  "owner": "70000562",
                  "documentation": "eesti_kirikute_koguduste_ja_koguduste_liitude_register",
                  "modified_date": "2015-09-05T00:36:26.212345",
                  "status": "MITTE KOOSKÕLASTATUD",
                  "uri": "http://base.url:8090/Eesti%20kirikuregister"
              }
          },
          {
              "description": {
                  "name": "Õpilaste ja üliõpilaste register",
                  "shortname": "Õppurite register",
                  "owner": "70000740",
                  "documentation": "opilaste_ja_uliopilaste_register",
                  "modified_date": "2013-11-14T13:43:13.456789",
                  "status": "MITTE KOOSKÕLASTATUD",
                  "uri": "http://base.url:8090/%C3%95ppurite%20register"
              }
          }
      ]
  };

  it('fills table with info system data', function() {
    loadFixtures('table.html');

    new Browser()._createTableRows(data);

    var rows = $('tbody tr');

    expect(rows.length).toBe(2);
    expect($(rows[0]).find('.name').text()).toBe('Eesti kirikute, koguduste ja koguduste liitude register');
    expect($(rows[0]).find('.owner').text()).toBe('70000562');
    expect($(rows[0]).find('.last-modified').text()).toBe('2015-09-05T00:36:26.212345');
    expect($(rows[0]).find('.status').text()).toBe('MITTE KOOSKÕLASTATUD');
  });
});


describe('Browser', function() {

  var data = [
    {
      "name": "Eesti kirikute, koguduste ja koguduste liitude register",
      "shortname": "Eesti kirikuregister",
      "owner": {
        "code": "70000562",
        "name": "Siseministeerium"
      },
      "documentation": "eesti_kirikute_koguduste_ja_koguduste_liitude_register",
      "meta": {
        "system_status": {
          "status": "INFOSYS_STAATUS_LOPETATUD",
          "timestamp": "2015-09-05T00:36:26.255215"
        },
        "approval_status": {
          "status": "MITTE KOOSKÕLASTATUD",
          "timestamp": "2016-09-05T00:36:26.255215"
        }
      },
      "uri": "http://base.url:8090/Eesti%20kirikuregister"
    },
    {
      "name": "Õpilaste ja üliõpilaste register",
      "shortname": "Õppurite register",
      "owner": {
        "code": "70000740",
        "name": "Haridus- ja Teadusministeerium"
      },
      "documentation": "opilaste_ja_uliopilaste_register",
      "meta": {
        "system_status": {
          "status": "INFOSYS_STAATUS_LOPETATUD",
          "timestamp": "2013-11-14T13:43:55.546948"
        }
      },
      "uri": "http://base.url:8090/%C3%95ppurite%20register"
    }
  ];

  it('fills table with info system data', function() {
    loadFixtures('table.html');

    var br = new Browser();
    br._indexRows(data);
    br._createTableRows(data);

    var rows = $('tbody tr');

    expect(rows.length).toBe(2);
    expect($(rows[0]).find('.name').text()).toBe('Eesti kirikute, koguduste ja koguduste liitude register');
    expect($(rows[0]).find('.owner').text()).toBe('70000562');
    expect($(rows[0]).find('.last-modified').text()).toBe('2015-09-05T00:36:26.255215');
    expect($(rows[0]).find('.status').text()).toBe('INFOSYS_STAATUS_LOPETATUD');
    expect($(rows[0]).find('.approval-status').text()).toBe('MITTE KOOSKÕLASTATUD');
    expect($(rows[0]).find('.approved').text()).toBe('2016-09-05T00:36:26.255215');
  });
});


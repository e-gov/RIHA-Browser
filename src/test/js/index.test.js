describe('Browser', function() {

  var data = [
    {
      "owner": "70000562",
      "meta": {
        "URI": "/70000562/Eesti kirikuregister",
        "timestamp": "2015-09-05T00:36:26.255215"
      },
      "documentation": "https://riha.eesti.ee/riha/main/inf/eesti_kirikute_koguduste_ja_koguduste_liitude_register",
      "name": "Eesti kirikute, koguduste ja koguduste liitude register",
      "shortname": "Eesti kirikuregister",
      "status": {
        "staatus": "Lõpetatud",
        "timestamp": "2015-08-05T08:29:58.328468"
      },
      "approval": {
        "timestamp": "2016-12-07T12:16:15.847",
        "status": "MITTE KOOSKÕLASTATUD"
      }
    },
    {
      "owner": "70000740",
      "meta": {
        "URI": "/70000740/Õppurite register",
        "timestamp": "2013-11-14T13:43:55.546948"
      },
      "documentation": "https://riha.eesti.ee/riha/main/inf/opilaste_ja_uliopilaste_register",
      "name": "Õpilaste ja üliõpilaste register",
      "shortname": "Õppurite register",
      "status": {
        "staatus": "Lõpetatud",
        "timestamp": "2013-11-08T15:46:15.121725"
      }
    }
  ];

  it('fills table with info system data', function() {
    loadFixtures('table.html');

    new Browser()._createTableRows(data);

    var rows = $('tbody tr:not(.template-row)');

    expect(rows.length).toBe(2);
    expect(rows.hasClass('hidden')).toBe(false);
    expect(rows.hasClass('template-row')).toBe(false);
    expect($(rows[0]).find('.name').text()).toBe('Eesti kirikute, koguduste ja koguduste liitude register');
    expect($(rows[0]).find('.owner').text()).toBe('70000562');
    expect($(rows[0]).find('.last-modified').text()).toBe('2015-08-05T08:29:58.328468');
    expect($(rows[0]).find('.status').text()).toBe('Lõpetatud');
    expect($(rows[0]).find('.approval-status').text()).toBe('MITTE KOOSKÕLASTATUD');
    expect($(rows[0]).find('.approved').text()).toBe('2016-12-07T12:16:15.847');
  });
});


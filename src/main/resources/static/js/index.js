"use strict";

function Browser() {

//  todo get this value from conf
  var infosystemsUrl = 'http://localhost:8081/infosystems/';

  var self = this;

  self.init = function() {
    loadInfosystems();
  };

  function loadInfosystems() {
    $.getJSON(infosystemsUrl, function(data) {
      self._createTableRows(data);
      $('#info-systems-table').DataTable({paging: false});
    });
  }
  self._createTableRows = function(data) {
    var template = $('.template-row');

    var tbody = $('tbody');
    data.forEach(function (infosystem) {
      var newRow = $(template).clone().removeClass('hidden').removeClass('template-row');
      newRow.attr('title', JSON.stringify(infosystem));
      newRow.find('.owner').text(infosystem.owner);
      newRow.find('.name').text(infosystem.name);
      newRow.find('.last-modified').text(infosystem.status ? infosystem.status.timestamp : '');
      newRow.find('.status').text(infosystem.status ? infosystem.status.staatus : '');
      newRow.find('.approved').text(infosystem.approval ? infosystem.approval.timestamp : '');
      newRow.find('.approval-status').text(infosystem.approval ? infosystem.approval.status : '');
      tbody.append(newRow);
    });
  }
}

"use strict";

function Browser(infosystemsUrl) {

  var self = this;

  self.init = function() {
    loadInfosystems();
  };

  function loadInfosystems() {
    $.getJSON(infosystemsUrl, function(data) {
      self._createTableRows(data);
      $('#info-systems-table').DataTable({
        language: { "url": "/js/vendor/jquery.dataTables.i18n.json" },
        paging: false,
        initComplete: initColumnFilters
      });
    });
  }

  function initColumnFilters() {
    this.api().columns().every(function () {
      addFilter(this);
    });
  }

  function addInputFilter(column) {
    var columnHeader = $(column.header());
    var input = $('<input>')
      .attr('placeholder', columnHeader.data('placeholder'))
      .on('click', function (e) {
        e.stopPropagation();
      })
      .on('keyup', function () {
        var val = $.fn.dataTable.util.escapeRegex(
          $(this).val()
        );

        column
          .search(val ? val : '', true, false)
          .draw();
      });

    columnHeader.append($('<div></div>').append(input));
  }

  function addSelectFilter(column) {
    var select = $('<select><option></option></select>')
      .appendTo($(column.header()))
      .on('click', function (e) {
        e.stopPropagation();
      })
      .on('change', function () {
        var val = $.fn.dataTable.util.escapeRegex(
          $(this).val()
        );

        column
          .search(val ? '^' + val + '$' : '', true, false)
          .draw();
      });
    column.data().unique().sort().each(function (d, j) {
      select.append('<option value="' + d + '">' + d + '</option>')
    });
  }

  function addFilter(column) {
    var filterType = $(column.header()).data('filter');

    if (filterType == 'input') {
      addInputFilter(column);
    }
    else if (filterType == 'select') {
      addSelectFilter(column);
    }
  }

  self._createTableRows = function(data) {
    var template = $('#row-template').html();
    var tbody = $('tbody');
    data.forEach(function (infosystem) {
      var newRow = $(template);
      newRow.attr('title', JSON.stringify(infosystem));
      newRow.find('.owner').text(infosystem.owner.code);
      newRow.find('.name').text(infosystem.name);
      newRow.find('.last-modified').text(infosystem.meta && infosystem.meta.system_status ? infosystem.meta.system_status.timestamp : '');
      tbody.append(newRow);
    });
  }
}

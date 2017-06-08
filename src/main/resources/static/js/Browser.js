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
    data.content.forEach(function (infosystem) {
      var description = infosystem.description;
      var newRow = $(template);
      newRow.attr('title', JSON.stringify(description));
      newRow.find('.owner').text(description.owner);
      newRow.find('.name').text(description.name);
      newRow.find('.last-modified').text(description.modified_date);
      newRow.find('.status').text(description.status);
      newRow.find('.approved').text("-- empty --");
      newRow.find('.approval-status').text("-- empty --");
      tbody.append(newRow);
    });
  }
}

"use strict";

function Browser(infosystemsUrl) {

  var self = this;

  var indexedRows = [];

  self.init = function() {
    loadInfosystems();
  };

  function loadInfosystems() {
    $.getJSON(infosystemsUrl, function(data) {
      self._indexRows(data);
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

  self._parseData = function(infosystem){
    indexedRows[infosystem.uri].parsedStatus = infosystem.meta && infosystem.meta.system_status ?  infosystem.meta.system_status.status : '';
    indexedRows[infosystem.uri].parsedLastModified = infosystem.meta && infosystem.meta.system_status ? infosystem.meta.system_status.timestamp : '';
    indexedRows[infosystem.uri].parsedApprovalStatusTimestamp = infosystem.meta && infosystem.meta.approval_status ? infosystem.meta.approval_status.timestamp : '';
    indexedRows[infosystem.uri].parsedApprovalStatus = infosystem.meta && infosystem.meta.approval_status ? infosystem.meta.approval_status.status : '';
  }

  self._indexRows = function (data){
    data.forEach(function (infosystem) {
      indexedRows[infosystem.uri] = infosystem;
    });
  }

  self._createTableRows = function(data) {
    var template = $('#row-template').html();
    var tbody = $('tbody');
    var rowData, newRow;

    data.forEach(function (rowData) {
      self._parseData(rowData);
      newRow = $(template);
      newRow.attr('data-id', rowData.uri);
      newRow.find('.owner').text(rowData.owner.code);
      newRow.find('.name').text(rowData.name);
      newRow.find('.last-modified').text(rowData.parsedLastModified);
      newRow.find('.status').text(rowData.parsedStatus);
      newRow.find('.approved').text(rowData.parsedApprovalStatusTimestamp);
      newRow.find('.approval-status').text(rowData.parsedApprovalStatus);
      tbody.append(newRow);
      self._bindOpenButtons();
    });
  }

  self._bindOpenButtons = function () {
    $('.btn-outline-info').unbind().click(function(event) {
      var data = indexedRows[$(this).closest('tr').data('id')];
      var modal = $('#detailsModal');
      modal.find('.modal-title').text(data.name);
      modal.find('.short-name').text(data.shortname);
      modal.find('.owner').text(data.owner.code);
      modal.find('.last-modified').text(data.parsedLastModified);
      modal.find('.status').text(data.parsedStatus);
      modal.find('.approved').text(data.meta.approval_status ? data.meta.approval_status.timestamp : '');
      modal.find('.approval-status').text(data.meta.approval_status ? data.meta.approval_status.status : '');
      modal.find('.documentation').attr('href', data.documentation).text(data.documentation);
      modal.find('.purpose').text(data.purpose);
      modal.modal('show');
    });
  }
}

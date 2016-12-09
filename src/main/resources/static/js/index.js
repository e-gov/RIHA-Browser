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
        initComplete: function () {
          this.api().columns().every( function () {
            var column = this;
            var select = $('<select></select>')
              .appendTo( $(column.header()) )
              .on( 'change', function () {
                var val = $.fn.dataTable.util.escapeRegex(
                  $(this).val()
                );

                column
                  .search( val ? '^'+val+'$' : '', true, false )
                  .draw();
              } );
            column.data().unique().sort().each( function ( d, j ) {
              select.append( '<option value="'+d+'">'+d+'</option>' )
            } );
          } );
        }
      });
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

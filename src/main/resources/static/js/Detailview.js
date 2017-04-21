
function Detailview(infosystems, ownerCode, shortName) {
    var template = $('#row-template').html();
    var tbody = $('tbody');

    this.init = function () {
        createInfosystem(infosystems, ownerCode, shortName);
        $('#info-systems-table').DataTable({
            language: { "url": "/js/vendor/jquery.dataTables.i18n.json" },
            paging: false,
        });
    }
    function load(infosystem, template, tbody) {
        var newRow = $(template);
        newRow.attr('title', JSON.stringify(infosystem));
        newRow.find('.name').text(infosystem.name);
        newRow.find('.last-modified').text(infosystem.meta && infosystem.meta.system_status ? infosystem.meta.system_status.timestamp : '');
        tbody.append(newRow);
    }

    function createInfosystem(infosystems, ownerCode, shortName) {
        $.getJSON(infosystems, function (index) {
            index.forEach(function (infosystem) {
                if((infosystem.owner.code === ownerCode) && (infosystem.shortname === shortName)){
                    load(infosystem, template, tbody);
                }
            });
        });
    }
}


function Detailview(infosystem) {
    this.init = function () {
        load(infosystem);
    }
    function load(infosystem) {
        var newRow = $(template);
        newRow.attr('title', JSON.stringify(infosystem));
        newRow.find('.owner').text(infosystem.owner.code);
        newRow.find('.name').text(infosystem.name);
        newRow.find('.last-modified').text(infosystem.meta && infosystem.meta.system_status ? infosystem.meta.system_status.timestamp : '');
        tbody.append(newRow);
    }
}

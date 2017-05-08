"use strict";

function Detailview(infosystems, ownerCode, shortName, data, conf) {

    var template = $('#row-template').html();
    var tbody = $('tbody');

    this.init = function () {
        createInfosystem(infosystems, ownerCode, shortName);
        $('#info-systems-table').DataTable({
            language: {"url": "/js/vendor/jquery.dataTables.i18n.json"},
            paging: false,
        });
    }

    function load(infosystem, template, tbody) {
        var newRow = $(template);
        for(var i in conf){
            newRow.find('.fieldname').text(conf[i].displayName);
            newRow.find('.fieldvalue').text(getValue(conf[i].fieldName));
            console.log(conf[i].displayName);
            tbody.append(newRow);
        }


    }

    function getValue(fieldName) {
        return eval("data." + fieldName.toLowerCase());
    }

    function createInfosystem(infosystems, ownerCode, shortName) {
        $.getJSON(infosystems, function (index) {
            index.forEach(function (infosystem) {
                if ((infosystem.owner.code === ownerCode) && (infosystem.shortname === shortName)) {
                    load(infosystem, template, tbody);
                    console.log(data);
                    console.log(conf);
                    console.log(infosystems);
                }
            });
        });
    }
}
"use strict";

function Detailview(infosystems, ownerCode, shortName, data, conf) {

    var template = $('#row-template').html();
    var tbody = $('tbody');

    this.init = function () {
        load(template, tbody);

    }

    function load(template, tbody) {
        for (var i = 0; i <= conf.length; i++) {
            var newRow = $(template);
            newRow.find('.fieldname').text(conf[i].displayName);
            newRow.find('.fieldvalue').text(getValue(conf[i].fieldName));
            console.log(conf[i].displayName);
            tbody.append(newRow);
        }


    }

    function getValue(fieldName) {
        if(fieldName===""){
            return null;
        }
        return eval("data." + fieldName.toLowerCase());
    }


}
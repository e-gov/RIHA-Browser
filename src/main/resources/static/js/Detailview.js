"use strict";

function Detailview(infosystems, ownerCode, shortName, sata, conf) {

    var template = $('#row-template').html();
    var tbody = $('tbody');

    this.init = function () {
        load(template, tbody);

    }

    function getSourceFiles() {
        var params = {"op":"get","path":"db/document/","token":"testToken","filter":[["main_resource_id","=","437050"],["kind","=","infosystem_source_document"]],"sort":"-name"};
        $.ajax({
            url: "https://riha-proxy.ci.kit/rest/api",
            dataType: 'json',
            type: "POST",
            data: JSON.stringify(params),
            cache: false,
            success: function (data) {
                console.log(data);
            }
        });
    }

    function getFiles() {
        var params = {"op":"get","path":"db/document/","token":"testToken","filter":[["main_resource_id","=","437050"],["kind","=","document"]],"sort":"-name"};
        $.ajax({
            url: "https://riha-proxy.ci.kit/rest/api",
            dataType: 'json',
            type: "POST",
            data: JSON.stringify(params),
            cache: false,
            success: function (data) {
                console.log(data);
            }
        });
    }

    function getEntity() {
        var params = {"op":"get","path":"db/data_object/","token":"testToken","filter":[["main_resource_id","=","437144"],["kind","=","entity"]],"sort":"-name"};
        $.ajax({
            url: "https://riha-proxy.ci.kit/rest/api",
            dataType: 'json',
            type: "POST",
            data: JSON.stringify(params),
            cache: false,
            success: function (data) {
                console.log(data);
            }
        });
    }

    function load(template, tbody) {
        for (var i = 0; i <= conf.length; i++) {
            var newRow = $(template);
            if(conf[i].displayName==="Viited infosüsteemiga seotud õigusaktidele"){
                getSourceFiles();
                getEntity();
                getFiles();
            }
            else {
                newRow.find('.fieldname').text(conf[i].displayName);
                newRow.find('.fieldvalue').text(getValue(conf[i].fieldName));
                tbody.append(newRow);

            }
        }


    }

    function getValue(fieldName) {
        if(fieldName===""){
            return null;
        }
        return eval("sata." + fieldName.toLowerCase());
    }


}
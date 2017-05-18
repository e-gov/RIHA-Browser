"use strict";

function Detailview() {

    var template = $('#row-template').html();
    var tbody = $('#detail');
    var sTemp = $('#row-template-source').html();
    var sBody = $('#source');
    var dv = $('detailTable.html');

    this.init = function () {
        loadInfosystem(template, tbody);
            getSourceFiles();
    }


    function loadInfosystem(template, tbody) {

        $.getJSON('https://raw.githubusercontent.com/TaaviMeinberg/RIHA-Browser/detailView/src/main/resources/templates/rr-pohiinfo.json', function (data) {

            $.getJSON('https://raw.githubusercontent.com/TaaviMeinberg/RIHA-Browser/detailView/src/main/resources/templates/conf.json', function (conf) {
                proccessData(data, conf, template, tbody);
            });
        });

    }

    function proccessData(data, conf, template, tbody) {
        for (var i = 0; i <= conf.length; i++) {
            var newRow = $(template);
            newRow.find('.fieldname').text(conf[i].displayName);
            newRow.find('.fieldvalue').text(getValue(conf[i].fieldName));
            tbody.append(newRow);

        }


        function getValue(fieldName) {
            if(fieldName===""){
                return null;
            }
            return eval("data." + fieldName.toLowerCase());
        }
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
                for (var i = 0; i <= data.length; i++) {
                    var newRow = $(sTemp);
                    newRow.find('.name').text(data[i].name);
                    newRow.find('.type').text(data[i].type);
                    newRow.find('.date').text(data[i].doc_date);

                    sBody.append(newRow);
                }
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


    function getSourceFiles() {

        var sTemp = $('#row-template-source').html();
        var sBody = $('#source');
        var params = {"op":"get","path":"db/document/","token":"testToken","filter":[["main_resource_id","=","437050"],["kind","=","infosystem_source_document"]],"sort":"-name"};
        $.ajax({
            url: "https://riha-proxy.ci.kit/rest/api",
            dataType: 'json',
            type: "POST",
            data: JSON.stringify(params),
            cache: false,
            success: function (data) {
                for (var i = 0; i <= data.length; i++) {
                    var newRow = $(sTemp);
                    newRow.find('.name').text(data[i].name);
                    newRow.find('.type').text(data[i].type);
                    newRow.find('.date').text(data[i].doc_date);

                    sBody.append(newRow);
                }
            }
        });
    }

}


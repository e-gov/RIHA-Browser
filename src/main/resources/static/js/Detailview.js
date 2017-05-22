"use strict";

function Detailview() {

    this.init = function () {
        loadInfosystem();
        getSourceFiles();
        getFiles();
        getEntity()
        jQuery(function () {
            jQuery('.targetDiv').hide();
            jQuery('#div1').show();
        });
    }


    function loadInfosystem() {

        $.getJSON('https://raw.githubusercontent.com/TaaviMeinberg/RIHA-Browser/detailView/src/main/resources/templates/rr-pohiinfo.json', function (data) {

            $.getJSON('https://raw.githubusercontent.com/TaaviMeinberg/RIHA-Browser/detailView/src/main/resources/templates/conf.json', function (conf) {
                proccessData(data, conf, $('#row-template').html(), $('#detail'));
            });
        });

    }

    function proccessData(data, conf, template, tbody) {
        for (var i = 0; i <= conf.length; i++) {
            var newRow = $(template);
            newRow.find('.fieldname').text(conf[i].displayName);
            if($.isArray(conf[i].fieldName)){
                var field = "";
                for(var j = 0; j < conf[i].fieldName.length;j++){
                    field += " " + getValue(conf[i].fieldName[j]);
                }
                newRow.find('.fieldvalue').text(field);     }
            else {
                newRow.find('.fieldvalue').text(getValue(conf[i].fieldName));
            }
            tbody.append(newRow);
        }


        function getValue(fieldName) {
            if (fieldName === "") {
                return null;
            }
            return eval("data." + fieldName.toLowerCase());
        }
    }


    function getFiles() {

        var sTemp = $('#row-template-docs').html();
        var sBody = $('#docs');
        var params = {
            "op": "get",
            "path": "db/document/",
            "token": "testToken",
            "filter": [["main_resource_id", "=", "437050"], ["kind", "=", "document"]],
            "sort": "-name"
        };
        $.ajax({
            url: "https://riha-proxy.ci.kit/rest/api",
            dataType: 'json',
            type: "POST",
            data: JSON.stringify(params),
            cache: false,
            success: function (data) {
                for (var i = 0; i <= data.length; i++) {
                    var newRow = $(sTemp);
                    if(data[i].filename!=null){
                        newRow.find('.name').text(data[i].filename);
                    }else {
                        newRow.find('.name').text("Nimetu");

                    }
                    newRow.find('.date').text(data[i].start_date);

                    sBody.append(newRow);
                }
            }
        });
    }

    function getEntity() {

        var sTemp = $('#row-template-entities').html();
        var sBody = $('#entities')
        var params = {
            "op": "get",
            "path": "db/data_object/",
            "token": "testToken",
            "filter": [["main_resource_id", "=", "437144"], ["kind", "=", "entity"]],
            "sort": "-name"
        };
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

                    sBody.append(newRow);
                }            }
        });
    }


    function getSourceFiles() {

        var sTemp = $('#row-template-source').html();
        var sBody = $('#source');
        var params = {
            "op": "get",
            "path": "db/document/",
            "token": "testToken",
            "filter": [["main_resource_id", "=", "437050"], ["kind", "=", "infosystem_source_document"]],
            "sort": "-name"
        };
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


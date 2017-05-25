"use strict";


function Detailview(shortName, ownerCode) {
    var resourceId;

    function createTables() {
        loadInfosystem(shortName, ownerCode);
    }

    this.init = function () {
        createTables();

        jQuery(function () {
            jQuery('.targetDiv').hide();
            jQuery('#div1').show();
        });

        jQuery(function () {
            jQuery('.showSingle').click(function () {
                jQuery('.targetDiv').hide();
                jQuery('#div' + $(this).attr('target')).show();
            });
        });
    }


    function loadInfosystem(shortName, ownerCode) {
        console.log(numerWithoutCommas(ownerCode));

        var params =
            {
                "op": "get",
                "path": "db/main_resource/",
                "token": "testToken",
                "filter": [["owner", "=", numerWithoutCommas(ownerCode)], ["short_name", "=", shortName]]
            };
        $.ajax({
            url: "https://riha-proxy.ci.kit/rest/api",
            dataType: 'json',
            type: "POST",
            data: JSON.stringify(params),
            cache: false,
            success: function (data) {

                console.log(data[0].main_resource_id);
                resourceId = data[0].main_resource_id;

                getSourceFiles(resourceId);
                getFiles(resourceId);
                getEntity(resourceId);

                $.getJSON('/js/conf.json', function (conf) {
                    proccessData(data[0], conf, $('#row-template').html(), $('#detail'));
                });
            }
        });


    }

    function iske(k, t, s) {
        return ((k!=null)&&(t!=null)&&(s!=null));
    }

    function proccessData(data, conf, template, tbody) {
        for (var i = 0; i <= conf.length; i++) {
            var newRow = $(template);
            newRow.find('.fieldname').text(conf[i].displayName);
            if ($.isArray(conf[i].fieldName)) {
                var field = "";

                for (var j = 0; j < conf[i].fieldName.length; j++) {
                    if (conf[i].displayName === "ISKE turvaosaklassid" && iske(getValue(conf[i].fieldName[j]), getValue(conf[i].fieldName[1]), getValue(conf[i].fieldName[2]))) {
                        field = calcIske(getValue(conf[i].fieldName[j]), getValue(conf[i].fieldName[1]), getValue(conf[i].fieldName[2]));
                        j = conf[i].fieldName.length - 1;
                    }
                    else {
                        field += " " + getValue(conf[i].fieldName[j]);
                    }
                }
                newRow.find('.fieldvalue').text(field);
            }
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


    function getFiles(resource) {

        var sTemp = $('#row-template-docs').html();
        var sBody = $('#docs');
        var params = {
            "op": "get",
            "path": "db/document/",
            "token": "testToken",
            "filter": [["main_resource_id", "=", resource], ["kind", "=", "document"]],
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
                    if (data[i].filename != null) {
                        newRow.find('.name').text(data[i].filename);
                    } else {
                        newRow.find('.name').text("Nimetu");

                    }
                    newRow.find('.date').text(data[i].start_date);

                    sBody.append(newRow);
                }
            }
        });
    }

    function getEntity(resource) {

        var sTemp = $('#row-template-entities').html();
        var sBody = $('#entities')
        var params = {
            "op": "get",
            "path": "db/data_object/",
            "token": "testToken",
            "filter": [["main_resource_id", "=", resource], ["kind", "=", "entity"]],
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
                    newRow.find('.description').text("");
                    sBody.append(newRow);
                }
            }
        });
    }


    function getSourceFiles(resource) {

        var sTemp = $('#row-template-source').html();
        var sBody = $('#source');
        var params = {
            "op": "get",
            "path": "db/document/",
            "token": "testToken",
            "filter": [["main_resource_id", "=", resource], ["kind", "=", "infosystem_source_document"]],
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

    function numerWithoutCommas(str) {
        return str.replace(/,/g, "");
    }

    var calcIske = function (k, t, s) {
        console.log(k+t+s);
        function toInt(str) {
            return parseInt(str.substr(1, 1));
        }

        var level = Math.max(toInt(k), toInt(t), toInt(s));
        if(level==3){
            return "Kõrge";
        }
        else if(level==2){
            return "Keskmine";
        }
        else {
            return "madal";

        }
    };
}


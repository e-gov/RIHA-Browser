/**
 * Created by janarp on 25/05/17.
 */
describe('Detailview', function () {
    var data = {
        "uri": "urn:fdc:riha.eesti.ee:2016:infosystem:437050",
        "name": "Eesti rahvastikuregister",
        "names": [
            {
                "en": "Population Register"
            }
        ],
        "owner": "70000562",
        "state": "C",
        "groups": [
            "INFOSYSTEEM_GRUPP_SISEMINISTEERIUM",
            "INFOSYSTEEM_GRUPP_X_TEEGA_LIITUNUD"
        ],
        "old_id": 77,
        "creator": "-",
        "kind_id": 401,
        "purpose": "Infosüsteemi eesmärk on tagada Eesti rahvastikuregistri objekti (Rahvastikuregistri objektiks on Eesti kodanik, Eestis elukoha registreerinud Euroopa Liidu, Euroopa Majanduspiirkonna liikmesriigi ja Šveitsi Konföderatsiooni kodanik ning Eestis elamisloa või elamisõiguse saanud välismaalane) peamiste isikuandmete kogumine ühte andmekogusse riigile ja kohalikule omavalitsusele seadusega sätestatud ülesannete täitmiseks isiku õiguste, vabaduste ja kohustuste realiseerimisel ning Eesti rahvastiku arvestuse pidamine.",
        "version": "1",
        "modifier": "-",
        "uses_dvk": true,
        "excellent": false,
        "older_data": 1890,
        "short_name": "rr",
        "uses_xroad": true,
        "classifiers": [
            "urn:fdc:riha.eesti.ee:2016:classifier:436836",
            "urn:fdc:riha.eesti.ee:2016:classifier:436828",
            "urn:fdc:riha.eesti.ee:2016:classifier:436822",
            "urn:fdc:riha.eesti.ee:2016:classifier:436818",
            "urn:fdc:riha.eesti.ee:2016:classifier:436811",
            "urn:fdc:riha.eesti.ee:2016:classifier:436370",
            "urn:fdc:riha.eesti.ee:2016:classifier:436564"
        ],
        "creation_date": "2008-07-09T08:11:03",
        "modified_date": "2016-11-09T15:35:59",
        "organizations": [
            {
                "type": "haldaja",
                "organization": "70000562"
            },
            {
                "type": "pidaja",
                "date_from": "2009-02-04T14:41:35",
                "organization": "10264823"
            },
            {
                "type": "pidaja",
                "organization": "10264823"
            }
        ],
        "personal_data": true,
        "formation_date": "2000-06-20T00:00:00",
        "infosystem_type": "muu",
        "xroad_join_date": "2002-04-30T00:00:00",
        "main_resource_id": 437050,
        "template_version": "1.0.0",
        "infosystem_status": "kasutusel",
        "only_xml_services": true,
        "service_time_24x7": true,
        "service_load_level": 200,
        "is_standard_solution": false,
        "iske_security_class_k": "K3",
        "iske_security_class_l": "H",
        "iske_security_class_s": "S2",
        "iske_security_class_t": "T2",
        "service_time_comments": "katkestusteta tööaeg üldajal 99,5%(aastas) ja kõrgendatud valmisoleku ajal 99,9%",
        "uses_standard_solution": false,
        "only_eid_authentication": true,
        "sensitive_personal_data": true,
        "status_of_applying_iske": "auditeerimata",
        "wcag_usability_level_aa": true,
        "acceptable_response_time": 5,
        "manner_of_archive_keeping": "elektrooniline",
        "separate_presentation_layer": true,
        "data_retention_period_eternal": true,
        "all_other_data_loaded_by_xroad": true,
        "incident_initial_response_time": 2,
        "services_use_only_open_standards": true,
        "service_consultancy_response_time": 3,
        "monthly_average_service_reliability": 3,
        "services_have_semantic_descriptions": true,
        "max_duration_of_service_interruption": 1,
        "monthly_average_service_availability": 99.9,
        "service_request_initial_response_time": 3,
        "all_main_data_accessible_through_xroad": true,
        "contains_required_architecture_description": true,
        "data_structures_have_semantic_descriptions": true,
        "all_public_services_free_for_public_organisations": true,
        "kind": "infosystem"
    };

    var conf = [
        {
            "fieldName": "name",
            "displayName": "Nimi"
        },
        {
            "fieldName": "short_name",
            "displayName": "Lühinimi"
        },
        {
            "fieldName": "owner",
            "displayName": "Omanik"
        },
        {
            "fieldName": "purpose",
            "displayName": "Infosüsteemi eesmärk"
        },
        {
            "fieldName": "",
            "displayName": "Kas infosüsteem on aktiivses arenduses?"
        },
        {
            "fieldName": "infosystem_status",
            "displayName": "Kas infosüsteem on kasutusele võetud?"
        },
        {
            "fieldName": "",
            "displayName": "Viide infosüsteemi kasutajaliidesele"
        },
        {
            "fieldName": "uses_standard_solution",
            "displayName": "Põhineb standardlahendusel"
        },
        {
            "fieldName": "",
            "displayName": "Valdkond"
        },
        {
            "fieldName": "",
            "displayName": "Ülemsüsteem"
        },
        {
            "fieldName": [ "iske_security_class_k", "iske_security_class_t", "iske_security_class_s"],
            "displayName": "ISKE turvaosaklassid"
        },
        {
            "fieldName": "",
            "displayName": "Kasutuseolev turvaraamistik"
        },
        {
            "fieldName": "",
            "displayName": "Viimase ISKE auditi kuupäev"
        },
        {
            "fieldName": "status_of_applying_iske",
            "displayName": "ISKE rakendatus"
        },
        {
            "fieldName": "",
            "displayName": "Muudatuse sisu lühikirjeldus"
        },
        {
            "fieldName": "",
            "displayName": "uue kooskõlastuse kuupäev"
        },
        {
            "fieldName": "",
            "displayName": "vea lühikokkuvõte"
        },
        {
            "fieldName": "",
            "displayName": "vea olulisus"
        },
        {
            "fieldName": "",
            "displayName": "vea asukoht"
        },
        {
            "fieldName": "",
            "displayName": "vea oodatud tulemus"
        },
        {
            "fieldName": "",
            "displayName": "vea tegelik tulemus"
        }
    ];
        it('table as correct amount of rows', function () {
            loadFixtures('detailTable.html');

            new Detailview().proccessData(data, conf, $('#row-template').html(), $('#detail'));

            var rows = $('tbody tr');

            expect(rows.length).toBe(conf.length);
        });

        it('fills table rows correctly', function () {
           loadFixtures('detailTable.html');


            new Detailview().proccessData(data, conf, $('#row-template').html(), $('#detail'));

            var rows = $('tbody tr');

            expect($(rows[0]).find('.fieldname').text()).toBe(conf[0].displayName);
            expect($(rows[0]).find('.fieldvalue').text()).toBe(data.name);
            expect($(rows[1]).find('.fieldname').text()).toBe(conf[1].displayName);
            expect($(rows[1]).find('.fieldvalue').text()).toBe(data.short_name);
            expect($(rows[2]).find('.fieldname').text()).toBe(conf[2].displayName);
            expect($(rows[2]).find('.fieldvalue').text()).toBe(data.owner);
            expect($(rows[3]).find('.fieldname').text()).toBe(conf[3].displayName);
            expect($(rows[3]).find('.fieldvalue').text()).toBe(data.purpose);
            expect($(rows[4]).find('.fieldname').text()).toBe(conf[4].displayName);
            expect($(rows[4]).find('.fieldvalue').text()).toBe("");
            expect($(rows[5]).find('.fieldname').text()).toBe(conf[5].displayName);
            expect($(rows[5]).find('.fieldvalue').text()).toBe(data.infosystem_status);
            expect($(rows[6]).find('.fieldname').text()).toBe(conf[6].displayName);
            expect($(rows[6]).find('.fieldvalue').text()).toBe("");
            expect($(rows[7]).find('.fieldname').text()).toBe(conf[7].displayName);
            expect($(rows[7]).find('.fieldvalue').text()).toBe("false");
            expect($(rows[8]).find('.fieldname').text()).toBe(conf[8].displayName);
            expect($(rows[8]).find('.fieldvalue').text()).toBe("");
            expect($(rows[9]).find('.fieldname').text()).toBe(conf[9].displayName);
            expect($(rows[9]).find('.fieldvalue').text()).toBe("");
            expect($(rows[10]).find('.fieldname').text()).toBe(conf[10].displayName);
            expect($(rows[10]).find('.fieldvalue').text()).toBe("Kõrge");
            expect($(rows[11]).find('.fieldname').text()).toBe(conf[11].displayName);
            expect($(rows[11]).find('.fieldvalue').text()).toBe("");
            expect($(rows[12]).find('.fieldname').text()).toBe(conf[12].displayName);
            expect($(rows[12]).find('.fieldvalue').text()).toBe("");
            expect($(rows[13]).find('.fieldname').text()).toBe(conf[13].displayName);
            expect($(rows[13]).find('.fieldvalue').text()).toBe(data.status_of_applying_iske);
            expect($(rows[14]).find('.fieldname').text()).toBe(conf[14].displayName);
            expect($(rows[14]).find('.fieldvalue').text()).toBe("");
            expect($(rows[15]).find('.fieldname').text()).toBe(conf[15].displayName);
            expect($(rows[15]).find('.fieldvalue').text()).toBe("");
            expect($(rows[16]).find('.fieldname').text()).toBe(conf[16].displayName);
            expect($(rows[16]).find('.fieldvalue').text()).toBe("");
            expect($(rows[17]).find('.fieldname').text()).toBe(conf[17].displayName);
            expect($(rows[17]).find('.fieldvalue').text()).toBe("");
            expect($(rows[18]).find('.fieldname').text()).toBe(conf[18].displayName);
            expect($(rows[18]).find('.fieldvalue').text()).toBe("");
            expect($(rows[19]).find('.fieldname').text()).toBe(conf[19].displayName);
            expect($(rows[19]).find('.fieldvalue').text()).toBe("");
            expect($(rows[20]).find('.fieldname').text()).toBe(conf[20].displayName);
            expect($(rows[20]).find('.fieldvalue').text()).toBe("");

        });

    it('returns correct iske level', function () {
        expect(new Detailview().calcIske(iske_security_class_k, iske_security_class_s, iske_security_class_t)).toBe("Kõrge")
    });
});
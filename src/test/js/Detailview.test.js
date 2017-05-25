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
        it('fills table with infosystem details', function () {
            loadFixtures('detailTable');

            new Detailview().proccessData(data, conf, $('#row-template').html(), $('#detail'));

            var rows = $('tbody tr');

            expect(rows.length).toBe(conf.length);
        });
});
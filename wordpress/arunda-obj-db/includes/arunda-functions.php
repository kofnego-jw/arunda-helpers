<?php
/**
 * Created by PhpStorm.
 * User: Joseph
 * Date: 27.08.2015
 * Time: 10:42
 */

// Class definitions for
include_once('arunda-classdefinition.php');

function arunda_getAusgabeID($ausgabeTitel) {

    preg_match('/^[^<]+/', preg_replace('/&#x?\d+;/','%', $ausgabeTitel), $ausgabeTitelNormiert);

    $ausgabeTitelNormiert = preg_replace('/[\x{8000}-\x{FFFF}]/u', '%', $ausgabeTitelNormiert);

    global $wpdb;

    $abfrage =  preg_replace('/%%/', '%',
        $wpdb -> prepare("SELECT ID1 from Ausgabe where Titel like '%s'", trim($ausgabeTitelNormiert[0]))
    );

    $result = $wpdb -> get_col(
            $abfrage
        );

    if (is_array($result)) return empty($result) ? -1 : $result[0];

    return $result;

}


function arunda_getAusgabeMetadata($ausgabeId) {

    if (is_null($ausgabeId) || $ausgabeId == -1 || $ausgabeId == 0) {
        return null;
    }

    global $wpdb;

    $queryString = $wpdb->prepare(
        "SELECT
ausgabe.*,

ausgabe.`ISBN-Nummer` AS isbn_Nummer,

herausgeber1.Nachname AS herausgeber1_Nachname, herausgeber1.Vorname AS herausgeber1_Vorname,
herausgeber1.Geburtsdatum AS herausgeber1_Geburtsdatum,herausgeber1.Todesdatum AS herausgeber1_Todesdatum,
herausgeber1.Freitext AS herausgeber1_Freitext, nation1.Nation AS herausgeber1_Nation,
herausgeber1.link AS herausgeber1_Link,

herausgeber2.Nachname AS herausgeber2_Nachname, herausgeber2.Vorname AS herausgeber2_Vorname,
herausgeber2.Geburtsdatum AS herausgeber2_Geburtsdatum,herausgeber2.Todesdatum AS herausgeber2_Todesdatum,
herausgeber2.Freitext AS herausgeber2_Freitext, nation2.Nation AS herausgeber2_Nation,
herausgeber2.link AS herausgeber2_Link,

herausgeber3.Nachname AS herausgeber3_Nachname, herausgeber3.Vorname AS herausgeber3_Vorname,
herausgeber3.Geburtsdatum AS herausgeber3_Geburtsdatum,herausgeber3.Todesdatum AS herausgeber3_Todesdatum,
herausgeber3.Freitext AS herausgeber3_Freitext, nation3.Nation AS herausgeber3_Nation,
herausgeber3.link AS herausgeber3_Link,

herausgeber4.Nachname AS herausgeber4_Nachname, herausgeber4.Vorname AS herausgeber4_Vorname,
herausgeber4.Geburtsdatum AS herausgeber4_Geburtsdatum,herausgeber4.Todesdatum AS herausgeber4_Todesdatum,
herausgeber4.Freitext AS herausgeber4_Freitext, nation4.Nation AS herausgeber4_Nation,
herausgeber4.link AS herausgeber4_Link,

herausgeber5.Nachname AS herausgeber5_Nachname, herausgeber5.Vorname AS herausgeber5_Vorname,
herausgeber5.Geburtsdatum AS herausgeber5_Geburtsdatum,herausgeber5.Todesdatum AS herausgeber5_Todesdatum,
herausgeber5.Freitext AS herausgeber5_Freitext, nation5.Nation AS herausgeber5_Nation,
herausgeber5.link AS herausgeber5_Link,

loci.Ort AS erscheinungsort,

sprache.Sprache AS sprache,

publisher.Verlag AS verlag


FROM Ausgabe AS ausgabe

LEFT OUTER JOIN Person AS herausgeber1 ON ausgabe.Herausgeber_1 = herausgeber1.ID
LEFT OUTER JOIN Nation AS nation1 ON herausgeber1.Nationalität = nation1.ID

LEFT OUTER JOIN Person AS herausgeber2 ON ausgabe.Herausgeber_2 = herausgeber2.ID
LEFT OUTER JOIN Nation AS nation2 ON herausgeber2.Nationalität = nation2.ID

LEFT OUTER JOIN Person AS herausgeber3 ON ausgabe.Herausgeber_3 = herausgeber3.ID
LEFT OUTER JOIN Nation AS nation3 ON herausgeber3.Nationalität = nation3.ID

LEFT OUTER JOIN Person AS herausgeber4 ON ausgabe.Herausgeber_4 = herausgeber4.ID
LEFT OUTER JOIN Nation AS nation4 ON herausgeber4.Nationalität = nation4.ID

LEFT OUTER JOIN Person AS herausgeber5 ON ausgabe.Herausgeber_5 = herausgeber5.ID
LEFT OUTER JOIN Nation AS nation5 ON herausgeber5.Nationalität = nation5.ID

LEFT OUTER JOIN Loci AS loci ON ausgabe.Erscheinungsort = loci.ID

LEFT OUTER JOIN Sprache AS sprache ON ausgabe.Sprachen = sprache.ID

LEFT OUTER JOIN Publisher AS publisher ON ausgabe.Verlang = publisher.ID

WHERE ausgabe.ID1 = %d;"
        ,$ausgabeId);

    $ausgabeRow = $wpdb->get_row($queryString);

    if (!is_null($ausgabeRow)) {
        return arunda_convertAusgabeRowToArundaAusgabeMetadaten($ausgabeRow);
    }

    return null;

}


function arunda_listAusgabe() {
    global $wpdb;

    $results = $wpdb->get_results(
        "SELECT
ausgabe.*,

ausgabe.`ISBN-Nummer` AS isbn_Nummer,

herausgeber1.Nachname AS herausgeber1_Nachname, herausgeber1.Vorname AS herausgeber1_Vorname,
herausgeber1.Geburtsdatum AS herausgeber1_Geburtsdatum,herausgeber1.Todesdatum AS herausgeber1_Todesdatum,
herausgeber1.Freitext AS herausgeber1_Freitext, nation1.Nation AS herausgeber1_Nation,
herausgeber1.link AS herausgeber1_Link,

herausgeber2.Nachname AS herausgeber2_Nachname, herausgeber2.Vorname AS herausgeber2_Vorname,
herausgeber2.Geburtsdatum AS herausgeber2_Geburtsdatum,herausgeber2.Todesdatum AS herausgeber2_Todesdatum,
herausgeber2.Freitext AS herausgeber2_Freitext, nation2.Nation AS herausgeber2_Nation,
herausgeber2.link AS herausgeber2_Link,

herausgeber3.Nachname AS herausgeber3_Nachname, herausgeber3.Vorname AS herausgeber3_Vorname,
herausgeber3.Geburtsdatum AS herausgeber3_Geburtsdatum,herausgeber3.Todesdatum AS herausgeber3_Todesdatum,
herausgeber3.Freitext AS herausgeber3_Freitext, nation3.Nation AS herausgeber3_Nation,
herausgeber3.link AS herausgeber3_Link,

herausgeber4.Nachname AS herausgeber4_Nachname, herausgeber4.Vorname AS herausgeber4_Vorname,
herausgeber4.Geburtsdatum AS herausgeber4_Geburtsdatum,herausgeber4.Todesdatum AS herausgeber4_Todesdatum,
herausgeber4.Freitext AS herausgeber4_Freitext, nation4.Nation AS herausgeber4_Nation,
herausgeber4.link AS herausgeber4_Link,

herausgeber5.Nachname AS herausgeber5_Nachname, herausgeber5.Vorname AS herausgeber5_Vorname,
herausgeber5.Geburtsdatum AS herausgeber5_Geburtsdatum,herausgeber5.Todesdatum AS herausgeber5_Todesdatum,
herausgeber5.Freitext AS herausgeber5_Freitext, nation5.Nation AS herausgeber5_Nation,
herausgeber5.link AS herausgeber5_Link,

loci.Ort AS erscheinungsort,

sprache.Sprache AS sprache,

publisher.Verlag AS verlag


FROM Ausgabe AS ausgabe

LEFT OUTER JOIN Person AS herausgeber1 ON ausgabe.Herausgeber_1 = herausgeber1.ID
LEFT OUTER JOIN Nation AS nation1 ON herausgeber1.Nationalität = nation1.ID

LEFT OUTER JOIN Person AS herausgeber2 ON ausgabe.Herausgeber_2 = herausgeber2.ID
LEFT OUTER JOIN Nation AS nation2 ON herausgeber2.Nationalität = nation2.ID

LEFT OUTER JOIN Person AS herausgeber3 ON ausgabe.Herausgeber_3 = herausgeber3.ID
LEFT OUTER JOIN Nation AS nation3 ON herausgeber3.Nationalität = nation3.ID

LEFT OUTER JOIN Person AS herausgeber4 ON ausgabe.Herausgeber_4 = herausgeber4.ID
LEFT OUTER JOIN Nation AS nation4 ON herausgeber4.Nationalität = nation4.ID

LEFT OUTER JOIN Person AS herausgeber5 ON ausgabe.Herausgeber_5 = herausgeber5.ID
LEFT OUTER JOIN Nation AS nation5 ON herausgeber5.Nationalität = nation5.ID

LEFT OUTER JOIN Loci AS loci ON ausgabe.Erscheinungsort = loci.ID

LEFT OUTER JOIN Sprache AS sprache ON ausgabe.Sprachen = sprache.ID

LEFT OUTER JOIN Publisher AS publisher ON ausgabe.Verlang = publisher.ID
ORDER BY ausgabe.Erscheinungsdatum, ausgabe.Bandnummer "
    );

    $ausgabes = array();

    foreach ($results as $ausgabeRow) {
        $ausgabe = arunda_convertAusgabeRowToArundaAusgabeMetadaten($ausgabeRow);
        if (!is_null($ausgabe)) {
            array_push($ausgabes, $ausgabe);
        }
    }

    return $ausgabes;
}


function arunda_convertAusgabeRowToArundaAusgabeMetadaten($ausgabeRow) {
    $result = new ArundaAusgabeDaten();
    $result->id = $ausgabeRow->ID1;

    $result->titel = $ausgabeRow -> Titel;
    $result->untertitel = $ausgabeRow -> Untertitel;
    $result->bandnummer = $ausgabeRow -> Bandnummer;
    $result->erscheinungsdatum = $ausgabeRow -> Erscheinungsdatum;
    $result->verlag = $ausgabeRow -> verlag;
    $result->ort = $ausgabeRow->erscheinungsort;
    $result->isbn = $ausgabeRow->isbn_Nummer;
    $result->erhaeltlichkeit = $ausgabeRow->Erhältlichkeit == 0 ? false : true;
    $result->erscheinungsdatum = $ausgabeRow->Erscheinungsdatum;
    $result->freitext = $ausgabeRow->Freitext;
    $result->beilage = $ausgabeRow->Beilagen;

    if (!is_null($ausgabeRow->Herausgeber_1)) {
        $herausgeber1 = new ArundaPersonDaten();
        $herausgeber1->id = $ausgabeRow->Herausgeber_1;
        $herausgeber1->nachname = $ausgabeRow->herausgeber1_Nachname;
        $herausgeber1->vorname  = $ausgabeRow->herausgeber1_Vorname;
        $herausgeber1->geburtsdatum = $ausgabeRow->herausgeber1_Geburtsdatum;
        $herausgeber1->todesdatum = $ausgabeRow->herausgeber1_Todesdatum;
        $herausgeber1->nationalitaet = $ausgabeRow->herausgeber1_Nation;
        $herausgeber1->freitext = $ausgabeRow->herausgeber1_Freitext;
        $herausgeber1->link = $ausgabeRow->herausgeber1_Link;
        $result->herausgeber1 = $herausgeber1;
    }



    if (!is_null($ausgabeRow->Herausgeber_2)) {
        $herausgeber2 = new ArundaPersonDaten();
        $herausgeber2->id = $ausgabeRow->Herausgeber_2;
        $herausgeber2->nachname = $ausgabeRow->herausgeber2_Nachname;
        $herausgeber2->vorname  = $ausgabeRow->herausgeber2_Vorname;
        $herausgeber2->geburtsdatum = $ausgabeRow->herausgeber2_Geburtsdatum;
        $herausgeber2->todesdatum = $ausgabeRow->herausgeber2_Todesdatum;
        $herausgeber2->nationalitaet = $ausgabeRow->herausgeber2_Nation;
        $herausgeber2->freitext = $ausgabeRow->herausgeber2_Freitext;
        $herausgeber2->link = $ausgabeRow->herausgeber2_Link;
        $result->herausgeber2 = $herausgeber2;

    }

    if (!is_null($ausgabeRow->Herausgeber_3)) {
        $herausgeber3 = new ArundaPersonDaten();
        $herausgeber3->id = $ausgabeRow->Herausgeber_3;
        $herausgeber3->nachname = $ausgabeRow->herausgeber3_Nachname;
        $herausgeber3->vorname  = $ausgabeRow->herausgeber3_Vorname;
        $herausgeber3->geburtsdatum = $ausgabeRow->herausgeber3_Geburtsdatum;
        $herausgeber3->todesdatum = $ausgabeRow->herausgeber3_Todesdatum;
        $herausgeber3->nationalitaet = $ausgabeRow->herausgeber3_Nation;
        $herausgeber3->freitext = $ausgabeRow->herausgeber3_Freitext;
        $herausgeber3->link = $ausgabeRow->herausgeber3_Link;

        $result->herausgeber3 = $herausgeber3;
    }
    if (!is_null($ausgabeRow->Herausgeber_4)) {

        $herausgeber4 = new ArundaPersonDaten();
        $herausgeber4->id = $ausgabeRow->Herausgeber_4;
        $herausgeber4->nachname = $ausgabeRow->herausgeber4_Nachname;
        $herausgeber4->vorname  = $ausgabeRow->herausgeber4_Vorname;
        $herausgeber4->geburtsdatum = $ausgabeRow->herausgeber4_Geburtsdatum;
        $herausgeber4->todesdatum = $ausgabeRow->herausgeber4_Todesdatum;
        $herausgeber4->nationalitaet = $ausgabeRow->herausgeber4_Nation;
        $herausgeber4->freitext = $ausgabeRow->herausgeber4_Freitext;
        $herausgeber4->link = $ausgabeRow->herausgeber4_Link;
        $result->herausgeber4 = $herausgeber4;
    }
    if (!is_null($ausgabeRow->Herausgeber_5)) {
        $herausgeber5 = new ArundaPersonDaten();
        $herausgeber5->id = $ausgabeRow->Herausgeber_5;
        $herausgeber5->nachname = $ausgabeRow->herausgeber5_Nachname;
        $herausgeber5->vorname  = $ausgabeRow->herausgeber5_Vorname;
        $herausgeber5->geburtsdatum = $ausgabeRow->herausgeber5_Geburtsdatum;
        $herausgeber5->todesdatum = $ausgabeRow->herausgeber5_Todesdatum;
        $herausgeber5->nationalitaet = $ausgabeRow->herausgeber5_Nation;
        $herausgeber5->freitext = $ausgabeRow->herausgeber5_Freitext;
        $herausgeber5->link = $ausgabeRow->herausgeber5_Link;
        $result->herausgeber1 = $herausgeber5;

    }
    return $result;
}

function arunda_searchAusgabeByHerausgeber($herausgeberId) {
    global $wpdb;

    $queryString = $wpdb->prepare(
        "SELECT
ausgabe.*,
ausgabe.`ISBN-Nummer` AS isbn_Nummer,

herausgeber1.Nachname AS herausgeber1_Nachname, herausgeber1.Vorname AS herausgeber1_Vorname,
herausgeber1.Geburtsdatum AS herausgeber1_Geburtsdatum,herausgeber1.Todesdatum AS herausgeber1_Todesdatum,
herausgeber1.Freitext AS herausgeber1_Freitext, nation1.Nation AS herausgeber1_Nation,
herausgeber1.link AS herausgeber1_Link,

herausgeber2.Nachname AS herausgeber2_Nachname, herausgeber2.Vorname AS herausgeber2_Vorname,
herausgeber2.Geburtsdatum AS herausgeber2_Geburtsdatum,herausgeber2.Todesdatum AS herausgeber2_Todesdatum,
herausgeber2.Freitext AS herausgeber2_Freitext, nation2.Nation AS herausgeber2_Nation,
herausgeber2.link AS herausgeber2_Link,

herausgeber3.Nachname AS herausgeber3_Nachname, herausgeber3.Vorname AS herausgeber3_Vorname,
herausgeber3.Geburtsdatum AS herausgeber3_Geburtsdatum,herausgeber3.Todesdatum AS herausgeber3_Todesdatum,
herausgeber3.Freitext AS herausgeber3_Freitext, nation3.Nation AS herausgeber3_Nation,
herausgeber3.link AS herausgeber3_Link,

herausgeber4.Nachname AS herausgeber4_Nachname, herausgeber4.Vorname AS herausgeber4_Vorname,
herausgeber4.Geburtsdatum AS herausgeber4_Geburtsdatum,herausgeber4.Todesdatum AS herausgeber4_Todesdatum,
herausgeber4.Freitext AS herausgeber4_Freitext, nation4.Nation AS herausgeber4_Nation,
herausgeber4.link AS herausgeber4_Link,

herausgeber5.Nachname AS herausgeber5_Nachname, herausgeber5.Vorname AS herausgeber5_Vorname,
herausgeber5.Geburtsdatum AS herausgeber5_Geburtsdatum,herausgeber5.Todesdatum AS herausgeber5_Todesdatum,
herausgeber5.Freitext AS herausgeber5_Freitext, nation5.Nation AS herausgeber5_Nation,
herausgeber5.link AS herausgeber5_Link,

loci.Ort AS erscheinungsort,

sprache.Sprache AS sprache,

publisher.Verlag AS verlag


FROM Ausgabe AS ausgabe

LEFT OUTER JOIN Person AS herausgeber1 ON ausgabe.Herausgeber_1 = herausgeber1.ID
LEFT OUTER JOIN Nation AS nation1 ON herausgeber1.Nationalität = nation1.ID

LEFT OUTER JOIN Person AS herausgeber2 ON ausgabe.Herausgeber_2 = herausgeber2.ID
LEFT OUTER JOIN Nation AS nation2 ON herausgeber2.Nationalität = nation2.ID

LEFT OUTER JOIN Person AS herausgeber3 ON ausgabe.Herausgeber_3 = herausgeber3.ID
LEFT OUTER JOIN Nation AS nation3 ON herausgeber3.Nationalität = nation3.ID

LEFT OUTER JOIN Person AS herausgeber4 ON ausgabe.Herausgeber_4 = herausgeber4.ID
LEFT OUTER JOIN Nation AS nation4 ON herausgeber4.Nationalität = nation4.ID

LEFT OUTER JOIN Person AS herausgeber5 ON ausgabe.Herausgeber_5 = herausgeber5.ID
LEFT OUTER JOIN Nation AS nation5 ON herausgeber5.Nationalität = nation5.ID

LEFT OUTER JOIN Loci AS loci ON ausgabe.Erscheinungsort = loci.ID

LEFT OUTER JOIN Sprache AS sprache ON ausgabe.Sprachen = sprache.ID

LEFT OUTER JOIN Publisher AS publisher ON ausgabe.Verlang = publisher.ID

WHERE ausgabe.Herausgeber_1 = %d OR ausgabe.Herausgeber_2 = %d
OR ausgabe.Herausgeber_3 = %d OR ausgabe.Herausgeber_4 = %d OR ausgabe.Herausgeber_5 = %d

ORDER BY ausgabe.Erscheinungsdatum, ausgabe.Bandnummer ",
        $herausgeberId, $herausgeberId, $herausgeberId, $herausgeberId, $herausgeberId);

    $results = $wpdb->get_results($queryString);

    $ausgabes = array();

    foreach ($results as $ausgabeRow) {
        $ausgabe = arunda_convertAusgabeRowToArundaAusgabeMetadaten($ausgabeRow);
        if (!is_null($ausgabe)) {
            array_push($ausgabes, $ausgabe);
        }
    }

    return $ausgabes;

}



<?php

define('ARUNDA_ARTIKEL_QUERY_DEF', 'SELECT artikel.*,
autor1.ID AS autor1_ID, autor1.Nachname AS autor1_Nachname, autor1.Vorname AS autor1_Vorname,
autor2.ID AS autor2_ID, autor2.Nachname AS autor2_Nachname, autor2.Vorname AS autor2_Vorname,
autor3.ID AS autor3_ID, autor3.Nachname AS autor3_Nachname, autor3.Vorname AS autor3_Vorname,
autor4.ID AS autor4_ID, autor4.Nachname AS autor4_Nachname, autor4.Vorname AS autor4_Vorname,
keyword1.Keyword AS keyword1,
keyword2.Keyword AS keyword2,
keyword3.Keyword AS keyword3,
keyword4.Keyword AS keyword4,
keyword5.Keyword AS keyword5,
keyword6.Keyword AS keyword6,
keyword7.Keyword AS keyword7,
keyword8.Keyword AS keyword8,
keyword9.Keyword AS keyword9,
keyword10.Keyword AS keyword10,
gattung1.Gattung AS gattung1,
gattung2.Gattung AS gattung2

FROM Artikel AS artikel
JOIN Ausgabe as ausgabe on artikel.Ausgabe = ausgabe.ID1
LEFT OUTER JOIN Person as autor1 ON artikel.Autor_1 = autor1.ID
LEFT OUTER JOIN Person as autor2 ON artikel.Autor_2 = autor2.ID
LEFT OUTER JOIN Person as autor3 ON artikel.Autor_3 = autor3.ID
LEFT OUTER JOIN Person as autor4 ON artikel.Autor_4 = autor4.ID
LEFT OUTER JOIN Keyword as keyword1 ON artikel.Keyword_1 = keyword1.ID
LEFT OUTER JOIN Keyword as keyword2 ON artikel.Keyword_2 = keyword2.ID
LEFT OUTER JOIN Keyword as keyword3 ON artikel.Keyword_3 = keyword3.ID
LEFT OUTER JOIN Keyword as keyword4 ON artikel.Keyword_4 = keyword4.ID
LEFT OUTER JOIN Keyword as keyword5 ON artikel.Keyword_5 = keyword5.ID
LEFT OUTER JOIN Keyword as keyword6 ON artikel.Keyword_6 = keyword6.ID
LEFT OUTER JOIN Keyword as keyword7 ON artikel.Keyword_7 = keyword7.ID
LEFT OUTER JOIN Keyword as keyword8 ON artikel.Keyword_8 = keyword8.ID
LEFT OUTER JOIN Keyword as keyword9 ON artikel.Keyword_9 = keyword9.ID
LEFT OUTER JOIN Keyword as keyword10 ON artikel.Keyword_10 = keyword10.ID
LEFT OUTER JOIN Gattung as gattung1 on artikel.Gattung_1 = gattung1.ID
LEFT OUTER JOIN Gattung as gattung2 on artikel.Gattung_2 = gattung2.ID
');

/**
 * @param $authorId
 *
 *
 * Search for Articles by authorId
 *
 * SELECT artikel.*,
autor1.ID AS autor1_ID, autor1.Nachname AS autor1_Nachname, autor1.Vorname AS autor1_Vorname,
autor2.ID AS autor2_ID, autor2.Nachname AS autor2_Nachname, autor2.Vorname AS autor2_Vorname,
autor3.ID AS autor3_ID, autor3.Nachname AS autor3_Nachname, autor3.Vorname AS autor3_Vorname,
autor4.ID AS autor4_ID, autor4.Nachname AS autor4_Nachname, autor4.Vorname AS autor4_Vorname,
keyword1.Keyword AS keyword1,
keyword2.Keyword AS keyword2,
keyword3.Keyword AS keyword3,
keyword4.Keyword AS keyword4,
keyword5.Keyword AS keyword5,
keyword6.Keyword AS keyword6,
keyword7.Keyword AS keyword7,
keyword8.Keyword AS keyword8,
keyword9.Keyword AS keyword9,
keyword10.Keyword AS keyword10,
gattung1.Gattung AS gattung1,
gattung2.Gattung AS gattung2

FROM Artikel AS artikel
JOIN Ausgabe as ausgabe on artikel.Ausgabe = ausgabe.ID1
LEFT OUTER JOIN Person as autor1 ON artikel.Autor_1 = autor1.ID
LEFT OUTER JOIN Person as autor2 ON artikel.Autor_2 = autor2.ID
LEFT OUTER JOIN Person as autor3 ON artikel.Autor_3 = autor3.ID
LEFT OUTER JOIN Person as autor4 ON artikel.Autor_4 = autor4.ID
LEFT OUTER JOIN Keyword as keyword1 ON artikel.Keyword_1 = keyword1.ID
LEFT OUTER JOIN Keyword as keyword2 ON artikel.Keyword_2 = keyword2.ID
LEFT OUTER JOIN Keyword as keyword3 ON artikel.Keyword_3 = keyword3.ID
LEFT OUTER JOIN Keyword as keyword4 ON artikel.Keyword_4 = keyword4.ID
LEFT OUTER JOIN Keyword as keyword5 ON artikel.Keyword_5 = keyword5.ID
LEFT OUTER JOIN Keyword as keyword6 ON artikel.Keyword_6 = keyword6.ID
LEFT OUTER JOIN Keyword as keyword7 ON artikel.Keyword_7 = keyword7.ID
LEFT OUTER JOIN Keyword as keyword8 ON artikel.Keyword_8 = keyword8.ID
LEFT OUTER JOIN Keyword as keyword9 ON artikel.Keyword_9 = keyword9.ID
LEFT OUTER JOIN Keyword as keyword10 ON artikel.Keyword_10 = keyword10.ID
LEFT OUTER JOIN Gattung as gattung1 on artikel.Gattung_1 = gattung1.ID
LEFT OUTER JOIN Gattung as gattung2 on artikel.Gattung_2 = gattung2.ID


WHERE artikel.Autor_1=1 OR artikel.Autor_2=1 OR artikel.Autor_3=1 OR artikel.Autor_4=1
ORDER BY ausgabe.Erscheinungsdatum;
 */

function arunda_searchArtikelByAutor($authorId) {

    global $wpdb;

    $queryString = $wpdb->prepare(ARUNDA_ARTIKEL_QUERY_DEF . '
    WHERE artikel.Autor_1=%d OR artikel.Autor_2=%d OR artikel.Autor_3=%d OR artikel.Autor_4=%d
    ORDER BY ausgabe.Erscheinungsdatum, ausgabe.Bandnummer, artikel.Seite_Beginn, artikel.Seite_Ende;
    ', $authorId, $authorId, $authorId, $authorId);


    $results = $wpdb->get_results($queryString);

    return buildArtikelArray($results);

}


/**
 * @param $keyword
 *
 *
 *
 */
function arunda_searchArtikelByKeyword($keywordId) {

    global $wpdb;

    $queryString = $wpdb->prepare(
        ARUNDA_ARTIKEL_QUERY_DEF.'WHERE keyword10.ID = %d OR
keyword1.ID = %d OR
keyword2.ID = %d OR
keyword3.ID = %d OR
keyword4.ID = %d OR
keyword5.ID = %d OR
keyword6.ID = %d OR
keyword7.ID = %d OR
keyword8.ID = %d OR
keyword9.ID = %d
ORDER BY ausgabe.Erscheinungsdatum, ausgabe.Bandnummer',
        $keywordId, $keywordId, $keywordId, $keywordId, $keywordId,
        $keywordId, $keywordId, $keywordId, $keywordId, $keywordId
    );

    $results = $wpdb->get_results($queryString);

    return buildArtikelArray($results);

}


/**
 * @param $gattung
 *
 *
 */
function arunda_searchArtikelByGattung($gattungId) {

    global $wpdb;

    $queryString = $wpdb->prepare(ARUNDA_ARTIKEL_QUERY_DEF . '
    WHERE gattung1.ID = %d OR
    gattung2.ID = %d

    ORDER BY ausgabe.Erscheinungsdatum, ausgabe.Bandnummer, artikel.Seite_Beginn, artikel.Seite_Ende
    ', $gattungId, $gattungId);

    $results = $wpdb->get_results($queryString);

    return buildArtikelArray($results);

}

/**
SELECT Gattung.* FROM Gattung as Gattung
ORDER BY Gattung.Gattung
 */
function arunda_listGattung() {

    global $wpdb;

    $results = $wpdb->get_results("SELECT * from Gattung ORDER BY Gattung");

    $gattungs = array();

    foreach ($results as $row) {
        $gattung = new ArundaGattung();
        $gattung->id = $row->ID;
        $gattung->gattung = $row->Gattung;
        array_push($gattungs, $gattung);
    }

    return $gattungs;
}

/**
SELECT Keyword.* FROM Keyword as Keyword ORDER BY Keyword.Keyword;
 */
function arunda_listKeyword() {

    global $wpdb;

    $results = $wpdb->get_results("SELECT * from Keyword ORDER BY Keyword");

    $keywords = array();

    foreach ($results as $row) {
        $k = new ArundaKeyword();
        $k->id = $row->ID;
        $k->keyword = $row->Keyword;
        array_push($keywords, $k);
    }

    return $keywords;

}

/**
 * @param $ausgabeId
 *
 *
SELECT artikel.*,
autor1.ID AS autor1_ID, autor1.Nachname AS autor1_Nachname, autor1.Vorname AS autor1_Vorname,
autor2.ID AS autor2_ID, autor2.Nachname AS autor2_Nachname, autor2.Vorname AS autor2_Vorname,
autor3.ID AS autor3_ID, autor3.Nachname AS autor3_Nachname, autor3.Vorname AS autor3_Vorname,
autor4.ID AS autor4_ID, autor4.Nachname AS autor4_Nachname, autor4.Vorname AS autor4_Vorname,
keyword1.Keyword AS keyword1,
keyword2.Keyword AS keyword2,
keyword3.Keyword AS keyword3,
keyword4.Keyword AS keyword4,
keyword5.Keyword AS keyword5,
keyword6.Keyword AS keyword6,
keyword7.Keyword AS keyword7,
keyword8.Keyword AS keyword8,
keyword9.Keyword AS keyword9,
keyword10.Keyword AS keyword10,
gattung1.Gattung AS gattung1,
gattung2.Gattung AS gattung2

FROM Artikel AS artikel
JOIN Ausgabe as ausgabe on artikel.Ausgabe = ausgabe.ID1
LEFT OUTER JOIN Person as autor1 ON artikel.Autor_1 = autor1.ID
LEFT OUTER JOIN Person as autor2 ON artikel.Autor_2 = autor2.ID
LEFT OUTER JOIN Person as autor3 ON artikel.Autor_3 = autor3.ID
LEFT OUTER JOIN Person as autor4 ON artikel.Autor_4 = autor4.ID
LEFT OUTER JOIN Keyword as keyword1 ON artikel.Keyword_1 = keyword1.ID
LEFT OUTER JOIN Keyword as keyword2 ON artikel.Keyword_2 = keyword2.ID
LEFT OUTER JOIN Keyword as keyword3 ON artikel.Keyword_3 = keyword3.ID
LEFT OUTER JOIN Keyword as keyword4 ON artikel.Keyword_4 = keyword4.ID
LEFT OUTER JOIN Keyword as keyword5 ON artikel.Keyword_5 = keyword5.ID
LEFT OUTER JOIN Keyword as keyword6 ON artikel.Keyword_6 = keyword6.ID
LEFT OUTER JOIN Keyword as keyword7 ON artikel.Keyword_7 = keyword7.ID
LEFT OUTER JOIN Keyword as keyword8 ON artikel.Keyword_8 = keyword8.ID
LEFT OUTER JOIN Keyword as keyword9 ON artikel.Keyword_9 = keyword9.ID
LEFT OUTER JOIN Keyword as keyword10 ON artikel.Keyword_10 = keyword10.ID
LEFT OUTER JOIN Gattung as gattung1 on artikel.Gattung_1 = gattung1.ID
LEFT OUTER JOIN Gattung as gattung2 on artikel.Gattung_2 = gattung2.ID

WHERE artikel.Ausgabe = %d


ORDER BY artikel.Seite_Beginn, artikel.Seite_Ende
 */
function arunda_listArtikelByAusgabe($ausgabeId) {

    global $wpdb;

    $queryString = $wpdb->prepare(
        ARUNDA_ARTIKEL_QUERY_DEF.
        'WHERE artikel.Ausgabe = %d ORDER BY artikel.Seite_Beginn, artikel.Seite_Ende', $ausgabeId
    );

//    echo $queryString;

    $results = $wpdb->get_results($queryString);

    return buildArtikelArray($results);

}


function buildArtikelArray($results) {

    $articles = array();

    foreach ($results as $row) {
        $article = new ArundaArticle();
        $article->id = $row->ID;
        $article->titel = $row->Titel;
        $article->untertitel = $row->Untertitel;
        $article->facsimile = $row->Faksimile == 0 ? false : true;
        if (!is_null($row->Autor_1)) {
            $autor1 = new ArundaPersonDaten();
            $autor1->nachname = $row->autor1_Nachname;
            $autor1->vorname = $row->autor1_Vorname;
            $autor1->id = $row->Autor_1;
            $article->autor1 = $autor1;
        }
        if (!is_null($row->Autor_2)) {
            $autor2 = new ArundaPersonDaten();
            $autor2->nachname = $row->autor2_Nachname;
            $autor2->vorname = $row->autor2_Vorname;
            $autor2->id = $row->Autor_2;
            $article->autor2 = $autor2;
        }
        if (!is_null($row->Autor_3)) {
            $autor3 = new ArundaPersonDaten();
            $autor3->nachname = $row->autor3_Nachname;
            $autor3->vorname = $row->autor3_Vorname;
            $autor3->id = $row->Autor_3;
            $article->autor3 = $autor3;
        }
        if (!is_null($row->Autor_4)) {
            $autor4 = new ArundaPersonDaten();
            $autor4->nachname = $row->autor4_Nachname;
            $autor4->vorname = $row->autor4_Vorname;
            $autor4->id = $row->Autor_4;
            $article->autor1 = $autor4;
        }
        if (!is_null($row->keyword1)) {
            $k1 = new ArundaKeyword();
            $k1->id = $row->Keyword_1;
            $k1->keyword = $row->keyword1;
            $article->keyword1 = $k1;
        }
        if (!is_null($row->keyword2)) {
            $k2 = new ArundaKeyword();
            $k2->id = $row->Keyword_2;
            $k2->keyword = $row->keyword2;
            $article->keyword2 = $k2;
        }
        if (!is_null($row->keyword3)) {
            $k3 = new ArundaKeyword();
            $k3->id = $row->Keyword_3;
            $k3->keyword = $row->keyword3;
            $article->keyword1 = $k3;
        }
        if (!is_null($row->keyword4)) {
            $k4 = new ArundaKeyword();
            $k4->id = $row->Keyword_4;
            $k4->keyword = $row->keyword4;
            $article->keyword4 = $k4;
        }
        if (!is_null($row->keyword5)) {
            $k5 = new ArundaKeyword();
            $k5->id = $row->Keyword_5;
            $k5->keyword = $row->keyword5;
            $article->keyword5 = $k5;
        }
        if (!is_null($row->keyword6)) {
            $k6 = new ArundaKeyword();
            $k6->id = $row->Keyword_6;
            $k6->keyword = $row->keyword6;
            $article->keyword = $k6;
        }
        if (!is_null($row->keyword7)) {
            $k7 = new ArundaKeyword();
            $k7->id = $row->Keyword_7;
            $k7->keyword = $row->keyword7;
            $article->keyword7 = $k7;
        }
        if (!is_null($row->keyword8)) {
            $k8 = new ArundaKeyword();
            $k8->id = $row->Keyword_8;
            $k8->keyword = $row->keyword8;
            $article->keyword8 = $k8;
        }
        if (!is_null($row->keyword9)) {
            $k9 = new ArundaKeyword();
            $k9->id = $row->Keyword_9;
            $k9->keyword = $row->keyword9;
            $article->keyword9 = $k9;
        }
        if (!is_null($row->keyword10)) {
            $k10 = new ArundaKeyword();
            $k10->id = $row->Keyword_10;
            $k10->keyword = $row->keyword10;
            $article->keyword10 = $k10;
        }
        if (!is_null($row->gattung1)) {
            $g1 = new ArundaGattung();
            $g1->id = $row->Gattung_1;
            $g1->gattung = $row->gattung1;
            $article->gattung1 = $g1;
        }
        if (!is_null($row->gattung2)) {
            $g2 = new ArundaGattung();
            $g2->id = $row->Gattung_2;
            $g2->gattung = $row->gattung2;
            $article->gattung2 = $g2;
        }
        $article->ausgabeId = $row->Ausgabe;
        $article->seiteBeginn = $row->Seite_Beginn;
        $article->seiteEnde = $row->Seite_Ende;
        $article->freitext = $row->Freitext;

        array_push($articles, $article);
    }

    return $articles;
}

function arunda_getAusgabeNummer($ausgabeId) {
    global $wpdb;

    $queryString = $wpdb->prepare(
        "SELECT ausgabe.Erscheinungsdatum AS jahr, ausgabe.Bandnummer AS bandnummer
        FROM Ausgabe AS ausgabe WHERE ausgabe.ID1 = %d", $ausgabeId
    );

    $row = $wpdb->get_row($queryString);

    if (!is_null($row->bandnummer)) {
        return $row->jahr . ' (' . $row->bandnummer . ')';
    }

    return $row->jahr;

}

function arunda_listPerson() {

    global $wpdb;

    $results = $wpdb->get_results(
"SELECT person.*,
		nation.Nation AS nation
    FROM Person AS person
    LEFT OUTER JOIN Nation AS nation ON person.Nationalität = nation.ID
    ORDER BY person.Nachname, person.Vorname, person.Geburtsdatum"
    );

    $persons = array();

    foreach ($results as $row) {
        $person = new ArundaPersonDaten();
        $person->id = $row->ID;
        $person->geburtsdatum = $row->Geburtsdatum;
        $person->freitext = $row->Freitext;
        $person->nachname = $row->Nachname;
        $person->nationalitaet = $row->nation;
        $person->vorname = $row->Vorname;
        $person->todesdatum = $row->Todesdatum;
        $person->link = $row->Link;

        array_push($persons, $person);
    }

    return $persons;
}

function arunda_getPersonById($personId) {
    global $wpdb;

    $queryString = $wpdb->prepare("SELECT person.*, nation.Nation AS nation
FROM Person AS person
LEFT OUTER JOIN Nation AS nation ON person.Nationalität = nation.ID
WHERE person.ID = %d", $personId);

//    echo $queryString;

    $rows = $wpdb->get_results($queryString);

    if (!is_null($rows) && is_array($rows)) {
        $row = array_pop($rows);
        $person = new ArundaPersonDaten();
        $person->id = $row->ID;
        $person->geburtsdatum = $row->Geburtsdatum;
        $person->freitext = $row->Freitext;
        $person->nachname = $row->Nachname;
        $person->nationalitaet = $row->nation;
        $person->vorname = $row->Vorname;
        $person->todesdatum = $row->Todesdatum;
        $person->link = $row->Link;

        return $person;
    }

    return null;

}

function arunda_getKeywordById($keywordId) {
    global $wpdb;

    $row = $wpdb->get_row(
        $wpdb->prepare(
            "SELECT * FROM Keyword
WHERE ID = %d" , $keywordId)
    );

    $keyword = new ArundaKeyword();
    $keyword->id = $row->ID;
    $keyword->keyword = $row->Keyword;

    return $keyword;
}

function arunda_getGattungById($gattungId) {
    global $wpdb;

    $row = $wpdb->get_row(
        $wpdb->prepare(
            "SELECT * FROM Gattung
WHERE ID = %d" , $gattungId)
    );

    $gattung = new ArundaGattung();
    $gattung->id = $row->ID;
    $gattung->gattung = $row->Gattung;

    return $gattung;
}

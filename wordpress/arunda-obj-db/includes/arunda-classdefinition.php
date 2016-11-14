<?php

/**
 * Class AusgabeMetadaten
 *
 * SQL
 *

SELECT
ausgabe.*,

herausgeber1.Nachname AS herausgeber1_Nachname, herausgeber1.Vorname AS herausgeber1_Vorname,
herausgeber1.Geburtsdatum AS herausgeber1_Geburtsdatum,herausgeber1.Todesdatum AS herausgeber1_Todesdatum,
herausgeber1.Freitext AS herausgeber1_Freitext, nation1.Nation AS herausgeber1_Nation,

herausgeber2.Nachname AS herausgeber2_Nachname, herausgeber2.Vorname AS herausgeber2_Vorname,
herausgeber2.Geburtsdatum AS herausgeber2_Geburtsdatum,herausgeber2.Todesdatum AS herausgeber2_Todesdatum,
herausgeber2.Freitext AS herausgeber2_Freitext, nation2.Nation AS herausgeber2_Nation,

herausgeber3.Nachname AS herausgeber3_Nachname, herausgeber3.Vorname AS herausgeber3_Vorname,
herausgeber3.Geburtsdatum AS herausgeber3_Geburtsdatum,herausgeber3.Todesdatum AS herausgeber3_Todesdatum,
herausgeber3.Freitext AS herausgeber3_Freitext, nation3.Nation AS herausgeber3_Nation,

herausgeber4.Nachname AS herausgeber4_Nachname, herausgeber4.Vorname AS herausgeber4_Vorname,
herausgeber4.Geburtsdatum AS herausgeber4_Geburtsdatum,herausgeber4.Todesdatum AS herausgeber4_Todesdatum,
herausgeber4.Freitext AS herausgeber4_Freitext, nation4.Nation AS herausgeber4_Nation,

herausgeber5.Nachname AS herausgeber5_Nachname, herausgeber5.Vorname AS herausgeber5_Vorname,
herausgeber5.Geburtsdatum AS herausgeber5_Geburtsdatum,herausgeber5.Todesdatum AS herausgeber5_Todesdatum,
herausgeber5.Freitext AS herausgeber5_Freitext, nation5.Nation AS herausgeber5_Nation,

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

WHERE ausgabe.ID1 = $d;




















 *
 */

/**
 * Class ArundaAusgabeDaten
 * ArundaAusgabeDaten has only public fields.
 */
class ArundaAusgabeDaten {
    public $id;
    public $herausgeber1, $herausgeber2, $herausgeber3, $herausgeber4, $herausgeber5;
    public $titel, $untertitel;
    public $bandnummer, $erscheinungsdatum, $erhaeltlichkeit, $isbn, $verlag, $ort;
    public $freitext, $beilage;

    function isEmpty() {
        if (is_null($this->id) || $this->id==0 || $this->id==-1) return true;
        return false;
    }

    public function herausgebers() {
        $result = array();
        if (!is_null($this->herausgeber1))
            array_push($result,$this->herausgeber1);
        if (!is_null($this->herausgeber2))
            array_push($result,$this->herausgeber2);
        if (!is_null($this->herausgeber3))
            array_push($result,$this->herausgeber3);
        if (!is_null($this->herausgeber4))
            array_push($result,$this->herausgeber4);
        if (!is_null($this->herausgeber5))
            array_push($result,$this->herausgeber5);
        return $result;
    }

    public function isErhaeltlich() {
        if (is_null($this->erhaeltlichkeit) || $this->erhaeltlichkeit==0)
            return false;
        return true;
    }

}

/**
 * Class ArundaPersonDaten
 * only public fields
 * one function to echo out the
 */
class ArundaPersonDaten {
    public $id, $nachname, $vorname, $geburtsdatum, $todesdatum, $nationalitaet, $freitext, $link;

    function isEmpty() {
        return ( is_null($this->id) || $this->id==0 || $this->id==-1) ?
            true : false;
    }

    function outputNameMitSuchLink()
    {
        echo '<a href="personSearchLink?id=' . $this->id . '">';

        if (!is_null($this->nachname)) {
            echo $this->nachname;
            if (!is_null($this->vorname) && $this->vorname !== '') {
                echo ', ' . $this->vorname;
            }
        }
        // echo " (" . $this -> nationalitaet . ")";

        echo "</a>";
    }
}

class ArundaArticle {
    public $id, $titel, $untertitel;
    public $autor1, $autor2, $autor3, $autor4;
    public $ausgabeId, $seiteBeginn, $seiteEnde;
    public $keyword1, $keyword2, $keyword3, $keyword4, $keyword5,
        $keyword6, $keyword7, $keyword8, $keyword9, $keyword10;
    public $gattung1, $gattung2;
    public $facsimile;
    public $freitext;


    function autors() {
        $result = array();
        if (!is_null($this->autor1))
            array_push($result, $this->autor1);
        if (!is_null($this->autor2))
            array_push($result, $this->autor2);
        if (!is_null($this->autor3))
            array_push($result, $this->autor3);
        if (!is_null($this->autor4))
            array_push($result, $this->autor4);
        return $result;
    }

    function keywords() {
        $result = array();
        if (!is_null($this->keyword1))
            array_push($result,$this->keyword1);
        if (!is_null($this->keyword2))
            array_push($result,$this->keyword2);
        if (!is_null($this->keyword3))
            array_push($result,$this->keyword3);
        if (!is_null($this->keyword4))
            array_push($result,$this->keyword4);
        if (!is_null($this->keyword5))
            array_push($result,$this->keyword5);
        if (!is_null($this->keyword6))
            array_push($result,$this->keyword6);
        if (!is_null($this->keyword7))
            array_push($result,$this->keyword7);
        if (!is_null($this->keyword8))
            array_push($result,$this->keyword8);
        if (!is_null($this->keyword9))
            array_push($result,$this->keyword9);
        if (!is_null($this->keyword10))
            array_push($result,$this->keyword10);
        return $result;
    }

    function gattungs() {
        $result = array();
        if (!is_null($this->gattung1))
            array_push($result, $this->gattung1);
        if (!is_null($this->gattung2))
            array_push($result, $this->gattung2);
        return $result;
    }
}

class ArundaGattung {
    public $id, $gattung;
}

class ArundaKeyword {
    public $id, $keyword;
}

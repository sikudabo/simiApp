package technologies.chatbot.ai.simi.com.simisemanticsearches;

public class FilmQuery{


    public String filmName;
    public String typeQuestion = "";
    public final String prefixes = "prefix foaf: <http://xmlns.com/foaf/0.1/> \n" +
            "prefix dbo: <http://dbpedia.org/ontology/> \n";

    public FilmQuery(String filmName) {
        this.filmName = filmName;
    }

    public String director() {
        String directorQuery = prefixes + "SELECT ?director WHERE{\n" +
                "?film dbo:director ?director . \n" +
                "?film foaf:name " + "\"" + filmName + "\"" +  "@en \n" +
                "}";
        this.typeQuestion = "director";
        return directorQuery;
    }

    public String producer() {
        String producerQuery = prefixes + "SELECT ?producer WHERE {\n" +
                "?film dbo:producer ?producer . \n" +
                "?film foaf:name " + "\"" + filmName + "\"" +  "@en \n" +
                "}";
        this.typeQuestion = "producer";
        return producerQuery;
    }

    public String starring() {
        String starringQuery = prefixes + "SELECT ?starring WHERE {\n" +
                "?film dbo:starring ?starring . \n" +
                "?film foaf:name " + "\"" + filmName + "\"" +  "@en \n" +
                "}";
        this.typeQuestion = "starring";
        return starringQuery;
    }

    public String musicComposer() {
        String musicComposerQuery = prefixes + "SELECT ?musicComposer WHERE {\n" +
                "?film dbo:musicComposer ?musicComposer . \n" +
                "?film foaf:name " + "\"" + filmName + "\"" +  "@en \n" +
                "}";
        this.typeQuestion = "musicComposer";
        return musicComposerQuery;
    }

    public String editing() {
        String editingQuery = prefixes + "SELECT ?editing WHERE {\n" +
                "?film dbo:editing ?editing . \n" +
                "?film foaf:name " + "\"" + filmName + "\"" +  "@en \n" +
                "}";
        this.typeQuestion = "editing";
        return editingQuery;
    }

    public String distributor() {
        String distributorQuery = prefixes + "SELECT ?distributor WHERE {\n" +
                "?film dbo:distributor ?distributor . \n" +
                "?film foaf:name " + "\"" + filmName + "\"" +  "@en \n" +
                "}";
        this.typeQuestion = "distributor";
        return distributorQuery;
    }

    public String budget() {
        String budgetQuery = prefixes + "SELECT ?budget WHERE {\n" +
                "?film dbo:budget ?budget . \n" +
                "?film foaf:name " + "\"" + filmName + "\"" +  "@en \n" +
                "}";
        this.typeQuestion = "budget";
        return budgetQuery;
    }

    public String gross() {
        String grossQuery = prefixes + "SELECT ?gross WHERE {\n" +
                "?film dbo:gross ?gross . \n" +
                "?film foaf:name " + "\"" + filmName + "\"" +  "@en \n" +
                "}";
        this.typeQuestion = "gross";
        return grossQuery;
    }

    public String runtime() {
        String runtimeQuery = prefixes + "SELECT ?runtime WHERE {\n" +
                "?film dbo:runtime ?runtime . \n" +
                "?film foaf:name " + "\"" + filmName + "\"" +  "@en \n" +
                "}";
        this.typeQuestion = "runtime";
        return runtimeQuery;
    }

    public String writer() {
        String writerQuery = prefixes + "SELECT ?writer WHERE {\n" +
                "?film dbo:writer ?writer . \n" +
                "?film foaf:name " + "\"" + filmName + "\"" +  "@en \n" +
                "}";
        this.typeQuestion = "writer";
        return writerQuery;
    }

}
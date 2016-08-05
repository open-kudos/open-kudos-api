package kudos.web.beans.request;

public class AddIdeaForm {
    String author;
    String phrase;

    public AddIdeaForm() {}

    public AddIdeaForm(String author, String phrase) {
        this.author = author;
        this.phrase = phrase;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }
}

package my.music.notes;

public class ExampleItem {
    private int ImageResource;
    private String Text1;
    private String Text2;

    public ExampleItem(int imageResource, String text1, String text2){
        this.ImageResource = imageResource;
        this.Text1 = text1;
        this.Text2 = text2;
    }

    public void changeText1(String text){
        Text1 = text;
    }

    public int getImageResource() {
        return ImageResource;
    }

    public String getText1(){
        return Text1;
    }

    public String getText2() {
        return Text2;
    }
}

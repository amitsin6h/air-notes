package in.amitsin6h.airnotes;

/**
 * Created by amitsin6h on 10/26/17.
 */

public class Notes {

    private int _id;
    private String  notes, category, createdAt;

    public Notes(){

    }


    public Notes(int _id, String notes, String category, String createdAt){
        this.notes = notes;
        this.category = category;
        this.createdAt = createdAt;
    }

    //constructor
    public Notes(String notes, String category, String createdAt){
        this.notes = notes;
        this.category = category;
        this.createdAt = createdAt;
    }

    public Notes(String category){
        this.category = category;
    }


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}

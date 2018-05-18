package com.example.eiraj.seekdoers.Modal;

/**
 * Created by Abhishek on 31-07-2017.
 */

public class SubCategory {

    int term_id;
    String job_category;
    String slug;
    int term_taxonomy_id;
    String parent;
    String image;

    public String getImage() {

        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getTerm_id() {
        return term_id;
    }

    public void setTerm_id(int term_id) {
        this.term_id = term_id;
    }

    public String getJob_category() {
        return job_category;
    }

    public void setJob_category(String job_category) {
        this.job_category = job_category;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public int getTerm_taxonomy_id() {
        return term_taxonomy_id;
    }

    public void setTerm_taxonomy_id(int term_taxonomy_id) {
        this.term_taxonomy_id = term_taxonomy_id;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }
}

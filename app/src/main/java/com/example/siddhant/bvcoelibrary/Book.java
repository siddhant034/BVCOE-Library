package com.example.siddhant.bvcoelibrary;

/**
 * Created by siddhant on 16/4/16.
 */
public class Book{

    private String book_name;
    private String book_department;
    private String book_author;
    private String book_edition;
    private String book_qty;

    public void setbookName (String book_name)
    {
        this.book_name = book_name;
    }

    public String getbookName()
    {
        return book_name;
    }

    public void setbookDepartment (String book_department)
    {
        this.book_department = book_department;
    }

    public String getbookDepartment()
    {
        return book_department;
    }

    public void setbookAuthor (String book_author)
    {
        this.book_author = book_author;
    }

    public String getBookAuthor()
    {
        return book_author;
    }

    public void setbookEdition (String book_edition)
    {
        this.book_edition = book_edition;
    }

    public String getbookEdition()
    {
        return book_edition;
    }

    public void setbookQty (String book_qty)
    {
        this.book_qty = book_qty;
    }

    public String getbookQty()
    {
        return book_qty;
    }

}  

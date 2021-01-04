package ir.scrumproject.api;

import java.util.List;

/**
 * Scrum Project
 * Created by yalda mohasseli  on  1/1/2021.
 */
public class Invoice {

    private int id;
    private int cost;
    private int GroupId;
    private boolean settled;
    private String title;
    private String description;
    private String category;
    private String createdAt;
    private String updatedAt;
    private List<Share> shares;
    private List<Payer> payers;


}

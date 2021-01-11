package ir.scrumproject.api;

import java.util.List;

/**
 * Scrum Project
 * Created by yalda mohasseli  on  1/1/2021.
 */
public class Invoice {
    private boolean settled;
    private int id;
    private String title;
    private String description;
    private String category;
    private int cost;
    private int GroupId;
    private String updatedAt;
    private String createdAt;
    private List<Share> shares;
    private List<Payer> payers;

    public Invoice(String title, String description, String category, int cost, int groupId, List<Share> shares, List<Payer> payers) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.cost = cost;
        GroupId = groupId;
        this.shares = shares;
        this.payers = payers;
    }

    public boolean isSettled() {
        return settled;
    }

    public void setSettled(boolean settled) {
        this.settled = settled;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getGroupId() {
        return GroupId;
    }

    public void setGroupId(int groupId) {
        GroupId = groupId;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<Share> getShares() {
        return shares;
    }

    public void setShares(List<Share> shares) {
        this.shares = shares;
    }

    public List<Payer> getPayers() {
        return payers;
    }

    public void setPayers(List<Payer> payers) {
        this.payers = payers;
    }
}

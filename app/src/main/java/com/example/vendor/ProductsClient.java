package com.example.vendor;

import android.app.Application;

import com.example.Models.ProductDescriptionResponse;
import com.example.Models.ProductDescriptionResponse;

import java.util.ArrayList;
import java.util.List;

public class ProductsClient extends Application {

    public List<ProductDescriptionResponse> completeList = new ArrayList<>();
    public List<ProductDescriptionResponse> servingList = new ArrayList<>();
    public List<ProductDescriptionResponse> editingList = new ArrayList<>();
    public List<ProductDescriptionResponse> editableVegetableList = new ArrayList<>();
    public List<ProductDescriptionResponse> editableJuicesList = new ArrayList<>();
    public List<ProductDescriptionResponse> editableRawList = new ArrayList<>();
    public List<ProductDescriptionResponse> completeVegetableList = new ArrayList<>();
    public List<ProductDescriptionResponse> completeJuicesList = new ArrayList<>();
    public List<ProductDescriptionResponse> completeRawList = new ArrayList<>();
    public List<ProductDescriptionResponse> myVegetableList = new ArrayList<>();
    public List<ProductDescriptionResponse> myJuicesList = new ArrayList<>();
    public List<ProductDescriptionResponse> myRawList = new ArrayList<>();
    public int check = 0;
    public int servingStatus = 0;
    public String phone_no = "9027259122";      //default values
    public String status = "inactive";          //default values
    public String userlat="";
    public String userlon="";

    public String getUserlat() {
        return userlat;
    }

    public void setUserlat(String userlat) {
        this.userlat = userlat;
    }

    public String getUserlon() {
        return userlon;
    }

    public void setUserlon(String userlon) {
        this.userlon = userlon;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public int getServingStatus() {
        return servingStatus;
    }

    public void setServingStatus(int servingStatus) {
        this.servingStatus = servingStatus;
    }

    public List<ProductDescriptionResponse> getCompleteList() {
        return completeList;
    }

    public void setCompleteList(List<ProductDescriptionResponse> completeList) {
        this.completeList = null;
        this.completeList = completeList;
    }

    public List<ProductDescriptionResponse> getCompleteVegetableList() {
        return completeVegetableList;
    }

    public void setCompleteVegetableList(List<ProductDescriptionResponse> completeVegetableList) {
        this.completeVegetableList = completeVegetableList;
    }

    public List<ProductDescriptionResponse> getCompleteJuicesList() {
        return completeJuicesList;
    }

    public void setCompleteJuicesList(List<ProductDescriptionResponse> completeJuicesList) {
        this.completeJuicesList = completeJuicesList;
    }

    public List<ProductDescriptionResponse> getCompleteRawList() {
        return completeRawList;
    }

    public void setCompleteRawList(List<ProductDescriptionResponse> completeRawList) {
        this.completeRawList = completeRawList;
    }

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }

    public List<ProductDescriptionResponse> getMyVegetableList() {
        return myVegetableList;
    }

    public void setMyVegetableList(List<ProductDescriptionResponse> myVegetableList) {
        this.myVegetableList = null;
        this.myVegetableList = myVegetableList;
    }

    public List<ProductDescriptionResponse> getMyJuicesList() {
        return myJuicesList;
    }

    public void setMyJuicesList(List<ProductDescriptionResponse> myJuicesList) {
        this.myJuicesList = null;
        this.myJuicesList = myJuicesList;
    }

    public List<ProductDescriptionResponse> getMyRawList() {
        return myRawList;
    }

    public void setMyRawList(List<ProductDescriptionResponse> myRawList) {
        this.myRawList = null;
        this.myRawList = myRawList;
    }

    public List<ProductDescriptionResponse> getServingList() {
        return servingList;
    }

    public void setServingList(List<ProductDescriptionResponse> servingList) {
        this.servingList = null;
        this.servingList = servingList;
    }

    public List<ProductDescriptionResponse> getEditingList() {
        return editingList;
    }

    public void setEditingList(List<ProductDescriptionResponse> editingList) {
        this.editingList = editingList;
    }

    public List<ProductDescriptionResponse> getEditableVegetableList() {
        return editableVegetableList;
    }

    public void setEditableVegetableList(List<ProductDescriptionResponse> editableVegetableList) {
        this.editableVegetableList = editableVegetableList;
    }

    public List<ProductDescriptionResponse> getEditableJuicesList() {
        return editableJuicesList;
    }

    public void setEditableJuicesList(List<ProductDescriptionResponse> editableJuicesList) {
        this.editableJuicesList = editableJuicesList;
    }

    public List<ProductDescriptionResponse> getEditableRawList() {
        return editableRawList;
    }

    public void setEditableRawList(List<ProductDescriptionResponse> editableRawList) {
        this.editableRawList = editableRawList;
    }

    public void completeAllList(){
        for(ProductDescriptionResponse item : completeList){
            if(item.getCategory_name().equals("Vegetable")){
                completeVegetableList.add(item);
            }
            else if(item.getCategory_name().equals("juices")){
                completeJuicesList.add(item);
            }
            else{
                completeRawList.add(item);
            }
        }
    }

    public void completeServingList(){
        for(ProductDescriptionResponse item : servingList){
            if(item.getCategory_name().equals("Vegetable")){
                myVegetableList.add(item);
            }
            else if(item.getCategory_name().equals("juices")){
                myJuicesList.add(item);
            }
            else{
                myRawList.add(item);
            }
        }
    }

    public void completeEditingList() {

        int flag = 0;

        ProductDescriptionResponse aProduct;
        editingList = new ArrayList<>();

        for (ProductDescriptionResponse allproduct : completeList) {
            flag = 0;
            label1:
            for (ProductDescriptionResponse myproduct : servingList) {
                if (allproduct.getProd_id() == myproduct.getProd_id()) {
                    aProduct = myproduct;
                    aProduct.setCheck(true);
                    editingList.add(aProduct);
                    flag = 1;
                    break label1;
                }
            }
            if (flag == 0) {
                editingList.add(allproduct);
            }
        }

        for (ProductDescriptionResponse item : editingList) {
            if (item.getCategory_name().equals("Vegetable")) {
                editableVegetableList.add(item);
            } else if (item.getCategory_name().equals("juices")) {
                editableJuicesList.add(item);
            } else {
                editableRawList.add(item);
            }
        }
    }

    public void clearAllLists(){
        completeList = new ArrayList<>();
        servingList = new ArrayList<>();
        editingList = new ArrayList<>();
        editableVegetableList = new ArrayList<>();
        editableJuicesList = new ArrayList<>();
        editableRawList = new ArrayList<>();
        completeVegetableList = new ArrayList<>();
        completeJuicesList = new ArrayList<>();
        completeRawList = new ArrayList<>();
        myVegetableList = new ArrayList<>();
        myJuicesList = new ArrayList<>();
        myRawList = new ArrayList<>();
        check = 0;
        servingStatus = 0;
        phone_no = "9027259122";      //default values
        status = "inactive";          //default values
        userlat="";
        userlon="";
    }

}

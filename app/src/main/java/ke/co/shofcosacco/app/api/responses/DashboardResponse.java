package ke.co.shofcosacco.app.api.responses;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import ke.co.shofcosacco.app.models.AccountBalanceBosa;
import ke.co.shofcosacco.app.models.Dashboard;


public class DashboardResponse implements Serializable {
    @SerializedName("success")
    public String success;
    @SerializedName("description")
    public String description;
    @SerializedName("name")
    public String name;
    @SerializedName("mobile_no")
    public String mobile_no;
    @SerializedName("member_no")
    public String member_no;
    @SerializedName("account_no")
    public String account_no;
    @SerializedName("account_name")
    public String account_name;
    @SerializedName("account_status")
    public String account_status;
    @SerializedName("payroll_no")
    public String payroll_no;

    @SerializedName("balance")
    public String balance;
    @SerializedName("bal_code")
    public String bal_code;
    @SerializedName("minlist")
    public List<Dashboard> minlist;






}

package ke.co.shofcosacco.app.api.responses;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import ke.co.shofcosacco.app.models.MiniStatement;


public class StatementResponse implements Serializable {

    @SerializedName("success")
    public String statusCode;
    @SerializedName("f100")
    public String statusDesc;
    @SerializedName("stm_list")
    public List<MiniStatement> miniStatement;


}
